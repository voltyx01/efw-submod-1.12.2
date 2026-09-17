package com.voltyx.mwccf.walkietalkie;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.voltyx.mwccf.mcore.MCoreItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemWalkieTalkie extends Item implements IBauble {

    public static final String NBT_ACTIVE   = "wt_active";
    public static final String NBT_MUTED    = "wt_muted";
    public static final String NBT_CHANNEL  = "wt_channel";

    /** Max channel number */
    public static final int MAX_CHANNEL = 99;

    /** Drain per tick while active (48000 = ~40 min at 20 tps) */
    private static final int DRAIN_PER_TICK = 1;

    public static final ItemWalkieTalkie INSTANCE = new ItemWalkieTalkie();

    public ItemWalkieTalkie() {
        this.setRegistryName("mwccf", "walkie_talkie");
        this.setTranslationKey("mwccf.walkie_talkie");
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.addPropertyOverride(
            new net.minecraft.util.ResourceLocation("active"),
            (stack, world, entity) -> isActive(stack) ? 1F : 0F
        );
    }

    // -----------------------------------------------------------------------
    //  Baubles
    // -----------------------------------------------------------------------

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack stack) {
        // Any bauble slot (belt, amulet, ring…) — use TRINKET which fits body
        return BaubleType.TRINKET;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canEquip(ItemStack stack, EntityLivingBase player) { return true; }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canUnequip(ItemStack stack, EntityLivingBase player) { return true; }

    /** Drain battery each tick while walkie-talkie is active */
    @Override
    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack stack, EntityLivingBase entity) {
        tickDrain(stack, entity);
    }

    // -----------------------------------------------------------------------
    //  Normal item ticking (when held, not in baubles slot)
    // -----------------------------------------------------------------------

    @Override
    public void onUpdate(ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        ensureNbt(stack);
        if (!world.isRemote && entity instanceof EntityPlayer) {
            tickDrain(stack, (EntityLivingBase) entity);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (slotChanged) return true;
        if (oldStack.getItem() != newStack.getItem()) return true;
        return isActive(oldStack) != isActive(newStack);
    }

    private void tickDrain(ItemStack stack, EntityLivingBase entity) {
        if (entity.world.isRemote) return;
        ensureNbt(stack);
        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.getBoolean(NBT_ACTIVE)) return;

        // Drain every 20 ticks (1 sec) to prevent inventory/hand animation twitching
        if (entity.ticksExisted % 20 != 0) return;

        int charge = tag.getInteger("battery_charge");
        if (charge > 0) {
            int newCharge = Math.max(0, charge - (DRAIN_PER_TICK * 20));
            tag.setInteger("battery_charge", newCharge);
            if (newCharge <= 0) {
                tag.setBoolean(NBT_ACTIVE, false);
            }
        } else {
            // No power — turn off
            tag.setBoolean(NBT_ACTIVE, false);
        }
    }

    // -----------------------------------------------------------------------
    //  Right-click: open frequency GUI
    // -----------------------------------------------------------------------

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        ensureNbt(stack);

        if (world.isRemote) {
            openGui(stack);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @SideOnly(Side.CLIENT)
    private void openGui(ItemStack stack) {
        net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(
                new WalkieTalkieGui(stack));
    }

    public static final String NBT_VOLUME   = "wt_volume";

    // -----------------------------------------------------------------------
    //  Tooltip
    // -----------------------------------------------------------------------

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world,
                               List<String> tooltip, net.minecraft.client.util.ITooltipFlag flag) {
        ensureNbt(stack);
        NBTTagCompound tag = stack.getTagCompound();
        boolean active = tag.getBoolean(NBT_ACTIVE);
        boolean muted  = tag.getBoolean(NBT_MUTED);
        int channel    = tag.getInteger(NBT_CHANNEL);
        int charge     = tag.getInteger("battery_charge");
        int pct        = (int)((charge / 48000f) * 100);

        String stateColor = active ? "\u00a7a" : "\u00a7c";
        String statusText = active
                ? net.minecraft.client.resources.I18n.format("tooltip.mwccf.walkie_talkie.status.on")
                : net.minecraft.client.resources.I18n.format("tooltip.mwccf.walkie_talkie.status.off");
        String channelText = net.minecraft.client.resources.I18n.format("tooltip.mwccf.walkie_talkie.channel", channel);
        String micStatus = muted
                ? "\u00a7c" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.walkie_talkie.mic.muted")
                : "\u00a7a" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.walkie_talkie.mic.on");
        String micText = net.minecraft.client.resources.I18n.format("tooltip.mwccf.walkie_talkie.mic", micStatus);

        tooltip.add(stateColor + statusText + " \u00a77| \u00a7e" + channelText + " \u00a77| " + micText);

        if (charge <= 0) {
            tooltip.add("\u00a7c" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.walkie_talkie.no_battery"));
        } else {
            String col = pct > 50 ? "\u00a7a" : (pct > 20 ? "\u00a7e" : "\u00a7c");
            tooltip.add(col + net.minecraft.client.resources.I18n.format("tooltip.mwccf.walkie_talkie.battery", pct));
        }

        String keyName = WalkieTalkieKeyHandler.WALKIE_TALKIE_TOGGLE_KEY.getDisplayName();
        tooltip.add("\u00a77" + net.minecraft.client.resources.I18n.format("tooltip.mwccf.walkie_talkie.controls", keyName));
    }

    // -----------------------------------------------------------------------
    //  Helpers
    // -----------------------------------------------------------------------

    public static int getVolume(ItemStack stack) {
        if (stack.isEmpty()) return 100;
        ensureNbt(stack);
        return stack.getTagCompound().hasKey(NBT_VOLUME) ? stack.getTagCompound().getInteger(NBT_VOLUME) : 100;
    }

    public static void setVolume(ItemStack stack, int vol) {
        if (stack.isEmpty()) return;
        ensureNbt(stack);
        stack.getTagCompound().setInteger(NBT_VOLUME, Math.max(0, Math.min(100, vol)));
    }

    public static boolean isActive(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemWalkieTalkie)) return false;
        ensureNbt(stack);
        return stack.getTagCompound().getBoolean(NBT_ACTIVE);
    }

    public static boolean isMuted(ItemStack stack) {
        if (stack.isEmpty()) return true;
        ensureNbt(stack);
        return stack.getTagCompound().getBoolean(NBT_MUTED);
    }

    public static int getChannel(ItemStack stack) {
        if (stack.isEmpty()) return 1;
        ensureNbt(stack);
        return stack.getTagCompound().getInteger(NBT_CHANNEL);
    }

    public static boolean hasBattery(ItemStack stack) {
        if (stack.isEmpty()) return false;
        ensureNbt(stack);
        return stack.getTagCompound().getInteger("battery_charge") > 0;
    }

    /**
     * Toggle active state on the server side.
     * @return new active state
     */
    public static boolean toggleActive(ItemStack stack) {
        ensureNbt(stack);
        NBTTagCompound tag = stack.getTagCompound();
        boolean current = tag.getBoolean(NBT_ACTIVE);
        if (!current && tag.getInteger("battery_charge") <= 0) {
            return false; // can't turn on without battery
        }
        boolean next = !current;
        tag.setBoolean(NBT_ACTIVE, next);
        return next;
    }

    private static void ensureNbt(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean(NBT_ACTIVE, false);
            tag.setBoolean(NBT_MUTED, false);
            tag.setInteger(NBT_CHANNEL, 1);
            tag.setInteger(NBT_VOLUME, 100);
            tag.setInteger("battery_charge", 0);
            stack.setTagCompound(tag);
        } else if (!stack.getTagCompound().hasKey(NBT_VOLUME)) {
            stack.getTagCompound().setInteger(NBT_VOLUME, 100);
        }
    }
}

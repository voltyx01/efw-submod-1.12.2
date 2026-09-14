package efw.item;

import efw.init.EfwModSounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBandage extends Item {

    public ItemBandage() {
        super();
        setMaxStackSize(1);
        setMaxDamage(2);
        setTranslationKey("mcore.bandage");
        setRegistryName("mwccf", "bandage");
        setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 100;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if (!(player instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer entityPlayer = (EntityPlayer) player;
        World world = entityPlayer.world;

        // Cancel if health is already full
        if (entityPlayer.getHealth() >= entityPlayer.getMaxHealth()) {
            if (entityPlayer.getCooldownTracker().hasCooldown(this)) {
                entityPlayer.getCooldownTracker().removeCooldown(this);
            }
            entityPlayer.removePotionEffect(MobEffects.REGENERATION);
            entityPlayer.stopActiveHand();
            return;
        }

        int elapsedTicks = getMaxItemUseDuration(stack) - count;
        if (elapsedTicks >= 60) {
            if (!world.isRemote) {
                // Play sound efw:med
                world.playSound(null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, EfwModSounds.MED, SoundCategory.NEUTRAL, 1.0F, 1.0F);

                // Regeneration III for 370 ticks (~18.5 seconds)
                entityPlayer.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 370, 2, false, false));

                // Damage item by 1
                stack.damageItem(1, entityPlayer);

                if (stack.getItemDamage() >= stack.getMaxDamage()) {
                    stack.shrink(1);
                } else {
                    entityPlayer.getCooldownTracker().setCooldown(this, 185);
                }
            }

            entityPlayer.swingArm(entityPlayer.getActiveHand());
            entityPlayer.stopActiveHand();
        }
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return 0.0F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GREEN + "Hold RMB for 3 seconds to use");
        int usesLeft = stack.getMaxDamage() - stack.getItemDamage();
        if (usesLeft > 0) {
            tooltip.add(TextFormatting.GRAY + "Uses left: " + usesLeft);
        }
    }
}

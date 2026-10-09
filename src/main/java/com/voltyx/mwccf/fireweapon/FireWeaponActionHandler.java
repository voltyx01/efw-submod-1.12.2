package com.voltyx.mwccf.fireweapon;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.fireweapon.network.PacketFireWeaponAction;
import efw.item.ItemCloth;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FireWeaponActionHandler {

    /**
     * Intercepts right click in GUI containers.
     * Handles: wrap with cloth, ignite with flint, soak with alcohol.
     * @return true if action was handled and vanilla click should be cancelled.
     */
    @SideOnly(Side.CLIENT)
    public static boolean handleGuiSlotClick(Container container, Slot slot, int slotId) {
        if (container == null || slot == null || !slot.getHasStack()) return false;
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player == null) return false;

        ItemStack targetStack = slot.getStack();
        if (!FireWeaponHelper.isWeapon(targetStack)) return false;

        ItemStack cursorStack = player.inventory.getItemStack();
        if (cursorStack.isEmpty()) return false;

        // 1. Wrap with cloth
        if (cursorStack.getItem() instanceof ItemCloth
            && (!FireWeaponHelper.isWrapped(targetStack) || FireWeaponHelper.isInCharPhase(targetStack))) {
            FireWeaponHelper.setWrapped(targetStack, true);
            boolean flamingCloth = cursorStack.getItem() instanceof efw.item.ItemFlamingCloth;
            if (flamingCloth) FireWeaponHelper.soakAndIgnite(targetStack);
            if (!player.capabilities.isCreativeMode) cursorStack.shrink(1);
            player.world.playSound(player.posX, player.posY, player.posZ,
                    SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0F, 1.2F, false);
            if (flamingCloth) {
                player.world.playSound(player.posX, player.posY, player.posZ,
                        SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F, false);
            }
            MwccfMod.PACKET_HANDLER.sendToServer(new PacketFireWeaponAction(container.windowId, slotId, PacketFireWeaponAction.ACTION_WRAP));
            return true;
        }

        // 2. Ignite with flint and steel (CLOTH mode)
        if (cursorStack.getItem() instanceof ItemFlintAndSteel
                && FireWeaponHelper.isWrapped(targetStack)
                && !FireWeaponHelper.isIgnited(targetStack)
                && !FireWeaponHelper.isInCharPhase(targetStack)
                && !FireWeaponHelper.isSoakedReady(targetStack)) {
            FireWeaponHelper.igniteCloth(targetStack);
            cursorStack.damageItem(1, player);
            player.world.playSound(player.posX, player.posY, player.posZ,
                    SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F, false);
            MwccfMod.PACKET_HANDLER.sendToServer(new PacketFireWeaponAction(container.windowId, slotId, PacketFireWeaponAction.ACTION_IGNITE));
            return true;
        }

        // 3. Soak wrapped cloth with alcohol (whiskey, tequila, gasoline):
        // - If not ignited: marks as soaked-ready.
        // - If already burning (CLOTH or SOAKED): immediately upgrades/restarts enhanced SOAKED burn mode!
        if (FireWeaponHelper.isAlcohol(cursorStack) && FireWeaponHelper.isWrapped(targetStack) && !FireWeaponHelper.isInCharPhase(targetStack)) {
            boolean isBurning = FireWeaponHelper.isIgnited(targetStack);
            if (!isBurning && FireWeaponHelper.isSoakedReady(targetStack)) return false;

            if (isBurning) {
                FireWeaponHelper.soakAndIgnite(targetStack);
                player.world.playSound(player.posX, player.posY, player.posZ,
                        SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 0.8F, false);
            } else {
                FireWeaponHelper.markSoaked(targetStack);
            }

            if (!player.capabilities.isCreativeMode) {
                boolean returnBottle = cursorStack.getItem() == com.voltyx.mwccf.si.SIItems.WHISKEY || cursorStack.getItem() == com.voltyx.mwccf.si.SIItems.TEQUILA;
                cursorStack.shrink(1);
                if (returnBottle) {
                    ItemStack bottle = new ItemStack(com.voltyx.mwccf.si.SIItems.EMPTY_BOTTLE);
                    if (!player.inventory.addItemStackToInventory(bottle)) {
                        player.dropItem(bottle, false);
                    }
                }
            }

            player.world.playSound(player.posX, player.posY, player.posZ,
                    SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 0.8F, 0.9F, false);
            MwccfMod.PACKET_HANDLER.sendToServer(new PacketFireWeaponAction(container.windowId, slotId, PacketFireWeaponAction.ACTION_SOAK));
            return true;
        }

        // 4. Ignite soaked-ready cloth with flint.
        if (cursorStack.getItem() instanceof ItemFlintAndSteel
                && FireWeaponHelper.isSoakedReady(targetStack)
                && !FireWeaponHelper.isIgnited(targetStack)) {
            FireWeaponHelper.soakAndIgnite(targetStack);
            cursorStack.damageItem(1, player);
            player.world.playSound(player.posX, player.posY, player.posZ,
                    SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 0.8F, false);
            MwccfMod.PACKET_HANDLER.sendToServer(new PacketFireWeaponAction(container.windowId, slotId, PacketFireWeaponAction.ACTION_SOAK));
            return true;
        }

        return false;
    }

    /**
     * In-world hand interaction: cloth wrap, flint ignite, alcohol soak.
     */
    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player == null) return;

        ItemStack main = player.getHeldItemMainhand();
        ItemStack off = player.getHeldItemOffhand();
        ItemStack weapon = FireWeaponHelper.isWeapon(main) ? main
                : (FireWeaponHelper.isWeapon(off) ? off : ItemStack.EMPTY);
        if (weapon.isEmpty()) return;

        boolean weaponInMainHand = weapon == main;
        ItemStack held = weaponInMainHand ? off : main;
        EnumHand heldHand = weaponInMainHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        if (held.isEmpty()) return;

        // Cloth wraps once; self-igniting cloth starts the enhanced soaked burn mode.
        if (held.getItem() instanceof ItemCloth
                && (!FireWeaponHelper.isWrapped(weapon) || FireWeaponHelper.isInCharPhase(weapon))) {
            FireWeaponHelper.setWrapped(weapon, true);
            boolean flamingCloth = held.getItem() instanceof efw.item.ItemFlamingCloth;
            if (flamingCloth) FireWeaponHelper.soakAndIgnite(weapon);
            if (!player.capabilities.isCreativeMode) held.shrink(1);
            player.world.playSound(null, player.posX, player.posY, player.posZ,
                    SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0F, 1.2F);
            if (flamingCloth) {
                player.world.playSound(null, player.posX, player.posY, player.posZ,
                        SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 0.8F);
            }
            player.swingArm(heldHand);
            syncPlayerInventory(player);
            event.setCanceled(true);
            return;
        }

        if (held.getItem() instanceof ItemFlintAndSteel && FireWeaponHelper.isInCharPhase(weapon)) {
            event.setCanceled(true);
            return;
        }

        if (held.getItem() instanceof ItemFlintAndSteel && FireWeaponHelper.isSoakedReady(weapon)
                && !FireWeaponHelper.isIgnited(weapon) && !FireWeaponHelper.isInCharPhase(weapon)) {
            FireWeaponHelper.soakAndIgnite(weapon);
            held.damageItem(1, player);
            player.world.playSound(null, player.posX, player.posY, player.posZ,
                    SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 0.8F);
            player.swingArm(heldHand);
            syncPlayerInventory(player);
            event.setCanceled(true);
            return;
        }

        if (held.getItem() instanceof ItemFlintAndSteel && FireWeaponHelper.isWrapped(weapon)
                && !FireWeaponHelper.isIgnited(weapon) && !FireWeaponHelper.isInCharPhase(weapon)
                && !FireWeaponHelper.isSoakedReady(weapon)) {
            FireWeaponHelper.igniteCloth(weapon);
            held.damageItem(1, player);
            player.world.playSound(null, player.posX, player.posY, player.posZ,
                    SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
            player.swingArm(heldHand);
            syncPlayerInventory(player);
            event.setCanceled(true);
            return;
        }

        if (FireWeaponHelper.isAlcohol(held) && FireWeaponHelper.isWrapped(weapon) && !FireWeaponHelper.isInCharPhase(weapon)) {
            boolean isBurning = FireWeaponHelper.isIgnited(weapon);
            if (!isBurning && FireWeaponHelper.isSoakedReady(weapon)) {
                // Already soaked and waiting for flint & steel
                event.setCanceled(true);
                return;
            }

            if (isBurning) {
                FireWeaponHelper.soakAndIgnite(weapon);
                player.world.playSound(null, player.posX, player.posY, player.posZ,
                        SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 0.8F);
            } else {
                FireWeaponHelper.markSoaked(weapon);
            }

            if (!player.capabilities.isCreativeMode) {
                boolean returnBottle = held.getItem() == com.voltyx.mwccf.si.SIItems.WHISKEY || held.getItem() == com.voltyx.mwccf.si.SIItems.TEQUILA;
                held.shrink(1);
                if (returnBottle) {
                    ItemStack bottle = new ItemStack(com.voltyx.mwccf.si.SIItems.EMPTY_BOTTLE);
                    if (!player.inventory.addItemStackToInventory(bottle)) {
                        player.dropItem(bottle, false);
                    }
                }
            }
            player.world.playSound(null, player.posX, player.posY, player.posZ,
                    SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 0.8F, 0.9F);
            player.swingArm(heldHand);
            syncPlayerInventory(player);
            event.setCanceled(true);
        }
    }

    private static void syncPlayerInventory(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
        }
    }

    /**
     * Ticks burning weapons; handles rain/water extinguish and char phase.
     */
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayer player = event.player;
        if (player == null || player.isDead || player.world.isRemote) return;

        boolean isWet = player.isWet(); // in water, in rain, or under waterfall

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (FireWeaponHelper.isIgnited(stack)) {
                // Rain/water extinguish
                if (isWet) {
                    boolean soaked = FireWeaponHelper.isSoaked(stack);
                    FireWeaponHelper.extinguish(stack, soaked); // SOAKED: cloth gone; CLOTH: cloth preserved
                    player.world.playSound(null, player.posX, player.posY, player.posZ,
                            SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.7F, 1.3F);
                    continue;
                }

                // Normal tick
                boolean finished = FireWeaponHelper.tickBurn(stack);
                if (finished) {
                    player.world.playSound(null, player.posX, player.posY, player.posZ,
                            SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.7F, 1.3F);
                }

            } else if (FireWeaponHelper.isInCharPhase(stack)) {
                // Tick char fade even when not burning
                FireWeaponHelper.tickBurn(stack);
            }
        }
    }

    /**
     * On player death: extinguish all burning weapons, consuming cloth in SOAKED mode.
     */
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (FireWeaponHelper.isIgnited(stack)) {
                boolean soaked = FireWeaponHelper.isSoaked(stack);
                FireWeaponHelper.extinguish(stack, soaked);
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.world instanceof net.minecraft.world.WorldServer) {
            com.voltyx.mwccf.fireweapon.smolder.SmolderingManager.updateServer((net.minecraft.world.WorldServer) event.world);
        }
    }
}


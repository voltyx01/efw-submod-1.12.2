package efw.events;

import efw.util.WeaponSlotRestrictions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WeaponSlotEnforcer {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayer player = event.player;
        if (player == null || player.world.isRemote) return;

        InventoryPlayer inv = player.inventory;
        boolean changed = false;

        // 1. Chestplate slot (slot 38, index 2 in armorInventory)
        ItemStack chest = inv.armorInventory.get(2);
        if (WeaponSlotRestrictions.isBackpack(chest)) {
            inv.armorInventory.set(2, ItemStack.EMPTY);
            WeaponSlotRestrictions.relocateOrDrop(player, chest);
            changed = true;
        }

        // 2. Hotbar slot 0: Pistols forbidden
        ItemStack slot0 = inv.mainInventory.get(0);
        if (WeaponSlotRestrictions.isPistol(slot0)) {
            inv.mainInventory.set(0, ItemStack.EMPTY);
            WeaponSlotRestrictions.relocateOrDrop(player, slot0);
            changed = true;
        }

        // 3. Hotbar slot 1: Other MWC firearms forbidden
        ItemStack slot1 = inv.mainInventory.get(1);
        if (WeaponSlotRestrictions.isOtherMwcGun(slot1)) {
            inv.mainInventory.set(1, ItemStack.EMPTY);
            WeaponSlotRestrictions.relocateOrDrop(player, slot1);
            changed = true;
        }

        // 4. Hotbar slots 2..8: All MWC firearms forbidden
        for (int i = 2; i <= 8; i++) {
            ItemStack stack = inv.mainInventory.get(i);
            if (WeaponSlotRestrictions.isMwcGun(stack)) {
                inv.mainInventory.set(i, ItemStack.EMPTY);
                WeaponSlotRestrictions.relocateOrDrop(player, stack);
                changed = true;
            }
        }

        // 5. Offhand slot: All MWC firearms forbidden
        ItemStack offhand = inv.offHandInventory.get(0);
        if (WeaponSlotRestrictions.isMwcGun(offhand)) {
            inv.offHandInventory.set(0, ItemStack.EMPTY);
            WeaponSlotRestrictions.relocateOrDrop(player, offhand);
            changed = true;
        }

        if (changed) {
            player.inventoryContainer.detectAndSendChanges();
            if (player.openContainer != null && player.openContainer != player.inventoryContainer) {
                player.openContainer.detectAndSendChanges();
            }
        }
    }
}

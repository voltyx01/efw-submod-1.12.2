package efw.events;

import com.paneedah.weaponlib.Weapon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OffhandWeaponBlocker {

    @SubscribeEvent
    public void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer && event.getSlot() == EntityEquipmentSlot.OFFHAND) {
            ItemStack to = event.getTo();
            if (to != null && !to.isEmpty() && to.getItem() instanceof Weapon) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
                if (!player.inventory.addItemStackToInventory(to)) {
                    player.dropItem(to, false);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null && mc.gameSettings.keyBindSwapHands.isKeyDown()) {
            ItemStack main = mc.player.getHeldItemMainhand();
            ItemStack off = mc.player.getHeldItemOffhand();
            boolean mainIsWeapon = !main.isEmpty() && main.getItem() instanceof Weapon;
            boolean offIsWeapon = !off.isEmpty() && off.getItem() instanceof Weapon;
            if (mainIsWeapon || offIsWeapon) {
                // Drain keypress so vanilla doesn't swap weapon
                while (mc.gameSettings.keyBindSwapHands.isPressed()) {
                    // consumed
                }
            }
        }
    }
}

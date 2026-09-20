package com.voltyx.mwccf.armor;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.mcore.MCoreItems;
import com.voltyx.mwccf.network.PacketToggleNVG;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class NVGKeyHandler {

    public static final KeyBinding KEY_TOGGLE_NVG = new KeyBinding(
            "key.mwccf.nvg_toggle",
            Keyboard.KEY_N,
            "key.categories.movement");

    public static void register() {
        ClientRegistry.registerKeyBinding(KEY_TOGGLE_NVG);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KEY_TOGGLE_NVG.isPressed()) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;
            if (player == null) return;

            ItemStack helm = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (helm.isEmpty()) return;

            if (SurvivalInstinctArmorHandler.isNVGHelmet(helm.getItem())) {
                net.minecraft.nbt.NBTTagCompound tag = helm.hasTagCompound() ? helm.getTagCompound() : new net.minecraft.nbt.NBTTagCompound();
                int charge = tag.hasKey("battery_charge") ? tag.getInteger("battery_charge") : 0;
                boolean current = tag.hasKey("nv_active") && tag.getBoolean("nv_active") && charge > 0;

                if (!current && charge <= 0) {
                    mc.ingameGUI.setOverlayMessage(
                            "\u00a7c" + net.minecraft.client.resources.I18n.format("tooltip.mcore.battery.required"), false);
                    mc.player.playSound(net.minecraft.init.SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.6F);
                    return;
                }

                boolean newState = !current;
                tag.setBoolean("nv_active", newState);
                helm.setTagCompound(tag);
                if (!newState) {
                    player.removePotionEffect(net.minecraft.init.MobEffects.NIGHT_VISION);
                }
                MwccfMod.PACKET_HANDLER.sendToServer(new PacketToggleNVG());
            }
        }
    }
}

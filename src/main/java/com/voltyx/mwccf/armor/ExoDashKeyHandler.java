package com.voltyx.mwccf.armor;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.network.PacketExoDash;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ExoDashKeyHandler {

    public static final KeyBinding KEY_EXO_DASH = new KeyBinding(
            "key.mwccf.exo_dash",
            Keyboard.KEY_X,
            "key.categories.movement");

    public static void register() {
        ClientRegistry.registerKeyBinding(KEY_EXO_DASH);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KEY_EXO_DASH.isPressed()) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;
            if (player == null) return;

            if (SurvivalInstinctArmorHandler.isWearingFullExo(player)) {
                if (SurvivalInstinctArmorHandler.getExoDashCooldown(player) <= 0) {
                    MwccfMod.PACKET_HANDLER.sendToServer(new PacketExoDash());
                }
            }
        }
    }
}

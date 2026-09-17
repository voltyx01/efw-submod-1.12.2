package com.voltyx.mwccf.walkietalkie;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class WalkieTalkieKeyHandler {

    public static final KeyBinding WALKIE_TALKIE_TOGGLE_KEY = new KeyBinding(
            "key.mwccf.walkie_talkie_toggle",
            Keyboard.KEY_B,
            "key.categories.mwccf"
    );

    public static void init() {
        ClientRegistry.registerKeyBinding(WALKIE_TALKIE_TOGGLE_KEY);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (WALKIE_TALKIE_TOGGLE_KEY.isPressed()) {
            PacketToggleWalkieTalkie.sendToggle();
        }
    }
}

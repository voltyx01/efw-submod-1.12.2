package com.voltyx.mwccf.geo;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class HeadlampKeyHandler {

    public static final KeyBinding HEADLAMP_TOGGLE_KEY = new KeyBinding("key.headlamp.toggle", Keyboard.KEY_NONE, "key.categories.mwccf");

    public static void init() {
        ClientRegistry.registerKeyBinding(HEADLAMP_TOGGLE_KEY);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (HEADLAMP_TOGGLE_KEY.isPressed()) {
            HeadlampNetwork.sendTogglePacket();
        }
    }
}

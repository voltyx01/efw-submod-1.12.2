package com.voltyx.mwccf.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class LocksMovementFixHandler {

    private static int resetTicksRemaining = 0;

    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen != null && mc.currentScreen.getClass().getName().contains("LockPickingGui")) {
            // LockPickingGui is closing or being replaced
            resetTicksRemaining = 3;
            resetMovementKeys(mc);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && resetTicksRemaining > 0) {
            Minecraft mc = Minecraft.getMinecraft();
            resetMovementKeys(mc);
            resetTicksRemaining--;
        }
    }

    private static void resetMovementKeys(Minecraft mc) {
        if (mc.gameSettings != null) {
            KeyBinding.unPressAllKeys();
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
        }

        if (mc.player != null && mc.player.movementInput != null) {
            mc.player.movementInput.moveForward = 0.0F;
            mc.player.movementInput.moveStrafe = 0.0F;
            mc.player.movementInput.forwardKeyDown = false;
            mc.player.movementInput.backKeyDown = false;
            mc.player.movementInput.leftKeyDown = false;
            mc.player.movementInput.rightKeyDown = false;
            mc.player.movementInput.jump = false;
            mc.player.movementInput.sneak = false;
        }
    }
}

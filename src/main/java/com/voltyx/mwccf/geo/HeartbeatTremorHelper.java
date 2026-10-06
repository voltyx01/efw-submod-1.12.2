package com.voltyx.mwccf.geo;

import com.paneedah.weaponlib.PlayerWeaponInstance;
import com.paneedah.weaponlib.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class HeartbeatTremorHelper {

    public static void applyTremor(RenderContext<?> renderContext) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.player == null || mc.isGamePaused()) {
            return;
        }

        float bpm = HeartbeatManager.currentBPM;
        if (bpm <= 110f) {
            return;
        }

        // Intensity: 0.0 at 110 BPM -> 1.0 at 175+ BPM
        float intensity = Math.min(1.0f, Math.max(0.0f, (bpm - 110f) / 65f));
        if (intensity <= 0.001f) {
            return;
        }

        long now = System.currentTimeMillis();
        double t = now * 0.001;

        // Tremor frequency slightly increases under intense adrenaline
        double freqMult = 1.0 + intensity * 0.35;

        // Multi-frequency organic nervous muscle tremor (8-14 Hz)
        double jX = Math.sin(t * 52.0 * freqMult) * 0.65 + Math.sin(t * 79.0 * freqMult) * 0.35;
        double jY = Math.cos(t * 61.0 * freqMult) * 0.65 + Math.sin(t * 93.0 * freqMult) * 0.35;
        double jRotX = Math.cos(t * 47.0 * freqMult) * 0.70 + Math.sin(t * 83.0 * freqMult) * 0.30;
        double jRotY = Math.sin(t * 55.0 * freqMult) * 0.70 + Math.cos(t * 73.0 * freqMult) * 0.30;
        double jRotZ = Math.sin(t * 41.0 * freqMult) * 0.75 + Math.cos(t * 67.0 * freqMult) * 0.25;

        // Ballistocardiographic pulse bump with each heartbeat thump
        long timeSinceBeat = now - HeartbeatManager.lastBeatTime;
        float pulse = 0f;
        if (timeSinceBeat >= 0 && timeSinceBeat < 300) {
            float progress = timeSinceBeat / 300f;
            pulse = (float) (Math.sin(progress * Math.PI) * Math.exp(-progress * 3.5));
        }

        boolean isAiming = false;
        if (renderContext != null && renderContext.getWeaponInstance() instanceof PlayerWeaponInstance) {
            isAiming = ((PlayerWeaponInstance) renderContext.getWeaponInstance()).isAimed();
        }

        if (isAiming) {
            // ADS: Lower translation to keep sight alignment, but subtle angular tremor wobbles reticle
            float transScale = 0.0035f * intensity;
            float rotScale = 0.32f * intensity;

            float posX = (float) (jX * transScale);
            float posY = (float) (jY * transScale + pulse * 0.002f * intensity);
            float posZ = (float) (pulse * 0.0015f * intensity);

            GL11.glTranslatef(posX, posY, posZ);
            GL11.glRotatef((float) (jRotX * rotScale + pulse * 0.25f * intensity), 1f, 0f, 0f);
            GL11.glRotatef((float) (jRotY * rotScale * 0.75), 0f, 1f, 0f);
            GL11.glRotatef((float) (jRotZ * rotScale * 0.6), 0f, 0f, 1f);
        } else {
            // Hip-fire / Ready: Noticeable hand tremor and heartbeat twitch
            float transScale = 0.010f * intensity;
            float rotScale = 0.65f * intensity;

            float posX = (float) (jX * transScale);
            float posY = (float) (jY * transScale + pulse * 0.005f * intensity);
            float posZ = (float) (pulse * 0.003f * intensity);

            GL11.glTranslatef(posX, posY, posZ);
            GL11.glRotatef((float) (jRotX * rotScale + pulse * 0.45f * intensity), 1f, 0f, 0f);
            GL11.glRotatef((float) (jRotY * rotScale * 0.8), 0f, 1f, 0f);
            GL11.glRotatef((float) (jRotZ * rotScale), 0f, 0f, 1f);
        }
    }
}

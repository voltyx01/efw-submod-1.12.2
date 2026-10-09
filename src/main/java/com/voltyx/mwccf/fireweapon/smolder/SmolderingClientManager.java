package com.voltyx.mwccf.fireweapon.smolder;

import com.voltyx.mwccf.fireweapon.client.FireWeaponSparkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Client-side tracker for smoldering visual effects and spark emissions on burned entities.
 */
@SideOnly(Side.CLIENT)
public class SmolderingClientManager {

    public static class ClientSmolder {
        public int ticksRemaining;
        public int totalTicks;
        public final List<BurnMark> marks = new ArrayList<>();
        public final DynamicTexture texture = new DynamicTexture(64, 64);
        public final DynamicTexture emissiveTexture = new DynamicTexture(64, 64);
        public final ResourceLocation textureLocation;
        public final ResourceLocation emissiveTextureLocation;

        public ClientSmolder(int entityId, int ticks) {
            this.ticksRemaining = ticks;
            this.totalTicks = Math.max(1, ticks);
            this.textureLocation = Minecraft.getMinecraft().getTextureManager()
                    .getDynamicTextureLocation("mwccf_smolder_" + entityId, texture);
                this.emissiveTextureLocation = Minecraft.getMinecraft().getTextureManager()
                    .getDynamicTextureLocation("mwccf_smolder_glow_" + entityId, emissiveTexture);
        }

        public float getIntensity() {
            return (float) ticksRemaining / (float) totalTicks;
        }
    }

    private static class BurnMark {
        final float u;
        final float v;
        final boolean horizontal;
        final int seed;
        int age = 1;

        BurnMark(float u, float v, boolean horizontal, int seed) {
            this.u = u;
            this.v = v;
            this.horizontal = horizontal;
            this.seed = seed;
        }
    }

    private static final Map<Integer, ClientSmolder> CLIENT_SMOLDERS = new ConcurrentHashMap<>();
    private static final Random RAND = new Random();

    public static void applySmolder(int entityId, int durationTicks, float markU, float markV, boolean soaked, int seed) {
        ClientSmolder smolder = CLIENT_SMOLDERS.get(entityId);
        if (smolder == null) {
            smolder = new ClientSmolder(entityId, durationTicks);
            CLIENT_SMOLDERS.put(entityId, smolder);
        }
        smolder.ticksRemaining = durationTicks;
        smolder.totalTicks = Math.max(1, durationTicks);
        smolder.marks.add(new BurnMark(markU, markV, soaked, seed));
        updateOverlay(smolder);
    }

    public static boolean isSmoldering(EntityLivingBase entity) {
        if (entity == null) return false;
        ClientSmolder s = CLIENT_SMOLDERS.get(entity.getEntityId());
        return s != null && s.ticksRemaining > 0;
    }

    public static float getSmolderIntensity(EntityLivingBase entity) {
        if (entity == null) return 0.0F;
        ClientSmolder s = CLIENT_SMOLDERS.get(entity.getEntityId());
        return s != null ? s.getIntensity() : 0.0F;
    }

    public static ResourceLocation getOverlayTexture(EntityLivingBase entity) {
        if (entity == null) return null;
        ClientSmolder smolder = CLIENT_SMOLDERS.get(entity.getEntityId());
        return smolder != null && !smolder.marks.isEmpty() ? smolder.textureLocation : null;
    }

    public static ResourceLocation getEmissiveTexture(EntityLivingBase entity) {
        if (entity == null) return null;
        ClientSmolder smolder = CLIENT_SMOLDERS.get(entity.getEntityId());
        return smolder != null && !smolder.marks.isEmpty() ? smolder.emissiveTextureLocation : null;
    }

    public static void updateClient() {
        Minecraft mc = Minecraft.getMinecraft();
        if (CLIENT_SMOLDERS.isEmpty()) return;

        Iterator<Map.Entry<Integer, ClientSmolder>> it = CLIENT_SMOLDERS.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, ClientSmolder> entry = it.next();
            Entity entity = mc.world == null ? null : mc.world.getEntityByID(entry.getKey());
            ClientSmolder s = entry.getValue();

            if (!(entity instanceof EntityLivingBase) || entity.isDead) {
                mc.getTextureManager().deleteTexture(s.textureLocation);
                mc.getTextureManager().deleteTexture(s.emissiveTextureLocation);
                it.remove();
                continue;
            }

            boolean hasLiveEmbers = false;
            for (BurnMark mark : s.marks) {
                if (mark.age < 100) {
                    mark.age++;
                    hasLiveEmbers = true;
                    EntityLivingBase living = (EntityLivingBase) entity;

                    // Compute base impact location on mob surface using body rotation
                    double angle = ((mark.u - 0.25D) / 0.375D - 0.5D) * Math.PI * 2.0D;
                    double bodyRotation = Math.toRadians(180.0D - living.renderYawOffset);
                    double localX = Math.cos(angle);
                    double localZ = Math.sin(angle);
                    double worldX = Math.cos(bodyRotation) * localX + Math.sin(bodyRotation) * localZ;
                    double worldZ = -Math.sin(bodyRotation) * localX + Math.cos(bodyRotation) * localZ;
                    float baseHeight = Math.max(0.2F, Math.min(0.85F, 1.0F - (mark.v - 0.25F) / 0.25F));

                    // Natural variation: sparks shouldn't all spawn from the exact same point or always at the bottom.
                    // Randomly offset slightly in front, to the sides, or around the mob.
                    double forwardYaw = Math.toRadians(180.0D - living.rotationYaw);
                    double forwardX = Math.sin(forwardYaw);
                    double forwardZ = -Math.cos(forwardYaw);
                    double rightX = -forwardZ;
                    double rightZ = forwardX;

                    // Jitter & forward/side projection:
                    // 40% chance of protruding forward in front of the mob (e.g. chest/face/front side)
                    double frontOffset = (RAND.nextFloat() < 0.45F) ? (0.10D + RAND.nextDouble() * 0.28D) : 0.0D;
                    double lateralJitter = (RAND.nextDouble() - 0.5D) * living.width * 0.5D;
                    double heightJitter = (RAND.nextDouble() - 0.5D) * living.height * 0.35D;

                    double sx = living.posX + worldX * living.width * 0.45D + forwardX * frontOffset + rightX * lateralJitter;
                    double sy = living.posY + living.height * baseHeight + heightJitter;
                    double sz = living.posZ + worldZ * living.width * 0.45D + forwardZ * frontOffset + rightZ * lateralJitter;

                    // Ensure spawn height stays within reasonable bounds around the mob
                    sy = Math.max(living.posY + 0.15D, Math.min(living.posY + living.height + 0.25D, sy));

                    // Velocity: upward buoyant drift + outward velocity from mob center + slight jitter
                    double outwardX = sx - living.posX;
                    double outwardZ = sz - living.posZ;
                    double outLen = Math.sqrt(outwardX * outwardX + outwardZ * outwardZ);
                    float outNormX = outLen > 0.001D ? (float) (outwardX / outLen) : 0.0F;
                    float outNormZ = outLen > 0.001D ? (float) (outwardZ / outLen) : 0.0F;

                    float vx = outNormX * (0.05F + RAND.nextFloat() * 0.15F) + (RAND.nextFloat() - 0.5F) * 0.16F;
                    float vy = 0.30F + RAND.nextFloat() * 0.50F;
                    float vz = outNormZ * (0.05F + RAND.nextFloat() * 0.15F) + (RAND.nextFloat() - 0.5F) * 0.16F;

                    if (mc.player != null) {
                        vx -= (float) mc.player.motionX * 0.7F;
                        vy += Math.max(0.0F, (float) mc.player.motionY) * 0.25F;
                        vz -= (float) mc.player.motionZ * 0.7F;
                    }
                    FireWeaponSparkManager.spawnSpark((float) sx, (float) sy, (float) sz, vx, vy, vz);
                }
            }
            if (s.ticksRemaining > 0) {
                s.ticksRemaining--;
                hasLiveEmbers = true;
            }
        }
    }

    private static void updateOverlay(ClientSmolder smolder) {
        int[] pixels = smolder.texture.getTextureData();
        int[] emissivePixels = smolder.emissiveTexture.getTextureData();
        Arrays.fill(pixels, 0);
        Arrays.fill(emissivePixels, 0);

        for (BurnMark mark : smolder.marks) {
            int centerX = Math.max(0, Math.min(63, Math.round(mark.u * 64.0F)));
            int centerY = Math.max(0, Math.min(63, Math.round(mark.v * 64.0F)));
            float radiusX = mark.horizontal ? 9.0F : 6.0F;
            float radiusY = mark.horizontal ? 3.5F : 5.5F;
            float growth = Math.min(1.0F, mark.age / 14.0F);

            for (int y = Math.max(0, centerY - (int) radiusY - 1); y <= Math.min(63, centerY + (int) radiusY + 1); y++) {
                for (int x = Math.max(0, centerX - (int) radiusX - 1); x <= Math.min(63, centerX + (int) radiusX + 1); x++) {
                    float dx = (x - centerX) / radiusX;
                    float dy = (y - centerY) / radiusY;
                    float angle = (float) Math.atan2(dy, dx);
                    float edgeNoise = (float) Math.sin(angle * 3.0F + mark.seed) * 0.11F
                            + (float) Math.cos(angle * 5.0F + mark.seed * 0.37F) * 0.07F
                            + (float) Math.sin(angle * 11.0F + mark.seed * 0.73F) * 0.035F;
                    float edge = 0.82F + edgeNoise;
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
                    if (distance > edge * growth) continue;

                    float grain = (float) Math.sin(x * 12.9898F + y * 78.233F + mark.seed * 0.17F);
                    grain = grain * 43758.5453F;
                    grain -= (float) Math.floor(grain);
                    float sootNoise = (float) Math.sin(x * 0.71F + y * 1.13F + mark.seed)
                            + (float) Math.cos(x * 1.37F - y * 0.83F + mark.seed * 0.41F);
                    int shade = Math.max(20, Math.min(92, (int) (25.0F + grain * 42.0F + sootNoise * 9.0F)));
                    int color = 0xFF000000 | (shade << 16) | (shade << 8) | shade;
                    float flicker = (float) Math.sin(x * 1.7F + y * 2.3F + mark.seed + mark.age * 0.48F);
                    boolean ember = mark.age < 100 && distance > edge * growth * 0.30F && flicker > 0.79F;
                    int index = y * 64 + x;
                    if (pixels[index] == 0 || ember) {
                        pixels[index] = color;
                        if (ember) emissivePixels[index] = 0xFFFF7620;
                    }
                }
            }
        }
        smolder.texture.updateDynamicTexture();
        smolder.emissiveTexture.updateDynamicTexture();
    }
}

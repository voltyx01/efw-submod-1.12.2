package com.voltyx.mwccf.client;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.network.PacketLootingComplete;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ClientLootingManager {

    private static final ResourceLocation CABINET_OPEN_SOUND_ID = new ResourceLocation("cfm", "cabinet_open");
    private static final ResourceLocation CABINET_CLOSE_SOUND_ID = new ResourceLocation("cfm", "cabinet_close");
    private static final ResourceLocation LOOT_PROGRESS_SOUND_ID = new ResourceLocation("mwccf", "loot.lootprog");
    private static final ResourceLocation CRATE_BLOCK_ID = new ResourceLocation("cfm", "crate");

    private static BlockPos targetBlock = null;
    private static int lootProgress = 0;
    private static final int REQUIRED_TICKS = 30; // 40 тиков (2 секунды)

    private static boolean suppressNextOpenSound = false;
    private static long suppressUntilMillis = 0L;
    private static final long SUPPRESS_WINDOW_MS = 900L;
    private static BlockPos lastLootedPos = null;
    private static final double POS_MATCH_RADIUS_SQ = 16.0;

    private static BlockPos lastCompletedLootPos = null;
    private static long lastCompletedLootTime = 0L;
    private static final long COOLDOWN_MS = 1500L;

    private static ISound activeLootSound = null;

    public static void startLooting(BlockPos pos) {
        if (pos.equals(lastCompletedLootPos) && (System.currentTimeMillis() - lastCompletedLootTime < COOLDOWN_MS)) {
            return;
        }

        if (targetBlock == null || !targetBlock.equals(pos)) {
            stopActiveLootSound();
            targetBlock = pos;
            lootProgress = 0;
            playLootStartSounds(pos);
        }
    }

    private static void stopActiveLootSound() {
        if (activeLootSound != null) {
            Minecraft.getMinecraft().getSoundHandler().stopSound(activeLootSound);
            activeLootSound = null;
        }
    }

    private static void playLootStartSounds(BlockPos pos) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) {
            return;
        }

        IBlockState state = mc.world.getBlockState(pos);
        ResourceLocation blockId = state.getBlock().getRegistryName();
        boolean isCrate = blockId != null && blockId.equals(CRATE_BLOCK_ID);

        if (!isCrate) {
            suppressNextOpenSound = false;

            SoundEvent openSound = SoundEvent.REGISTRY.getObject(CABINET_OPEN_SOUND_ID);
            if (openSound != null) {
                mc.world.playSound(mc.player, pos, openSound, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            suppressNextOpenSound = true;
            suppressUntilMillis = System.currentTimeMillis() + SUPPRESS_WINDOW_MS;
            lastLootedPos = pos;
        }
    }

    private static void playLootInterruptSound(BlockPos pos) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null || pos == null) {
            return;
        }

        IBlockState state = mc.world.getBlockState(pos);
        ResourceLocation blockId = state.getBlock().getRegistryName();
        boolean isCrate = blockId != null && blockId.equals(CRATE_BLOCK_ID);

        if (!isCrate) {
            SoundEvent closeSound = SoundEvent.REGISTRY.getObject(CABINET_CLOSE_SOUND_ID);
            if (closeSound != null) {
                mc.world.playSound(mc.player, pos, closeSound, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @SubscribeEvent
    public void onClientInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote && targetBlock != null) {
            if (event.getPos().equals(targetBlock)) {
                event.setCanceled(true);

                Minecraft mc = Minecraft.getMinecraft();
                if (mc.player != null) {
                    mc.player.isSwingInProgress = false;
                    mc.player.swingProgressInt = 0;
                    mc.player.swingProgress = 0.0F;
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || targetBlock == null)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null)
            return;

        boolean isRightClicking = mc.gameSettings.keyBindUseItem.isKeyDown();

        boolean lookingAtTarget = false;
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            if (mc.objectMouseOver.getBlockPos().equals(targetBlock)) {
                lookingAtTarget = true;
            }
        }

        if (isRightClicking && lookingAtTarget) {
            lootProgress++;

            if (lootProgress == 1) {
                SoundEvent lootSound = SoundEvent.REGISTRY.getObject(LOOT_PROGRESS_SOUND_ID);
                if (lootSound != null) {
                    activeLootSound = new PositionedSoundRecord(lootSound, SoundCategory.BLOCKS, 1.0F, 1.0F,
                            targetBlock.getX() + 0.5F, targetBlock.getY() + 0.5F, targetBlock.getZ() + 0.5F);
                    mc.getSoundHandler().playSound(activeLootSound);
                }
            }

            if (lootProgress >= REQUIRED_TICKS) {
                MwccfMod.PACKET_HANDLER.sendToServer(new PacketLootingComplete(targetBlock));

                if (suppressNextOpenSound) {
                    suppressUntilMillis = System.currentTimeMillis() + SUPPRESS_WINDOW_MS;
                }

                lastCompletedLootPos = targetBlock;
                lastCompletedLootTime = System.currentTimeMillis();

                stopActiveLootSound();
                targetBlock = null;
                lootProgress = 0;
            }
        } else {
            stopActiveLootSound();
            playLootInterruptSound(targetBlock);
            targetBlock = null;
            lootProgress = 0;
        }
    }

    @SubscribeEvent
    public void onPlaySound(PlaySoundEvent event) {
        if (!suppressNextOpenSound || event.getSound() == null) {
            return;
        }

        ISound sound = event.getSound();

        if (System.currentTimeMillis() > suppressUntilMillis) {
            suppressNextOpenSound = false;
            return;
        }

        ResourceLocation soundLocation = sound.getSoundLocation();
        boolean nameMatches = soundLocation != null && soundLocation.equals(CABINET_OPEN_SOUND_ID);

        boolean posMatches = true;
        if (lastLootedPos != null) {
            double dx = sound.getXPosF() - (lastLootedPos.getX() + 0.5);
            double dy = sound.getYPosF() - (lastLootedPos.getY() + 0.5);
            double dz = sound.getZPosF() - (lastLootedPos.getZ() + 0.5);
            double distSq = dx * dx + dy * dy + dz * dz;
            posMatches = distSq <= POS_MATCH_RADIUS_SQ;
        }

        if (nameMatches && posMatches) {
            event.setResultSound(null);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || targetBlock == null)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = event.getResolution();

        // Теперь координаты рассчитываются как дробные числа (float)
        float x = sr.getScaledWidth() / 2.0f + 1.0f;
        float y = sr.getScaledHeight() / 2.0f + 0.5f;

        // Сдвигаем на полпикселя влево и вверх (можешь менять эти значения)
        x -= 0.5f;
        y -= 0.5f;

        float progress = lootProgress / (float) REQUIRED_TICKS;
        float radius = 6.0f;
        float thickness = 2.5f;

        // Рисуем фоновое полупрозрачное черное кольцо
        drawThickArc(x, y, radius, thickness, 1.0f, 0x88000000);

        // Рисуем заполняющееся белое кольцо прогресса
        drawThickArc(x, y, radius, thickness, progress, 0xFFFFFFFF);

        // Текст под прицелом
        String lootText = net.minecraft.client.resources.I18n.format("gui.mwccf.looting");
        mc.fontRenderer.drawStringWithShadow(lootText, x - mc.fontRenderer.getStringWidth(lootText) / 2.0f, y + 20,
                0xFFFFFF);
    }

    // Хелпер теперь принимает координаты x и y как float!
    private void drawThickArc(float x, float y, float radius, float thickness, float progress, int color) {
        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.color(red, green, blue, alpha);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION);

        int segments = 40;
        float targetAngle = 360.0F * progress;
        float step = 360.0F / segments;
        float innerRadius = radius - thickness / 2.0F;
        float outerRadius = radius + thickness / 2.0F;

        for (float i = 0; i < targetAngle + step; i += step) {
            float currentAngle = (i > targetAngle) ? targetAngle : i;
            double rad = (currentAngle - 90) * Math.PI / 180.0;

            buffer.pos(x + Math.cos(rad) * outerRadius, y + Math.sin(rad) * outerRadius, 0.0D).endVertex();
            buffer.pos(x + Math.cos(rad) * innerRadius, y + Math.sin(rad) * innerRadius, 0.0D).endVertex();

            if (currentAngle == targetAngle)
                break;
        }

        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
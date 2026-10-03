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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
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
                boolean isRadio = mc.world != null && targetBlock != null && mc.world.getBlockState(targetBlock).getBlock() instanceof com.voltyx.mwccf.radio.BlockOldRadio;
                MwccfMod.PACKET_HANDLER.sendToServer(new PacketLootingComplete(targetBlock));

                if (isRadio) {
                    if (mc.player != null) {
                        mc.player.playSound(efw.init.EfwModSounds.DIARYOPEN, 1.5f, 1.0f);
                    }
                    com.voltyx.mwccf.client.inspect.InspectTransitionHandler.startTransition(
                            new net.minecraft.item.ItemStack(com.voltyx.mwccf.mcore.MCoreItems.RADIO_BOARD), null);
                }

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
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null)
            return;

        ScaledResolution sr = event.getResolution();
        float x = sr.getScaledWidth() / 2.0f + 0.5f;
        float y = sr.getScaledHeight() / 2.0f;

        float size = 7.0f;
        float thickness = 2.0f;

        if (targetBlock != null) {
            float progress = MathHelper.clamp(lootProgress / (float) REQUIRED_TICKS, 0.0f, 1.0f);
            drawSquareProgressBar(x, y, size, thickness, progress);

            // Текст под прицелом (не рисуем для старого радио)
            boolean isRadio = mc.world != null && mc.world.getBlockState(targetBlock).getBlock() instanceof com.voltyx.mwccf.radio.BlockOldRadio;
            if (!isRadio) {
                String lootText = net.minecraft.client.resources.I18n.format("gui.mwccf.looting");
                mc.fontRenderer.drawStringWithShadow(lootText, x - mc.fontRenderer.getStringWidth(lootText) / 2.0f, y + 20,
                        0xFFFFFF);
            }
        } else if (mc.player.isHandActive()) {
            ItemStack activeStack = mc.player.getActiveItemStack();
            if (isMedicalItem(activeStack)) {
                int totalDuration = getMedicalItemDuration(activeStack);
                int inUseCount = mc.player.getItemInUseCount();
                int elapsed = activeStack.getMaxItemUseDuration() - inUseCount;
                float progress = MathHelper.clamp(elapsed / (float) totalDuration, 0.0f, 1.0f);
                drawSquareProgressBar(x, y, size, thickness, progress);
            }
        }
    }

    public static boolean isMedicalItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        Item item = stack.getItem();
        if (item instanceof com.voltyx.mwccf.item.ItemAdrenaline
                || item instanceof com.voltyx.mwccf.item.ItemMorphineSyringe
                || item instanceof efw.item.ItemBandage
                || item instanceof efw.item.ItemMedKit) {
            return true;
        }
        ResourceLocation reg = item.getRegistryName();
        if (reg != null) {
            String path = reg.getPath().toLowerCase();
            return path.contains("adrenaline")
                    || path.contains("morphine")
                    || path.contains("bandage")
                    || path.contains("med_kit")
                    || path.contains("medkit")
                    || path.contains("plaster");
        }
        return false;
    }

    public static int getMedicalItemDuration(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 20;
        Item item = stack.getItem();
        if (item instanceof efw.item.ItemBandage || item instanceof efw.item.ItemMedKit) {
            return 60;
        }
        int max = stack.getMaxItemUseDuration();
        return max > 0 ? max : 20;
    }

    public static void drawSquareProgressBar(float cx, float cy, float size, float thickness, float progress) {
        if (progress <= 0.0F) return;
        progress = Math.min(progress, 1.0F);

        float s = size;
        float t = thickness;
        float totalPerimeter = 8.0F * s;
        float d = progress * totalPerimeter;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableBlend(); // Чистый непрозрачный белый цвет без альфы
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        // 1. Верхнее ребро: слева направо (-s, -s) -> (+s, -s)
        float d1 = Math.min(d, 2.0F * s);
        if (d1 > 0.0F) {
            drawQuad(buffer, cx - s, cy - s, cx - s + d1, cy - s + t);
        }

        // 2. Правое ребро: сверху вниз (+s, -s) -> (+s, +s)
        if (d > 2.0F * s) {
            float d2 = Math.min(d - 2.0F * s, 2.0F * s);
            if (d2 > 0.0F) {
                drawQuad(buffer, cx + s - t, cy - s, cx + s, cy - s + d2);
            }
        }

        // 3. Нижнее ребро: справа налево (+s, +s) -> (-s, +s)
        if (d > 4.0F * s) {
            float d3 = Math.min(d - 4.0F * s, 2.0F * s);
            if (d3 > 0.0F) {
                drawQuad(buffer, cx + s - d3, cy + s - t, cx + s, cy + s);
            }
        }

        // 4. Левое ребро: снизу вверх (-s, +s) -> (-s, -s)
        if (d > 6.0F * s) {
            float d4 = Math.min(d - 6.0F * s, 2.0F * s);
            if (d4 > 0.0F) {
                drawQuad(buffer, cx - s, cy + s - d4, cx - s + t, cy + s);
            }
        }

        tessellator.draw();

        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private static void drawQuad(BufferBuilder buffer, float x0, float y0, float x1, float y1) {
        float minX = Math.min(x0, x1);
        float maxX = Math.max(x0, x1);
        float minY = Math.min(y0, y1);
        float maxY = Math.max(y0, y1);

        buffer.pos(minX, maxY, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(maxX, maxY, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(maxX, minY, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.pos(minX, minY, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
    }
}
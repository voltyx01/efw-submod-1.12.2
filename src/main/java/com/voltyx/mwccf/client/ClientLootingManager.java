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
    private static int openPauseTicks = 0;
    private static final int OPEN_PAUSE_TICKS_DEFAULT = 12; // ~0.6 сек пауза, чтобы звук открытия блока успел проиграться
    private static final int REQUIRED_TICKS = 30; // 30 тиков (1.5 секунды)

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
            SoundEvent openSound = playLootStartSounds(pos);
            openPauseTicks = openSound != null ? OPEN_PAUSE_TICKS_DEFAULT : 0;
        }
    }

    private static void stopActiveLootSound() {
        if (activeLootSound != null) {
            Minecraft.getMinecraft().getSoundHandler().stopSound(activeLootSound);
            activeLootSound = null;
        }
    }

    private static ResourceLocation lastTargetOpenSoundId = null;

    private static SoundEvent getBlockOpenSound(IBlockState state, BlockPos pos) {
        if (state == null) return null;
        net.minecraft.block.Block block = state.getBlock();
        if (block instanceof com.voltyx.mwccf.radio.BlockOldRadio) {
            return null;
        }

        Minecraft mc = Minecraft.getMinecraft();
        net.minecraft.tileentity.TileEntity te = (mc.world != null && pos != null) ? mc.world.getTileEntity(pos) : null;
        if (te != null) {
            if (te instanceof com.voltyx.mwccf.furniture.tileentity.TileEntityFridge) {
                return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_FRIDGE_OPEN;
            }
            if (te instanceof com.voltyx.mwccf.furniture.tileentity.TileEntityMicrowave) {
                return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_MICROWAVE_OPEN;
            }
            if (te instanceof com.voltyx.mwccf.furniture.tileentity.TileEntityStove) {
                return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_STOVE_OPEN;
            }
            if (te instanceof com.voltyx.mwccf.furniture.tileentity.TileEntityCabinet) {
                if (block instanceof com.voltyx.mwccf.furniture.BlockCooler) {
                    return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_COOLER_OPEN;
                } else if (block instanceof com.voltyx.mwccf.furniture.BlockKitchenCabinetry) {
                    return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_KITCHEN_DRAWER_OPEN;
                } else {
                    return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_OPEN;
                }
            }
            if (te instanceof net.minecraft.tileentity.TileEntityChest) {
                return net.minecraft.init.SoundEvents.BLOCK_CHEST_OPEN;
            }
            if (te instanceof net.minecraft.tileentity.TileEntityEnderChest) {
                return net.minecraft.init.SoundEvents.BLOCK_ENDERCHEST_OPEN;
            }
            if (te instanceof net.minecraft.tileentity.TileEntityShulkerBox) {
                return net.minecraft.init.SoundEvents.BLOCK_SHULKER_BOX_OPEN;
            }

            String teName = te.getClass().getName();
            if (teName.equals("com.mrcrayfish.furniture.tileentity.TileEntityBin")) {
                SoundEvent bin = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "bin_open"));
                return bin != null ? bin : com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_OPEN;
            }
            if (teName.startsWith("com.mrcrayfish.furniture.tileentity.")) {
                SoundEvent cfmCab = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "cabinet_open"));
                if (cfmCab != null) return cfmCab;
            }
        }

        ResourceLocation reg = block.getRegistryName();
        String domain = reg != null ? reg.getNamespace() : "";
        String path = reg != null ? reg.getPath().toLowerCase(java.util.Locale.ROOT) : "";

        if (block instanceof com.voltyx.mwccf.furniture.BlockCooler || path.contains("cooler") || path.contains("esky")) {
            if ("cfm".equals(domain)) {
                SoundEvent cfmCab = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "cabinet_open"));
                if (cfmCab != null) return cfmCab;
            }
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_COOLER_OPEN;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockFridge || path.contains("fridge") || path.contains("freezer")) {
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_FRIDGE_OPEN;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockKitchenCabinetry || path.contains("drawer")) {
            if ("cfm".equals(domain)) {
                SoundEvent cfmCab = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "cabinet_open"));
                if (cfmCab != null) return cfmCab;
            }
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_KITCHEN_DRAWER_OPEN;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockMicrowave || path.contains("microwave")) {
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_MICROWAVE_OPEN;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockStove || path.contains("stove") || path.contains("oven")) {
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_STOVE_OPEN;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockRecycleBin || path.contains("bin") || path.contains("trash")) {
            SoundEvent bin = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "bin_open"));
            return bin != null ? bin : com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_OPEN;
        }
        if (path.contains("sliding_door")) {
            SoundEvent door = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "sliding_door_open"));
            return door != null ? door : com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_OPEN;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockCrate || path.contains("crate")) {
            if ("cfm".equals(domain)) {
                SoundEvent cfmCab = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "cabinet_open"));
                if (cfmCab != null) return cfmCab;
            }
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_OPEN;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockKitchenStorageCabinet || path.contains("cabinet") || path.contains("cupboard") || path.contains("wardrobe") || path.contains("stand")) {
            if ("cfm".equals(domain)) {
                SoundEvent cfmCab = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "cabinet_open"));
                if (cfmCab != null) return cfmCab;
            }
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_OPEN;
        }
        if (block instanceof net.minecraft.block.BlockChest || path.contains("chest") || path.contains("safe")) {
            if (block instanceof net.minecraft.block.BlockEnderChest || path.contains("ender")) {
                return net.minecraft.init.SoundEvents.BLOCK_ENDERCHEST_OPEN;
            }
            return net.minecraft.init.SoundEvents.BLOCK_CHEST_OPEN;
        }
        if (block instanceof net.minecraft.block.BlockShulkerBox || path.contains("shulker")) {
            return net.minecraft.init.SoundEvents.BLOCK_SHULKER_BOX_OPEN;
        }
        if (block instanceof net.minecraft.block.BlockTrapDoor || path.contains("trapdoor")) {
            if (path.contains("iron")) {
                return net.minecraft.init.SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN;
            }
            return net.minecraft.init.SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN;
        }
        if (block instanceof net.minecraft.block.BlockDoor || path.contains("door")) {
            if (path.contains("iron")) {
                return net.minecraft.init.SoundEvents.BLOCK_IRON_DOOR_OPEN;
            }
            return net.minecraft.init.SoundEvents.BLOCK_WOODEN_DOOR_OPEN;
        }

        if ("cfm".equals(domain)) {
            SoundEvent cfmCab = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "cabinet_open"));
            if (cfmCab != null) return cfmCab;
        }
        if ("refurbished_furniture".equals(domain) || "mwccf".equals(domain)) {
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_OPEN;
        }
        return net.minecraft.init.SoundEvents.BLOCK_CHEST_OPEN;
    }

    private static SoundEvent getBlockCloseSound(IBlockState state, BlockPos pos) {
        if (state == null) return null;
        net.minecraft.block.Block block = state.getBlock();
        if (block instanceof com.voltyx.mwccf.radio.BlockOldRadio) {
            return null;
        }

        Minecraft mc = Minecraft.getMinecraft();
        net.minecraft.tileentity.TileEntity te = (mc.world != null && pos != null) ? mc.world.getTileEntity(pos) : null;
        if (te != null) {
            if (te instanceof com.voltyx.mwccf.furniture.tileentity.TileEntityFridge) {
                return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_FRIDGE_CLOSE;
            }
            if (te instanceof com.voltyx.mwccf.furniture.tileentity.TileEntityMicrowave) {
                return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_MICROWAVE_CLOSE;
            }
            if (te instanceof com.voltyx.mwccf.furniture.tileentity.TileEntityStove) {
                return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_STOVE_CLOSE;
            }
            if (te instanceof com.voltyx.mwccf.furniture.tileentity.TileEntityCabinet) {
                if (block instanceof com.voltyx.mwccf.furniture.BlockCooler) {
                    return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_COOLER_CLOSE;
                } else if (block instanceof com.voltyx.mwccf.furniture.BlockKitchenCabinetry) {
                    return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_KITCHEN_DRAWER_CLOSE;
                } else {
                    return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_CLOSE;
                }
            }
            if (te instanceof net.minecraft.tileentity.TileEntityChest) {
                return net.minecraft.init.SoundEvents.BLOCK_CHEST_CLOSE;
            }
            if (te instanceof net.minecraft.tileentity.TileEntityEnderChest) {
                return net.minecraft.init.SoundEvents.BLOCK_ENDERCHEST_CLOSE;
            }
            if (te instanceof net.minecraft.tileentity.TileEntityShulkerBox) {
                return net.minecraft.init.SoundEvents.BLOCK_SHULKER_BOX_CLOSE;
            }

            String teName = te.getClass().getName();
            if (teName.equals("com.mrcrayfish.furniture.tileentity.TileEntityBin")) {
                SoundEvent bin = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "bin_close"));
                return bin != null ? bin : com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_CLOSE;
            }
            if (teName.startsWith("com.mrcrayfish.furniture.tileentity.")) {
                SoundEvent cfmCab = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "cabinet_close"));
                if (cfmCab != null) return cfmCab;
            }
        }

        ResourceLocation reg = block.getRegistryName();
        String domain = reg != null ? reg.getNamespace() : "";
        String path = reg != null ? reg.getPath().toLowerCase(java.util.Locale.ROOT) : "";

        if (block instanceof com.voltyx.mwccf.furniture.BlockCooler || path.contains("cooler") || path.contains("esky")) {
            if ("cfm".equals(domain)) {
                SoundEvent cfmCab = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "cabinet_close"));
                if (cfmCab != null) return cfmCab;
            }
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_COOLER_CLOSE;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockFridge || path.contains("fridge") || path.contains("freezer")) {
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_FRIDGE_CLOSE;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockKitchenCabinetry || path.contains("drawer")) {
            if ("cfm".equals(domain)) {
                SoundEvent cfmCab = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "cabinet_close"));
                if (cfmCab != null) return cfmCab;
            }
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_KITCHEN_DRAWER_CLOSE;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockMicrowave || path.contains("microwave")) {
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_MICROWAVE_CLOSE;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockStove || path.contains("stove") || path.contains("oven")) {
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_STOVE_CLOSE;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockRecycleBin || path.contains("bin") || path.contains("trash")) {
            SoundEvent bin = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "bin_close"));
            return bin != null ? bin : com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_CLOSE;
        }
        if (path.contains("sliding_door")) {
            SoundEvent door = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "sliding_door_close"));
            return door != null ? door : com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_CLOSE;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockCrate || path.contains("crate")) {
            if ("cfm".equals(domain)) {
                SoundEvent cfmCab = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "cabinet_close"));
                if (cfmCab != null) return cfmCab;
            }
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_CLOSE;
        }
        if (block instanceof com.voltyx.mwccf.furniture.BlockKitchenStorageCabinet || path.contains("cabinet") || path.contains("cupboard") || path.contains("wardrobe") || path.contains("stand")) {
            if ("cfm".equals(domain)) {
                SoundEvent cfmCab = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "cabinet_close"));
                if (cfmCab != null) return cfmCab;
            }
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_CLOSE;
        }
        if (block instanceof net.minecraft.block.BlockChest || path.contains("chest") || path.contains("safe")) {
            if (block instanceof net.minecraft.block.BlockEnderChest || path.contains("ender")) {
                return net.minecraft.init.SoundEvents.BLOCK_ENDERCHEST_CLOSE;
            }
            return net.minecraft.init.SoundEvents.BLOCK_CHEST_CLOSE;
        }
        if (block instanceof net.minecraft.block.BlockShulkerBox || path.contains("shulker")) {
            return net.minecraft.init.SoundEvents.BLOCK_SHULKER_BOX_CLOSE;
        }
        if (block instanceof net.minecraft.block.BlockTrapDoor || path.contains("trapdoor")) {
            if (path.contains("iron")) {
                return net.minecraft.init.SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE;
            }
            return net.minecraft.init.SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE;
        }
        if (block instanceof net.minecraft.block.BlockDoor || path.contains("door")) {
            if (path.contains("iron")) {
                return net.minecraft.init.SoundEvents.BLOCK_IRON_DOOR_CLOSE;
            }
            return net.minecraft.init.SoundEvents.BLOCK_WOODEN_DOOR_CLOSE;
        }

        if ("cfm".equals(domain)) {
            SoundEvent cfmCab = SoundEvent.REGISTRY.getObject(new ResourceLocation("cfm", "cabinet_close"));
            if (cfmCab != null) return cfmCab;
        }
        if ("refurbished_furniture".equals(domain) || "mwccf".equals(domain)) {
            return com.voltyx.mwccf.furniture.FurnitureSounds.BLOCK_CABINET_CLOSE;
        }
        return net.minecraft.init.SoundEvents.BLOCK_CHEST_CLOSE;
    }

    private static SoundEvent playLootStartSounds(BlockPos pos) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) {
            return null;
        }

        IBlockState state = mc.world.getBlockState(pos);
        SoundEvent openSound = getBlockOpenSound(state, pos);
        lastTargetOpenSoundId = openSound != null ? openSound.getRegistryName() : null;

        if (openSound != null) {
            suppressNextOpenSound = false;
            suppressUntilMillis = 0L;
            PositionedSoundRecord record = new PositionedSoundRecord(
                    openSound, SoundCategory.BLOCKS, 0.8F, 1.0F,
                    pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F
            );
            mc.getSoundHandler().playSound(record);
        }
        return openSound;
    }

    private static void playLootInterruptSound(BlockPos pos) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null || pos == null) {
            return;
        }

        IBlockState state = mc.world.getBlockState(pos);
        SoundEvent closeSound = getBlockCloseSound(state, pos);
        if (closeSound != null) {
            PositionedSoundRecord record = new PositionedSoundRecord(
                    closeSound, SoundCategory.BLOCKS, 0.8F, 1.0F,
                    pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F
            );
            mc.getSoundHandler().playSound(record);
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
            if (openPauseTicks > 0) {
                openPauseTicks--;
            } else {
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
                            mc.player.playSound(efw.init.EfwModSounds.ITEMSOUND, 1.5f, 1.0f);
                        }
                        com.voltyx.mwccf.client.inspect.InspectTransitionHandler.startTransition(
                                new net.minecraft.item.ItemStack(com.voltyx.mwccf.mcore.MCoreItems.RADIO_BOARD), null);
                    }

                    suppressNextOpenSound = true;
                    suppressUntilMillis = System.currentTimeMillis() + 1500L;
                    lastLootedPos = targetBlock;

                    lastCompletedLootPos = targetBlock;
                    lastCompletedLootTime = System.currentTimeMillis();

                    stopActiveLootSound();
                    targetBlock = null;
                    lootProgress = 0;
                    openPauseTicks = 0;
                }
            }
        } else {
            stopActiveLootSound();
            playLootInterruptSound(targetBlock);
            targetBlock = null;
            lootProgress = 0;
            openPauseTicks = 0;
        }
    }

    @SubscribeEvent
    public void onPlaySound(PlaySoundEvent event) {
        if (!suppressNextOpenSound || event.getSound() == null) {
            return;
        }

        if (System.currentTimeMillis() > suppressUntilMillis) {
            suppressNextOpenSound = false;
            return;
        }

        ISound sound = event.getSound();
        ResourceLocation soundLocation = sound.getSoundLocation();
        if (soundLocation == null) {
            return;
        }

        boolean posMatches = true;
        if (lastLootedPos != null) {
            double dx = sound.getXPosF() - (lastLootedPos.getX() + 0.5);
            double dy = sound.getYPosF() - (lastLootedPos.getY() + 0.5);
            double dz = sound.getZPosF() - (lastLootedPos.getZ() + 0.5);
            double distSq = dx * dx + dy * dy + dz * dz;
            posMatches = distSq <= POS_MATCH_RADIUS_SQ;
        }

        String path = soundLocation.getPath().toLowerCase(java.util.Locale.ROOT);
        boolean nameMatches = (lastTargetOpenSoundId != null && soundLocation.equals(lastTargetOpenSoundId))
                || path.contains("open")
                || soundLocation.equals(CABINET_OPEN_SOUND_ID);

        if (nameMatches && posMatches) {
            event.setResultSound(null);
            suppressNextOpenSound = false;
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
        int midX = sr.getScaledWidth() / 2;
        int midY = sr.getScaledHeight() / 2;
        float x = midX + 0.5f;
        float y = midY + 0.5f;

        float size = 7.5f;
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
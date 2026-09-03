package efw.client;

import efw.sounds.OtbwgSounds;
import efw.world.biome.OtbwgBiomes;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.state.IBlockState;

import java.util.Random;

@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
public class OtbwgClientEffects {

    private static final Random rand = new Random();
    private static AmbientBiomeSound currentAmbientSound = null;

    @SubscribeEvent
    public static void registerBlockColors(net.minecraftforge.client.event.ColorHandlerEvent.Block event) {
        event.getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
            if (worldIn != null && pos != null) {
                return net.minecraft.world.biome.BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
            }
            return net.minecraft.world.ColorizerGrass.getGrassColor(0.5D, 1.0D);
        }, efw.blocks.OtbwgBlocks.LOAMY_GRASS, efw.blocks.OtbwgBlocks.CLOVER_PATCH, efw.blocks.OtbwgBlocks.OVERGROWN_DACITE, efw.blocks.OtbwgBlocks.OVERGROWN_STONE);
        
        event.getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
            if (worldIn != null && pos != null) {
                return net.minecraft.world.biome.BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
            }
            return net.minecraft.world.ColorizerFoliage.getFoliageColorBasic();
        }, efw.blocks.OtbwgBlocks.MAPLE_LEAVES, efw.blocks.OtbwgBlocks.MAHOGANY_LEAVES, 
           efw.blocks.OtbwgBlocks.TINY_LILY_PADS, 
           efw.blocks.OtbwgBlocks.FLOWERING_TINY_LILY_PADS, 
           efw.blocks.OtbwgBlocks.WATER_SILK,
           net.minecraft.block.Block.getBlockFromName("mwccf:poison_ivy"), 
           net.minecraft.block.Block.getBlockFromName("mwccf:leaf_pile"));
    }

    @SubscribeEvent
    public static void registerItemColors(net.minecraftforge.client.event.ColorHandlerEvent.Item event) {
        event.getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            IBlockState iblockstate = ((net.minecraft.item.ItemBlock)stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
            return Minecraft.getMinecraft().getBlockColors().colorMultiplier(iblockstate, null, null, tintIndex);
        }, efw.blocks.OtbwgBlocks.LOAMY_GRASS, efw.blocks.OtbwgBlocks.CLOVER_PATCH, efw.blocks.OtbwgBlocks.OVERGROWN_DACITE, efw.blocks.OtbwgBlocks.OVERGROWN_STONE,
           efw.blocks.OtbwgBlocks.MAPLE_LEAVES, efw.blocks.OtbwgBlocks.MAHOGANY_LEAVES, 
           efw.blocks.OtbwgBlocks.TINY_LILY_PADS,
           efw.blocks.OtbwgBlocks.FLOWERING_TINY_LILY_PADS, 
           efw.blocks.OtbwgBlocks.WATER_SILK,
           net.minecraft.block.Block.getBlockFromName("mwccf:poison_ivy"), 
           net.minecraft.block.Block.getBlockFromName("mwccf:leaf_pile"));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null || mc.isGamePaused()) return;

        BlockPos playerPos = mc.player.getPosition();
        Biome currentBiome = mc.world.getBiome(playerPos);

        if (currentBiome == OtbwgBiomes.CRIMSON_TUNDRA) {
            handleCrimsonTundraEffects(mc, playerPos);
        } else {
            stopAmbientSound();
        }
    }

    private static int ambientSoundTicks = 0;

    private static void handleCrimsonTundraEffects(Minecraft mc, BlockPos pos) {
        // 1. Spawn White Ash particles
        if (rand.nextFloat() < 0.6f) { // Increased frequency
            for (int i = 0; i < 15; i++) { // More particles per tick
                double x = ((net.minecraft.util.math.Vec3i) pos).getX() + (rand.nextDouble() - 0.5D) * 64.0D; // Larger radius
                double y = ((net.minecraft.util.math.Vec3i) pos).getY() + rand.nextDouble() * 24.0D - 4.0D; // Higher up
                double z = ((net.minecraft.util.math.Vec3i) pos).getZ() + (rand.nextDouble() - 0.5D) * 64.0D; // Larger radius

                if (mc.world.isAirBlock(new BlockPos(x, y, z))) {
                    ParticleWhiteAsh particle = new ParticleWhiteAsh(mc.world, x, y, z);
                    mc.effectRenderer.addEffect(particle);
                }
            }
        }

        // 2. Play ambient sound (Soul Sand Valley loop)
        if (currentAmbientSound == null || currentAmbientSound.isDonePlaying() || !mc.getSoundHandler().isSoundPlaying(currentAmbientSound)) {
            if (currentAmbientSound != null) currentAmbientSound.stopPlaying();
            currentAmbientSound = new AmbientBiomeSound(mc.player, OtbwgSounds.AMBIENT_SOUL_SAND_VALLEY_LOOP);
            mc.getSoundHandler().playSound(currentAmbientSound);
            ambientSoundTicks = 0;
        } else {
            ambientSoundTicks++;
            if (ambientSoundTicks >= 660) { // Crossfade at 33 seconds instead of 35.5 to avoid tail silence
                currentAmbientSound.stopPlaying();
                currentAmbientSound = new AmbientBiomeSound(mc.player, OtbwgSounds.AMBIENT_SOUL_SAND_VALLEY_LOOP);
                mc.getSoundHandler().playSound(currentAmbientSound);
                ambientSoundTicks = 0;
            }
        }
    }

    private static void stopAmbientSound() {
        if (currentAmbientSound != null && !currentAmbientSound.isDonePlaying()) {
            currentAmbientSound.stopPlaying();
            currentAmbientSound = null;
        }
    }
}

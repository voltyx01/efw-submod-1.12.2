package efw.world.biome;

import efw.blocks.OtbwgBlocks;
import efw.world.gen.OtbwgNBTTreeGenerator;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.monster.*;

import java.util.Random;

public class BiomeTwilightMeadow extends Biome {

    public BiomeTwilightMeadow() {
        super(new BiomeProperties("Twilight Meadow")
                .setBaseHeight(0.2F)
                .setHeightVariation(0.2F)
                .setTemperature(0.25F)
                .setRainfall(0.8F));

        this.setRegistryName("twilight_meadow");

        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();

        this.decorator.treesPerChunk = 2; // Sparsely growing Zelkova
        this.decorator.flowersPerChunk = 0; // Removed vanilla flowers
        this.decorator.grassPerChunk = 5;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;

        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntitySheep.class, 12, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPig.class, 10, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityChicken.class, 10, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityCow.class, 8, 4, 4));
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        String[] canopies = {
                "features/trees/zelkova/zelkova_tree_canopy1",
                "features/trees/zelkova/zelkova_tree_canopy2",
                "features/trees/zelkova/zelkova_tree_canopy3"
        };
        String canopy = canopies[rand.nextInt(canopies.length)];

        // Use the min/max height constructor to trigger the TYG downward-drawing log placement logic
        return new OtbwgNBTTreeGenerator(false, "features/trees/zelkova/zelkova_tree_trunk1", canopy, efw.blocks.OtbwgBlocks.ZELKOVA_LOG.getDefaultState(), efw.blocks.OtbwgBlocks.BROWN_ZELKOVA_LEAVES.getDefaultState(), false, 5, 15);
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, net.minecraft.world.chunk.ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        if (noiseVal > 1.5D) {
            this.topBlock = Blocks.DIRT.getStateFromMeta(1); // Coarse Dirt
        } else if (noiseVal < -1.5D) {
            this.topBlock = efw.blocks.OtbwgBlocks.PEAT.getDefaultState();
        } else {
            this.topBlock = Blocks.GRASS.getDefaultState();
        }
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        // Boulders (Rocky Stone, Granite, Polished Granite, Mossy Stone)
        if (net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, rand, new net.minecraft.util.math.ChunkPos(pos), net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.ROCK)) {
            for (int i = 0; i < 3; ++i) {
                if (rand.nextFloat() < 0.3f) continue; // Reduce by 30%
                int x = rand.nextInt(16) + 8;
                int z = rand.nextInt(16) + 8;
                BlockPos blockpos = worldIn.getHeight(pos.add(x, 0, z)).down();
                
                // Ensure we place it on ground, not on leaves or water
                net.minecraft.block.Block topBlock = worldIn.getBlockState(blockpos).getBlock();
                if (topBlock != Blocks.GRASS && topBlock != Blocks.DIRT && topBlock != efw.blocks.OtbwgBlocks.PEAT) {
                    continue;
                }
                blockpos = blockpos.up();

                IBlockState rockState;
                int r = rand.nextInt(4);
                if (r == 0) rockState = efw.blocks.OtbwgBlocks.ROCKY_STONE.getDefaultState();
                else if (r == 1) rockState = efw.blocks.OtbwgBlocks.MOSSY_STONE.getDefaultState();
                else if (r == 2) rockState = Blocks.STONE.getDefaultState().withProperty(net.minecraft.block.BlockStone.VARIANT, net.minecraft.block.BlockStone.EnumType.GRANITE);
                else rockState = Blocks.STONE.getDefaultState().withProperty(net.minecraft.block.BlockStone.VARIANT, net.minecraft.block.BlockStone.EnumType.GRANITE_SMOOTH);
                
                int radius = 1 + rand.nextInt(2);
                for (int bx = -radius; bx <= radius; bx++) {
                    for (int by = -1; by <= radius; by++) {
                        for (int bz = -radius; bz <= radius; bz++) {
                            if (bx*bx + by*by + bz*bz <= radius*radius) {
                                BlockPos p = blockpos.add(bx, by, bz);
                                net.minecraft.block.Block b = worldIn.getBlockState(p).getBlock();
                                if ((b.isReplaceable(worldIn, p) || b == Blocks.GRASS || b == Blocks.DIRT || b == efw.blocks.OtbwgBlocks.PEAT) && b != Blocks.WATER && b != Blocks.FLOWING_WATER) {
                                    worldIn.setBlockState(p, rockState, 2);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Custom Flowers (Pink & White Anemone, Rose)
        for (int i = 0; i < 5; ++i) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            BlockPos flowerPos = worldIn.getHeight(pos.add(x, 0, z));
            if (worldIn.isAirBlock(flowerPos) && Blocks.RED_FLOWER.canBlockStay(worldIn, flowerPos, Blocks.RED_FLOWER.getDefaultState())) {
                net.minecraft.block.Block pink = net.minecraft.block.Block.getBlockFromName("mwccf:pink_anemone");
                net.minecraft.block.Block white = net.minecraft.block.Block.getBlockFromName("mwccf:white_anemone");
                IBlockState anemone = null;
                if (pink != null && white != null) {
                    int fr = rand.nextInt(3);
                    if (fr == 0) anemone = pink.getDefaultState();
                    else if (fr == 1) anemone = white.getDefaultState();
                    else anemone = efw.blocks.OtbwgBlocks.ROSE.getDefaultState();
                }
                if (anemone != null) worldIn.setBlockState(flowerPos, anemone, 2);
            }
        }

    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return getModdedBiomeGrassColor(0x9A6532); // Тускло оранжево-коричневый
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return getModdedBiomeFoliageColor(0x9A6532); // Тускло оранжево-коричневый
    }
}

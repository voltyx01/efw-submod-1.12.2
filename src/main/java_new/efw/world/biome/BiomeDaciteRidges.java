package efw.world.biome;

import efw.blocks.OtbwgBlocks;
import efw.world.gen.OtbwgNBTTreeGenerator;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BiomeDaciteRidges extends Biome {

    private final WorldGenTaiga2 spruceGenerator = new WorldGenTaiga2(false);

    public BiomeDaciteRidges() {
        super(new BiomeProperties("Dacite Ridges")
                .setBaseHeight(0.3F)
                .setHeightVariation(0.2F)
                .setTemperature(0.25F)
                .setRainfall(0.8F));

        this.setRegistryName("dacite_ridges");

        this.topBlock = efw.blocks.OtbwgBlocks.OVERGROWN_DACITE.getDefaultState();
        this.fillerBlock = efw.blocks.OtbwgBlocks.DACITE.getDefaultState();

        this.decorator.treesPerChunk = 6;
        this.decorator.flowersPerChunk = 6;
        this.decorator.grassPerChunk = 8;
        this.decorator.mushroomsPerChunk = 2;

        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntitySheep.class, 12, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityWolf.class, 8, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        this.spawnableCaveCreatureList.add(new Biome.SpawnListEntry(EntityBat.class, 10, 8, 8));
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        if (rand.nextInt(5) == 0) {
            return spruceGenerator; // 20% Vanilla Spruce (DACITE_RIDGE_TREES)
        } else {
            int treeType = rand.nextInt(4);
            if (treeType == 0) {
                return new OtbwgNBTTreeGenerator(false, "features/trees/generic_trunk", "features/trees/holly/holly_canopy1", efw.blocks.OtbwgBlocks.HOLLY_LOG.getDefaultState(), efw.blocks.OtbwgBlocks.HOLLY_LEAVES.getDefaultState(), true, 10, 16);
            } else if (treeType == 1) {
                return new OtbwgNBTTreeGenerator(false, "features/trees/generic_trunk", "features/trees/holly/holly_canopy2", efw.blocks.OtbwgBlocks.HOLLY_LOG.getDefaultState(), efw.blocks.OtbwgBlocks.HOLLY_LEAVES.getDefaultState(), true, 8, 14);
            } else if (treeType == 2) {
                return new OtbwgNBTTreeGenerator(false, "features/trees/holly/holly_trunk3", "features/trees/holly/holly_canopy3", efw.blocks.OtbwgBlocks.HOLLY_LOG.getDefaultState(), efw.blocks.OtbwgBlocks.HOLLY_LEAVES.getDefaultState(), true, 2, 12);
            } else {
                return new OtbwgNBTTreeGenerator(false, "features/trees/generic_trunk", "features/trees/holly/holly_canopy4", efw.blocks.OtbwgBlocks.HOLLY_LOG.getDefaultState(), efw.blocks.OtbwgBlocks.HOLLY_LEAVES.getDefaultState(), true, 10, 16);
            }
        }
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, net.minecraft.world.chunk.ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        IBlockState originalTop = this.topBlock;
        if (noiseVal > 0.8D) {
            this.topBlock = Blocks.DIRT.getStateFromMeta(1); // Coarse Dirt
        } else if (noiseVal > -0.2D) {
            this.topBlock = OtbwgBlocks.PODZOL_DACITE != null ? OtbwgBlocks.PODZOL_DACITE.getDefaultState() : this.topBlock;
        } else {
            this.topBlock = OtbwgBlocks.OVERGROWN_DACITE != null ? OtbwgBlocks.OVERGROWN_DACITE.getDefaultState() : this.topBlock;
        }
        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
        this.topBlock = originalTop;
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random rand) {
        return rand.nextInt(5) > 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
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
                if (topBlock != Blocks.GRASS && topBlock != Blocks.DIRT && topBlock != efw.blocks.OtbwgBlocks.OVERGROWN_DACITE && topBlock != efw.blocks.OtbwgBlocks.DACITE && topBlock != efw.blocks.OtbwgBlocks.PODZOL_DACITE) {
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
                                if ((b.isReplaceable(worldIn, p) || b == Blocks.GRASS || b == Blocks.DIRT || b == efw.blocks.OtbwgBlocks.OVERGROWN_DACITE || b == efw.blocks.OtbwgBlocks.PODZOL_DACITE) && b != Blocks.WATER && b != Blocks.FLOWING_WATER) {
                                    worldIn.setBlockState(p, rockState, 2);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Custom Flowers (Blue Rose Bush)
        for (int i = 0; i < 4; ++i) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            BlockPos flowerPos = worldIn.getHeight(pos.add(x, 0, z));
            if (worldIn.isAirBlock(flowerPos) && Blocks.RED_FLOWER.canBlockStay(worldIn, flowerPos, Blocks.RED_FLOWER.getDefaultState())) {
                net.minecraft.block.Block blueRose = net.minecraft.block.Block.getBlockFromName("mwccf:blue_rose_bush");
                if (blueRose != null) {
                    worldIn.setBlockState(flowerPos, blueRose.getDefaultState(), 2);
                }
            }
        }

    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return getModdedBiomeGrassColor(0x4C763C); // 5011004
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return getModdedBiomeFoliageColor(0x4C763C); // Match grass color
    }
}

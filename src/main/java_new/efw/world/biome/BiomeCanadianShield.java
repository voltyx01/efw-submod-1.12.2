package efw.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.Random;
import efw.blocks.OtbwgBlocks;
import efw.world.gen.OtbwgNBTTreeGenerator;

public class BiomeCanadianShield extends Biome {

    public BiomeCanadianShield() {
        super(new BiomeProperties("Canadian Shield")
                .setTemperature(0.25f)
                .setRainfall(0.5f));
        this.setRegistryName("canadian_shield");

        this.topBlock = OtbwgBlocks.OVERGROWN_STONE != null ? OtbwgBlocks.OVERGROWN_STONE.getDefaultState() : Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.STONE.getDefaultState();

        this.decorator.treesPerChunk = 8;
        this.decorator.grassPerChunk = 6;
        this.decorator.flowersPerChunk = 4;
        this.decorator.deadBushPerChunk = 0;

        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntitySheep.class, 12, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPig.class, 10, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityChicken.class, 10, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityCow.class, 8, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityWolf.class, 8, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        
        this.spawnableCaveCreatureList.add(new Biome.SpawnListEntry(EntityBat.class, 10, 8, 8));
        
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySpider.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombie.class, 95, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombieVillager.class, 5, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySkeleton.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityCreeper.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySlime.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityEnderman.class, 10, 1, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityWitch.class, 5, 1, 1));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getSkyColorByTemp(float currentTemperature) {
        return 8233983;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return 0x71A68A;
    }

    @Override
    public int getWaterColorMultiplier() {
        return 4159204;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return 0x71A68A; // или отдельный цвет для травы, если нужен другой тон
    }
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        float type = rand.nextFloat();
        
        // 40% chance for Fir trees (Bluff shapes)
        if (type < 0.40f) {
            boolean isBluff1 = rand.nextBoolean();
            return new OtbwgNBTTreeGenerator(
                false, 
                isBluff1 ? "features/trees/generic_trunk" : "features/trees/bluff/bluff_trunk2", 
                isBluff1 ? "features/trees/bluff/bluff_canopy1" : "features/trees/bluff/bluff_canopy2", 
                efw.blocks.OtbwgBlocks.FIR_LOG.getDefaultState(), 
                efw.blocks.OtbwgBlocks.FIR_LEAVES.getDefaultState(), 
                true
            );
        }
        // 20% chance for Pine trees
        else if (type < 0.60f) {
            boolean isPine1 = rand.nextBoolean();
            return new OtbwgNBTTreeGenerator(
                false, 
                "features/trees/pine/large_pine_trunk1", 
                isPine1 ? "features/trees/pine/pine_canopy1" : "features/trees/pine/pine_canopy2", 
                efw.blocks.OtbwgBlocks.PINE_LOG.getDefaultState(), 
                efw.blocks.OtbwgBlocks.PINE_LEAVES.getDefaultState(), 
                true
            );
        }
        // 20% chance for Spruce trees (Conifer shapes)
        else if (type < 0.80f) {
            return new OtbwgNBTTreeGenerator(
                false, 
                "features/trees/conifer/conifer_trunk1", 
                "features/trees/conifer/conifer_canopy6", 
                Blocks.LOG.getStateFromMeta(1), // Spruce log
                Blocks.LEAVES.getStateFromMeta(1), // Spruce leaves
                true
            );
        }
        // 20% chance for Aspen trees
        else {
            boolean isAspen1 = rand.nextBoolean();
            return new OtbwgNBTTreeGenerator(
                false, 
                isAspen1 ? "features/trees/aspen/aspen_trunk1" : "features/trees/aspen/aspen_trunk2", 
                isAspen1 ? "features/trees/aspen/aspen_canopy1" : "features/trees/aspen/aspen_canopy2", 
                efw.blocks.OtbwgBlocks.ASPEN_LOG.getDefaultState(), 
                efw.blocks.OtbwgBlocks.ASPEN_LEAVES.getDefaultState(), 
                true
            );
        }
    }

    @Override
    public net.minecraft.world.gen.feature.WorldGenerator getRandomWorldGenForGrass(Random rand) {
        return rand.nextInt(3) == 0 ? new net.minecraft.world.gen.feature.WorldGenTallGrass(net.minecraft.block.BlockTallGrass.EnumType.FERN) : new net.minecraft.world.gen.feature.WorldGenTallGrass(net.minecraft.block.BlockTallGrass.EnumType.GRASS);
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        
        // Boulders (Mossy Stone, Rocky Stone)
        if (net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, rand, new net.minecraft.util.math.ChunkPos(pos), net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.ROCK)) {
            for (int i = 0; i < 4; ++i) {
                if (rand.nextFloat() < 0.2f) continue;
                int x = rand.nextInt(16) + 8;
                int z = rand.nextInt(16) + 8;
                BlockPos blockpos = worldIn.getHeight(pos.add(x, 0, z)).down();
                
                net.minecraft.block.Block topBlock = worldIn.getBlockState(blockpos).getBlock();
                if (topBlock != Blocks.GRASS && topBlock != Blocks.DIRT && topBlock != efw.blocks.OtbwgBlocks.OVERGROWN_STONE) {
                    continue;
                }
                blockpos = blockpos.up();

                IBlockState rockState = rand.nextBoolean() ? efw.blocks.OtbwgBlocks.MOSSY_STONE.getDefaultState() : efw.blocks.OtbwgBlocks.ROCKY_STONE.getDefaultState();
                
                int radius = 1 + rand.nextInt(2);
                for (int bx = -radius; bx <= radius; bx++) {
                    for (int by = -1; by <= radius; by++) {
                        for (int bz = -radius; bz <= radius; bz++) {
                            if (bx*bx + by*by + bz*bz <= radius*radius) {
                                BlockPos p = blockpos.add(bx, by, bz);
                                net.minecraft.block.Block b = worldIn.getBlockState(p).getBlock();
                                if ((b.isReplaceable(worldIn, p) || b == Blocks.GRASS || b == Blocks.DIRT || b == efw.blocks.OtbwgBlocks.OVERGROWN_STONE) && b != Blocks.WATER && b != Blocks.FLOWING_WATER) {
                                    worldIn.setBlockState(p, rockState, 2);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Custom Flowers (Anemones, Hydrangeas, Blue Rose)
        for (int i = 0; i < 5; ++i) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            BlockPos flowerPos = worldIn.getHeight(pos.add(x, 0, z));
            if (worldIn.isAirBlock(flowerPos) && Blocks.RED_FLOWER.canBlockStay(worldIn, flowerPos, Blocks.RED_FLOWER.getDefaultState())) {
                net.minecraft.block.Block pink = net.minecraft.block.Block.getBlockFromName("mwccf:pink_anemone");
                net.minecraft.block.Block white = net.minecraft.block.Block.getBlockFromName("mwccf:white_anemone");
                net.minecraft.block.Block rose = net.minecraft.block.Block.getBlockFromName("mwccf:blue_rose_bush");
                net.minecraft.block.Block hydrangea = net.minecraft.block.Block.getBlockFromName("mwccf:hydrangea_bush");
                
                IBlockState plant = null;
                int r = rand.nextInt(4);
                if (r == 0 && pink != null) plant = pink.getDefaultState();
                else if (r == 1 && white != null) plant = white.getDefaultState();
                else if (r == 2 && rose != null) plant = rose.getDefaultState();
                else if (r == 3 && hydrangea != null) plant = hydrangea.getDefaultState();
                
                if (plant != null) {
                    worldIn.setBlockState(flowerPos, plant, 2);
                }
            }
        }
    }
}

package efw.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityHusk;
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

public class BiomeAtacamaOutback extends BiomeDesert {

    public BiomeAtacamaOutback() {
        super(new BiomeProperties("atacamaOutback")
                .setTemperature(2.0f)
                .setRainfall(0.0f)
                .setBaseHeight(0.125f)
                .setHeightVariation(0.05f));
        this.setRegistryName("atacama_outback");

        this.topBlock = OtbwgBlocks.CRACKED_RED_SAND != null ? OtbwgBlocks.CRACKED_RED_SAND.getDefaultState() : Blocks.SAND.getStateFromMeta(1);
        this.fillerBlock = Blocks.RED_SANDSTONE.getDefaultState();

        this.decorator.treesPerChunk = -999;
        this.decorator.deadBushPerChunk = 2;
        this.decorator.cactiPerChunk = 1;
        this.decorator.grassPerChunk = 0;
        this.decorator.reedsPerChunk = 0;

        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();

        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        this.spawnableCaveCreatureList.add(new Biome.SpawnListEntry(EntityBat.class, 10, 8, 8));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySpider.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombie.class, 19, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityHusk.class, 80, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombieVillager.class, 1, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySkeleton.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityCreeper.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySlime.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityEnderman.class, 10, 1, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityWitch.class, 5, 1, 1));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return getModdedBiomeGrassColor(10855786);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return getModdedBiomeFoliageColor(10855786);
    }

    @Override
    public int getWaterColorMultiplier() {
        return 6200521;
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        IBlockState originalTop = this.topBlock;
        
        if (noiseVal > 1.75D) {
            this.topBlock = Blocks.SAND.getStateFromMeta(1); // Red Sand
        }
        
        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
        
        this.topBlock = originalTop;
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);

        // Boulders
        if (rand.nextInt(4) == 0) {
            int bx = rand.nextInt(16) + 8;
            int bz = rand.nextInt(16) + 8;
            BlockPos surfacePos = worldIn.getHeight(pos.add(bx, 0, bz)).down();
            net.minecraft.block.Block topBlock = worldIn.getBlockState(surfacePos).getBlock();
            if (topBlock == this.topBlock.getBlock() || topBlock == this.fillerBlock.getBlock() || topBlock == Blocks.SAND || topBlock == Blocks.STAINED_HARDENED_CLAY) {
                new WorldGenBlockBlob(Blocks.STONE, 1).generate(worldIn, rand, surfacePos.up());
            }
        }
        if (rand.nextInt(4) == 0) {
            int bx = rand.nextInt(16) + 8;
            int bz = rand.nextInt(16) + 8;
            BlockPos surfacePos = worldIn.getHeight(pos.add(bx, 0, bz)).down();
            net.minecraft.block.Block topBlock = worldIn.getBlockState(surfacePos).getBlock();
            if (topBlock == this.topBlock.getBlock() || topBlock == this.fillerBlock.getBlock() || topBlock == Blocks.SAND || topBlock == Blocks.STAINED_HARDENED_CLAY) {
                surfacePos = surfacePos.up();
                net.minecraft.block.state.IBlockState orangeTerracotta = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(1);
                int radius = rand.nextInt(2) + 1;
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dy = -radius; dy <= radius; dy++) {
                        for (int dz = -radius; dz <= radius; dz++) {
                            if (dx * dx + dy * dy + dz * dz <= radius * radius) {
                                BlockPos target = surfacePos.add(dx, dy, dz);
                                Block block = worldIn.getBlockState(target).getBlock();
                                if (block == Blocks.AIR || block == this.topBlock.getBlock() || block == this.fillerBlock.getBlock() || block == Blocks.SAND || block == Blocks.STAINED_HARDENED_CLAY) {
                                    worldIn.setBlockState(target, orangeTerracotta, 2);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Palo Verde Trees (rare)
        if (rand.nextInt(15) == 0) {
            int bx = rand.nextInt(16) + 8;
            int bz = rand.nextInt(16) + 8;
            BlockPos surfacePos = worldIn.getHeight(pos.add(bx, 0, bz));
            getRandomTreeFeature(rand).generate(worldIn, rand, surfacePos);
        }

        // Mod Vegetation
        String[] plants = {
            "mwccf:aloe_vera", "mwccf:barrel_cactus", "mwccf:flowering_barrel_cactus",
            "mwccf:golden_spined_cactus", "mwccf:mini_cactus", "mwccf:prickly_pear_cactus",
            "mwccf:shrub", "mwccf:pink_anemone", "mwccf:white_anemone",
            "mwccf:blue_sage", "mwccf:purple_sage", "mwccf:white_sage"
        };
        
        for (int i = 0; i < 4; i++) {
            int bx = rand.nextInt(16) + 8;
            int bz = rand.nextInt(16) + 8;
            BlockPos surfacePos = worldIn.getHeight(pos.add(bx, 0, bz));
            
            if (worldIn.isAirBlock(surfacePos)) {
                Block blockBelow = worldIn.getBlockState(surfacePos.down()).getBlock();
                if (blockBelow == this.topBlock.getBlock() || blockBelow == Blocks.SAND) {
                    Block plantBlock = Block.getBlockFromName(plants[rand.nextInt(plants.length)]);
                    if (plantBlock != null) {
                        worldIn.setBlockState(surfacePos, plantBlock.getDefaultState(), 2);
                    }
                }
            }
        }
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        Block paloLog = Block.getBlockFromName("mwccf:palo_verde_log");
        Block paloLeaf = Block.getBlockFromName("mwccf:palo_verde_leaves");
        
        IBlockState logState = paloLog != null ? paloLog.getDefaultState() : Blocks.LOG.getDefaultState();
        IBlockState leafState = paloLeaf != null ? paloLeaf.getDefaultState() : Blocks.LEAVES.getDefaultState();

        return new OtbwgNBTTreeGenerator(
            false, 
            "features/trees/palo_verde_trunk_1", 
            "features/trees/palo_verde_canopy_1", 
            logState, 
            leafState,
            true
        );
    }
}

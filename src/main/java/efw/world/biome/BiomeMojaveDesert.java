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

public class BiomeMojaveDesert extends BiomeDesert {

    public BiomeMojaveDesert() {
        super(new BiomeProperties("mojaveDesert")
                .setTemperature(2.0f)
                .setRainfall(0.0f)
                .setBaseHeight(0.125f)
                .setHeightVariation(0.05f));
        this.setRegistryName("mojave_desert");

        this.topBlock = Blocks.SAND.getDefaultState();
        this.fillerBlock = Blocks.SAND.getDefaultState();

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
        return getModdedBiomeGrassColor(10780492); // Dried orange-brown
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return getModdedBiomeFoliageColor(10386255); // Dried orange-brown foliage
    }

    @Override
    public int getWaterColorMultiplier() {
        return 4159204;
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        IBlockState originalTop = this.topBlock;
        IBlockState originalFiller = this.fillerBlock;
        
        int surfaceChoice = rand.nextInt(7);
        if (surfaceChoice < 3) {
            this.topBlock = Blocks.SAND.getDefaultState();
            this.fillerBlock = Blocks.SAND.getDefaultState();
        } else if (surfaceChoice == 3) {
            this.topBlock = OtbwgBlocks.CRACKED_SAND != null ? OtbwgBlocks.CRACKED_SAND.getDefaultState() : Blocks.SAND.getDefaultState();
            this.fillerBlock = Blocks.SANDSTONE.getDefaultState();
        } else if (surfaceChoice == 4) {
            this.topBlock = Blocks.DIRT.getStateFromMeta(1); // Coarse Dirt
            this.fillerBlock = Blocks.DIRT.getDefaultState();
        } else {
            this.topBlock = Blocks.GRASS.getDefaultState();
            this.fillerBlock = Blocks.DIRT.getDefaultState();
        }
        
        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
        
        this.topBlock = originalTop;
        this.fillerBlock = originalFiller;
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);

        // Boulders
        if (rand.nextInt(6) == 0) {
            int bx = rand.nextInt(16) + 8;
            int bz = rand.nextInt(16) + 8;
            BlockPos surfacePos = worldIn.getHeight(pos.add(bx, 0, bz)).down();
            net.minecraft.block.Block topBlock = worldIn.getBlockState(surfacePos).getBlock();
            if (topBlock == this.topBlock.getBlock() || topBlock == this.fillerBlock.getBlock() || topBlock == Blocks.SAND || topBlock == Blocks.STAINED_HARDENED_CLAY) {
                new WorldGenBlockBlob(Blocks.STONE, 1).generate(worldIn, rand, surfacePos.up());
            }
        }

        // Yucca Trees
        if (rand.nextInt(8) == 0) {
            int bx = rand.nextInt(16) + 8;
            int bz = rand.nextInt(16) + 8;
            BlockPos surfacePos = worldIn.getHeight(pos.add(bx, 0, bz));
            getRandomTreeFeature(rand).generate(worldIn, rand, surfacePos);
        }

        // Mod Vegetation
        for (int i = 0; i < 5; i++) {
            int bx = rand.nextInt(16) + 8;
            int bz = rand.nextInt(16) + 8;
            BlockPos surfacePos = worldIn.getHeight(pos.add(bx, 0, bz));
            
            if (worldIn.isAirBlock(surfacePos)) {
                Block blockBelow = worldIn.getBlockState(surfacePos.down()).getBlock();
                if (blockBelow == Blocks.SAND || blockBelow == OtbwgBlocks.SANDY_DIRT || blockBelow == OtbwgBlocks.CRACKED_SAND || blockBelow == Blocks.GRASS || blockBelow == Blocks.DIRT) {
                    float chance = rand.nextFloat();
                    String plantName = "mwccf:golden_spined_cactus";
                    if (chance < 0.1f) plantName = "mwccf:firecracker_bush";
                    else if (chance < 0.3f) plantName = "mwccf:aloe_vera";
                    else if (chance < 0.6f) plantName = "mwccf:prickly_pear_cactus";
                    else if (chance < 0.9f) plantName = "mwccf:mini_cactus";
                    
                    Block plantBlock = Block.getBlockFromName(plantName);
                    if (plantBlock != null) {
                        worldIn.setBlockState(surfacePos, plantBlock.getDefaultState(), 2);
                    }
                }
            }
        }
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        Block yuccaLog = Block.getBlockFromName("mwccf:yucca_log");
        Block yuccaLeaf = Block.getBlockFromName("mwccf:yucca_leaves");
        
        IBlockState logState = yuccaLog != null ? yuccaLog.getDefaultState() : Blocks.LOG.getDefaultState();
        IBlockState leafState = yuccaLeaf != null ? yuccaLeaf.getDefaultState() : Blocks.LEAVES.getDefaultState();

        String trunk = rand.nextBoolean() ? "features/trees/yucca/yucca_trunk1" : "features/trees/yucca/yucca_trunk2";
        String canopy = rand.nextBoolean() ? "features/trees/yucca/yucca_canopy1" : "features/trees/yucca/yucca_canopy2";

        return new OtbwgNBTTreeGenerator(
            false, 
            trunk, 
            canopy, 
            logState, 
            leafState,
            true
        );
    }
}

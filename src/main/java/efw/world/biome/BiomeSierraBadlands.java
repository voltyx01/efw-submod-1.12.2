package efw.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMesa;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityHusk;
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

public class BiomeSierraBadlands extends BiomeMesa {

    protected static final WorldGenSavannaTree ACACIA_TREE = new WorldGenSavannaTree(false);

    public BiomeSierraBadlands() {
        // brycePillars = true, hasForest = false (убираем дерн с высоких гор)
        super(true, false, new BiomeProperties("Sierra Badlands")
                .setTemperature(1.2f)
                .setRainfall(0.1f));
        this.setRegistryName("sierra_badlands");

        this.decorator.treesPerChunk = 2; // Palo Verde and Acacia
        this.decorator.grassPerChunk = 5;
        this.decorator.flowersPerChunk = 0;
        this.decorator.deadBushPerChunk = 20;

        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        this.spawnableCaveCreatureList.add(new Biome.SpawnListEntry(EntityBat.class, 10, 8, 8));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySpider.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityHusk.class, 95, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombieVillager.class, 5, 1, 1));
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
        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);

        int k1 = x & 15;
        int l1 = z & 15;
        
        int topY = 255;
        while (topY >= 0 && chunkPrimerIn.getBlockState(l1, topY, k1).getMaterial() == net.minecraft.block.material.Material.AIR) {
            topY--;
        }
        
        // Ограничиваем высоту, чтобы трава и Coarse Dirt не появлялись на вершинах терракотовых гор-худу (примерно до Y=80)
        if (topY > 0 && topY < 80) {
            net.minecraft.block.state.IBlockState currentState = chunkPrimerIn.getBlockState(l1, topY, k1);
            if (currentState.getBlock() == Blocks.STAINED_HARDENED_CLAY || currentState.getBlock() == Blocks.HARDENED_CLAY || currentState.getBlock() == Blocks.SAND || currentState.getBlock() == Blocks.GRASS || currentState.getBlock() == Blocks.DIRT) {
                if (noiseVal > 1.75D) {
                    chunkPrimerIn.setBlockState(l1, topY, k1, Blocks.DIRT.getStateFromMeta(1)); // COARSE_DIRT
                    chunkPrimerIn.setBlockState(l1, topY - 1, k1, Blocks.DIRT.getDefaultState());
                } else if (noiseVal <= -0.95D) {
                    chunkPrimerIn.setBlockState(l1, topY, k1, Blocks.GRASS.getDefaultState());
                    chunkPrimerIn.setBlockState(l1, topY - 1, k1, Blocks.DIRT.getDefaultState());
                    chunkPrimerIn.setBlockState(l1, topY - 2, k1, Blocks.DIRT.getDefaultState());
                }
            }
        }
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        
        if (net.minecraftforge.event.terraingen.TerrainGen.generateOre(worldIn, rand, new net.minecraft.world.gen.feature.WorldGenMinable(Blocks.EMERALD_ORE.getDefaultState(), 1), pos, net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.EMERALD)) {
            for (int j = 0; j < 3 + rand.nextInt(6); ++j) {
                int x = rand.nextInt(16) + 8;
                int y = rand.nextInt(28) + 4;
                int z = rand.nextInt(16) + 8;
                BlockPos emeraldPos = pos.add(x, y, z);
                if (worldIn.getBlockState(emeraldPos).getBlock().isReplaceableOreGen(worldIn.getBlockState(emeraldPos), worldIn, emeraldPos, net.minecraft.block.state.pattern.BlockMatcher.forBlock(Blocks.STONE))) {
                    worldIn.setBlockState(emeraldPos, Blocks.EMERALD_ORE.getDefaultState(), 2);
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            if (rand.nextInt(3) == 0) {
                int bx = rand.nextInt(16) + 8;
                int bz = rand.nextInt(16) + 8;
                BlockPos surfacePos = worldIn.getHeight(pos.add(bx, 0, bz));
                net.minecraft.block.state.IBlockState orangeTerracotta = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(1);
                int radius = rand.nextInt(2) + 1;
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dy = -radius; dy <= radius; dy++) {
                        for (int dz = -radius; dz <= radius; dz++) {
                            if (dx * dx + dy * dy + dz * dz <= radius * radius) {
                                BlockPos target = surfacePos.add(dx, dy, dz);
                                Block block = worldIn.getBlockState(target).getBlock();
                                if (block == Blocks.AIR || block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.SAND || block == Blocks.STAINED_HARDENED_CLAY || block == Blocks.HARDENED_CLAY) {
                                    worldIn.setBlockState(target, orangeTerracotta, 2);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            int bx = rand.nextInt(16) + 8;
            int bz = rand.nextInt(16) + 8;
            BlockPos surfacePos = worldIn.getHeight(pos.add(bx, 0, bz));
            if (worldIn.isAirBlock(surfacePos) && worldIn.getBlockState(surfacePos.down()).getBlock() == Blocks.GRASS) {
                worldIn.setBlockState(surfacePos, Blocks.LEAVES.getDefaultState());
            }
        }

        Block firecracker = Block.getBlockFromName("mwccf:firecracker_flower_bush");
        if (firecracker != null) {
            for (int i = 0; i < 3; i++) {
                int bx = rand.nextInt(16) + 8;
                int bz = rand.nextInt(16) + 8;
                BlockPos surfacePos = worldIn.getHeight(pos.add(bx, 0, bz));
                
                if (worldIn.isAirBlock(surfacePos) && worldIn.getBlockState(surfacePos.down()).getBlock() == Blocks.GRASS) {
                    worldIn.setBlockState(surfacePos, firecracker.getDefaultState());
                }
            }
        }
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        if (rand.nextInt(10) == 0) {
            return ACACIA_TREE;
        }

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

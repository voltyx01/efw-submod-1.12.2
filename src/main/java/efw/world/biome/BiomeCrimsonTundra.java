package efw.world.biome;

import efw.blocks.OtbwgBlocks;
import efw.world.gen.OtbwgNBTTreeGenerator;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import java.util.Random;

public class BiomeCrimsonTundra extends Biome {

    public BiomeCrimsonTundra() {
        super(new BiomeProperties("Crimson Tundra").setBaseHeight(0.15F).setHeightVariation(0.15F).setTemperature(0.2F).setRainfall(0.8F).setSnowEnabled());

        this.setRegistryName("crimson_tundra");
        
        this.topBlock = OtbwgBlocks.LOAMY_GRASS.getDefaultState();
        this.fillerBlock = efw.blocks.OtbwgBlocks.LOAMY_DIRT.getDefaultState();

        this.decorator.treesPerChunk = 0;
        this.decorator.flowersPerChunk = 0;
        this.decorator.grassPerChunk = 0;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;
        
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
    }


    private static final NoiseGeneratorPerlin WARP_NOISE = new NoiseGeneratorPerlin(new Random(777L), 3);
    private static final NoiseGeneratorPerlin PATCH_NOISE = new NoiseGeneratorPerlin(new Random(42L), 2);
    private static final NoiseGeneratorPerlin PATH_NOISE_GEN = new NoiseGeneratorPerlin(new Random(123L), 2);

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noise) {
        // noise — это готовое значение шума от Minecraft (обычно от -1.0 до 1.0 или около того)
        // Добавляем случайность, чтобы границы были "рваными" и естественными
        double jitter = rand.nextDouble() * 0.2D;
        double combinedNoise = noise + jitter;

        // Используем готовое значение для логики
        if (combinedNoise > 0.8D) {
            this.topBlock = OtbwgBlocks.PEAT.getDefaultState();
            this.fillerBlock = OtbwgBlocks.LOAMY_DIRT.getDefaultState();
        } else if (combinedNoise > 0.2D) {
            this.topBlock = OtbwgBlocks.ROCKY_STONE.getDefaultState();
            this.fillerBlock = OtbwgBlocks.ROCKY_STONE.getDefaultState();
        } else if (combinedNoise < -0.3D) {
            this.topBlock = OtbwgBlocks.LOAMY_GRASS.getDefaultState();
            this.fillerBlock = OtbwgBlocks.LOAMY_DIRT.getDefaultState();
        } else {
            this.topBlock = OtbwgBlocks.LOAMY_DIRT.getDefaultState();
            this.fillerBlock = OtbwgBlocks.LOAMY_DIRT.getDefaultState();
        }

        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noise);
    }
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        String[] canopies = {
                "features/trees/spruce/spruce_tree_canopy1",
                "features/trees/spruce/spruce_tree_canopy2",
                "features/trees/spruce/spruce_tree_canopy3",
                "features/trees/spruce/spruce_tree_canopy4",
                "features/trees/spruce/spruce_tree_medium_canopy1",
                "features/trees/spruce/spruce_tree_medium_canopy2"
        };
        String trunk;
        String canopy;
        if (rand.nextInt(10) == 0) {
            trunk = "features/trees/spruce/spruce_tree_large_trunk1";
            canopy = "features/trees/spruce/spruce_tree_large_canopy1";
        } else {
            trunk = rand.nextBoolean() ? "features/trees/generic_trunk" : "features/trees/spruce/spruce_tree_medium_trunk2";
            canopy = canopies[rand.nextInt(canopies.length)];
        }

        IBlockState log = Blocks.LOG.getDefaultState();
        IBlockState spruceLog = Blocks.LOG.getStateFromMeta(1);
        
        IBlockState leaves = rand.nextInt(10) == 0 ? OtbwgBlocks.ORANGE_SPRUCE_LEAVES.getDefaultState() : OtbwgBlocks.RED_SPRUCE_LEAVES.getDefaultState();

        return new OtbwgNBTTreeGenerator(false, trunk, canopy, spruceLog, leaves, true);
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);

        // Generate Large Ferns
        if (rand.nextFloat() < 0.33f) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            int y = ((net.minecraft.util.math.Vec3i) worldIn.getHeight(pos.add(x, 0, z))).getY();
            BlockPos largeFernPos = pos.add(x, y, z);
            net.minecraft.world.gen.feature.WorldGenDoublePlant doublePlantGen = new net.minecraft.world.gen.feature.WorldGenDoublePlant();
            doublePlantGen.setPlantType(net.minecraft.block.BlockDoublePlant.EnumPlantType.FERN);
            doublePlantGen.generate(worldIn, rand, largeFernPos);
        }

        // Generate Ferns
        for (int i = 0; i < 3; ++i) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            int y = ((net.minecraft.util.math.Vec3i) worldIn.getHeight(pos.add(x, 0, z))).getY() * 2;
            if (y > 0) {
                BlockPos fernPos = pos.add(x, rand.nextInt(y), z);
                new net.minecraft.world.gen.feature.WorldGenTallGrass(BlockTallGrass.EnumType.FERN).generate(worldIn, rand, fernPos);
            }
        }

        // Generate Short Grass
        for (int i = 0; i < 5; ++i) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            int y = ((net.minecraft.util.math.Vec3i) worldIn.getHeight(pos.add(x, 0, z))).getY() * 2;
            if (y > 0) {
                BlockPos grassPos = pos.add(x, rand.nextInt(y), z);
                new net.minecraft.world.gen.feature.WorldGenTallGrass(BlockTallGrass.EnumType.GRASS).generate(worldIn, rand, grassPos);
            }
        }

        // Generate Clover Patch
        for (int i = 0; i < 3; ++i) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            int y = ((net.minecraft.util.math.Vec3i) worldIn.getHeight(pos.add(x, 0, z))).getY();
            BlockPos cloverPos = pos.add(x, y, z);
            net.minecraft.block.Block blockDown = worldIn.getBlockState(cloverPos.down()).getBlock();
            if (worldIn.isAirBlock(cloverPos) && (blockDown == efw.blocks.OtbwgBlocks.LOAMY_GRASS || blockDown == efw.blocks.OtbwgBlocks.PEAT || blockDown == Blocks.DIRT || blockDown == efw.blocks.OtbwgBlocks.LOAMY_DIRT)) {
                worldIn.setBlockState(cloverPos, OtbwgBlocks.CLOVER_PATCH.getDefaultState(), 2);
            }
        }

        // Generate Rose
        for (int i = 0; i < 1; ++i) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            int y = ((net.minecraft.util.math.Vec3i) worldIn.getHeight(pos.add(x, 0, z))).getY();
            BlockPos rosePos = pos.add(x, y, z);
            net.minecraft.block.Block blockDown = worldIn.getBlockState(rosePos.down()).getBlock();
            if (worldIn.isAirBlock(rosePos) && (blockDown == efw.blocks.OtbwgBlocks.LOAMY_GRASS || blockDown == efw.blocks.OtbwgBlocks.PEAT || blockDown == Blocks.DIRT || blockDown == efw.blocks.OtbwgBlocks.LOAMY_DIRT)) {
                worldIn.setBlockState(rosePos, OtbwgBlocks.ROSE.getDefaultState(), 2);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getSkyColorByTemp(float currentTemperature) {
        return 12700876;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return 0x990000;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return 0x990000;
    }
}

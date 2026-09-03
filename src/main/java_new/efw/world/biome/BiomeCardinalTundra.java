package efw.world.biome;

import efw.blocks.OtbwgBlocks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BiomeCardinalTundra extends Biome {

    public BiomeCardinalTundra() {
        super(new BiomeProperties("Cardinal Tundra")
                .setBaseHeight(0.125F)
                .setHeightVariation(0.05F)
                .setTemperature(0.35F)
                .setRainfall(0.8F)
                .setSnowEnabled());

        this.setRegistryName("cardinal_tundra");

        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();

        this.decorator.treesPerChunk = -999;
        this.decorator.flowersPerChunk = 0;
        this.decorator.grassPerChunk = 2;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;

        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();

        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 10, 2, 3));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPolarBear.class, 1, 1, 2));
    }

    @Override
    public net.minecraft.world.gen.feature.WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return new net.minecraft.world.gen.feature.WorldGenTaiga2(false);
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        // Generate Rocky Stone Boulders - only 30% chance per chunk
        if (rand.nextFloat() < 0.3f && net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, rand, new net.minecraft.util.math.ChunkPos(pos), net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.ROCK)) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(x, 0, z)).down();
            net.minecraft.block.Block topBlock = worldIn.getBlockState(blockpos).getBlock();
            if (topBlock == Blocks.GRASS || topBlock == Blocks.DIRT || topBlock == Blocks.SNOW || topBlock == Blocks.SNOW_LAYER) {
                (new WorldGenBlockBlob(OtbwgBlocks.ROCKY_STONE, 0)).generate(worldIn, rand, blockpos.up());
            }
        }

        // Generate Large Ferns
        if (rand.nextFloat() < 0.2f) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            int y = worldIn.getHeight(pos.add(x, 0, z)).getY();
            BlockPos largeFernPos = pos.add(x, y, z);
            net.minecraft.world.gen.feature.WorldGenDoublePlant doublePlantGen = new net.minecraft.world.gen.feature.WorldGenDoublePlant();
            doublePlantGen.setPlantType(net.minecraft.block.BlockDoublePlant.EnumPlantType.FERN);
            doublePlantGen.generate(worldIn, rand, largeFernPos);
        }

        // Generate Normal Ferns
        for (int i = 0; i < 3; ++i) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            int y = worldIn.getHeight(pos.add(x, 0, z)).getY() * 2;
            if (y > 0) {
                BlockPos fernPos = pos.add(x, rand.nextInt(y), z);
                new WorldGenTallGrass(BlockTallGrass.EnumType.FERN).generate(worldIn, rand, fernPos);
            }
        }

    }

    // ------------------------------------
    // Цвета (Grass Color)
    // ------------------------------------

    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return getModdedBiomeGrassColor(0xA52525); // Бледно красноватый (Indian Red)
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return getModdedBiomeFoliageColor(0xA52525);
    }
}

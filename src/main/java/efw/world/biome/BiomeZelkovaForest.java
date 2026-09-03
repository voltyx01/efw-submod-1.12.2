package efw.world.biome;

import efw.blocks.OtbwgBlocks;
import efw.world.gen.OtbwgNBTTreeGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BiomeZelkovaForest extends Biome {

    public BiomeZelkovaForest() {
        super(new BiomeProperties("Zelkova Forest")
                .setBaseHeight(0.1F)
                .setHeightVariation(0.2F)
                .setTemperature(0.25F)
                .setRainfall(0.8F));

        this.setRegistryName("zelkova_forest");

        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();

        this.decorator.treesPerChunk = 5; // Dense forest
        this.decorator.flowersPerChunk = 4;
        this.decorator.grassPerChunk = 5;

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
        return new OtbwgNBTTreeGenerator(false, "features/trees/zelkova/zelkova_tree_trunk1", canopy, efw.blocks.OtbwgBlocks.ZELKOVA_LOG.getDefaultState(), efw.blocks.OtbwgBlocks.ZELKOVA_LEAVES.getDefaultState(), false, 5, 15);
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        // Custom Flowers (Anemone, Rose)
        for (int i = 0; i < 4; ++i) {
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
        return getModdedBiomeGrassColor(0x52A575);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return getModdedBiomeFoliageColor(0x52A575);
    }
}

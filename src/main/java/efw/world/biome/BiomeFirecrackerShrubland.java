package efw.world.biome;

import efw.blocks.OtbwgBlocks;
import efw.world.gen.OtbwgNBTTreeGenerator;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BiomeFirecrackerShrubland extends Biome {

    public BiomeFirecrackerShrubland() {
        super(new BiomeProperties("Firecracker Shrubland")
                .setBaseHeight(0.125F)
                .setHeightVariation(0.05F)
                .setTemperature(2.0F)
                .setRainfall(0.4F));

        this.setRegistryName("firecracker_shrubland");

        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();

        this.decorator.treesPerChunk = 2;
        this.decorator.flowersPerChunk = 0;
        this.decorator.grassPerChunk = 15;
        this.decorator.deadBushPerChunk = 1;
        this.decorator.mushroomsPerChunk = 1;
        this.decorator.bigMushroomsPerChunk = 0;

        // Clear standard farm animals and add specific
        this.spawnableCreatureList.clear();
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityHorse.class, 5, 2, 6));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityDonkey.class, 1, 1, 3));
        
        this.spawnableCaveCreatureList.clear();
        this.spawnableCaveCreatureList.add(new Biome.SpawnListEntry(EntityBat.class, 10, 8, 8));
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        if (rand.nextInt(3) == 0) {
            return new WorldGenTrees(false);
        } else {
            String[] canopies = {
                    "features/trees/firecracker_canopy1",
                    "features/trees/firecracker_canopy2"
            };
            String canopy = canopies[rand.nextInt(canopies.length)];
            return new OtbwgNBTTreeGenerator(false, "features/trees/firecracker_trunk", canopy, Blocks.LOG.getDefaultState(), Blocks.LEAVES.getDefaultState(), true);
        }
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);

        // Generate Firecracker Flower Bush
        for (int i = 0; i < 4; ++i) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            int y = worldIn.getHeight(pos.add(x, 0, z)).getY();
            BlockPos bushPos = pos.add(x, y, z);
            net.minecraft.block.Block blockDown = worldIn.getBlockState(bushPos.down()).getBlock();
            net.minecraft.block.Block firecrackerBush = net.minecraft.block.Block.getBlockFromName("mwccf:firecracker_flower_bush");
            if (worldIn.isAirBlock(bushPos) && (blockDown == Blocks.GRASS || blockDown == Blocks.DIRT) && firecrackerBush != null) {
                worldIn.setBlockState(bushPos, firecrackerBush.getDefaultState(), 2);
            }
        }
    }

    // ------------------------------------
    // Colors
    // ------------------------------------

    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return getModdedBiomeGrassColor(0x8FBC8F); // Бледно-зеленый (DarkSeaGreen)
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return getModdedBiomeFoliageColor(0x7EB37E); // Более темный бледно-зеленый
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getWaterColorMultiplier() {
        return 4159204;
    }
}

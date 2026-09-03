package efw.world.gen;

import efw.blocks.OtbwgBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;

public class WorldGenCikaTree extends WorldGenAbstractTree {

    public WorldGenCikaTree() {
        super(false);
    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int variant = rand.nextInt(3) + 1; // 1, 2, 3
        
        String trunkPath = "features/trees/cika/cika_trunk" + variant;
        String canopyPath = "features/trees/cika/cika_canopy" + variant;

        OtbwgNBTTreeGenerator generator = new OtbwgNBTTreeGenerator(
            true, 
            trunkPath, 
            canopyPath, 
            OtbwgBlocks.CIKA_LOG.getDefaultState(), 
            OtbwgBlocks.CIKA_LEAVES.getDefaultState(),
            true // Canopy should sit on top of the trunk using sizeY
        );

        return generator.generate(worldIn, rand, position);
    }
}

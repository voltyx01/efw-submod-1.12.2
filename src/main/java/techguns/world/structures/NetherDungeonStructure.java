package techguns.world.structures;

import java.util.Random;
import net.minecraft.world.World;

public class NetherDungeonStructure extends WorldgenStructure {
    public NetherDungeonStructure() {
        this.heightdiffLimit=10;
    }

    @Override
    public void setBlocks(World world, int posX, int posY, int posZ, int sizeX, int sizeY, int sizeZ, int direction,
            BiomeColorType colorType, Random rnd) {
        // Stubbed out: Dungeon system not fully ported
    }

    @Override
    public int getSizeX(Random rnd) {
        return 32+rnd.nextInt(16);
    }

    @Override
    public int getSizeZ(Random rnd) {
        return 32+rnd.nextInt(16);
    }
    
    @Override
    public int getSizeY(Random rnd) {
        return 24+rnd.nextInt(16);
    }
}

package techguns.util;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

import techguns.TGBlocks;
import techguns.TGOreClusters.OreCluster;
import techguns.TGOreClusters.OreClusterWeightedEntry;
import techguns.blocks.EnumOreClusterType;
import techguns.client.render.fx.MultiScreenEffect;
import techguns.world.EnumLootType;
import techguns.world.structures.WorldgenStructure.BiomeColorType;

public class MBlockOreclusterType extends MultiMMBlockIndexRoll {

	float oreChance=0;

	MBlock[] oreMBlocks;
	
	public MBlockOreclusterType(EnumOreClusterType[] types, int[] weights, float oreChance, MBlock[] ores) {
		super(fillMBlocks(types), weights);
		this.oreChance=oreChance;
		this.oreMBlocks=ores;
	}
	
	protected static MBlock[] fillMBlocks(EnumOreClusterType[] types) {
		MBlock[] mBlocks = new MBlock[types.length];
		int i=-1;
		for(EnumOreClusterType o: types) {
			OreCluster clust = null;
			mBlocks[++i] = new MBlock(net.minecraft.init.Blocks.AIR.getDefaultState());
		}
		return mBlocks;
	}
	
	@Override
	public void setBlock(World w, MutableBlockPos pos, int rotation, EnumLootType loottype, BiomeColorType biome, int index, Random rnd) {
		if(oreChance>0 && oreMBlocks!=null && rnd.nextFloat()<oreChance) {
			this.oreMBlocks[index].setBlock(w, pos, rotation, loottype, biome);
		} else {
			this.mBlocks[index].setBlock(w, pos, rotation, loottype, biome);
		}
	}
	
}

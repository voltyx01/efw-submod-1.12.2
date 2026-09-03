package efw.world.gen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OtbwgNBTTreeGenerator extends WorldGenAbstractTree {

    private final String trunkNbtPath;
    private final String[] canopyNbtPaths;
    private final IBlockState logState;
    private final IBlockState leafState;
    private final boolean stacked;
    private final int minHeight;
    private final int maxHeight;

    public OtbwgNBTTreeGenerator(boolean notify, String trunkNbtPath, String[] canopyNbtPaths, IBlockState logState, IBlockState leafState, boolean stacked, int minHeight, int maxHeight) {
        super(notify);
        this.trunkNbtPath = trunkNbtPath;
        this.canopyNbtPaths = canopyNbtPaths;
        this.logState = logState;
        this.leafState = leafState;
        this.stacked = stacked;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }
    
    public OtbwgNBTTreeGenerator(boolean notify, String trunkNbtPath, String canopyNbtPath, IBlockState logState, IBlockState leafState, boolean stacked) {
        this(notify, trunkNbtPath, new String[]{canopyNbtPath}, logState, leafState, stacked, 0, 0);
    }
    
    public OtbwgNBTTreeGenerator(boolean notify, String trunkNbtPath, String canopyNbtPath, IBlockState logState, IBlockState leafState, boolean stacked, int minHeight, int maxHeight) {
        this(notify, trunkNbtPath, new String[]{canopyNbtPath}, logState, leafState, stacked, minHeight, maxHeight);
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        while (position.getY() > 0 && (world.isAirBlock(position) || world.getBlockState(position).getBlock().isReplaceable(world, position) || world.getBlockState(position).getBlock() instanceof net.minecraft.block.BlockBush || world.getBlockState(position).getBlock() instanceof net.minecraft.block.BlockSnow)) {
            position = position.down();
        }

        net.minecraft.block.Block blockUp = world.getBlockState(position.up()).getBlock();
        if (blockUp == net.minecraft.init.Blocks.WATER || blockUp == net.minecraft.init.Blocks.FLOWING_WATER || world.getBlockState(position.up()).getMaterial() == net.minecraft.block.material.Material.WATER) {
            return false;
        }

        net.minecraft.block.Block blockDown = world.getBlockState(position.down()).getBlock();
        if (blockDown != net.minecraft.init.Blocks.DIRT && 
            blockDown != net.minecraft.init.Blocks.GRASS && 
            blockDown != efw.blocks.OtbwgBlocks.LUSH_DIRT && 
            blockDown != efw.blocks.OtbwgBlocks.LOAMY_DIRT && 
            blockDown != efw.blocks.OtbwgBlocks.LOAMY_GRASS &&
            blockDown != efw.blocks.OtbwgBlocks.PEAT &&
            blockDown != efw.blocks.OtbwgBlocks.OVERGROWN_DACITE &&
            blockDown != efw.blocks.OtbwgBlocks.PODZOL_DACITE &&
            blockDown != efw.blocks.OtbwgBlocks.DACITE &&
            blockDown != efw.blocks.OtbwgBlocks.OVERGROWN_STONE &&
            blockDown != net.minecraft.init.Blocks.STONE) {
            return false;
        }

        int trunkHeight = 0;
        int currentHeight = 0;

        if (minHeight > 0) {
            trunkHeight = rand.nextInt(maxHeight - minHeight + 1) + minHeight;
            if (trunkNbtPath != null && !trunkNbtPath.isEmpty()) {
                placeStructure(world, position, trunkNbtPath, logState, leafState, true, 0, false);
            }
            if (canopyNbtPaths != null && canopyNbtPaths.length > 0) {
                String selectedCanopy = canopyNbtPaths[rand.nextInt(canopyNbtPaths.length)];
                if (selectedCanopy != null && !selectedCanopy.isEmpty()) {
                    BlockPos canopyPos = position.up(trunkHeight);
                    placeStructure(world, canopyPos, selectedCanopy, logState, leafState, false, 0, stacked);
                }
            }
            return true;
        }

        // Legacy behavior for non-min/max height
        int pillarExtension = 0;
        if (trunkNbtPath != null && trunkNbtPath.contains("spruce")) {
            pillarExtension = rand.nextInt(5) + 3; // 3 to 7 blocks tall pillar
        } else if (trunkNbtPath != null && trunkNbtPath.contains("generic_trunk")) {
            pillarExtension = rand.nextInt(4) + 2; // 2 to 5 blocks tall pillar
        } else if (trunkNbtPath != null && trunkNbtPath.contains("zelkova")) {
            pillarExtension = rand.nextInt(5) + 2; // 2 to 6 blocks tall pillar
        }

        if (trunkNbtPath != null && !trunkNbtPath.isEmpty()) {
            trunkHeight = placeStructure(world, position, trunkNbtPath, logState, leafState, true, pillarExtension, false);
        } else {
            trunkHeight = rand.nextInt(11) + 5; // 5 to 15 height
            for (int i = 0; i < trunkHeight; i++) {
                this.setBlockAndNotifyAdequately(world, position.up(i), logState);
            }
        }
        
        currentHeight = stacked ? trunkHeight : 0;

        if (canopyNbtPaths != null) {
            for (int i = 0; i < canopyNbtPaths.length; i++) {
                String cPath = canopyNbtPaths[i];
                if (cPath != null && !cPath.isEmpty()) {
                    BlockPos canopyPos = position.up(currentHeight);
                    boolean isCanopyStacked = stacked || i > 0;
                    
                    int cSizeY = placeStructure(world, canopyPos, cPath, logState, leafState, false, 0, isCanopyStacked);
                    
                    if (isCanopyStacked) {
                        currentHeight += (cSizeY - 1);
                    } else {
                        currentHeight += cSizeY;
                    }
                }
            }
            
            // Programmatically draw the missing 8 blocks of trunk for cika_canopy3
            for (String cPath : canopyNbtPaths) {
                if (cPath != null && cPath.contains("cika_canopy3")) {
                    BlockPos canopyPos = stacked ? position.up(trunkHeight) : position;
                    for (int i = 0; i <= 8; i++) {
                        this.setBlockAndNotifyAdequately(world, canopyPos.add(-1, i - 1, -1), logState);
                    }
                }
            }
        }
        return true;
    }

    // Returns the sizeY of the placed structure
    private int placeStructure(World world, BlockPos basePos, String nbtPath, IBlockState mappedLog, IBlockState mappedLeaf, boolean isTrunk, int pillarExtension, boolean isStacked) {
        int sizeY = 0;
        try {
            InputStream is = OtbwgNBTTreeGenerator.class.getResourceAsStream("/assets/mwccf/structures/" + nbtPath + ".nbt");
            if (is == null) {
                System.err.println("Failed to load tree structure: " + nbtPath);
                return 0;
            }
            NBTTagCompound compound = CompressedStreamTools.readCompressed(is);
            is.close();

            NBTTagList sizeList = compound.getTagList("size", 3); // 3 = Int
            int sizeX = sizeList.getIntAt(0);
            sizeY = sizeList.getIntAt(1);
            int sizeZ = sizeList.getIntAt(2);

            NBTTagList blocks = compound.getTagList("blocks", 10); // 10 = Compound
            NBTTagList palette = compound.getTagList("palette", 10);
            
            java.util.Map<Integer, String> paletteMap = new java.util.HashMap<>();
            int whiteWoolStateIdx = -1;
            int redWoolStateIdx = -1;

            for (int i = 0; i < palette.tagCount(); i++) {
                NBTTagCompound blockStateTag = palette.getCompoundTagAt(i);
                String blockName = blockStateTag.getString("Name");
                paletteMap.put(i, blockName);
                if (blockName.equals("minecraft:white_wool")) {
                    whiteWoolStateIdx = i;
                } else if (blockName.equals("minecraft:red_wool")) {
                    redWoolStateIdx = i;
                }
            }
            
            int anchorX = sizeX / 2;
            int anchorY = 0;
            int anchorZ = sizeZ / 2;
            
            java.util.List<BlockPos> redWoolPositions = new java.util.ArrayList<>();
            int maxLogY = 0;

            for (int i = 0; i < blocks.tagCount(); i++) {
                NBTTagCompound blockTag = blocks.getCompoundTagAt(i);
                NBTTagList posTag = blockTag.getTagList("pos", 3);
                int x = posTag.getIntAt(0);
                int y = posTag.getIntAt(1);
                int z = posTag.getIntAt(2);
                int stateIdx = blockTag.getInteger("state");
                
                if (stateIdx == whiteWoolStateIdx) {
                    anchorX = x;
                    // anchorY is intentionally ignored (always 0) to match TYG mechanics
                    anchorZ = z;
                }
                if (stateIdx == redWoolStateIdx) {
                    redWoolPositions.add(new BlockPos(x, y, z));
                }

                String blockName = paletteMap.getOrDefault(stateIdx, "minecraft:air");
                if (blockName.contains("log") || blockName.contains("stem") || blockName.contains("wood")) {
                    if (y > maxLogY) {
                        maxLogY = y;
                    }
                }
            }

            int offsetX = -anchorX;
            int offsetY = -anchorY;
            int offsetZ = -anchorZ;
            
            // Legacy stacked logic if White Wool wasn't used
            if (whiteWoolStateIdx == -1 && !isTrunk && isStacked) {
                offsetY = -1;
            }

            for (int i = 0; i < blocks.tagCount(); i++) {
                NBTTagCompound blockTag = blocks.getCompoundTagAt(i);
                NBTTagList posTag = blockTag.getTagList("pos", 3);
                int x = posTag.getIntAt(0);
                int y = posTag.getIntAt(1);
                int z = posTag.getIntAt(2);
                int stateIdx = blockTag.getInteger("state");

                if (stateIdx == whiteWoolStateIdx || stateIdx == redWoolStateIdx) {
                    continue; // Do not place wool markers
                }

                String blockName = paletteMap.getOrDefault(stateIdx, "minecraft:air");
                
                int actualX = x + offsetX;
                int actualZ = z + offsetZ;
                
                BlockPos targetPos = basePos.add(actualX, y + offsetY, actualZ);
                
                if (targetPos.getY() < 0 || targetPos.getY() > 255) {
                    continue;
                }
                
                boolean isLog = blockName.contains("log") || blockName.contains("stem") || blockName.contains("wood");
                boolean isLeaf = blockName.contains("leaves");
                IBlockState stateToPlace = isLeaf ? mappedLeaf : mappedLog;
                
                if (isLog || isLeaf || !blockName.equals("minecraft:air")) {
                    if (!world.isBlockLoaded(targetPos, false)) continue;
                    net.minecraft.block.Block currentBlock = world.getBlockState(targetPos).getBlock();
                    if (world.isAirBlock(targetPos) || currentBlock.isReplaceable(world, targetPos) || 
                        currentBlock instanceof net.minecraft.block.BlockLeaves || 
                        currentBlock instanceof net.minecraft.block.BlockBush || 
                        currentBlock instanceof net.minecraft.block.BlockSnow ||
                        currentBlock == net.minecraft.init.Blocks.LOG ||
                        currentBlock == net.minecraft.init.Blocks.LOG2 ||
                        currentBlock == efw.blocks.OtbwgBlocks.ZELKOVA_LOG) {
                        this.setBlockAndNotifyAdequately(world, targetPos, stateToPlace);
                    }
                }
            }
            
            // Draw logs downwards from Red Wool positions
            if (!redWoolPositions.isEmpty()) {
                for (BlockPos rPos : redWoolPositions) {
                    BlockPos currentPos = basePos.add(rPos.getX() + offsetX, rPos.getY() + offsetY, rPos.getZ() + offsetZ);
                    // Draw downwards until we hit a non-air block, or we go below the feature base
                    for (int d = 0; d < 30; d++) {
                        BlockPos downPos = currentPos.down(d);
                        if (downPos.getY() < 0 || downPos.getY() > 255 || !world.isBlockLoaded(downPos, false)) {
                            break;
                        }
                        net.minecraft.block.Block b = world.getBlockState(downPos).getBlock();
                        if (!world.isAirBlock(downPos) && !b.isReplaceable(world, downPos) && !(b instanceof net.minecraft.block.BlockLeaves) && !(b instanceof net.minecraft.block.BlockSnow) && !(b instanceof net.minecraft.block.BlockBush)) {
                            // Hit solid ground or another log
                            break;
                        }
                        this.setBlockAndNotifyAdequately(world, downPos, mappedLog);
                    }
                }
            } else if (isTrunk && pillarExtension > 0) {
                // Legacy pillar support if no red wool
                int topLayerY = sizeY - 1;
                for (int i = 0; i < blocks.tagCount(); i++) {
                    NBTTagCompound blockTag = blocks.getCompoundTagAt(i);
                    NBTTagList posTag = blockTag.getTagList("pos", 3);
                    int y = posTag.getIntAt(1);
                    if (y == topLayerY) {
                        int stateIdx = blockTag.getInteger("state");
                        String blockName = paletteMap.getOrDefault(stateIdx, "minecraft:air");
                        if (blockName.contains("log") || blockName.contains("stem") || blockName.contains("wood")) {
                            for (int p = 1; p <= pillarExtension; p++) {
                                BlockPos pillarPos = basePos.add(posTag.getIntAt(0) + offsetX, topLayerY + p + offsetY, posTag.getIntAt(2) + offsetZ);
                                this.setBlockAndNotifyAdequately(world, pillarPos, mappedLog);
                            }
                        }
                    }
                }
                sizeY += pillarExtension;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sizeY;
    }

    @Override
    protected void setBlockAndNotifyAdequately(World worldIn, BlockPos pos, IBlockState state) {
        if (pos.getY() >= 0 && pos.getY() < 256 && worldIn.isBlockLoaded(pos, false)) {
            worldIn.setBlockState(pos, state, 18);
        }
    }
}

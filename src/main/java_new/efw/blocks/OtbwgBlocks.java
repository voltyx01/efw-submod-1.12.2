package efw.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "mwccf")
public class OtbwgBlocks {

    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final List<Item> ITEMS = new ArrayList<>();

    // ==========================================
    // УМНЫЕ МЕТОДЫ РЕГИСТРАЦИИ (В 1 СТРОЧКУ)
    // ==========================================
    private static Block makeCube(String name, Material mat, SoundType sound, float hardness, float resist) {
        Block block = new Block(mat) { { this.setSoundType(sound); } }
                .setRegistryName("mwccf", name).setTranslationKey("otbwg." + name)
                .setHardness(hardness).setResistance(resist).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        BLOCKS.add(block);
        ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        return block;
    }

    private static Block makeGrass(String name) {
        Block block = new net.minecraft.block.BlockGrass() {
            { this.setSoundType(SoundType.PLANT); }
            @Override public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.util.EnumFacing direction, net.minecraftforge.common.IPlantable plantable) { return true; }
            @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
            @Override public net.minecraft.util.BlockRenderLayer getRenderLayer() { return net.minecraft.util.BlockRenderLayer.CUTOUT_MIPPED; }
        }.setRegistryName("mwccf", name).setTranslationKey("otbwg." + name).setHardness(0.6F).setResistance(0.6F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        BLOCKS.add(block);
        ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        return block;
    }

    private static Block makeIce(String name) {
        Block block = new Block(Material.ICE) {
            { this.setSoundType(SoundType.GLASS); this.slipperiness = 0.98F; }
        }.setRegistryName("mwccf", name).setTranslationKey("otbwg." + name).setHardness(0.5F).setResistance(0.5F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        BLOCKS.add(block);
        ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        return block;
    }
    private static Block makeLog(String name) {
        return new BlockOtbwgLog(name); // Теперь бревна используют правильный класс
    }

    private static Block makePlanks(String name) {
        Block block = new BlockOtbwgPlanks(name);
        BLOCKS.add(block);
        ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));

        String baseName = name.replace("_planks", "");

        Block stairs = new BlockOtbwgStairs(baseName + "_stairs", block.getDefaultState());
        BLOCKS.add(stairs);
        ITEMS.add(new ItemBlock(stairs).setRegistryName(stairs.getRegistryName()));

        Block slab = new BlockOtbwgSlab(baseName + "_slab", Material.WOOD);
        BLOCKS.add(slab);
        ITEMS.add(new ItemBlock(slab).setRegistryName(slab.getRegistryName()));

        Block fence = new BlockOtbwgFence(baseName + "_fence", Material.WOOD, net.minecraft.block.material.MapColor.WOOD);
        BLOCKS.add(fence);
        ITEMS.add(new ItemBlock(fence).setRegistryName(fence.getRegistryName()));

        Block gate = new BlockOtbwgFenceGate(baseName + "_fence_gate");
        BLOCKS.add(gate);
        ITEMS.add(new ItemBlock(gate).setRegistryName(gate.getRegistryName()));

        return block;
    }

    // ==========================================
    // ЛИСТЬЯ И БАЗОВАЯ ЗЕМЛЯ
    // ==========================================
    // ==========================================
    // ЛИСТЬЯ И БАЗОВАЯ ЗЕМЛЯ
    // ==========================================
    public static final Block RED_SPRUCE_LEAVES = new BlockOtbwgLeaves("red_spruce_leaves");
    public static final Block ORANGE_SPRUCE_LEAVES = new BlockOtbwgLeaves("orange_spruce_leaves");

    // ДОБАВЬ ВОТ ЭТИ ДВЕ СТРОЧКИ:
    public static final Block CLOVER_PATCH = new BlockCloverPatch("clover_patch");
    public static final Block ROSE = new BlockOtbwgFlower("rose");

    public static final Block LOAMY_GRASS = makeGrass("loamy_grass");
    public static final Block LOAMY_DIRT = makeLoamyDirt("loamy_dirt");
    public static final Block ROCKY_STONE = makeCube("rocky_stone", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block PEAT = makePlantableDirt("peat", Material.GROUND, SoundType.GROUND, 0.5f, 0.5f);
    public static final Block SANDY_DIRT = makeCube("sandy_dirt", Material.GROUND, SoundType.GROUND, 0.5f, 0.5f);
    public static final Block LUSH_DIRT = makeCube("lush_dirt", Material.GROUND, SoundType.GROUND, 0.5f, 0.5f);
    // ==========================================
// ДЕРЕВЬЯ (БРЕВНА И ДОСКИ)
// ==========================================
    public static final Block ASPEN_LOG = makeLog("aspen_log");
    public static final Block STRIPPED_ASPEN_LOG = makeLog("stripped_aspen_log");
    public static final Block ASPEN_WOOD = makeLog("aspen_wood");
    public static final Block ASPEN_PLANKS = makePlanks("aspen_planks");
    public static final Block ASPEN_LEAVES = new BlockOtbwgLeaves("aspen_leaves");

    public static final Block BAOBAB_LOG = makeLog("baobab_log");
    public static final Block STRIPPED_BAOBAB_LOG = makeLog("stripped_baobab_log");
    public static final Block BAOBAB_WOOD = makeLog("baobab_wood");
    public static final Block BAOBAB_PLANKS = makePlanks("baobab_planks");
    public static final Block BAOBAB_LEAVES = new BlockOtbwgLeaves("baobab_leaves");

    public static final Block BLUE_ENCHANTED_LOG = makeLog("blue_enchanted_log");
    public static final Block STRIPPED_BLUE_ENCHANTED_LOG = makeLog("stripped_blue_enchanted_log");
    public static final Block BLUE_ENCHANTED_WOOD = makeLog("blue_enchanted_wood");
    public static final Block BLUE_ENCHANTED_PLANKS = makePlanks("blue_enchanted_planks");
    public static final Block BLUE_ENCHANTED_LEAVES = new BlockOtbwgLeaves("blue_enchanted_leaves");

    public static final Block CIKA_LOG = makeLog("cika_log");
    public static final Block STRIPPED_CIKA_LOG = makeLog("stripped_cika_log");
    public static final Block CIKA_WOOD = makeLog("cika_wood");
    public static final Block CIKA_PLANKS = makePlanks("cika_planks");
    public static final Block CIKA_LEAVES = new BlockOtbwgLeaves("cika_leaves");

    public static final Block CYPRESS_LOG = makeLog("cypress_log");
    public static final Block STRIPPED_CYPRESS_LOG = makeLog("stripped_cypress_log");
    public static final Block CYPRESS_WOOD = makeLog("cypress_wood");
    public static final Block CYPRESS_PLANKS = makePlanks("cypress_planks");
    public static final Block CYPRESS_LEAVES = new BlockOtbwgLeaves("cypress_leaves");

    public static final Block EBONY_LOG = makeLog("ebony_log");
    public static final Block STRIPPED_EBONY_LOG = makeLog("stripped_ebony_log");
    public static final Block EBONY_WOOD = makeLog("ebony_wood");
    public static final Block EBONY_PLANKS = makePlanks("ebony_planks");
    public static final Block EBONY_LEAVES = new BlockOtbwgLeaves("ebony_leaves");

    public static final Block FIR_LOG = makeLog("fir_log");
    public static final Block STRIPPED_FIR_LOG = makeLog("stripped_fir_log");
    public static final Block FIR_WOOD = makeLog("fir_wood");
    public static final Block FIR_PLANKS = makePlanks("fir_planks");
    public static final Block FIR_LEAVES = new BlockOtbwgLeaves("fir_leaves");

    public static final Block GREEN_ENCHANTED_LOG = makeLog("green_enchanted_log");
    public static final Block STRIPPED_GREEN_ENCHANTED_LOG = makeLog("stripped_green_enchanted_log");
    public static final Block GREEN_ENCHANTED_WOOD = makeLog("green_enchanted_wood");
    public static final Block GREEN_ENCHANTED_PLANKS = makePlanks("green_enchanted_planks");
    public static final Block GREEN_ENCHANTED_LEAVES = new BlockOtbwgLeaves("green_enchanted_leaves");

    public static final Block HOLLY_LOG = makeLog("holly_log");
    public static final Block STRIPPED_HOLLY_LOG = makeLog("stripped_holly_log");
    public static final Block HOLLY_WOOD = makeLog("holly_wood");
    public static final Block HOLLY_PLANKS = makePlanks("holly_planks");
    public static final Block HOLLY_LEAVES = new BlockOtbwgLeaves("holly_leaves");

    public static final Block IRONWOOD_LOG = makeLog("ironwood_log");
    public static final Block STRIPPED_IRONWOOD_LOG = makeLog("stripped_ironwood_log");
    public static final Block IRONWOOD_WOOD = makeLog("ironwood_wood");
    public static final Block IRONWOOD_PLANKS = makePlanks("ironwood_planks");
    public static final Block IRONWOOD_LEAVES = new BlockOtbwgLeaves("ironwood_leaves");

    public static final Block JACARANDA_LOG = makeLog("jacaranda_log");
    public static final Block STRIPPED_JACARANDA_LOG = makeLog("stripped_jacaranda_log");
    public static final Block JACARANDA_WOOD = makeLog("jacaranda_wood");
    public static final Block JACARANDA_PLANKS = makePlanks("jacaranda_planks");
    public static final Block JACARANDA_LEAVES = new BlockOtbwgLeaves("jacaranda_leaves");

    public static final Block MAHOGANY_LOG = makeLog("mahogany_log");
    public static final Block STRIPPED_MAHOGANY_LOG = makeLog("stripped_mahogany_log");
    public static final Block MAHOGANY_WOOD = makeLog("mahogany_wood");
    public static final Block MAHOGANY_PLANKS = makePlanks("mahogany_planks");
    public static final Block MAHOGANY_LEAVES = new BlockOtbwgLeaves("mahogany_leaves");

    public static final Block MAPLE_LOG = makeLog("maple_log");
    public static final Block STRIPPED_MAPLE_LOG = makeLog("stripped_maple_log");
    public static final Block MAPLE_WOOD = makeLog("maple_wood");
    public static final Block MAPLE_PLANKS = makePlanks("maple_planks");
    public static final Block MAPLE_LEAVES = new BlockOtbwgLeaves("maple_leaves");

    public static final Block PALM_LOG = makeLog("palm_log");
    public static final Block STRIPPED_PALM_LOG = makeLog("stripped_palm_log");
    public static final Block PALM_WOOD = makeLog("palm_wood");
    public static final Block PALM_PLANKS = makePlanks("palm_planks");
    public static final Block PALM_LEAVES = new BlockOtbwgLeaves("palm_leaves");

    public static final Block PINE_LOG = makeLog("pine_log");
    public static final Block STRIPPED_PINE_LOG = makeLog("stripped_pine_log");
    public static final Block PINE_WOOD = makeLog("pine_wood");
    public static final Block PINE_PLANKS = makePlanks("pine_planks");
    public static final Block PINE_LEAVES = new BlockOtbwgLeaves("pine_leaves");

    public static final Block RAINBOW_EUCALYPTUS_LOG = makeLog("rainbow_eucalyptus_log");
    public static final Block STRIPPED_RAINBOW_EUCALYPTUS_LOG = makeLog("stripped_rainbow_eucalyptus_log");
    public static final Block RAINBOW_EUCALYPTUS_WOOD = makeLog("rainbow_eucalyptus_wood");
    public static final Block RAINBOW_EUCALYPTUS_PLANKS = makePlanks("rainbow_eucalyptus_planks");
    public static final Block RAINBOW_EUCALYPTUS_LEAVES = new BlockOtbwgLeaves("rainbow_eucalyptus_leaves");

    public static final Block REDWOOD_LOG = makeLog("redwood_log");
    public static final Block STRIPPED_REDWOOD_LOG = makeLog("stripped_redwood_log");
    public static final Block REDWOOD_WOOD = makeLog("redwood_wood");
    public static final Block REDWOOD_PLANKS = makePlanks("redwood_planks");
    public static final Block REDWOOD_LEAVES = new BlockOtbwgLeaves("redwood_leaves");

    public static final Block SKYRIS_LOG = makeLog("skyris_log");
    public static final Block STRIPPED_SKYRIS_LOG = makeLog("stripped_skyris_log");
    public static final Block SKYRIS_WOOD = makeLog("skyris_wood");
    public static final Block SKYRIS_PLANKS = makePlanks("skyris_planks");
    public static final Block SKYRIS_LEAVES = new BlockOtbwgLeaves("skyris_leaves");

    public static final Block WHITE_MANGROVE_LOG = makeLog("white_mangrove_log");
    public static final Block STRIPPED_WHITE_MANGROVE_LOG = makeLog("stripped_white_mangrove_log");
    public static final Block WHITE_MANGROVE_WOOD = makeLog("white_mangrove_wood");
    public static final Block WHITE_MANGROVE_PLANKS = makePlanks("white_mangrove_planks");
    public static final Block WHITE_MANGROVE_LEAVES = new BlockOtbwgLeaves("white_mangrove_leaves");

    public static final Block WILLOW_LOG = makeLog("willow_log");
    public static final Block STRIPPED_WILLOW_LOG = makeLog("stripped_willow_log");
    public static final Block WILLOW_WOOD = makeLog("willow_wood");
    public static final Block WILLOW_PLANKS = makePlanks("willow_planks");
    public static final Block WILLOW_LEAVES = new BlockOtbwgLeaves("willow_leaves");

    public static final Block WITCH_HAZEL_LOG = makeLog("witch_hazel_log");
    public static final Block STRIPPED_WITCH_HAZEL_LOG = makeLog("stripped_witch_hazel_log");
    public static final Block WITCH_HAZEL_WOOD = makeLog("witch_hazel_wood");
    public static final Block WITCH_HAZEL_PLANKS = makePlanks("witch_hazel_planks");
    public static final Block WITCH_HAZEL_LEAVES = new BlockOtbwgLeaves("witch_hazel_leaves");

    public static final Block ZELKOVA_LOG = makeLog("zelkova_log");
    public static final Block STRIPPED_ZELKOVA_LOG = makeLog("stripped_zelkova_log");
    public static final Block ZELKOVA_WOOD = makeLog("zelkova_wood");
    public static final Block ZELKOVA_PLANKS = makePlanks("zelkova_planks");
    public static final Block ZELKOVA_LEAVES = new BlockOtbwgLeaves("zelkova_leaves");
    public static final Block BROWN_ZELKOVA_LEAVES = new BlockOtbwgLeaves("brown_zelkova_leaves");
    // ==========================================
    // КАМНИ И ПЕСКИ
    // ==========================================
    public static final Block BLACK_SAND = makeCube("black_sand", Material.SAND, SoundType.SAND, 0.5f, 0.5f);
    public static final Block WHITE_SAND = makeCube("white_sand", Material.SAND, SoundType.SAND, 0.5f, 0.5f);
    public static final Block BLUE_SAND = makeCube("blue_sand", Material.SAND, SoundType.SAND, 0.5f, 0.5f);
    public static final Block PURPLE_SAND = makeCube("purple_sand", Material.SAND, SoundType.SAND, 0.5f, 0.5f);
    public static final Block PINK_SAND = makeCube("pink_sand", Material.SAND, SoundType.SAND, 0.5f, 0.5f);
    public static final Block WINDSWEPT_SAND = makeCube("windswept_sand", Material.SAND, SoundType.SAND, 0.5f, 0.5f);
    public static final Block CRACKED_SAND = makeCube("cracked_sand", Material.SAND, SoundType.SAND, 0.5f, 0.5f);
    public static final Block CRACKED_RED_SAND = makeCube("cracked_red_sand", Material.SAND, SoundType.SAND, 0.5f, 0.5f);
    public static final Block QUICKSAND = makeCube("quicksand", Material.SAND, SoundType.SAND, 0.5f, 0.5f);
    public static final Block RED_QUICKSAND = makeCube("red_quicksand", Material.SAND, SoundType.SAND, 0.5f, 0.5f);

    public static final Block PODZOL_DACITE = new Block(Material.GROUND) {
        { this.setSoundType(SoundType.GROUND); }
        @Override
        public boolean canSustainPlant(IBlockState state, net.minecraft.world.IBlockAccess world, net.minecraft.util.math.BlockPos pos, net.minecraft.util.EnumFacing direction, net.minecraftforge.common.IPlantable plantable) {
            return true;
        }
    }.setRegistryName("mwccf", "podzol_dacite").setTranslationKey("otbwg.podzol_dacite").setHardness(1.5f).setResistance(10f).setCreativeTab(net.minecraft.creativetab.CreativeTabs.BUILDING_BLOCKS);

    static {
        BLOCKS.add(PODZOL_DACITE);
        ITEMS.add(new net.minecraft.item.ItemBlock(PODZOL_DACITE).setRegistryName(PODZOL_DACITE.getRegistryName()));
    }
    public static final Block OVERGROWN_DACITE = makeGrass("overgrown_dacite");
    public static final Block OVERGROWN_STONE = makeGrass("overgrown_stone");

    public static final Block BARREL_CACTUS = new BlockSolidCactus("barrel_cactus");
    public static final Block FLOWERING_BARREL_CACTUS = new BlockSolidCactus("flowering_barrel_cactus");
    public static final Block CARVED_BARREL_CACTUS = new BlockSolidCactus("carved_barrel_cactus");

    public static final Block BLACK_SANDSTONE = makeCube("black_sandstone", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block WHITE_SANDSTONE = makeCube("white_sandstone", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block BLUE_SANDSTONE = makeCube("blue_sandstone", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block PURPLE_SANDSTONE = makeCube("purple_sandstone", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block PINK_SANDSTONE = makeCube("pink_sandstone", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block WINDSWEPT_SANDSTONE = makeCube("windswept_sandstone", Material.ROCK, SoundType.STONE, 1.5f, 10f);

    public static final Block DACITE = makeCube("dacite", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block DACITE_BRICKS = makeCube("dacite_bricks", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block CRACKED_DACITE_BRICKS = makeCube("cracked_dacite_bricks", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block MOSSY_DACITE_BRICKS = makeCube("mossy_dacite_bricks", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block CHISELED_DACITE_BRICKS = makeCube("chiseled_dacite_bricks", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block DACITE_COBBLESTONE = makeCube("dacite_cobblestone", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block DACITE_TILES = makeCube("dacite_tiles", Material.ROCK, SoundType.STONE, 1.5f, 10f);

    public static final Block WHITE_DACITE = makeCube("white_dacite", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block WHITE_DACITE_BRICKS = makeCube("white_dacite_bricks", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block CRACKED_WHITE_DACITE_BRICKS = makeCube("cracked_white_dacite_bricks", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block MOSSY_WHITE_DACITE_BRICKS = makeCube("mossy_white_dacite_bricks", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block CHISELED_WHITE_DACITE_BRICKS = makeCube("chiseled_white_dacite_bricks", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block WHITE_DACITE_COBBLESTONE = makeCube("white_dacite_cobblestone", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block WHITE_DACITE_TILES = makeCube("white_dacite_tiles", Material.ROCK, SoundType.STONE, 1.5f, 10f);

    public static final Block RED_ROCK = makeCube("red_rock", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block RED_ROCK_BRICKS = makeCube("red_rock_bricks", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block CRACKED_RED_ROCK_BRICKS = makeCube("cracked_red_rock_bricks", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block CHISELED_RED_ROCK_BRICKS = makeCube("chiseled_red_rock_bricks", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block MOSSY_RED_ROCK_BRICKS = makeCube("mossy_red_rock_bricks", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block POLISHED_RED_ROCK = makeCube("polished_red_rock", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block RED_ROCK_TILES = makeCube("red_rock_tiles", Material.ROCK, SoundType.STONE, 1.5f, 10f);
    public static final Block MOSSY_STONE = makeCube("mossy_stone", Material.ROCK, SoundType.STONE, 1.5f, 10f);

    public static final Block BLACK_ICE = makeIce("black_ice");
    public static final Block PACKED_BLACK_ICE = makeIce("packed_black_ice");
    public static final Block BOREALIS_ICE = makeIce("borealis_ice");
    public static final Block PACKED_BOREALIS_ICE = makeIce("packed_borealis_ice");

    // ==========================================
    // ЛЕПЕСТКИ, ГРИБЫ И ЯБЛОКИ (Кубы-заглушки)
    // ==========================================
    public static final Block ALLIUM_PETAL_BLOCK = makeCube("allium_petal_block", Material.LEAVES, SoundType.PLANT, 0.2f, 0.2f);
    public static final Block PINK_ALLIUM_PETAL_BLOCK = makeCube("pink_allium_petal_block", Material.LEAVES, SoundType.PLANT, 0.2f, 0.2f);
    public static final Block WHITE_ALLIUM_PETAL_BLOCK = makeCube("white_allium_petal_block", Material.LEAVES, SoundType.PLANT, 0.2f, 0.2f);
    public static final Block ROSE_PETAL_BLOCK = makeCube("rose_petal_block", Material.LEAVES, SoundType.PLANT, 0.2f, 0.2f);
    public static final Block GREEN_MUSHROOM_BLOCK = makeCube("green_mushroom_block", Material.WOOD, SoundType.WOOD, 0.2f, 0.2f);
    public static final Block WEEPING_MILKCAP_MUSHROOM_BLOCK = makeCube("weeping_milkcap_mushroom_block", Material.WOOD, SoundType.WOOD, 0.2f, 0.2f);
    public static final Block WOOD_BLEWIT_MUSHROOM_BLOCK = makeCube("wood_blewit_mushroom_block", Material.WOOD, SoundType.WOOD, 0.2f, 0.2f);
    public static final Block WHITE_MUSHROOM_STEM = makeCube("white_mushroom_stem", Material.WOOD, SoundType.WOOD, 0.2f, 0.2f);
    public static final Block BROWN_MUSHROOM_STEM = makeCube("brown_mushroom_stem", Material.WOOD, SoundType.WOOD, 0.2f, 0.2f);

    // ==========================================
    // КУВШИНКИ (Регистрируются через отдельный класс)
    // ==========================================
    public static final Block TINY_LILY_PADS = new BlockOtbwgLilyPad("tiny_lily_pads");
    public static final Block FLOWERING_TINY_LILY_PADS = new BlockOtbwgLilyPad("flowering_tiny_lily_pads");
    public static final Block WATER_SILK = new BlockOtbwgLilyPad("water_silk");

    // ==========================================
    // ВСЕ 85 ЦВЕТОВ И КУСТОВ (Регистрируются автоматически)
    // ==========================================
    private static Block makePlantableDirt(String name, Material mat, SoundType sound, float hardness, float resist) {
        Block block = new Block(mat) {
            { this.setSoundType(sound); }

            @Override
            public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos,
                                           net.minecraft.util.EnumFacing direction,
                                           net.minecraftforge.common.IPlantable plantable) {
                return true; // Разрешаем посадку
            }
        }.setRegistryName("mwccf", name).setTranslationKey("otbwg." + name)
                .setHardness(hardness).setResistance(resist).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

        BLOCKS.add(block);
        ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        return block;
    }
    // Добавьте этот метод или модифицируйте текущий makeCube
    private static Block makeLoamyDirt(String name) {
        Block block = new Block(Material.GROUND) {
            { this.setSoundType(SoundType.GROUND); }

            @Override
            public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos,
                                           net.minecraft.util.EnumFacing direction,
                                           net.minecraftforge.common.IPlantable plantable) {
                // Разрешаем сажать растения на этот блок
                return true;
            }
        }.setRegistryName("mwccf", name).setTranslationKey("otbwg." + name)
                .setHardness(0.5f).setResistance(0.5f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

        BLOCKS.add(block);
        ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        return block;
    }
    // ==========================================
    // СОБЫТИЯ РЕГИСТРАЦИИ (ОЧЕНЬ КОРОТКИЕ)
    // ==========================================
    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        // 1. Регистрируем все, что было создано как public static final Block
        for (Block block : BLOCKS) {
            event.getRegistry().register(block);
        }

        // 2. РЕГИСТРИРУЕМ ЦВЕТЫ ЗДЕСЬ, А НЕ В СТАТИКЕ
        String[] flowers = {
                "blueberry_bush", "flowering_jacaranda_bush", "jacaranda_bush",
                "flowering_indigo_jacaranda_bush", "indigo_jacaranda_bush", "hydrangea_bush", "hydrangea_hedge",
                "shrub", "firecracker_flower_bush", "oddion_crop", "green_mushroom", "weeping_milkcap",
                "wood_blewit", "tall_allium", "allium_flower_bush", "pink_allium", "tall_pink_allium",
                "pink_allium_flower_bush", "white_allium", "tall_white_allium", "white_allium_flower_bush",
                "cyan_pitcher_plant", "magenta_pitcher_plant", "osiria_rose", "black_rose", "cyan_rose",
                "blue_rose_bush", "cyan_tulip", "green_tulip", "magenta_tulip", "purple_tulip", "yellow_tulip",
                "amaranth", "cyan_amaranth", "magenta_amaranth", "orange_amaranth", "purple_amaranth", "blue_sage",
                "purple_sage", "white_sage", "daffodil", "pink_daffodil", "yellow_daffodil", "pink_anemone",
                "white_anemone", "alpine_bellflower", "lazarus_bellflower", "peach_leather_flower",
                "violet_leather_flower", "angelica", "begonia", "bistort", "california_poppy", "crocus",
                "delphinium", "fairy_slipper", "foxglove", "guzmania", "incan_lily", "iris", "japanese_orchid",
                "kovan_flower", "lollipop_flower", "orange_daisy", "protea_flower", "richea", "silver_vase_flower",
                "horseweed", "winter_succulent", "snowdrops", "winter_cyclamen", "winter_rose", "winter_scilla",
                "cattail_sprout", "cattail", "white_puffball", "tall_prairie_grass", "prairie_grass",
                "tall_beach_grass", "beach_grass", "leaf_pile", "flower_patch", "white_sakura_petals",
                "yellow_sakura_petals", "poison_ivy", "skyris_vine", "witch_hazel_branch", "witch_hazel_blossom",
                "shelf_fungi", "mini_cactus", "prickly_pear_cactus", "golden_spined_cactus",
                "aloe_vera", "blooming_aloe_vera",
                "apple_fruit", "baobab_fruit", "green_apple_fruit", "yucca_fruit"
        };

        for (String f : flowers) {
            Block b = new BlockOtbwgFlower(f);
            event.getRegistry().register(b);
        }
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        for (Item item : ITEMS) { event.getRegistry().register(item); }
    }

    @SubscribeEvent
    public static void onModelRegister(net.minecraftforge.client.event.ModelRegistryEvent event) {
        for (Item item : ITEMS) {
            net.minecraft.client.renderer.block.model.ModelResourceLocation location = new net.minecraft.client.renderer.block.model.ModelResourceLocation(item.getRegistryName(), "inventory");
            net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(item, 0, location);
        }
    }

    public static void registerOres() {
        for (Block block : BLOCKS) {
            if (block instanceof BlockOtbwgLog) {
                net.minecraftforge.oredict.OreDictionary.registerOre("logWood", block);
            } else if (block instanceof BlockOtbwgLeaves) {
                net.minecraftforge.oredict.OreDictionary.registerOre("treeLeaves", block);
            } else if (block instanceof BlockOtbwgPlanks) {
                net.minecraftforge.oredict.OreDictionary.registerOre("plankWood", block);
            } else if (block instanceof BlockOtbwgStairs) {
                net.minecraftforge.oredict.OreDictionary.registerOre("stairWood", block);
            } else if (block instanceof BlockOtbwgSlab) {
                net.minecraftforge.oredict.OreDictionary.registerOre("slabWood", block);
            } else if (block instanceof BlockOtbwgFence) {
                net.minecraftforge.oredict.OreDictionary.registerOre("fenceWood", block);
            } else if (block instanceof BlockOtbwgFenceGate) {
                net.minecraftforge.oredict.OreDictionary.registerOre("fenceGateWood", block);
            }
        }
    }

    // ==========================================
    // ВНУТРЕННИЕ КЛАССЫ
    // ==========================================
    public static class BlockOtbwgLeaves extends BlockLeaves {
        int[] surroundings;

        public BlockOtbwgLeaves(String name) {
            this.setRegistryName("mwccf", name);
            ((net.minecraft.block.Block) this).setTranslationKey("otbwg." + name);
            this.setCreativeTab(CreativeTabs.DECORATIONS);
            this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, true));
            BLOCKS.add(this);
            ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        }

        @Override
        public void updateTick(net.minecraft.world.World worldIn, BlockPos pos, IBlockState state, java.util.Random rand) {
            if (!worldIn.isRemote) {
                if (((Boolean)state.getValue(CHECK_DECAY)).booleanValue() && ((Boolean)state.getValue(DECAYABLE)).booleanValue()) {
                    int i = 8;
                    int j = 9;
                    int k = pos.getX();
                    int l = pos.getY();
                    int i1 = pos.getZ();

                    if (this.surroundings == null) {
                        this.surroundings = new int[32768];
                    }

                    if (worldIn.isAreaLoaded(new BlockPos(k - j, l - j, i1 - j), new BlockPos(k + j, l + j, i1 + j))) {
                        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                        for (int i2 = -i; i2 <= i; ++i2) {
                            for (int j2 = -i; j2 <= i; ++j2) {
                                for (int k2 = -i; k2 <= i; ++k2) {
                                    IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos.setPos(k + i2, l + j2, i1 + k2));
                                    net.minecraft.block.Block block = iblockstate.getBlock();

                                    if (!block.canSustainLeaves(iblockstate, worldIn, blockpos$mutableblockpos.setPos(k + i2, l + j2, i1 + k2))) {
                                        if (block.isLeaves(iblockstate, worldIn, blockpos$mutableblockpos.setPos(k + i2, l + j2, i1 + k2))) {
                                            this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = -2;
                                        } else {
                                            this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = -1;
                                        }
                                    } else {
                                        this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = 0;
                                    }
                                }
                            }
                        }

                        for (int l2 = 1; l2 <= i; ++l2) {
                            for (int i3 = -i; i3 <= i; ++i3) {
                                for (int j3 = -i; j3 <= i; ++j3) {
                                    for (int k3 = -i; k3 <= i; ++k3) {
                                        if (this.surroundings[(i3 + 16) * 1024 + (j3 + 16) * 32 + k3 + 16] == l2 - 1) {
                                            if (this.surroundings[(i3 + 16 - 1) * 1024 + (j3 + 16) * 32 + k3 + 16] == -2) {
                                                this.surroundings[(i3 + 16 - 1) * 1024 + (j3 + 16) * 32 + k3 + 16] = l2;
                                            }
                                            if (this.surroundings[(i3 + 16 + 1) * 1024 + (j3 + 16) * 32 + k3 + 16] == -2) {
                                                this.surroundings[(i3 + 16 + 1) * 1024 + (j3 + 16) * 32 + k3 + 16] = l2;
                                            }
                                            if (this.surroundings[(i3 + 16) * 1024 + (j3 + 16 - 1) * 32 + k3 + 16] == -2) {
                                                this.surroundings[(i3 + 16) * 1024 + (j3 + 16 - 1) * 32 + k3 + 16] = l2;
                                            }
                                            if (this.surroundings[(i3 + 16) * 1024 + (j3 + 16 + 1) * 32 + k3 + 16] == -2) {
                                                this.surroundings[(i3 + 16) * 1024 + (j3 + 16 + 1) * 32 + k3 + 16] = l2;
                                            }
                                            if (this.surroundings[(i3 + 16) * 1024 + (j3 + 16) * 32 + (k3 + 16 - 1)] == -2) {
                                                this.surroundings[(i3 + 16) * 1024 + (j3 + 16) * 32 + (k3 + 16 - 1)] = l2;
                                            }
                                            if (this.surroundings[(i3 + 16) * 1024 + (j3 + 16) * 32 + k3 + 16 + 1] == -2) {
                                                this.surroundings[(i3 + 16) * 1024 + (j3 + 16) * 32 + k3 + 16 + 1] = l2;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    int l3 = this.surroundings[16912];

                    if (l3 >= 0) {
                        worldIn.setBlockState(pos, state.withProperty(CHECK_DECAY, Boolean.valueOf(false)), 4);
                    } else {
                        this.dropBlockAsItem(worldIn, pos, state, 0);
                        worldIn.setBlockToAir(pos);
                    }
                }
            }
        }

        @Override public boolean isOpaqueCube(IBlockState state) { return false; }
        @Override public boolean isFullCube(IBlockState state) { return false; }
        @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
        @Override public net.minecraft.util.BlockRenderLayer getRenderLayer() { return net.minecraft.util.BlockRenderLayer.CUTOUT_MIPPED; }
        @Override public EnumType getWoodType(int meta) { return EnumType.SPRUCE; }
        @Override public NonNullList<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) { return NonNullList.withSize(1, new ItemStack(this)); }
        @Override protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE); }
        @Override public IBlockState getStateFromMeta(int meta) { return this.getDefaultState().withProperty(DECAYABLE, (meta & 4) == 0).withProperty(CHECK_DECAY, (meta & 8) > 0); }
        @Override public int getMetaFromState(IBlockState state) { int i = 0; if (!state.getValue(DECAYABLE)) i |= 4; if (state.getValue(CHECK_DECAY)) i |= 8; return i; }
        @Override public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) { items.add(new ItemStack(this)); }
        @Override protected ItemStack getSilkTouchDrop(IBlockState state) { return new ItemStack(this); }
    }
    public static class BlockOtbwgLog extends net.minecraft.block.BlockLog {
        public BlockOtbwgLog(String name) {
            this.setRegistryName("mwccf", name);
            this.setTranslationKey("otbwg." + name);
            this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
            this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, net.minecraft.block.BlockLog.EnumAxis.Y));
            BLOCKS.add(this);
            ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        }

        @Override
        public IBlockState getStateFromMeta(int meta) {
            IBlockState state = this.getDefaultState();
            switch (meta & 12) {
                case 0: state = state.withProperty(LOG_AXIS, net.minecraft.block.BlockLog.EnumAxis.Y); break;
                case 4: state = state.withProperty(LOG_AXIS, net.minecraft.block.BlockLog.EnumAxis.X); break;
                case 8: state = state.withProperty(LOG_AXIS, net.minecraft.block.BlockLog.EnumAxis.Z); break;
                default: state = state.withProperty(LOG_AXIS, net.minecraft.block.BlockLog.EnumAxis.NONE); break;
            }
            return state;
        }

        @Override
        @SuppressWarnings("incomplete-switch")
        public int getMetaFromState(IBlockState state) {
            int meta = 0;
            switch (state.getValue(LOG_AXIS)) {
                case X: meta |= 4; break;
                case Z: meta |= 8; break;
                case NONE: meta |= 12; break;
            }
            return meta;
        }

        @Override
        protected BlockStateContainer createBlockState() {
            return new BlockStateContainer(this, LOG_AXIS);
        }
    }
    public static class BlockCloverPatch extends BlockBush {
        protected static final AxisAlignedBB CLOVER_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
        public BlockCloverPatch(String name) {
            super(Material.PLANTS);
            this.setRegistryName("mwccf", name);
            ((net.minecraft.block.Block) this).setTranslationKey("otbwg." + name);
            this.setCreativeTab(CreativeTabs.DECORATIONS);
            this.setSoundType(SoundType.PLANT);
            BLOCKS.add(this);
            ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        }
        @Override public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) { return CLOVER_AABB; }
    }

    public static class BlockOtbwgFlower extends BlockBush {
        public BlockOtbwgFlower(String name) {
            super(Material.PLANTS);
            this.setRegistryName("mwccf", name);
            ((net.minecraft.block.Block) this).setTranslationKey("otbwg." + name);
            this.setCreativeTab(CreativeTabs.DECORATIONS);
            this.setSoundType(SoundType.PLANT);
            BLOCKS.add(this);
            ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        }

        @Override
        protected boolean canSustainBush(net.minecraft.block.state.IBlockState state) {
            net.minecraft.block.Block block = state.getBlock();
            return super.canSustainBush(state) || block == net.minecraft.init.Blocks.SAND || block == net.minecraft.init.Blocks.HARDENED_CLAY || block == net.minecraft.init.Blocks.STAINED_HARDENED_CLAY || block == CRACKED_RED_SAND || block == CRACKED_SAND || block == RED_QUICKSAND;
        }
    }

    public static class BlockSolidCactus extends net.minecraft.block.Block {
        public BlockSolidCactus(String name) {
            super(Material.CACTUS);
            this.setRegistryName("mwccf", name);
            this.setTranslationKey("otbwg." + name);
            this.setCreativeTab(CreativeTabs.DECORATIONS);
            this.setSoundType(SoundType.CLOTH);
            this.setHardness(0.4f);
            BLOCKS.add(this);
            ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        }

        @Override
        public boolean isOpaqueCube(net.minecraft.block.state.IBlockState state) {
            return false;
        }

        @Override
        public boolean isFullCube(net.minecraft.block.state.IBlockState state) {
            return false;
        }

        @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
        @Override
        public net.minecraft.util.BlockRenderLayer getRenderLayer() {
            return net.minecraft.util.BlockRenderLayer.CUTOUT;
        }
    }

    public static class BlockOtbwgLilyPad extends net.minecraft.block.BlockLilyPad {
        public BlockOtbwgLilyPad(String name) {
            this.setRegistryName("mwccf", name);
            ((net.minecraft.block.Block) this).setTranslationKey("otbwg." + name);
            this.setCreativeTab(CreativeTabs.DECORATIONS);
            BLOCKS.add(this);
            ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        }
    }
}
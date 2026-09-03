package efw.world.biome;

import efw.blocks.OtbwgBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.block.BlockDirt;
import java.util.Random;

/**
 * АВТОГЕНЕРИРОВАНО из cika_woods.json
 * Это ЗАГОТОВКА, не финальный класс. Обязательно проверь и доработай:
 *  - блоки topBlock/fillerBlock (поставлены по умолчанию, в json для них нет прямого аналога)
 *  - все TODO ниже (NBT-деревья, кастомные фичи, структуры, недостающие мобы)
 *  - climate (temperature/rainfall) перенесён напрямую из json - шкалы 1.20.1 и 1.12.2 близки,
 *    обычно не требует пересчёта, но можно подправить под художественную задумку биома
 *  - fog_color и water_fog_color не имеют прямого хука в Biome 1.12.2 (см. TODO внизу класса)
 */
public class BiomeCikaWoods extends Biome {

    public BiomeCikaWoods() {
        super(new BiomeProperties("Cika Woods")
                .setBaseHeight(0.15F)
                .setHeightVariation(0.15F)
                .setTemperature(0.35F) // напрямую из json (temperature) - шкала 1.20.1 близка к шкале 1.12.2, можно не пересчитывать
                .setRainfall(0.5F)        // напрямую из json (downfall)

        );

        this.setRegistryName("cika_woods");
        
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();

        this.decorator.treesPerChunk = 10;
        this.decorator.flowersPerChunk = 2;
        this.decorator.grassPerChunk = 5;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 2;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 1;

        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();

        // --- creature (passive) ---
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntitySheep.class, 12, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPig.class, 10, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityChicken.class, 10, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityCow.class, 8, 4, 4));
        // --- monster ---
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySpider.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombie.class, 95, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombieVillager.class, 5, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySkeleton.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityCreeper.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySlime.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityEnderman.class, 10, 1, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityWitch.class, 5, 1, 1));
        // --- ambient (cave) ---
        this.spawnableCaveCreatureList.add(new Biome.SpawnListEntry(EntityBat.class, 10, 8, 8));
    }

    // --- Рельеф: профиль из mappings/terrain_profiles.json ---
    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noise)
    {
        // Профиль рельефа из mappings/terrain_profiles.json ("cika_woods")
        // jitter делает границы участков рваными/естественными, как в ручном Crimson Tundra
        double jitter = rand.nextDouble() * 0.2D;
        double combinedNoise = noise + jitter;

        {
            if (combinedNoise > 1.75D) {
                this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
                this.fillerBlock = Blocks.DIRT.getDefaultState();
            } else if (combinedNoise > -0.95D) {
                this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
                this.fillerBlock = Blocks.DIRT.getDefaultState();
            } else {
                this.topBlock = Blocks.GRASS.getDefaultState();
                this.fillerBlock = Blocks.DIRT.getDefaultState();
            }
        }

        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noise);
    }

    @Override
    public net.minecraft.world.gen.feature.WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        if (rand.nextInt(3) == 0) {
            return new net.minecraft.world.gen.feature.WorldGenCanopyTree(false);
        }
        return new efw.world.gen.WorldGenCikaTree();
    }

    // --- Фичи из json, требующие ручной реализации (NBT-деревья, кастомные растения, структуры) ---
        // TODO (minecraft:lake_lava_underground): неизвестная vanilla-фича 1.20.x, аналога может не быть, нужна ручная проверка
        // TODO (minecraft:amethyst_geode): неизвестная vanilla-фича 1.20.x, аналога может не быть, нужна ручная проверка
        // TODO (minecraft:monster_room): неизвестная vanilla-фича 1.20.x, аналога может не быть, нужна ручная проверка
        // TODO (minecraft:monster_room_deep): неизвестная vanilla-фича 1.20.x, аналога может не быть, нужна ручная проверка
        // TODO (minecraft:spring_water): неизвестная vanilla-фича 1.20.x, аналога может не быть, нужна ручная проверка
        // TODO (minecraft:spring_lava): неизвестная vanilla-фича 1.20.x, аналога может не быть, нужна ручная проверка
        // TODO (minecraft:glow_lichen): неизвестная vanilla-фича 1.20.x, аналога может не быть, нужна ручная проверка
        // TODO (minecraft:patch_grass_badlands): неизвестная vanilla-фича 1.20.x, аналога может не быть, нужна ручная проверка
        // TODO (minecraft:patch_grass_forest): неизвестная vanilla-фича 1.20.x, аналога может не быть, нужна ручная проверка
        // TODO (minecraft:patch_pumpkin): неизвестная vanilla-фича 1.20.x, аналога может не быть, нужна ручная проверка
        // TODO (minecraft:patch_berry_rare): неизвестная vanilla-фича 1.20.x, аналога может не быть, нужна ручная проверка
        // TODO (biomeswevegone:cika_trees): кастомная фича мода (biomeswevegone) - блок/фича не существует в 1.12.2, нужен свой порт + WorldGenerator
        // TODO (biomeswevegone:large_pumpkin): кастомная фича мода (biomeswevegone) - блок/фича не существует в 1.12.2, нужен свой порт + WorldGenerator
        // TODO (biomeswevegone:medium_pumpkin): кастомная фича мода (biomeswevegone) - блок/фича не существует в 1.12.2, нужен свой порт + WorldGenerator
        // TODO (biomeswevegone:blue_berry_bush): кастомная фича мода (biomeswevegone) - блок/фича не существует в 1.12.2, нужен свой порт + WorldGenerator
        // TODO (biomeswevegone:blue_berry_bush_lush): кастомная фича мода (biomeswevegone) - блок/фича не существует в 1.12.2, нужен свой порт + WorldGenerator
        // TODO (biomeswevegone:anemones): кастомная фича мода (biomeswevegone) - блок/фича не существует в 1.12.2, нужен свой порт + WorldGenerator
        // TODO (biomeswevegone:crocus): кастомная фича мода (biomeswevegone) - блок/фича не существует в 1.12.2, нужен свой порт + WorldGenerator
        // TODO (biomeswevegone:iris): кастомная фича мода (biomeswevegone) - блок/фича не существует в 1.12.2, нужен свой порт + WorldGenerator
        // TODO (biomeswevegone:blue_rose_bush): кастомная фича мода (biomeswevegone) - блок/фича не существует в 1.12.2, нужен свой порт + WorldGenerator
        // TODO (biomeswevegone:winter_succulent): кастомная фича мода (biomeswevegone) - блок/фича не существует в 1.12.2, нужен свой порт + WorldGenerator
        // TODO (biomeswevegone:mushrooms): кастомная фича мода (biomeswevegone) - блок/фича не существует в 1.12.2, нужен свой порт + WorldGenerator
        // TODO (minecraft:freeze_top_layer): неизвестная vanilla-фича 1.20.x, аналога может не быть, нужна ручная проверка

    // --- Фичи из json, которые в 1.12.2 обычно покрываются стандартной генерацией ---
        // (minecraft:ore_dirt) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_gravel) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_granite_upper) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_granite_lower) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_diorite_upper) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_diorite_lower) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_andesite_upper) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_andesite_lower) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_tuff) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_coal_upper) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_coal_lower) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_iron_upper) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_iron_middle) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_iron_small) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_gold) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_gold_lower) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_redstone) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_redstone_lower) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_diamond) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_diamond_large) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_diamond_buried) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_lapis) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_lapis_buried) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:ore_copper) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:underwater_magma) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:disk_sand) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:disk_clay) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне
        // (minecraft:disk_gravel) пропущено: руда/диск - обычно печётся в genTerrainBlocks/стандартной ore-генерации, как у тебя в шаблоне

    private void placePlant(World worldIn, Random rand, BlockPos pos, net.minecraft.block.Block plantBlock, int tries) {
        if (plantBlock == null) return;
        for (int i = 0; i < tries; ++i) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            int y = ((net.minecraft.util.math.Vec3i) worldIn.getHeight(pos.add(x, 0, z))).getY();
            BlockPos targetPos = pos.add(x, y, z);
            net.minecraft.block.Block blockDown = worldIn.getBlockState(targetPos.down()).getBlock();
            if (worldIn.isAirBlock(targetPos) && (blockDown == net.minecraft.init.Blocks.DIRT || blockDown == net.minecraft.init.Blocks.GRASS)) {
                worldIn.setBlockState(targetPos, plantBlock.getDefaultState(), 2);
            }
        }
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        
        // Sweet Berry Bush (using blueberry as BYG equivalent)
        placePlant(worldIn, rand, pos, net.minecraft.block.Block.getBlockFromName("mwccf:blueberry_bush"), 3);

        // Ferns
        for (int i = 0; i < 5; ++i) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            BlockPos targetPos = worldIn.getHeight(pos.add(x, 0, z));
            new net.minecraft.world.gen.feature.WorldGenTallGrass(net.minecraft.block.BlockTallGrass.EnumType.FERN).generate(worldIn, rand, targetPos);
        }

        // Pumpkins
        if (rand.nextInt(16) == 0) {
            int x = rand.nextInt(16) + 8;
            int z = rand.nextInt(16) + 8;
            BlockPos targetPos = worldIn.getHeight(pos.add(x, 0, z));
            new net.minecraft.world.gen.feature.WorldGenPumpkin().generate(worldIn, rand, targetPos);
        }

        // Flowers
        net.minecraft.block.Block[] flowers = {
            net.minecraft.block.Block.getBlockFromName("mwccf:white_anemone"),
            net.minecraft.block.Block.getBlockFromName("mwccf:pink_anemone"),
            net.minecraft.block.Block.getBlockFromName("mwccf:crocus"),
            net.minecraft.block.Block.getBlockFromName("mwccf:iris")
        };
        for (net.minecraft.block.Block flower : flowers) {
            if (rand.nextInt(3) == 0) {
                placePlant(worldIn, rand, pos, flower, 1);
            }
        }
        
        // Winter Succulent
        if (rand.nextInt(4) == 0) {
            placePlant(worldIn, rand, pos, net.minecraft.block.Block.getBlockFromName("mwccf:winter_succulent"), 1);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getSkyColorByTemp(float currentTemperature) {
        return 0x7CA3FF; // из json effects.sky_color
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return 0xCCAF5B; // из json effects.grass_color
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return 0xCCAF5B; // из json effects.foliage_color
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getWaterColorMultiplier() {
        return 0x3F76E4; // из json effects.water_color (это МНОЖИТЕЛЬ на базовую текстуру воды - после переноса сверь визуально)
    }

    // TODO: effects.fog_color = 0xC0D8FF - в стандартном Biome 1.12.2 нет per-biome хука на fog,
    // переноси через рендер-эвент (например EntityViewRenderEvent.FogColors), если важно.
    // TODO: effects.water_fog_color = 0x050533 - аналогично, нет прямого хука.
    // TODO: effects.ambient_sound = "", effects.particle = {}, effects.mood_sound = {"block_search_extent": 8, "offset": 2.0, "sound": "minecraft:ambient.cave", "tick_delay": 6000}
    // переноси через свой клиентский particle/ambient-sound регистратор, как ты уже делал для пепла в Crimson Tundra.
}

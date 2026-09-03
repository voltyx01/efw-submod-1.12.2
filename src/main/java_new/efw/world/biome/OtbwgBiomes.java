package efw.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "mwccf")
public class OtbwgBiomes {

    public static final List<Biome> BIOMES = new ArrayList<>();

    public static final Biome CIKA_WOODS = new BiomeCikaWoods();
    public static final Biome CRIMSON_TUNDRA = new BiomeCrimsonTundra();
    public static final Biome CARDINAL_TUNDRA = new BiomeCardinalTundra();
    public static final Biome SIERRA_BADLANDS = new BiomeSierraBadlands();
    public static final Biome ATACAMA_OUTBACK = new BiomeAtacamaOutback();
    public static final Biome FIRECRACKER_SHRUBLAND = new BiomeFirecrackerShrubland();
    public static final Biome TWILIGHT_MEADOW = new BiomeTwilightMeadow();
    public static final Biome ZELKOVA_FOREST = new BiomeZelkovaForest();
    public static final Biome DACITE_RIDGES = new BiomeDaciteRidges();
    public static final Biome MOJAVE_DESERT = new BiomeMojaveDesert();
    public static final Biome CANADIAN_SHIELD = new BiomeCanadianShield();

    static {
        BIOMES.add(CRIMSON_TUNDRA);
        BIOMES.add(CIKA_WOODS);
        BIOMES.add(SIERRA_BADLANDS);
        BIOMES.add(ATACAMA_OUTBACK);
        BIOMES.add(CARDINAL_TUNDRA);
        BIOMES.add(FIRECRACKER_SHRUBLAND);
        BIOMES.add(TWILIGHT_MEADOW);
        BIOMES.add(ZELKOVA_FOREST);
        BIOMES.add(DACITE_RIDGES);
        BIOMES.add(MOJAVE_DESERT);
        BIOMES.add(CANADIAN_SHIELD);
    }

    @SubscribeEvent
    public static void onBiomeRegister(RegistryEvent.Register<Biome> event) {
        for (Biome biome : BIOMES) {
            event.getRegistry().register(biome);
        }

        // Add Biome to Generation
        BiomeManager.addBiome(BiomeManager.BiomeType.ICY, new BiomeManager.BiomeEntry(CRIMSON_TUNDRA, 10));
        BiomeManager.addBiome(BiomeManager.BiomeType.ICY, new BiomeManager.BiomeEntry(CARDINAL_TUNDRA, 10));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(CIKA_WOODS, 10));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(FIRECRACKER_SHRUBLAND, 10));
        BiomeManager.addBiome(BiomeManager.BiomeType.DESERT, new BiomeManager.BiomeEntry(SIERRA_BADLANDS, 10));
        BiomeManager.addBiome(BiomeManager.BiomeType.DESERT, new BiomeManager.BiomeEntry(ATACAMA_OUTBACK, 10));
        BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(TWILIGHT_MEADOW, 10));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(ZELKOVA_FOREST, 10));
        BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(DACITE_RIDGES, 10));
        BiomeManager.addBiome(BiomeManager.BiomeType.DESERT, new BiomeManager.BiomeEntry(MOJAVE_DESERT, 10));
        BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(CANADIAN_SHIELD, 10));
        
        // Tag it in BiomeDictionary
        BiomeDictionary.addTypes(CRIMSON_TUNDRA, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.COLD);
        BiomeDictionary.addTypes(CARDINAL_TUNDRA, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.COLD);
        BiomeDictionary.addTypes(CIKA_WOODS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MAGICAL);
        BiomeDictionary.addTypes(FIRECRACKER_SHRUBLAND, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SPARSE);
        BiomeDictionary.addTypes(SIERRA_BADLANDS, BiomeDictionary.Type.MESA, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SPARSE);
        BiomeDictionary.addTypes(ATACAMA_OUTBACK, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SPARSE);
        BiomeDictionary.addTypes(TWILIGHT_MEADOW, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.MAGICAL);
        BiomeDictionary.addTypes(ZELKOVA_FOREST, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE);
        BiomeDictionary.addTypes(DACITE_RIDGES, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.MOUNTAIN);
        BiomeDictionary.addTypes(MOJAVE_DESERT, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SPARSE);
        BiomeDictionary.addTypes(CANADIAN_SHIELD, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.COLD);
    }

    @SubscribeEvent
    public static void onDecorate(net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate event) {
        if (event.getWorld().getBiome(event.getPos()) == CRIMSON_TUNDRA) {
            if (event.getType() == net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_WATER ||
                event.getType() == net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_LAVA) {
                event.setResult(net.minecraftforge.fml.common.eventhandler.Event.Result.DENY);
            }
        }
    }
}

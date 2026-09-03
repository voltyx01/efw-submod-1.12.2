package efw;

import net.minecraft.world.biome.Biome;
import net.minecraft.init.Biomes;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaSavannaPlateauM;

public class TestBiome {
    public static void main(String[] args) {
        RealisticBiomeVanillaSavannaPlateauM rBiome = new RealisticBiomeVanillaSavannaPlateauM();
        System.out.println("Biome: " + Biome.getIdForBiome(rBiome.baseBiome()));
    }
}

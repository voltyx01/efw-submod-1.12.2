package efw.world.biome.rtg;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.fml.common.Loader;
import rtg.api.RTGAPI;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.api.world.terrain.heighteffect.GroundEffect;
import efw.world.biome.OtbwgBiomes;

import java.util.Random;

public class RTGIntegration {

    public static void init() {
        if (Loader.isModLoaded("rtgc")) {
            registerRTGBiomes();
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new LostCitiesRTGEventHandler());
        }
    }

    private static void registerRTGBiomes() {
        System.out.println("[MWCCF] Registering RTG biomes for MWCCF...");
        System.out.println("[MWCCF] Canadian Shield ID: " + Biome.getIdForBiome(OtbwgBiomes.CANADIAN_SHIELD));

        // Flat/Plains/Forest biomes
        RTGAPI.addRTGBiomes(new OtbwgRealisticBiome(OtbwgBiomes.CIKA_WOODS, TerrainType.FOREST));
        RTGAPI.addRTGBiomes(new OtbwgRealisticBiome(OtbwgBiomes.CRIMSON_TUNDRA, TerrainType.PLAINS));
        RTGAPI.addRTGBiomes(new OtbwgRealisticBiome(OtbwgBiomes.CARDINAL_TUNDRA, TerrainType.PLAINS));
        RTGAPI.addRTGBiomes(new OtbwgRealisticBiome(OtbwgBiomes.SIERRA_BADLANDS, TerrainType.HILLS));
        RTGAPI.addRTGBiomes(new OtbwgRealisticBiome(OtbwgBiomes.ATACAMA_OUTBACK, TerrainType.PLAINS));
        RTGAPI.addRTGBiomes(new OtbwgRealisticBiome(OtbwgBiomes.FIRECRACKER_SHRUBLAND, TerrainType.PLAINS));
        RTGAPI.addRTGBiomes(new OtbwgRealisticBiome(OtbwgBiomes.TWILIGHT_MEADOW, TerrainType.PLAINS));
        RTGAPI.addRTGBiomes(new OtbwgRealisticBiome(OtbwgBiomes.ZELKOVA_FOREST, TerrainType.FOREST));
        RTGAPI.addRTGBiomes(new OtbwgRealisticBiome(OtbwgBiomes.DACITE_RIDGES, TerrainType.MOUNTAINS));
        RTGAPI.addRTGBiomes(new OtbwgRealisticBiome(OtbwgBiomes.MOJAVE_DESERT, TerrainType.HILLS));
        RTGAPI.addRTGBiomes(new OtbwgRealisticBiome(OtbwgBiomes.CANADIAN_SHIELD, TerrainType.FOREST));
        
        System.out.println("[MWCCF] RTG biomes registered successfully!");
    }
    
    public enum TerrainType {
        PLAINS, FOREST, HILLS, MOUNTAINS
    }

    public static class OtbwgRealisticBiome extends RealisticBiomeBase {

        private final TerrainType type;

        public OtbwgRealisticBiome(Biome baseBiome, TerrainType type) {
            super(baseBiome);
            this.type = type;
        }

        @Override
        public TerrainBase initTerrain() {
            return new TerrainBase() {
                private GroundEffect groundEffect = new GroundEffect(4f);
                @Override
                public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {
                    switch (type) {
                        case PLAINS:
                            return riverized(65f + groundEffect.added(rtgWorld, x, y), river);
                        case FOREST:
                            return terrainForest(x, y, rtgWorld, river, 65f);
                        case HILLS:
                            return terrainRollingHills(x, y, rtgWorld, river, 60f, 68f, 6f, 65f);
                        case MOUNTAINS:
                            return terrainHighland(x, y, rtgWorld, river, 60f, 40f, 60f, 65f);
                        default:
                            return riverized(65f + groundEffect.added(rtgWorld, x, y), river);
                    }
                }
            };
        }

        @Override
        public SurfaceBase initSurface() {
            return new SurfaceBase(getConfig(), baseBiome().topBlock, baseBiome().fillerBlock) {
                @Override
                public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {
                    Random rand = rtgWorld.rand();
                    float c = TerrainBase.calcCliff(x, z, noise, river);
                    boolean cliff = c > 1.4f;

                    net.minecraft.block.state.IBlockState currentTop = topBlock;
                    net.minecraft.block.state.IBlockState currentFiller = fillerBlock;

                    if (baseBiome() == OtbwgBiomes.CRIMSON_TUNDRA) {
                        float noiseVal = rtgWorld.simplexInstance(0).noise2f(x * 0.25f, z * 0.25f);
                        float jitter = rand.nextFloat() * 0.2f;
                        float combinedNoise = noiseVal + jitter;

                        if (combinedNoise > 0.8f) {
                            currentTop = efw.blocks.OtbwgBlocks.PEAT.getDefaultState();
                            currentFiller = efw.blocks.OtbwgBlocks.LOAMY_DIRT.getDefaultState();
                        } else if (combinedNoise > 0.2f) {
                            currentTop = efw.blocks.OtbwgBlocks.ROCKY_STONE.getDefaultState();
                            currentFiller = efw.blocks.OtbwgBlocks.ROCKY_STONE.getDefaultState();
                        } else if (combinedNoise < -0.3f) {
                            currentTop = efw.blocks.OtbwgBlocks.LOAMY_GRASS.getDefaultState();
                            currentFiller = efw.blocks.OtbwgBlocks.LOAMY_DIRT.getDefaultState();
                        } else {
                            currentTop = efw.blocks.OtbwgBlocks.LOAMY_DIRT.getDefaultState();
                            currentFiller = efw.blocks.OtbwgBlocks.LOAMY_DIRT.getDefaultState();
                        }
                    }

                    for (int k = 255; k > -1; k--) {
                        net.minecraft.block.Block b = primer.getBlockState(x, k, z).getBlock();
                        if (b == net.minecraft.init.Blocks.AIR) {
                            depth = -1;
                        }
                        else if (b == net.minecraft.init.Blocks.STONE) {
                            depth++;

                            if (cliff) {
                                if (depth > -1 && depth < 2) {
                                    primer.setBlockState(x, k, z, rand.nextInt(3) == 0 ? hcCobble() : hcStone());
                                } else if (depth < 10) {
                                    primer.setBlockState(x, k, z, hcStone());
                                }
                            }
                            else {
                                if (depth == 0 && k > 61) {
                                    primer.setBlockState(x, k, z, currentTop);
                                }
                                else if (depth < 4) {
                                    primer.setBlockState(x, k, z, currentFiller);
                                }
                            }
                        }
                    }
                }
            };
        }

        @Override
        public void initDecos() {
            // No custom RTG decos needed, let Vanilla/EFW custom trees spawn via biome.decorate()
        }

        @Override
        public boolean allowVanillaTrees() {
            return true;
        }

        @Override
        public void overrideDecorations() {
            // Empty, we allow mwccf custom trees/decorations
        }
    }
}

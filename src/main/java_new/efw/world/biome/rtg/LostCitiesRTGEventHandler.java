package efw.world.biome.rtg;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

public class LostCitiesRTGEventHandler {

    @SubscribeEvent
    public void onReplaceBiomeBlocks(ChunkGeneratorEvent.ReplaceBiomeBlocks event) {
        IChunkGenerator gen = event.getGen();
        if (gen != null && gen.getClass().getName().equals("mcjty.lostcities.dimensions.world.LostCityChunkGenerator")) {
            try {
                Class<?> buildingInfoClass = Class.forName("mcjty.lostcities.dimensions.world.lost.BuildingInfo");
                Class<?> lostCityGenClass = Class.forName("mcjty.lostcities.dimensions.world.LostCityChunkGenerator");
                Method isCityMethod = buildingInfoClass.getMethod("isCity", int.class, int.class, lostCityGenClass);
                boolean isCity = (boolean) isCityMethod.invoke(null, event.getX(), event.getZ(), gen);
                if (isCity) {
                    return;
                }

                Class<?> rtgClass = Class.forName("rtg.world.gen.ChunkGeneratorRTG");
                Field otherGeneratorField = gen.getClass().getField("otherGenerator");
                Object otherGen = otherGeneratorField.get(gen);
                if (otherGen != null && rtgClass.isInstance(otherGen)) {
                    Method replaceMethod = rtgClass.getMethod("replaceBiomeBlocksForLostCities", int.class, int.class, ChunkPrimer.class);
                    replaceMethod.invoke(otherGen, event.getX(), event.getZ(), event.getPrimer());
                    event.setResult(Event.Result.DENY);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void onDecoratePre(DecorateBiomeEvent.Pre event) {
        IChunkProvider provider = event.getWorld().getChunkProvider();
        if (provider instanceof ChunkProviderServer) {
            IChunkGenerator gen = ((ChunkProviderServer) provider).chunkGenerator;
            if (gen != null && gen.getClass().getName().equals("mcjty.lostcities.dimensions.world.LostCityChunkGenerator")) {
                try {
                    int chunkX = event.getPos().getX() >> 4;
                    int chunkZ = event.getPos().getZ() >> 4;
                    
                    Class<?> buildingInfoClass = Class.forName("mcjty.lostcities.dimensions.world.lost.BuildingInfo");
                    Class<?> lostCityGenClass = Class.forName("mcjty.lostcities.dimensions.world.LostCityChunkGenerator");
                    Method isCityMethod = buildingInfoClass.getMethod("isCity", int.class, int.class, lostCityGenClass);
                    boolean isCity = (boolean) isCityMethod.invoke(null, chunkX, chunkZ, gen);
                    if (isCity) {
                        return;
                    }

                    Class<?> rtgClass = Class.forName("rtg.world.gen.ChunkGeneratorRTG");
                    Field otherGeneratorField = gen.getClass().getField("otherGenerator");
                    Object otherGen = otherGeneratorField.get(gen);
                    if (otherGen != null && rtgClass.isInstance(otherGen)) {
                        Method decorateMethod = rtgClass.getMethod("decorateRTGForLostCities", int.class, int.class, Random.class);
                        decorateMethod.invoke(otherGen, chunkX, chunkZ, event.getRand());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


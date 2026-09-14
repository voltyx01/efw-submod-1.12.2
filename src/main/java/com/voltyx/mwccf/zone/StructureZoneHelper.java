package com.voltyx.mwccf.zone;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StructureZoneHelper {

    public static final String NBT_KEY = "mwccf_quest_zones";

    /**
     * Захватывает все зоны, находящиеся внутри кубоида структуры,
     * и сохраняет их с относительными координатами в NBTTagList.
     */
    public static NBTTagList captureZones(World world, BlockPos startPos, BlockPos size) {
        NBTTagList list = new NBTTagList();
        if (world == null || size.getX() < 1 || size.getY() < 1 || size.getZ() < 1) {
            return list;
        }

        QuestZoneData data = QuestZoneData.get(world);
        if (data == null) {
            return list;
        }

        int dim = world.provider.getDimension();
        BlockPos minCorner = new BlockPos(
                Math.min(startPos.getX(), startPos.getX() + size.getX() - 1),
                Math.min(startPos.getY(), startPos.getY() + size.getY() - 1),
                Math.min(startPos.getZ(), startPos.getZ() + size.getZ() - 1)
        );
        BlockPos maxCorner = new BlockPos(
                Math.max(startPos.getX(), startPos.getX() + size.getX() - 1),
                Math.max(startPos.getY(), startPos.getY() + size.getY() - 1),
                Math.max(startPos.getZ(), startPos.getZ() + size.getZ() - 1)
        );

        for (QuestZone zone : data.getZones()) {
            if (zone.getDimension() != dim) continue;

            // Проверяем пересечение зоны со структурой
            boolean intersects = zone.getMaxX() >= minCorner.getX() && zone.getMinX() <= maxCorner.getX()
                    && zone.getMaxY() >= minCorner.getY() && zone.getMinY() <= maxCorner.getY()
                    && zone.getMaxZ() >= minCorner.getZ() && zone.getMinZ() <= maxCorner.getZ();

            if (intersects) {
                // Вычисляем относительные координаты (обрезаем по границам структуры)
                int relMinX = Math.max(0, zone.getMinX() - minCorner.getX());
                int relMinY = Math.max(0, zone.getMinY() - minCorner.getY());
                int relMinZ = Math.max(0, zone.getMinZ() - minCorner.getZ());
                int relMaxX = Math.min(size.getX() - 1, zone.getMaxX() - minCorner.getX());
                int relMaxY = Math.min(size.getY() - 1, zone.getMaxY() - minCorner.getY());
                int relMaxZ = Math.min(size.getZ() - 1, zone.getMaxZ() - minCorner.getZ());

                if (relMinX <= relMaxX && relMinY <= relMaxY && relMinZ <= relMaxZ) {
                    NBTTagCompound zTag = new NBTTagCompound();
                    zTag.setString("Name", zone.getName());
                    zTag.setInteger("RelMinX", relMinX);
                    zTag.setInteger("RelMinY", relMinY);
                    zTag.setInteger("RelMinZ", relMinZ);
                    zTag.setInteger("RelMaxX", relMaxX);
                    zTag.setInteger("RelMaxY", relMaxY);
                    zTag.setInteger("RelMaxZ", relMaxZ);
                    list.appendTag(zTag);
                }
            }
        }
        return list;
    }

    /**
     * Восстанавливает зоны в мире с учетом поворота, зеркалирования и смещения структуры.
     */
    public static void applyZonesToWorld(World world, BlockPos originPos, PlacementSettings placementIn, NBTTagList zonesList) {
        if (world == null || world.isRemote || zonesList == null || zonesList.tagCount() == 0) {
            return;
        }

        QuestZoneData data = QuestZoneData.get(world);
        if (data == null) {
            return;
        }

        int dim = world.provider.getDimension();
        boolean anyAdded = false;

        for (int i = 0; i < zonesList.tagCount(); i++) {
            NBTTagCompound zTag = zonesList.getCompoundTagAt(i);
            String name = zTag.hasKey("Name") ? zTag.getString("Name") : "Комната";
            int relMinX = zTag.getInteger("RelMinX");
            int relMinY = zTag.getInteger("RelMinY");
            int relMinZ = zTag.getInteger("RelMinZ");
            int relMaxX = zTag.getInteger("RelMaxX");
            int relMaxY = zTag.getInteger("RelMaxY");
            int relMaxZ = zTag.getInteger("RelMaxZ");

            // Трансформируем 8 углов кубоида с учетом Mirror и Rotation
            BlockPos c1 = transformPoint(new BlockPos(relMinX, relMinY, relMinZ), placementIn).add(originPos);
            BlockPos c2 = transformPoint(new BlockPos(relMaxX, relMinY, relMinZ), placementIn).add(originPos);
            BlockPos c3 = transformPoint(new BlockPos(relMinX, relMaxY, relMinZ), placementIn).add(originPos);
            BlockPos c4 = transformPoint(new BlockPos(relMaxX, relMaxY, relMinZ), placementIn).add(originPos);
            BlockPos c5 = transformPoint(new BlockPos(relMinX, relMinY, relMaxZ), placementIn).add(originPos);
            BlockPos c6 = transformPoint(new BlockPos(relMaxX, relMinY, relMaxZ), placementIn).add(originPos);
            BlockPos c7 = transformPoint(new BlockPos(relMinX, relMaxY, relMaxZ), placementIn).add(originPos);
            BlockPos c8 = transformPoint(new BlockPos(relMaxX, relMaxY, relMaxZ), placementIn).add(originPos);

            int worldMinX = min(c1.getX(), c2.getX(), c3.getX(), c4.getX(), c5.getX(), c6.getX(), c7.getX(), c8.getX());
            int worldMaxX = max(c1.getX(), c2.getX(), c3.getX(), c4.getX(), c5.getX(), c6.getX(), c7.getX(), c8.getX());
            int worldMinY = min(c1.getY(), c2.getY(), c3.getY(), c4.getY(), c5.getY(), c6.getY(), c7.getY(), c8.getY());
            int worldMaxY = max(c1.getY(), c2.getY(), c3.getY(), c4.getY(), c5.getY(), c6.getY(), c7.getY(), c8.getY());
            int worldMinZ = min(c1.getZ(), c2.getZ(), c3.getZ(), c4.getZ(), c5.getZ(), c6.getZ(), c7.getZ(), c8.getZ());
            int worldMaxZ = max(c1.getZ(), c2.getZ(), c3.getZ(), c4.getZ(), c5.getZ(), c6.getZ(), c7.getZ(), c8.getZ());

            // Создаем новую неломаемую зону
            String newId = UUID.randomUUID().toString().substring(0, 8);
            QuestZone newZone = new QuestZone(newId, name, dim, worldMinX, worldMinY, worldMinZ, worldMaxX, worldMaxY, worldMaxZ);
            data.addZone(newZone);
            anyAdded = true;
        }

        if (anyAdded) {
            QuestZoneEventHandler.syncZonesToAll(world);
        }
    }

    private static BlockPos transformPoint(BlockPos pos, PlacementSettings placementIn) {
        if (placementIn == null) {
            return pos;
        }
        return Template.transformedBlockPos(placementIn, pos);
    }

    private static int min(int... vals) {
        int m = vals[0];
        for (int v : vals) {
            if (v < m) m = v;
        }
        return m;
    }

    private static int max(int... vals) {
        int m = vals[0];
        for (int v : vals) {
            if (v > m) m = v;
        }
        return m;
    }
}
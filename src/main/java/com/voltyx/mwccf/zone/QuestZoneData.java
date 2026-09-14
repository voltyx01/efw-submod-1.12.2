package com.voltyx.mwccf.zone;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class QuestZoneData extends WorldSavedData {

    private static final String DATA_NAME = "mwccf_quest_zones";
    private final Map<String, QuestZone> zones = new LinkedHashMap<>();

    public QuestZoneData() {
        super(DATA_NAME);
    }

    public QuestZoneData(String name) {
        super(name);
    }

    public static QuestZoneData get(World world) {
        if (world instanceof WorldServer) {
            WorldServer ws = ((WorldServer) world).getMinecraftServer().getWorld(0);
            QuestZoneData data = (QuestZoneData) ws.getMapStorage().getOrLoadData(QuestZoneData.class, DATA_NAME);
            if (data == null) {
                data = new QuestZoneData();
                ws.getMapStorage().setData(DATA_NAME, data);
            }
            return data;
        }
        return null;
    }

    public Collection<QuestZone> getZones() {
        return Collections.unmodifiableCollection(zones.values());
    }

    public QuestZone getZone(String id) {
        return zones.get(id);
    }

    public QuestZone findZoneAt(int dim, BlockPos pos) {
        for (QuestZone zone : zones.values()) {
            if (zone.contains(dim, pos)) {
                return zone;
            }
        }
        return null;
    }

    public QuestZone findZoneTouching(int dim, BlockPos pos, int radius) {
        for (QuestZone zone : zones.values()) {
            if (zone.intersectsOrTouches(dim, pos, radius)) {
                return zone;
            }
        }
        return null;
    }

    public boolean isProtected(int dim, BlockPos pos) {
        return findZoneAt(dim, pos) != null;
    }

    public void addZone(QuestZone zone) {
        zones.put(zone.getId(), zone);
        markDirty();
    }

    public boolean removeZone(String id) {
        if (zones.remove(id) != null) {
            markDirty();
            return true;
        }
        return false;
    }

    public boolean removeZoneAt(int dim, BlockPos pos) {
        QuestZone found = findZoneAt(dim, pos);
        if (found != null) {
            zones.remove(found.getId());
            markDirty();
            return true;
        }
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        zones.clear();
        if (nbt.hasKey("Zones", Constants.NBT.TAG_LIST)) {
            NBTTagList list = nbt.getTagList("Zones", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                QuestZone zone = QuestZone.readFromNBT(tag);
                zones.put(zone.getId(), zone);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for (QuestZone zone : zones.values()) {
            list.appendTag(zone.writeToNBT(new NBTTagCompound()));
        }
        compound.setTag("Zones", list);
        return compound;
    }
}

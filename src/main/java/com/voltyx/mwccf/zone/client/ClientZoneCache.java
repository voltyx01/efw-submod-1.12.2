package com.voltyx.mwccf.zone.client;

import com.voltyx.mwccf.zone.QuestZone;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientZoneCache {

    private static List<QuestZone> zones = new ArrayList<>();

    public static synchronized void setZones(List<QuestZone> newZones) {
        zones = newZones != null ? new ArrayList<>(newZones) : new ArrayList<>();
    }

    public static synchronized List<QuestZone> getZones() {
        return Collections.unmodifiableList(zones);
    }
}

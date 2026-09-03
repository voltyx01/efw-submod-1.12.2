/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 */
package com.eruannie_9.extragore;

import com.eruannie_9.extragore.pack.BloodDamageSyncHandler;
import com.eruannie_9.extragore.pack.ExtraGoreNetwork;
import com.eruannie_9.extragore.pack.PacketBloodDamage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        ExtraGoreNetwork.init();
    }

    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register((Object)new BloodDamageSyncHandler());
    }

    public void handleBloodDamagePacket(PacketBloodDamage message) {
    }
}


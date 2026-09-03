/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.config.Config$Type
 *  net.minecraftforge.common.config.ConfigManager
 *  net.minecraftforge.fml.client.event.ConfigChangedEvent$OnConfigChangedEvent
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.SidedProxy
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.relauncher.Side
 */
package com.eruannie_9.extragore;

import com.eruannie_9.extragore.CommonProxy;
import com.eruannie_9.extragore.json.BloodEntityConfig;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ExtraGore {
    public static final String MODID = "extragore";
    public static final String NAME = "Extra Gore";
    public static final String VERSION = "6.0";
    public static final int BLOOD_PARTICLE_ID = 2000;
    
    public static CommonProxy proxy;

    public static void preInit(FMLPreInitializationEvent event) {
        if (proxy != null) proxy.preInit(event);
    }

    public static void init(FMLInitializationEvent event) {
        if (proxy != null) proxy.init(event);
    }

    @Mod.EventBusSubscriber(modid="mwccf", value={Side.CLIENT})
    public static final class ClientEvents {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals("mwccf")) {
                ConfigManager.sync("mwccf", (Config.Type)Config.Type.INSTANCE);
            }
        }
    }
}

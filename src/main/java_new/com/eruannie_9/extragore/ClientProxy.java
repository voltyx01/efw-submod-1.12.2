/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.particle.IParticleFactory
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 */
package com.eruannie_9.extragore;

import com.eruannie_9.extragore.BloodOnHitHandler;
import com.eruannie_9.extragore.CommonProxy;
import com.eruannie_9.extragore.pack.PacketBloodDamage;
import com.eruannie_9.extragore.particle.ClientSprites;
import com.eruannie_9.extragore.particle.ParticleBloodFactory;
import com.eruannie_9.extragore.particle.render.BloodRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy
extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        com.eruannie_9.extragore.json.BloodEntityConfig.init(event);
        MinecraftForge.EVENT_BUS.register((Object)new ClientSprites());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.effectRenderer != null) {
            mc.effectRenderer.registerParticle(2000, (IParticleFactory)new ParticleBloodFactory());
        }
        MinecraftForge.EVENT_BUS.register((Object)BloodOnHitHandler.getInstance());
        MinecraftForge.EVENT_BUS.register((Object)new BloodRenderType());
    }

    @Override
    public void handleBloodDamagePacket(PacketBloodDamage message) {
        BloodOnHitHandler.handleSyncedDamage(message);
    }
}


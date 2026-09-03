/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.NetworkRegistry
 *  net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
 *  net.minecraftforge.fml.relauncher.Side
 */
package com.eruannie_9.extragore.pack;

import com.eruannie_9.extragore.pack.PacketBloodDamage;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class ExtraGoreNetwork {
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel("extragore");
    private static int nextId = 0;

    private ExtraGoreNetwork() {
    }

    public static void init() {
        CHANNEL.registerMessage(PacketBloodDamage.Handler.class, PacketBloodDamage.class, nextId++, Side.CLIENT);
    }
}


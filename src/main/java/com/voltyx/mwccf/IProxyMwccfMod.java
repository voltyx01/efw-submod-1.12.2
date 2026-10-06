package com.voltyx.mwccf;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public interface IProxyMwccfMod {
	void preInit(FMLPreInitializationEvent event);

	void init(FMLInitializationEvent event);

	void postInit(FMLPostInitializationEvent event);

	void serverLoad(FMLServerStartingEvent event);

	default void updateGeneratorSound(com.voltyx.mwccf.furniture.tileentity.TileEntityElectricityGenerator generator) {}

	default void stopGeneratorSound(com.voltyx.mwccf.furniture.tileentity.TileEntityElectricityGenerator generator) {}
}

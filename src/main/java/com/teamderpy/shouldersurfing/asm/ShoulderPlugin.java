package com.teamderpy.shouldersurfing.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.IEarlyMixinLoader; // <--- Импортируем интерфейс

import java.util.Collections;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class ShoulderPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader { // <--- Добавляем интерфейс

	public ShoulderPlugin() {
		System.out.println(">>> [SHOULDER SURFING] PLUGIN ИНИЦИАЛИЗИРОВАН <<<");
	}

	// ===================================================
	// МЕТОД ИЗ IEarlyMixinLoader - ТУТ МЫ ОТДАЕМ МИКСИНЫ
	// ===================================================
	@Override
	public List<String> getMixinConfigs() {
		System.out.println(">>> [SHOULDER SURFING] ПЕРЕДАЕМ МИКСИНЫ В MIXINBOOTER <<<");
		return Collections.singletonList("mixins.shouldersurfing.json");
	}

	// ===================================================
	// ОСТАЛЬНЫЕ МЕТОДЫ COREMOD'А
	// ===================================================
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"com.teamderpy.shouldersurfing.asm.ShoulderTransformer"};
	}

	@Override public String getModContainerClass() { return null; }
	@Override public String getSetupClass() { return null; }
	@Override public void injectData(Map<String, Object> data) {}
	@Override public String getAccessTransformerClass() { return null; }
}
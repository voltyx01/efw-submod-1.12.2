package com.paneedah.mwc.items.guns;

import com.paneedah.mwc.proxies.CommonProxy;
import net.minecraft.item.Item;
import com.paneedah.weaponlib.animation.jim.BBLoader;

public interface GunFactory {
	
	public Item createGun(CommonProxy commonProxy);
}

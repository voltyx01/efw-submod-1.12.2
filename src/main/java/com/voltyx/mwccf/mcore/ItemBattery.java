package com.voltyx.mwccf.mcore;

/**
 * Backward compatibility subclass.
 */
public class ItemBattery extends com.voltyx.mwccf.battery.ItemBattery {
    public ItemBattery(String name) {
        super(name, 48000, false, 0);
    }
}

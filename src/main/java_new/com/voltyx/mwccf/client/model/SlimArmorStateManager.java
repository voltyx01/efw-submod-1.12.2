package com.voltyx.mwccf.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class SlimArmorStateManager {
    public static final ThreadLocal<Entity> CURRENT_ENTITY = new ThreadLocal<>();
    public static final ThreadLocal<ModelBiped> CURRENT_MODEL = new ThreadLocal<>();
}

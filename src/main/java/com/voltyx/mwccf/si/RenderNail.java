package com.voltyx.mwccf.si;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNail extends RenderSnowball<EntityNail> {
    public RenderNail(RenderManager renderManagerIn) {
        super(renderManagerIn, SIItems.NAIL, Minecraft.getMinecraft().getRenderItem());
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.client.event.TextureStitchEvent$Pre
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.eruannie_9.extragore.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class ClientSprites {
    public static final TextureAtlasSprite[] BLOOD = new TextureAtlasSprite[4];
    private static final ResourceLocation[] BLOOD_RLS = new ResourceLocation[]{new ResourceLocation("extragore", "particle/blood_square_0"), new ResourceLocation("extragore", "particle/blood_square_1"), new ResourceLocation("extragore", "particle/blood_square_2"), new ResourceLocation("extragore", "particle/blood_square_3")};

    @SubscribeEvent
    public void onTextureStitchPre(TextureStitchEvent.Pre event) {
        if (event.getMap() != Minecraft.getMinecraft().getTextureMapBlocks()) {
            return;
        }
        for (int i = 0; i < BLOOD_RLS.length; ++i) {
            ClientSprites.BLOOD[i] = event.getMap().registerSprite(BLOOD_RLS[i]);
        }
    }

    public static TextureAtlasSprite getBloodSprite(int variantIndex) {
        int len = BLOOD.length;
        int idx = Math.floorMod(variantIndex, len);
        TextureAtlasSprite sprite = BLOOD[idx];
        if (sprite == null) {
            return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
        }
        return sprite;
    }
}


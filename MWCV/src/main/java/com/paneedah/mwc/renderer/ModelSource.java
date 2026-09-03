package com.paneedah.mwc.renderer;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ModelSource implements IBakedModel {

	private ModelResourceLocation modelResourceLocation;

	public ModelResourceLocation getModelResourceLocation() {
		return modelResourceLocation;
	}

	public void setModelResourceLocation(ModelResourceLocation modelResourceLocation) {
		this.modelResourceLocation = modelResourceLocation;
	}
}

package com.paneedah.mwc.tileentities;

import com.paneedah.mwc.MWC;
import com.paneedah.mwc.proxies.CommonProxy;
import com.paneedah.weaponlib.tile.CustomTileEntityBlock;
import com.paneedah.weaponlib.tile.LootBoxConfiguration;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class TileEntities {

    public static void init(CommonProxy commonProxy) {
    	new TurretBaseFactory().createTileEntity(MWC.modContext);

        new LootBoxConfiguration()
        .withMaterial(Material.WOOD)
        .withName("body_bag")
        .withModelClassName("com.paneedah.mwc.models.BodyBag")
        .withTextureName("textures/models/bodybag.png")
        .withCreativeTab(MWC.PROPS_TAB)
        .withPositioning(tileEntity -> {
            GL11.glScalef(0.9f, 0.9f, 0.9f);
            GL11.glTranslatef(0.5f, -0.9f, 0.55f);
            GL11.glRotatef(-90F, 0f, 1f, 0f);
        })
        .build(MWC.modContext);
        
        new LootBoxConfiguration()
        .withMaterial(Material.WOOD)
        .withName("hanging_body")
        .withModelClassName("com.paneedah.mwc.models.HangingBody")
        .withTextureName("textures/models/hangingbody.png")
        .withCreativeTab(MWC.PROPS_TAB)
        .withBoundingBox(
        		blockState -> {
        			AxisAlignedBB boundingBox = null;
        			EnumFacing facing = blockState.getValue(CustomTileEntityBlock.FACING);
        			switch(facing) {
        			case WEST:
        				boundingBox = new AxisAlignedBB(0, -1, 0, 1, 1, 1);
        				break;
        			case EAST:
        				boundingBox = new AxisAlignedBB(0, -1, 0, 1, 1, 1);
        				break;
        			case NORTH:
        				boundingBox = new AxisAlignedBB(0, -1, 0, 1, 1, 1);
        				break;
        			case SOUTH:
        				boundingBox = new AxisAlignedBB(0, -1, 0, 1, 1, 1);
        				break;
        			default:
        			}
        			return boundingBox;
        		}
        )
        .withPositioning(tileEntity -> {
            GL11.glScalef(0.9f, 0.9f, 0.9f);
            GL11.glTranslatef(0.5f, 1.8f, 0.55f);
            GL11.glRotatef(-90F, 0f, 1f, 0f);
        })
        .build(MWC.modContext);
        
        new LootBoxConfiguration()
        .withMaterial(Material.WOOD)
        .withName("impaled_body")
        .withModelClassName("com.paneedah.mwc.models.ImpaledBody")
        .withTextureName("textures/models/impaledbody.png")
        .withCreativeTab(MWC.PROPS_TAB)
        .withBoundingBox(
        		blockState -> {
        			AxisAlignedBB boundingBox = null;
        			EnumFacing facing = blockState.getValue(CustomTileEntityBlock.FACING);
        			switch(facing) {
        			case WEST:
        				boundingBox = new AxisAlignedBB(0, 0, 0, 1, 3, 1);
        				break;
        			case EAST:
        				boundingBox = new AxisAlignedBB(0, 0, 0, 1, 3, 1);
        				break;
        			case NORTH:
        				boundingBox = new AxisAlignedBB(0, 0, 0, 1, 3, 1);
        				break;
        			case SOUTH:
        				boundingBox = new AxisAlignedBB(0, 0, 0, 1, 3, 1);
        				break;
        			default:
        			}
        			return boundingBox;
        		}
        )
        .withPositioning(tileEntity -> {
            GL11.glScalef(0.9f, 0.9f, 0.9f);
            GL11.glTranslatef(0.5f, 0.2f, 0.55f);
            GL11.glRotatef(-90F, 0f, 1f, 0f);
        })
        .build(MWC.modContext);

    }
}

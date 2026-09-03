package com.voltyx.mwccf.furniture.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import com.voltyx.mwccf.furniture.tileentity.*;

public class FurnitureGuiHandler implements IGuiHandler {

    public static final int GUI_NOTE = efw.procedures.NoteRightclickedOnBlockProcedure.GUI_ID; // 0
    public static final int GUI_DIARY = efw.procedures.DiaryOpenProcedure.GUI_ID; // 1
    public static final int GUI_FRIDGE = 200;
    public static final int GUI_STOVE = 201;
    public static final int GUI_MICROWAVE = 202;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_NOTE) {
            return new efw.world.inventory.NoteContainer(player, world);
        } else if (ID == GUI_DIARY) {
            return new efw.world.inventory.DiaryContainer(player, world);
        }
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityFridge) {
            return new ContainerFridge(player.inventory, (TileEntityFridge) te);
        } else if (te instanceof TileEntityStove) {
            return new ContainerStove(player.inventory, (TileEntityStove) te);
        } else if (te instanceof TileEntityMicrowave) {
            return new ContainerMicrowave(player.inventory, (TileEntityMicrowave) te);
        }
        return null;
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_NOTE) {
            return new efw.client.gui.NoteGui(new efw.world.inventory.NoteContainer(player, world), player);
        } else if (ID == GUI_DIARY) {
            return new efw.client.gui.DiaryGui(new efw.world.inventory.DiaryContainer(player, world), player);
        }
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityFridge) {
            return new GuiFridge(player.inventory, (TileEntityFridge) te);
        } else if (te instanceof TileEntityStove) {
            return new GuiStove(player.inventory, (TileEntityStove) te);
        } else if (te instanceof TileEntityMicrowave) {
            return new GuiMicrowave(player.inventory, (TileEntityMicrowave) te);
        }
        return null;
    }
}

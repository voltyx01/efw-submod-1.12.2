package efw.world.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class NoteContainer extends Container {

    public final World world;
    public final EntityPlayer player;

    public NoteContainer(EntityPlayer player, World world) {
        this.player = player;
        this.world  = world;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        return ItemStack.EMPTY;
    }
}

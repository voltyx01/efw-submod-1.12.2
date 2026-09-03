package efw.world.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DiaryContainer extends Container {

    public final World world;
    public final EntityPlayer player;

    public DiaryContainer(EntityPlayer player, World world) {
        this.player = player;
        this.world  = world;
    }

    /** Returns the diary item held in the main hand (or off-hand fallback). */
    public ItemStack getDiaryStack() {
        ItemStack main = player.getHeldItemMainhand();
        if (!main.isEmpty() && main.getItem() instanceof efw.item.CDiaryItem) {
            return main;
        }
        ItemStack off = player.getHeldItemOffhand();
        if (!off.isEmpty() && off.getItem() instanceof efw.item.CDiaryItem) {
            return off;
        }
        return main;
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

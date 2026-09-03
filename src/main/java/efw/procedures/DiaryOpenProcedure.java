package efw.procedures;

import efw.init.EfwModSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DiaryOpenProcedure {

    // GUI ID registered in the GuiHandler
    public static final int GUI_ID = 1;

    public static void execute(World world, EntityPlayer player, ItemStack stack) {
        if (player == null || world == null || stack.isEmpty()) return;

        if (world.isRemote) {
            // Client-side: play diary open sound
            player.playSound(EfwModSounds.DIARYOPEN, 1.5f, 1.0f);
            // Open inspect GUI with transition
            com.voltyx.mwccf.client.inspect.InspectTransitionHandler.startTransition(stack.copy(), null);
        }
    }
}

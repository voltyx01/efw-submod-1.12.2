package efw.procedures;

import efw.init.EfwModSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class NoteRightclickedOnBlockProcedure {

    public static final int GUI_ID = 0;

    public static void execute(World world, EntityPlayer player, ItemStack stack) {
        if (player == null || world == null || stack.isEmpty()) return;

        if (world.isRemote) {
            // Client-side: play sound once per player
            if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
            NBTTagCompound root = stack.getTagCompound();
            if (!root.hasKey("efw_note")) root.setTag("efw_note", new NBTTagCompound());
            NBTTagCompound noteTag = root.getCompoundTag("efw_note");

            String playerId = player.getUniqueID().toString();
            if (!noteTag.hasKey("playedItemSound")) noteTag.setTag("playedItemSound", new NBTTagCompound());
            NBTTagCompound playedMap = noteTag.getCompoundTag("playedItemSound");

            if (!playedMap.hasKey(playerId)) {
                player.playSound(EfwModSounds.ITEMSOUND, 1.5f, 1.0f);
                playedMap.setBoolean(playerId, true);
            }
            player.playSound(EfwModSounds.NOTES, 1.0f, 1.0f);

            // Open inspect GUI with transition
            com.voltyx.mwccf.client.inspect.InspectTransitionHandler.startTransition(stack.copy(), null);
        }
    }
}

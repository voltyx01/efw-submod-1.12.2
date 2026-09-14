package efw.item;

import efw.procedures.DiaryOpenProcedure;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class CDiaryItem extends Item {

    public CDiaryItem() {
        setMaxStackSize(1);
        setTranslationKey("c_diary");
        setRegistryName("mwccf", "c_diary");
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack) {
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        DiaryOpenProcedure.execute(world, player, stack);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    /**
     * Absorb a NoteItem from 'other' into this diary's StoredNotes list.
     * Returns true if absorbed.
     */
    public static boolean tryAbsorbNote(ItemStack diary, ItemStack other, EntityPlayer player) {
        if (diary == null || diary.isEmpty() || !(diary.getItem() instanceof CDiaryItem))
            return false;
        if (other == null || other.isEmpty() || !(other.getItem() instanceof NoteItem))
            return false;

        int noteId = NoteItem.getNoteId(other);
        int variant = NoteItem.getVariant(other);
        boolean isQuest = NoteItem.isQuest(other);
        if (!other.hasTagCompound() || !other.getTagCompound().hasKey("efw_note")) {
            int maxNotes = efw.config.NotesConfig.getEntriesCount();
            noteId = maxNotes > 0 ? (int) (Math.random() * maxNotes) + 1 : 1;
            variant = (int) (Math.random() * 10) + 1;
            isQuest = false;
        }

        NBTTagCompound noteNbt = new NBTTagCompound();
        noteNbt.setInteger("noteId", noteId);
        noteNbt.setInteger("variant", variant);
        noteNbt.setBoolean("isQuest", isQuest);

        // Ensure diary has tag
        if (!diary.hasTagCompound())
            diary.setTagCompound(new NBTTagCompound());
        NBTTagCompound diaryRoot = diary.getTagCompound();
        if (!diaryRoot.hasKey("efw_diary"))
            diaryRoot.setTag("efw_diary", new NBTTagCompound());
        NBTTagCompound diaryTag = diaryRoot.getCompoundTag("efw_diary");

        NBTTagList notesList = diaryTag.getTagList("StoredNotes", 10);
        notesList.appendTag(noteNbt);
        diaryTag.setTag("StoredNotes", notesList);

        other.shrink(1);
        if (player != null) {
            player.playSound(efw.init.EfwModSounds.DIARYOPEN, 1.2f, 1.0f);
        }
        return true;
    }

    public static NBTTagList getStoredNotes(ItemStack diary) {
        if (!diary.hasTagCompound())
            return new NBTTagList();
        NBTTagCompound root = diary.getTagCompound();
        if (!root.hasKey("efw_diary"))
            return new NBTTagList();
        return root.getCompoundTag("efw_diary").getTagList("StoredNotes", 10);
    }
}

package efw.item;

import efw.config.NotesConfig;
import efw.procedures.NoteRightclickedOnBlockProcedure;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NoteItem extends Item {

    public NoteItem() {
        setMaxStackSize(1);
        setTranslationKey("note");
        setRegistryName("mwccf", "note");
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    private int getRandomNoteId() {
        int maxNotes = NotesConfig.getEntriesCount();
        if (maxNotes <= 0)
            return 1;
        return (int) (Math.random() * maxNotes) + 1;
    }

    private int getRandomNoteVariant() {
        return (int) (Math.random() * 10) + 1;
    }

    private static NBTTagCompound ensureNoteNbt(ItemStack stack) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        NBTTagCompound root = stack.getTagCompound();
        if (!root.hasKey("efw_note"))
            root.setTag("efw_note", new NBTTagCompound());
        return root.getCompoundTag("efw_note");
    }

    @Override
    public void onUpdate(ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        if (!world.isRemote) {
            NBTTagCompound nbt = ensureNoteNbt(stack);
            if (!nbt.hasKey("noteId")) {
                nbt.setInteger("noteId", getRandomNoteId());
                nbt.setInteger("variant", getRandomNoteVariant());
            }
        }
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack) {
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            NBTTagCompound nbt = ensureNoteNbt(stack);
            if (!nbt.hasKey("noteId")) {
                nbt.setInteger("noteId", getRandomNoteId());
                nbt.setInteger("variant", getRandomNoteVariant());
            }
        }
        NoteRightclickedOnBlockProcedure.execute(world, player, stack);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
            EnumHand hand, net.minecraft.util.EnumFacing facing,
            float hitX, float hitY, float hitZ) {
        NoteRightclickedOnBlockProcedure.execute(world, player, player.getHeldItem(hand));
        return world.isRemote ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
    }

    public static int getVariant(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("efw_note")) {
            NBTTagCompound sub = stack.getTagCompound().getCompoundTag("efw_note");
            if (sub.hasKey("variant"))
                return sub.getInteger("variant");
        }
        return 1;
    }

    public static int getNoteId(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("efw_note")) {
            NBTTagCompound sub = stack.getTagCompound().getCompoundTag("efw_note");
            if (sub.hasKey("noteId"))
                return sub.getInteger("noteId");
        }
        return 1;
    }
}

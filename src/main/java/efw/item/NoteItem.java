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
                nbt.setBoolean("isQuest", false);
            }
        }

        // Если игрок в креативе и в приседе: открываем GUI настройки записки
        if (player.capabilities.isCreativeMode && player.isSneaking()) {
            if (world.isRemote) {
                openConfigGui(player, hand);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        NoteRightclickedOnBlockProcedure.execute(world, player, stack);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
            EnumHand hand, net.minecraft.util.EnumFacing facing,
            float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.capabilities.isCreativeMode && player.isSneaking()) {
            if (world.isRemote) {
                openConfigGui(player, hand);
            }
            return EnumActionResult.SUCCESS;
        }

        NoteRightclickedOnBlockProcedure.execute(world, player, stack);
        return world.isRemote ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
    }

    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    private void openConfigGui(EntityPlayer player, EnumHand hand) {
        net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(
                new efw.client.gui.GuiNoteConfig(player, hand)
        );
    }

    public static boolean isQuest(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("efw_note")) {
            NBTTagCompound sub = stack.getTagCompound().getCompoundTag("efw_note");
            if (sub.hasKey("isQuest"))
                return sub.getBoolean("isQuest");
        }
        return false;
    }

    public static void setNoteData(ItemStack stack, int noteId, int variant, boolean isQuest) {
        NBTTagCompound nbt = ensureNoteNbt(stack);
        nbt.setInteger("noteId", noteId);
        nbt.setInteger("variant", variant);
        nbt.setBoolean("isQuest", isQuest);
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

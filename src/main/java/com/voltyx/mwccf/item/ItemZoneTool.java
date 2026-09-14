package com.voltyx.mwccf.item;

import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.furniture.FurnitureCreativeTab;
import com.voltyx.mwccf.zone.network.PacketCreateZone;
import com.voltyx.mwccf.zone.network.PacketDeleteZoneAt;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemZoneTool extends Item {

    public ItemZoneTool(String registryName) {
        setRegistryName(registryName);
        this.setTranslationKey("mwccf." + registryName);
        setMaxStackSize(1);
        setCreativeTab(FurnitureCreativeTab.INSTANCE);
    }

    public static BlockPos getPos1(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Pos1")) {
            return BlockPos.fromLong(stack.getTagCompound().getLong("Pos1"));
        }
        return null;
    }

    public static void setPos1(ItemStack stack, BlockPos pos) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (pos != null) {
            stack.getTagCompound().setLong("Pos1", pos.toLong());
        } else {
            stack.getTagCompound().removeTag("Pos1");
        }
    }

    public static BlockPos getPos2(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Pos2")) {
            return BlockPos.fromLong(stack.getTagCompound().getLong("Pos2"));
        }
        return null;
    }

    public static void setPos2(ItemStack stack, BlockPos pos) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (pos != null) {
            stack.getTagCompound().setLong("Pos2", pos.toLong());
        } else {
            stack.getTagCompound().removeTag("Pos2");
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        // ЛКМ по блоку:
        // Если Shift + ЛКМ -> запрос на удаление зоны в этой точке
        // Если обычный ЛКМ -> задать Pos1
        if (player.world.isRemote) {
            if (player.isSneaking()) {
                MwccfMod.PACKET_HANDLER.sendToServer(new PacketDeleteZoneAt(pos));
            } else {
                setPos1(itemstack, pos);
                player.sendStatusMessage(new TextComponentString(
                        TextFormatting.GOLD + "[Зоны] " +
                        TextFormatting.GREEN + "Точка 1 (Pos1) установлена: " +
                        TextFormatting.YELLOW + "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]"
                ), true);
                player.world.playSound(player, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.6F, 1.2F);
            }
        } else {
            if (player.isSneaking()) {
                // Серверное удаление выполнится через пакет
            } else {
                setPos1(itemstack, pos);
            }
        }
        return true; // Предотвращаем разрушение блока этим кликом
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (player.isSneaking()) {
            // Shift + ПКМ: создать зону из Pos1 и Pos2
            BlockPos p1 = getPos1(stack);
            BlockPos p2 = getPos2(stack);

            if (p1 == null || p2 == null) {
                if (world.isRemote) {
                    player.sendStatusMessage(new TextComponentString(
                            TextFormatting.RED + "[Зоны] Сначала установите обе точки! (ЛКМ = Pos1, ПКМ = Pos2)"
                    ), true);
                    world.playSound(player, player.getPosition(), SoundEvents.BLOCK_NOTE_BASS, SoundCategory.PLAYERS, 0.8F, 0.6F);
                }
                return EnumActionResult.FAIL;
            }

            if (world.isRemote) {
                MwccfMod.PACKET_HANDLER.sendToServer(new PacketCreateZone("Комната", p1, p2));
            }
            return EnumActionResult.SUCCESS;
        } else {
            // Обычный ПКМ по блоку: задать Pos2
            setPos2(stack, pos);
            if (world.isRemote) {
                player.sendStatusMessage(new TextComponentString(
                        TextFormatting.GOLD + "[Зоны] " +
                        TextFormatting.AQUA + "Точка 2 (Pos2) установлена: " +
                        TextFormatting.YELLOW + "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]"
                ), true);
                world.playSound(player, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.6F, 1.5F);
            }
            return EnumActionResult.SUCCESS;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking()) {
            // Shift + ПКМ в воздух: также создать зону, если обе точки уже установлены
            BlockPos p1 = getPos1(stack);
            BlockPos p2 = getPos2(stack);
            if (p1 != null && p2 != null) {
                if (world.isRemote) {
                    MwccfMod.PACKET_HANDLER.sendToServer(new PacketCreateZone("Комната", p1, p2));
                }
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GOLD + "Инструмент разметки защищенных зон");
        tooltip.add(TextFormatting.GRAY + "Защищает комнаты от любых разрушений и взрывов.");
        tooltip.add("");
        tooltip.add(TextFormatting.YELLOW + "Управление:");
        tooltip.add(TextFormatting.WHITE + "• ЛКМ по блоку: " + TextFormatting.GRAY + "установить точку 1 (Pos1)");
        tooltip.add(TextFormatting.WHITE + "• ПКМ по блоку: " + TextFormatting.GRAY + "установить точку 2 (Pos2)");
        tooltip.add(TextFormatting.WHITE + "• Shift + ПКМ: " + TextFormatting.GREEN + "создать/сохранить защищенную зону");
        tooltip.add(TextFormatting.WHITE + "• Shift + ЛКМ: " + TextFormatting.RED + "удалить зону под прицелом");
        tooltip.add("");

        BlockPos p1 = getPos1(stack);
        BlockPos p2 = getPos2(stack);
        tooltip.add(TextFormatting.DARK_AQUA + "Pos 1: " + (p1 != null ? TextFormatting.WHITE + "" + p1.getX() + ", " + p1.getY() + ", " + p1.getZ() : TextFormatting.DARK_GRAY + "не установлена"));
        tooltip.add(TextFormatting.DARK_AQUA + "Pos 2: " + (p2 != null ? TextFormatting.WHITE + "" + p2.getX() + ", " + p2.getY() + ", " + p2.getZ() : TextFormatting.DARK_GRAY + "не установлена"));
    }
}

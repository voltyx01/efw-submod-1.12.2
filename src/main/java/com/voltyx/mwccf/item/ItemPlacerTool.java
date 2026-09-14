package com.voltyx.mwccf.item;

import com.voltyx.mwccf.furniture.BlockPlacedItem;
import com.voltyx.mwccf.furniture.FurnitureBlocks;
import com.voltyx.mwccf.furniture.FurnitureCreativeTab;
import com.voltyx.mwccf.furniture.client.gui.GuiPlacedItemConfig;
import com.voltyx.mwccf.furniture.tileentity.TileEntityPlacedItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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

public class ItemPlacerTool extends Item {

    public ItemPlacerTool(String registryName) {
        setRegistryName(registryName);
        this.setTranslationKey("mwccf." + registryName);
        setMaxStackSize(1);
        setCreativeTab(FurnitureCreativeTab.INSTANCE);
    }

    public static int getSelectedSlot(ItemStack tool) {
        if (tool.hasTagCompound() && tool.getTagCompound().hasKey("TargetSlot")) {
            int s = tool.getTagCompound().getInteger("TargetSlot");
            return Math.max(0, Math.min(8, s));
        }
        return 0; // Default slot 1 (index 0)
    }

    public static void setSelectedSlot(ItemStack tool, int slot) {
        if (!tool.hasTagCompound()) {
            tool.setTagCompound(new NBTTagCompound());
        }
        tool.getTagCompound().setInteger("TargetSlot", Math.max(0, Math.min(8, slot)));
    }

    public static int getSourceSlot(EntityPlayer player, ItemStack tool) {
        int target = getSelectedSlot(tool);
        ItemStack targetStack = player.inventory.getStackInSlot(target);
        if (!targetStack.isEmpty() && targetStack.getItem() != tool.getItem()) {
            return target;
        }
        // Smart fallback: check Slot 1 (index 0), then Slot 2 (index 1 / Pistol), then any hotbar slot 0..8
        if (!player.inventory.getStackInSlot(0).isEmpty() && player.inventory.getStackInSlot(0).getItem() != tool.getItem()) {
            return 0;
        }
        if (!player.inventory.getStackInSlot(1).isEmpty() && player.inventory.getStackInSlot(1).getItem() != tool.getItem()) {
            return 1;
        }
        for (int i = 0; i < 9; i++) {
            ItemStack s = player.inventory.getStackInSlot(i);
            if (!s.isEmpty() && s.getItem() != tool.getItem()) {
                return i;
            }
        }
        return target;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GOLD + "=== Инструмент размещения предметов ===");

        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            int slot = getSourceSlot(player, stack);
            ItemStack targetStack = player.inventory.getStackInSlot(slot);
            if (!targetStack.isEmpty()) {
                tooltip.add(TextFormatting.AQUA + "Выбран слот " + (slot + 1) + ": " + TextFormatting.YELLOW + targetStack.getDisplayName());
            } else {
                tooltip.add(TextFormatting.RED + "Хотбар пуст! Положите оружие или предмет в хотбар.");
            }
        }

        tooltip.add("");
        tooltip.add(TextFormatting.GRAY + "• ПКМ в воздух: " + TextFormatting.WHITE + "переключить слот (Shift+ПКМ: назад)");
        tooltip.add(TextFormatting.GRAY + "• ПКМ по столу/полу: " + TextFormatting.WHITE + "поставить предмет");
        tooltip.add(TextFormatting.GRAY + "• ПКМ по уже стоящему предмету: " + TextFormatting.WHITE + "настроить угол и позицию");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack tool = player.getHeldItem(hand);
        int current = getSelectedSlot(tool);
        boolean backwards = player.isSneaking();
        int step = backwards ? -1 : 1;

        int next = -1;
        for (int i = 1; i <= 9; i++) {
            int candidate = (current + i * step + 18) % 9;
            ItemStack s = player.inventory.getStackInSlot(candidate);
            if (!s.isEmpty() && s.getItem() != tool.getItem()) {
                next = candidate;
                break;
            }
        }

        if (next != -1) {
            setSelectedSlot(tool, next);
            ItemStack chosen = player.inventory.getStackInSlot(next);
            if (world.isRemote) {
                player.sendStatusMessage(new TextComponentString(
                        TextFormatting.GOLD + "[Установщик] " +
                        TextFormatting.YELLOW + "Выбран слот " + (next + 1) + ": " +
                        TextFormatting.GREEN + chosen.getDisplayName()
                ), true);
                world.playSound(player, player.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.5F, 1.2F);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, tool);
        } else {
            if (world.isRemote) {
                player.sendStatusMessage(new TextComponentString(
                        TextFormatting.RED + "[Установщик] В хотбаре нет других предметов для размещения!"
                ), true);
            }
            return new ActionResult<>(EnumActionResult.FAIL, tool);
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        // If clicking on an existing placed item, let the block open GUI
        if (world.getBlockState(pos).getBlock() instanceof BlockPlacedItem) {
            return EnumActionResult.PASS;
        }

        ItemStack tool = player.getHeldItem(hand);
        int sourceSlot = getSourceSlot(player, tool);
        ItemStack itemToPlace = player.inventory.getStackInSlot(sourceSlot);
        if (itemToPlace.isEmpty() || itemToPlace.getItem() instanceof ItemPlacerTool) {
            if (world.isRemote) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + "[Установщик] Выбранный слот пуст! Положите оружие или предмет в слоты хотбара 1-9."));
            }
            return EnumActionResult.FAIL;
        }

        BlockPos targetPos = world.getBlockState(pos).getBlock().isReplaceable(world, pos) ? pos : pos.offset(facing);

        if (!player.canPlayerEdit(targetPos, facing, tool)) {
            return EnumActionResult.FAIL;
        }

        if (!world.mayPlace(FurnitureBlocks.PLACED_ITEM, targetPos, false, facing, player)) {
            return EnumActionResult.FAIL;
        }

        if (!world.isRemote) {
            world.setBlockState(targetPos, FurnitureBlocks.PLACED_ITEM.getDefaultState(), 3);
            TileEntity te = world.getTileEntity(targetPos);
            if (te instanceof TileEntityPlacedItem) {
                TileEntityPlacedItem placed = (TileEntityPlacedItem) te;
                ItemStack copy = itemToPlace.copy();
                copy.setCount(1);
                placed.setStack(copy);

                if (!player.capabilities.isCreativeMode) {
                    itemToPlace.shrink(1);
                    if (itemToPlace.isEmpty()) {
                        player.inventory.setInventorySlotContents(sourceSlot, ItemStack.EMPTY);
                    }
                }

                if (facing == EnumFacing.UP) {
                    placed.setOffsetX(hitX);
                    placed.setOffsetY(0.02f);
                    placed.setOffsetZ(hitZ);
                } else {
                    placed.setOffsetX(0.5f);
                    placed.setOffsetY(0.02f);
                    placed.setOffsetZ(0.5f);
                }

                float yaw = Math.round(player.rotationYaw / 15.0f) * 15.0f;
                yaw = (yaw % 360.0f + 360.0f) % 360.0f;
                placed.setRotationYaw(yaw);
                placed.setRotationPitch(0.0f);

                String itemCls = itemToPlace.getItem().getClass().getName();
                boolean isGun = itemToPlace.getItem() instanceof com.paneedah.weaponlib.Weapon || itemCls.contains("weaponlib") || itemCls.contains("Weapon");
                placed.setRotationRoll(isGun && facing == EnumFacing.UP ? 90.0f : 0.0f);

                placed.setScale(1.0f);
                placed.setLocked(false);

                placed.markDirty();
                world.notifyBlockUpdate(targetPos, world.getBlockState(targetPos), world.getBlockState(targetPos), 3);
            }
            world.playSound(null, targetPos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 0.8F, 1.2F);
        } else {
            // Client side: schedule opening the configuration GUI immediately!
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntity te = world.getTileEntity(targetPos);
                if (te instanceof TileEntityPlacedItem) {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiPlacedItemConfig((TileEntityPlacedItem) te));
                }
            });
        }

        return EnumActionResult.SUCCESS;
    }
}

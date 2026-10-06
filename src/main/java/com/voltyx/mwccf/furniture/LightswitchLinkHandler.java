package com.voltyx.mwccf.furniture;

import com.voltyx.mwccf.furniture.tileentity.TileEntityLightswitch;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "mwccf")
public class LightswitchLinkHandler {

    public static boolean isLightswitchItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        Item item = stack.getItem();
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            if (block instanceof BlockLightswitch) {
                return true;
            }
        }
        ResourceLocation reg = item.getRegistryName();
        if (reg != null) {
            String path = reg.getPath();
            if (path.contains("lightswitch") || path.contains("light_switch")) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack held = event.getItemStack();
        if (!isLightswitchItem(held)) return;

        World world = event.getWorld();
        BlockPos targetPos = event.getPos();
        IBlockState targetState = world.getBlockState(targetPos);
        Block targetBlock = targetState.getBlock();
        EntityPlayer player = event.getEntityPlayer();

        // 1. Right click on lamp -> Link / Unlink
        if (TileEntityLightswitch.isLamp(targetBlock)) {
            event.setCanceled(true);
            event.setCancellationResult(EnumActionResult.SUCCESS);

            if (!world.isRemote) {
                toggleLinkLamp(held, targetPos, player, world);
            } else {
                player.swingArm(event.getHand());
            }
            return;
        }

        // 2. Right click on placed switch -> Transfer links to placed switch
        if (targetBlock instanceof BlockLightswitch) {
            TileEntity te = world.getTileEntity(targetPos);
            if (te instanceof TileEntityLightswitch) {
                TileEntityLightswitch switchTe = (TileEntityLightswitch) te;
                List<BlockPos> itemLights = getLightsFromItem(held);
                if (!itemLights.isEmpty()) {
                    event.setCanceled(true);
                    event.setCancellationResult(EnumActionResult.SUCCESS);

                    if (!world.isRemote) {
                        for (BlockPos lampPos : itemLights) {
                            BlockPos offset = new BlockPos(
                                    lampPos.getX() - targetPos.getX(),
                                    lampPos.getY() - targetPos.getY(),
                                    lampPos.getZ() - targetPos.getZ()
                            );
                            switchTe.addLinkedOffset(offset);
                        }
                        world.playSound(null, targetPos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.8F, 1.2F);
                        player.sendStatusMessage(new TextComponentTranslation(
                                "message.mwccf.lightswitch.transferred", switchTe.getLinkedOffsets().size()), true);
                    } else {
                        player.swingArm(event.getHand());
                    }
                }
            }
        }
    }

    private static void toggleLinkLamp(ItemStack switchStack, BlockPos lampPos, EntityPlayer player, World world) {
        NBTTagCompound tag = switchStack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            switchStack.setTagCompound(tag);
        }

        NBTTagCompound teTag = tag.getCompoundTag("BlockEntityTag");
        NBTTagList list = teTag.getTagList("lights", 10);

        int existingIndex = -1;
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound item = list.getCompoundTagAt(i);
            if (item.getInteger("x") == lampPos.getX()
                    && item.getInteger("y") == lampPos.getY()
                    && item.getInteger("z") == lampPos.getZ()) {
                existingIndex = i;
                break;
            }
        }

        if (existingIndex >= 0) {
            list.removeTag(existingIndex);
            world.playSound(null, lampPos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.8F, 0.6F);
            player.sendStatusMessage(new TextComponentTranslation(
                    "message.mwccf.lightswitch.unlinked", list.tagCount()), true);
        } else {
            NBTTagCompound newEntry = new NBTTagCompound();
            newEntry.setInteger("x", lampPos.getX());
            newEntry.setInteger("y", lampPos.getY());
            newEntry.setInteger("z", lampPos.getZ());
            list.appendTag(newEntry);

            world.playSound(null, lampPos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.8F, 1.2F);
            player.sendStatusMessage(new TextComponentTranslation(
                    "message.mwccf.lightswitch.linked", list.tagCount()), true);
        }

        teTag.setTag("lights", list);
        tag.setTag("BlockEntityTag", teTag);
    }

    public static List<BlockPos> getLightsFromItem(ItemStack switchStack) {
        List<BlockPos> result = new ArrayList<>();
        if (switchStack == null || !switchStack.hasTagCompound()) return result;

        NBTTagCompound tag = switchStack.getTagCompound();
        NBTTagList list = null;

        if (tag.hasKey("BlockEntityTag", 10)) {
            NBTTagCompound teTag = tag.getCompoundTag("BlockEntityTag");
            if (teTag.hasKey("lights", 9)) {
                list = teTag.getTagList("lights", 10);
            }
        } else if (tag.hasKey("lights", 9)) {
            list = tag.getTagList("lights", 10);
        }

        if (list != null) {
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound item = list.getCompoundTagAt(i);
                result.add(new BlockPos(item.getInteger("x"), item.getInteger("y"), item.getInteger("z")));
            }
        }
        return result;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (isLightswitchItem(stack)) {
            List<BlockPos> lights = getLightsFromItem(stack);
            if (!lights.isEmpty()) {
                event.getToolTip().add(TextFormatting.GREEN + I18n.format("tooltip.mwccf.lightswitch.linked", lights.size()));
            }
            event.getToolTip().add(TextFormatting.DARK_GRAY + I18n.format("tooltip.mwccf.lightswitch.hint"));
        }
    }
}

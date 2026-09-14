package com.voltyx.mwccf.block.lamp;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockFlickeringLamp extends ItemBlock {

    private final LampFlickerType flickerType;

    public ItemBlockFlickeringLamp(Block block, LampFlickerType flickerType) {
        super(block);
        this.flickerType = flickerType;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String descKey = "tooltip.mwccf.flickering_lamp_" + flickerType.getName() + ".desc";
        if (I18n.hasKey(descKey)) {
            tooltip.add(TextFormatting.GRAY + I18n.format(descKey));
        }
        String redstoneKey = "tooltip.mwccf.flickering_lamp.redstone";
        if (I18n.hasKey(redstoneKey)) {
            tooltip.add(TextFormatting.DARK_GRAY + I18n.format(redstoneKey));
        }
    }
}

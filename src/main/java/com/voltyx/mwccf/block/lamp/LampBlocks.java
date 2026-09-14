package com.voltyx.mwccf.block.lamp;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "mwccf")
public class LampBlocks {

    public static final List<BlockFlickeringLamp> BLOCKS = new ArrayList<>();
    public static final List<ItemBlock> ITEMS = new ArrayList<>();

    public static BlockFlickeringLamp LAMP_SOFT;
    public static BlockFlickeringLamp LAMP_BROKEN;
    public static BlockFlickeringLamp LAMP_DYING;

    public static void init() {
        if (!BLOCKS.isEmpty()) return;

        LAMP_SOFT = registerBlock("flickering_lamp_soft", LampFlickerType.SOFT);
        LAMP_BROKEN = registerBlock("flickering_lamp_broken", LampFlickerType.BROKEN);
        LAMP_DYING = registerBlock("flickering_lamp_dying", LampFlickerType.DYING);

        GameRegistry.registerTileEntity(TileEntityFlickeringLamp.class, new ResourceLocation("mwccf", "flickering_lamp"));
    }

    private static BlockFlickeringLamp registerBlock(String name, LampFlickerType type) {
        BlockFlickeringLamp block = new BlockFlickeringLamp(name, type);
        BLOCKS.add(block);
        ItemBlockFlickeringLamp itemBlock = new ItemBlockFlickeringLamp(block, type);
        itemBlock.setRegistryName(block.getRegistryName());
        ITEMS.add(itemBlock);
        return block;
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        init();
        for (Block block : BLOCKS) {
            event.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        init();
        for (Item item : ITEMS) {
            event.getRegistry().register(item);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        init();
        for (Item item : ITEMS) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }
}

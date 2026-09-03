package com.voltyx.mwccf.furniture;

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
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class FurnitureBlocks {

    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final List<Item> ITEMS = new ArrayList<>();

    public static Block WORKBENCH;
    
    // Chairs
    public static Block CHAIR_OAK;
    public static Block CHAIR_SPRUCE;
    public static Block CHAIR_BIRCH;
    public static Block CHAIR_JUNGLE;
    public static Block CHAIR_ACACIA;
    public static Block CHAIR_DARK_OAK;

    // Fridges
    public static Block FRIDGE_LIGHT;
    public static Block FRIDGE_DARK;

    public static void init() {
        CHAIR_OAK = registerBlock(new BlockChair("oak_chair"));
        CHAIR_SPRUCE = registerBlock(new BlockChair("spruce_chair"));
        CHAIR_BIRCH = registerBlock(new BlockChair("birch_chair"));
        CHAIR_JUNGLE = registerBlock(new BlockChair("jungle_chair"));
        CHAIR_ACACIA = registerBlock(new BlockChair("acacia_chair"));
        CHAIR_DARK_OAK = registerBlock(new BlockChair("dark_oak_chair"));

        FRIDGE_LIGHT = registerBlock(new BlockFridge("light_fridge"));
        FRIDGE_DARK = registerBlock(new BlockFridge("dark_fridge"));

        registerBlock(new BlockStove("light_stove"));
        registerBlock(new BlockStove("dark_stove"));

        registerBlock(new BlockMicrowave("light_microwave"));
        registerBlock(new BlockMicrowave("dark_microwave"));

        // Kitchen Sinks & Cabinetry
        String[] woodTypes = new String[]{"oak", "spruce", "birch", "jungle", "acacia", "dark_oak"};
        for (String type : woodTypes) {
            registerBlock(new BlockKitchenSink(type + "_kitchen_sink"));
            registerBlock(new BlockKitchenCabinetry(type + "_kitchen_cabinetry"));
            registerBlock(new BlockKitchenCabinetry(type + "_kitchen_drawer"));
        }

        // Tables
        for (String type : woodTypes) {
            registerBlock(new BlockTable(type + "_table"));
            registerBlock(new BlockTable(type + "_desk"));
        }

        // Electronics & Entertainment
        registerBlock(new BlockComputer("computer"));
        registerBlock(new BlockTelevision("television"));

        // Bathroom
        registerBlock(new BlockToilet("toilet"));
        registerBlock(new BlockBath("bath"));

        // Outdoor & Fences
        registerBlock(new BlockMailbox("mailbox"));
        registerBlock(new BlockGrill("grill_light"));
        registerBlock(new BlockGrill("grill_dark"));

        for (String type : woodTypes) {
            registerBlock(new BlockLatticeFence(type + "_lattice_fence"));
            registerBlock(new BlockLatticeFence(type + "_lattice_fence_gate"));
        }

        // Lighting & Fans
        registerBlock(new BlockCeilingFan("ceiling_fan_light"));
        registerBlock(new BlockCeilingFan("ceiling_fan_dark"));

        // Sofas (16 Dye Colors)
        String[] colors = new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};
        for (String color : colors) {
            registerBlock(new BlockSofa(color + "_sofa"));
        }
        
        // Bathroom & Utility
        registerBlock(new BlockWashingMachine("washing_machine"));

        // Lighting & Lamps
        for (String color : colors) {
            registerBlock(new BlockLamp(color + "_lamp"));
        }

        net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity(com.voltyx.mwccf.furniture.tileentity.TileEntityFridge.class, new ResourceLocation("mwccf", "fridge"));
        net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity(com.voltyx.mwccf.furniture.tileentity.TileEntityStove.class, new ResourceLocation("mwccf", "stove"));
        net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity(com.voltyx.mwccf.furniture.tileentity.TileEntityMicrowave.class, new ResourceLocation("mwccf", "microwave"));
        net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity(com.voltyx.mwccf.furniture.tileentity.TileEntityCabinet.class, new ResourceLocation("mwccf", "cabinet"));
        net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity(com.voltyx.mwccf.furniture.tileentity.TileEntityComputer.class, new ResourceLocation("mwccf", "computer"));
        net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity(com.voltyx.mwccf.furniture.tileentity.TileEntityWashingMachine.class, new ResourceLocation("mwccf", "washing_machine"));
    }

    private static Block registerBlock(Block block) {
        BLOCKS.add(block);
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        ITEMS.add(itemBlock);
        return block;
    }

    @Mod.EventBusSubscriber(modid = "mwccf")
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            FurnitureBlocks.init();
            for (Block block : BLOCKS) {
                event.getRegistry().register(block);
            }
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            for (Item item : ITEMS) {
                event.getRegistry().register(item);
            }
        }

        @SubscribeEvent
        public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
            EntityEntry seatEntry = EntityEntryBuilder.create()
                    .entity(EntitySeat.class)
                    .id(new ResourceLocation("mwccf", "furniture_seat"), 105)
                    .name("furniture_seat")
                    .tracker(80, 3, false)
                    .build();
            event.getRegistry().register(seatEntry);
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void registerModels(ModelRegistryEvent event) {
            for (Item item : ITEMS) {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
            }
        }
    }
}

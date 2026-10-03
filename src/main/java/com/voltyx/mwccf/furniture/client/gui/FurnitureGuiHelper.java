package com.voltyx.mwccf.furniture.client.gui;

import com.voltyx.mwccf.furniture.BlockFurnitureHorizontal;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class FurnitureGuiHelper {

    private static boolean registered = false;
    private static Field quarkSearchBarField = null;
    private static boolean quarkFieldLookupDone = false;

    // Track recently clicked furniture block
    private static BlockPos lastFurniturePos = null;
    private static long lastFurnitureClickTime = 0;

    // Cache current container state while open
    private static GuiScreen currentScreen = null;
    private static boolean isCurrentFurniture = false;

    private static final Set<String> KNOWN_CONTAINER_KEYS = new HashSet<>(Arrays.asList(
            "container.refurbished_furniture.kitchen_drawer",
            "container.refurbished_furniture.storage_cabinet",
            "container.refurbished_furniture.cabinet",
            "container.refurbished_furniture.cooler",
            "container.refurbished_furniture.crate",
            "container.refurbished_furniture.post_box",
            "container.refurbished_furniture.recycle_bin",
            "container.refurbished_furniture.workbench",
            "container.refurbished_furniture.drawer",
            "container.refurbished_furniture.fridge",
            "container.refurbished_furniture.freezer",
            "container.refurbished_furniture.microwave",
            "container.refurbished_furniture.stove",
            "container.refurbished_furniture.mailbox",
            "container.refurbished_furniture.electricity_generator",
            "container.refurbished_furniture.washing_machine"
    ));

    private static final String[] FURNITURE_TITLE_KEYWORDS = new String[] {
            "kitchen drawer", "storage cabinet", "cabinet", "cooler", "crate",
            "post box", "recycle bin", "workbench", "drawer", "fridge", "freezer",
            "microwave", "stove", "mailbox", "washing machine",
            "кухонный ящик", "настенный шкаф", "шкаф", "термосумка", "деревянный ящик",
            "почтовый ящик", "почтовая колонка", "мусорное ведро", "стиральная машина",
            "холодильник", "морозильник", "микроволновка", "плита", "верстак",
            "переносной холодильник", "банка для хранения"
    };

    public static void init() {
        if (!registered) {
            registered = true;
            MinecraftForge.EVENT_BUS.register(new FurnitureGuiHelper());
        }
    }

    public static boolean isFurnitureBlock(Block block) {
        if (block == null) return false;
        if (block instanceof BlockFurnitureHorizontal) return true;
        ResourceLocation reg = block.getRegistryName();
        if (reg != null) {
            String ns = reg.getNamespace();
            if ("refurbished_furniture".equals(ns) || "cfm".equals(ns)) return true;
        }
        String cls = block.getClass().getName();
        return cls.contains("furniture") || cls.startsWith("com.mrcrayfish.furniture.");
    }

    public static boolean isFurnitureContainerOpen() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.currentScreen == null) return false;
        return isFurnitureContainer(mc.currentScreen);
    }

    public static boolean isFurnitureContainer(GuiScreen screen) {
        if (screen == null) return false;
        if (screen == currentScreen) {
            return isCurrentFurniture;
        }

        boolean result = checkIsFurnitureContainer(screen);
        currentScreen = screen;
        isCurrentFurniture = result;
        return result;
    }

    private static boolean checkIsFurnitureContainer(GuiScreen screen) {
        if (screen == null) return false;

        // 1. Direct class check
        String cls = screen.getClass().getName();
        if (cls.contains("furniture") || cls.startsWith("com.mrcrayfish.furniture.")) {
            return true;
        }

        // 2. GuiChest checks
        if (screen instanceof GuiChest) {
            GuiChest chest = (GuiChest) screen;
            if (chest.inventorySlots instanceof ContainerChest) {
                IInventory lower = ((ContainerChest) chest.inventorySlots).getLowerChestInventory();
                if (lower != null) {
                    // Check lower inventory class
                    String lowerCls = lower.getClass().getName();
                    if (lowerCls.contains("furniture") || lowerCls.contains("Cabinet")
                            || lowerCls.contains("WashingMachine") || lowerCls.contains("Cooler")
                            || lowerCls.contains("Crate") || lowerCls.contains("PostBox")
                            || lowerCls.contains("RecycleBin")) {
                        return true;
                    }

                    // Check ITextComponent key
                    ITextComponent comp = lower.getDisplayName();
                    if (comp instanceof TextComponentTranslation) {
                        String key = ((TextComponentTranslation) comp).getKey();
                        if (key != null) {
                            if (key.contains("refurbished_furniture") || key.contains("furniture") || key.contains("cfm")) {
                                return true;
                            }
                            if (KNOWN_CONTAINER_KEYS.contains(key)) {
                                return true;
                            }
                        }
                    }

                    // Check unformatted / raw name
                    String name = lower.getName();
                    if (name != null) {
                        String lowerName = name.toLowerCase().trim();
                        if (lowerName.contains("refurbished_furniture") || lowerName.contains("cfm")) {
                            return true;
                        }
                        for (String kw : FURNITURE_TITLE_KEYWORDS) {
                            if (lowerName.equals(kw) || lowerName.contains(kw)) {
                                return true;
                            }
                        }
                    }

                    if (comp != null) {
                        String unformatted = comp.getUnformattedText().toLowerCase().trim();
                        for (String kw : FURNITURE_TITLE_KEYWORDS) {
                            if (unformatted.equals(kw) || unformatted.contains(kw)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        // 3. World context check (player right-clicked or is looking at a furniture block)
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world != null) {
            // Check recently clicked furniture block
            if (lastFurniturePos != null && (System.currentTimeMillis() - lastFurnitureClickTime < 6000)) {
                if (mc.player != null && mc.player.getDistanceSq(lastFurniturePos) <= 64.0D) {
                    IBlockState state = mc.world.getBlockState(lastFurniturePos);
                    if (isFurnitureBlock(state.getBlock())) {
                        return true;
                    }
                    TileEntity te = mc.world.getTileEntity(lastFurniturePos);
                    if (te != null && te.getClass().getName().contains("furniture")) {
                        return true;
                    }
                }
            }

            // Check raytrace / objectMouseOver
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = mc.objectMouseOver.getBlockPos();
                IBlockState state = mc.world.getBlockState(pos);
                if (isFurnitureBlock(state.getBlock())) {
                    return true;
                }
                TileEntity te = mc.world.getTileEntity(pos);
                if (te != null && te.getClass().getName().contains("furniture")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isInventoryText(String text) {
        if (text == null) return false;
        String clean = TextFormatting.getTextWithoutFormattingCodes(text).trim();
        if (clean.equalsIgnoreCase("Inventory") || clean.equalsIgnoreCase("Инвентарь")) {
            return true;
        }
        try {
            String localized = I18n.format("container.inventory");
            if (localized != null && clean.equalsIgnoreCase(TextFormatting.getTextWithoutFormattingCodes(localized).trim())) {
                return true;
            }
        } catch (Throwable ignored) {}
        return false;
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) {
            BlockPos pos = event.getPos();
            Block block = event.getWorld().getBlockState(pos).getBlock();
            if (isFurnitureBlock(block)) {
                lastFurniturePos = pos;
                lastFurnitureClickTime = System.currentTimeMillis();
            }
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        currentScreen = null;
        isCurrentFurniture = false;
        if (event.getGui() != null && isFurnitureContainer(event.getGui())) {
            disableQuarkSearchBar();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (isFurnitureContainer(event.getGui())) {
            disableQuarkSearchBar();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onDrawScreenPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (isFurnitureContainer(event.getGui())) {
            disableQuarkSearchBar();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (isFurnitureContainer(event.getGui())) {
            disableQuarkSearchBar();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderTooltip(RenderTooltipEvent.Pre event) {
        if (isFurnitureContainerOpen()) {
            disableQuarkSearchBar();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseInput(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (isFurnitureContainer(event.getGui())) {
            disableQuarkSearchBar();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onKeyboardInput(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        if (isFurnitureContainer(event.getGui())) {
            disableQuarkSearchBar();
        }
    }

    public static void disableQuarkSearchBar() {
        if (!Loader.isModLoaded("quark")) return;
        try {
            vazkii.quark.client.feature.ChestSearchBar.searchBar = null;
        } catch (Throwable t) {
            try {
                if (!quarkFieldLookupDone) {
                    quarkFieldLookupDone = true;
                    Class<?> clazz = Class.forName("vazkii.quark.client.feature.ChestSearchBar");
                    quarkSearchBarField = clazz.getField("searchBar");
                }
                if (quarkSearchBarField != null) {
                    quarkSearchBarField.set(null, null);
                }
            } catch (Throwable ignored) {}
        }
    }
}

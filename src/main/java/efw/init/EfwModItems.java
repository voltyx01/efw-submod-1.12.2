package efw.init;

import efw.item.CDiaryItem;
import efw.item.DporItem;
import efw.item.ManualItem;
import efw.item.NoteItem;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EfwModItems {

    public static NoteItem NOTE;
    public static CDiaryItem C_DIARY;
    public static efw.item.ItemMachete MACHETE;
    public static efw.item.ItemModernAxe MODERN_AXE;
    public static efw.item.ItemMedKit MED_KIT;
    public static efw.item.ItemBandage BANDAGE;
    public static efw.item.ItemCloth CLOTH;

    // Расходник для повышения уровня порока (Seven Screen)
    public static DporItem DPOR;

    // Руководства (изучение через X прокачивает соответствующую категорию порока)
    public static ManualItem MANUAL_MELEE;
    public static ManualItem MANUAL_FIREARMS;
    public static ManualItem MANUAL_MEDS;
    public static ManualItem MANUAL_LOCKPICK;
    public static ManualItem MANUAL_TOOLS;

    public static void register() {
        NOTE = new NoteItem();
        C_DIARY = new CDiaryItem();
        MACHETE = new efw.item.ItemMachete();
        MODERN_AXE = new efw.item.ItemModernAxe();
        MED_KIT = new efw.item.ItemMedKit();
        BANDAGE = new efw.item.ItemBandage();
        CLOTH = new efw.item.ItemCloth();
        DPOR = new DporItem();
        MANUAL_MELEE = new ManualItem(ManualItem.ManualType.MELEE);
        MANUAL_FIREARMS = new ManualItem(ManualItem.ManualType.FIREARMS);
        MANUAL_MEDS = new ManualItem(ManualItem.ManualType.MEDS);
        MANUAL_LOCKPICK = new ManualItem(ManualItem.ManualType.LOCKPICK);
        MANUAL_TOOLS = new ManualItem(ManualItem.ManualType.TOOLS);

        ForgeRegistries.ITEMS.register(NOTE);
        ForgeRegistries.ITEMS.register(C_DIARY);
        ForgeRegistries.ITEMS.register(MACHETE);
        ForgeRegistries.ITEMS.register(MODERN_AXE);
        ForgeRegistries.ITEMS.register(MED_KIT);
        ForgeRegistries.ITEMS.register(BANDAGE);
        ForgeRegistries.ITEMS.register(CLOTH);
        ForgeRegistries.ITEMS.register(DPOR);
        ForgeRegistries.ITEMS.register(MANUAL_MELEE);
        ForgeRegistries.ITEMS.register(MANUAL_FIREARMS);
        ForgeRegistries.ITEMS.register(MANUAL_MEDS);
        ForgeRegistries.ITEMS.register(MANUAL_LOCKPICK);
        ForgeRegistries.ITEMS.register(MANUAL_TOOLS);
    }

    /** Возвращает предмет-руководство для данной категории (см. {@link ManualItem.ManualType}). */
    public static ManualItem getManualForCategory(int categoryIndex) {
        switch (categoryIndex) {
            case 0: return MANUAL_MELEE;
            case 1: return MANUAL_FIREARMS;
            case 2: return MANUAL_MEDS;
            case 3: return MANUAL_LOCKPICK;
            case 4: return MANUAL_TOOLS;
            default: return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        // CDiary — simple single model
        ModelLoader.setCustomModelResourceLocation(C_DIARY, 0,
                new ModelResourceLocation("efw:c_diary", "inventory"));

        ModelLoader.setCustomModelResourceLocation(MACHETE, 0,
                new ModelResourceLocation("mwccf:machete", "inventory"));

        ModelLoader.setCustomModelResourceLocation(MODERN_AXE, 0,
                new ModelResourceLocation("mwccf:modern_axe", "inventory"));

        ModelLoader.setCustomModelResourceLocation(MED_KIT, 0,
                new ModelResourceLocation("mwccf:med_kit", "inventory"));

        ModelLoader.setCustomModelResourceLocation(BANDAGE, 0,
                new ModelResourceLocation("mwccf:bandage", "inventory"));

        ModelLoader.setCustomModelResourceLocation(CLOTH, 0,
                new ModelResourceLocation("mwccf:cloth", "inventory"));

        // Note — pick model by NBT variant (1–10), no damage value used
        ModelBakery.registerItemVariants(NOTE,
                noteLocation(1), noteLocation(2), noteLocation(3),
                noteLocation(4), noteLocation(5), noteLocation(6),
                noteLocation(7), noteLocation(8), noteLocation(9),
                noteLocation(10));

        ModelLoader.setCustomMeshDefinition(NOTE, (ItemStack stack) -> {
            int variant = NoteItem.getVariant(stack);
            if (variant < 1 || variant > 10)
                variant = 1;
            return new ModelResourceLocation("efw:note" + variant, "inventory");
        });

        // DPOR — simple single model, same style as a note
        ModelLoader.setCustomModelResourceLocation(DPOR, 0,
                new ModelResourceLocation("efw:dpor", "inventory"));

        // Manuals — simple single models, one per category
        ModelLoader.setCustomModelResourceLocation(MANUAL_MELEE, 0,
                new ModelResourceLocation("efw:manual_melee", "inventory"));
        ModelLoader.setCustomModelResourceLocation(MANUAL_FIREARMS, 0,
                new ModelResourceLocation("efw:manual_firearms", "inventory"));
        ModelLoader.setCustomModelResourceLocation(MANUAL_MEDS, 0,
                new ModelResourceLocation("efw:manual_meds", "inventory"));
        ModelLoader.setCustomModelResourceLocation(MANUAL_LOCKPICK, 0,
                new ModelResourceLocation("efw:manual_lockpick", "inventory"));
        ModelLoader.setCustomModelResourceLocation(MANUAL_TOOLS, 0,
                new ModelResourceLocation("efw:manual_tools", "inventory"));
    }

    private static ResourceLocation noteLocation(int i) {
        return new ResourceLocation("efw:note" + i);
    }
}

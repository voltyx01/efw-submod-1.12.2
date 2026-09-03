package efw.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Руководства ("manuals") — предметы, изучение которых (клавиша X в
 * инвентаре, см. {@code ItemInspectKeyHandler}) прокачивает соответствующую
 * категорию в {@code ISinCapability#getLoreBooksProgress()} и уничтожает сам
 * предмет.
 * <p>
 * Ведут себя как обычные предметы в Inspect GUI (эпическая редкость,
 * стандартный fallback-рендер 2D-предмета), см.
 * {@code ItemInspectConfig.InspectGroup.ITEMS_2D}.
 * <p>
 * Названия берутся из lang-файлов по ключу {@code item.<id>.name}, 1:1 по
 * модели с {@link CDiaryItem} (простой translationKey/registryName без
 * префикса домена).
 */
public class ManualItem extends Item {

    public enum ManualType {
        MELEE(0, "manual_melee"),
        FIREARMS(1, "manual_firearms"),
        MEDS(2, "manual_meds"),
        LOCKPICK(3, "manual_lockpick"),
        TOOLS(4, "manual_tools");

        /** Индекс категории в {@code ISinCapability#getLoreBooksProgress()}. */
        public final int categoryIndex;
        public final String id;

        ManualType(int categoryIndex, String id) {
            this.categoryIndex = categoryIndex;
            this.id = id;
        }
    }

    public final ManualType type;

    public ManualItem(ManualType type) {
        this.type = type;
        setMaxStackSize(16);
        setTranslationKey(type.id);
        setRegistryName("mwccf", type.id);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}

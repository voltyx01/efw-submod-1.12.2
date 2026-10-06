package efw.mixin;

import com.voltyx.mwccf.search.EnglishLanguageMap;
import java.util.Locale;
import mezz.jei.gui.ingredients.IIngredientListElement;
import mezz.jei.ingredients.IngredientFilter;
import mezz.jei.suffixtree.GeneralizedSuffixTree;
import mezz.jei.util.Translator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = IngredientFilter.class, remap = false)
public abstract class MixinJEIIngredientFilter {

    @Shadow
    private GeneralizedSuffixTree searchTree;

    @Shadow
    private NonNullList<IIngredientListElement> elementList;

    @Inject(
        method = "addIngredient",
        at = @At(
            value = "INVOKE",
            target = "Lmezz/jei/suffixtree/GeneralizedSuffixTree;put(Ljava/lang/String;I)V",
            ordinal = 0
        ),
        remap = false
    )
    private <V> void onAddIngredientSearchTree(IIngredientListElement<V> element, CallbackInfo ci) {
        try {
            Object ingredient = element.getIngredient();
            if (ingredient instanceof ItemStack) {
                String english = EnglishLanguageMap.getEnglishName((ItemStack) ingredient);
                if (english != null && !english.isEmpty()) {
                    String lowerEnglish = english.toLowerCase(Locale.ENGLISH);
                    String lowerDisplay = Translator.toLowercaseWithLocale(element.getDisplayName());
                    if (!lowerEnglish.equals(lowerDisplay)) {
                        int index = this.elementList.size() - 1;
                        this.searchTree.put(lowerEnglish, index);
                    }
                }
            }
        } catch (Throwable ignored) {}
    }
}

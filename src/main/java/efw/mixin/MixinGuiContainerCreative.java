package efw.mixin;

import com.voltyx.mwccf.search.EnglishLanguageMap;
import java.util.Locale;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainerCreative.class)
public abstract class MixinGuiContainerCreative {

    @Shadow
    private GuiTextField searchField;

    @Inject(method = {"updateFilteredItems", "func_147053_i"}, at = @At("RETURN"))
    private void efw$onUpdateFilteredItems(CallbackInfo ci) {
        if (this.searchField == null) return;
        String query = this.searchField.getText().trim().toLowerCase(Locale.ROOT);
        if (query.isEmpty()) return;

        GuiContainerCreative self = (GuiContainerCreative) (Object) this;
        if (self.inventorySlots instanceof GuiContainerCreative.ContainerCreative) {
            GuiContainerCreative.ContainerCreative container = (GuiContainerCreative.ContainerCreative) self.inventorySlots;
            NonNullList<ItemStack> allItems = NonNullList.create();
            for (Item item : Item.REGISTRY) {
                try {
                    item.getSubItems(CreativeTabs.SEARCH, allItems);
                } catch (Throwable ignored) {}
            }
            for (ItemStack stack : allItems) {
                if (stack != null && !stack.isEmpty()) {
                    String english = EnglishLanguageMap.getEnglishName(stack);
                    if (english != null && english.toLowerCase(Locale.ROOT).contains(query)) {
                        boolean alreadyPresent = false;
                        for (ItemStack existing : container.itemList) {
                            if (ItemStack.areItemStacksEqual(existing, stack)) {
                                alreadyPresent = true;
                                break;
                            }
                        }
                        if (!alreadyPresent) {
                            container.itemList.add(stack);
                        }
                    }
                }
            }
            container.scrollTo(0.0F);
        }
    }
}

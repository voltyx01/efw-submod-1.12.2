package efw.mixin;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] ItemMixin class loaded!");
    }

    @Inject(method = "addInformation", at = @At("RETURN"))
    private void changeTooltipColor(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn, CallbackInfo ci) {
        // Р›РѕРіРёРєР° РґР»СЏ РёР·РјРµРЅРµРЅРёСЏ С†РІРµС‚Р°
        for (int i = 0; i < tooltip.size(); i++) {
            String line = tooltip.get(i);
            if (line.contains(TextFormatting.GREEN.toString())) {
                tooltip.set(i, line.replace(TextFormatting.GREEN.toString(), TextFormatting.RED.toString()));
            }
        }
    }
}
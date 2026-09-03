package efw.mixin;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.quark.oddities.item.ItemBackpack;
import org.spongepowered.asm.mixin.Pseudo;
@Pseudo
@Mixin(Slot.class)
public class MixinPreventBackpackEquip {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinPreventBackpackEquip class loaded!");
    }

    @Inject(method = "isItemValid", at = @At("HEAD"), cancellable = true)
    private void preventBackpackEquip(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        // РџСЂРѕРІРµСЂСЏРµРј, С‡С‚Рѕ СЌС‚РѕС‚ СЃР»РѕС‚ вЂ” СЃР»РѕС‚ РЅР°РіСЂСѓРґРЅРёРєР°
        // Р”Р»СЏ СЌС‚РѕРіРѕ СЃРјРѕС‚СЂРёРј, РµСЃР»Рё Сѓ СЃР»РѕС‚Р° РµСЃС‚СЊ РјРµС‚РѕРґ getSlotIndex() - РёРЅРґРµРєСЃ СЃР»РѕС‚Р° РІ РёРЅРІРµРЅС‚Р°СЂРµ.
        // Р’ РёРЅРІРµРЅС‚Р°СЂРµ РёРіСЂРѕРєР° СЃР»РѕС‚ РЅР°РіСЂСѓРґРЅРёРєР° РѕР±С‹С‡РЅРѕ РёРЅРґРµРєСЃ 6 (РјРѕР¶РµС‚ РѕС‚Р»РёС‡Р°С‚СЊСЃСЏ, РЅСѓР¶РЅРѕ РїСЂРѕРІРµСЂРёС‚СЊ).

        int slotIndex = ((Slot)(Object)this).getSlotIndex();

        // Р’ InventoryPlayer (1.12.2) РЅР°РіСЂСѓРґРЅРёРє вЂ” СЃР»РѕС‚ СЃ РёРЅРґРµРєСЃРѕРј 6 (0-3: РіРѕСЂСЏС‡Р°СЏ РїР°РЅРµР»СЊ, 4-7: Р±СЂРѕРЅСЏ)
        // РќРѕ Р»СѓС‡С€Рµ РїСЂРѕРІРµСЂРёС‚СЊ.

        if (slotIndex == 38) { 
            if (stack.getItem() instanceof ItemBackpack) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}

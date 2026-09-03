package efw.mixin;

import com.paneedah.weaponlib.Weapon;
import com.paneedah.weaponlib.config.BalancePackManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Mixin(Slot.class)
public abstract class InventoryPlayerMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] InventoryPlayerMixin class loaded!");
    }

    @Shadow
    public IInventory inventory;

    @Shadow
    public int slotIndex;

    @Inject(method = "isItemValid", at = @At("HEAD"), cancellable = true)
    private void onIsItemValid(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!(inventory instanceof InventoryPlayer)) {
            return;
        }

        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof Weapon)) {
            return;
        }

        Weapon weapon = (Weapon) stack.getItem();
        int targetSlot = this.slotIndex;

        // РЎР»РѕС‚ 40 РІ InventoryPlayer вЂ” СЌС‚Рѕ СЃР»РѕС‚ Р»РµРІРѕР№ СЂСѓРєРё (Offhand). РћСЂСѓР¶РёРµ MWC С‚СѓРґР° РєР»Р°СЃС‚СЊ РЅРµР»СЊР·СЏ!
        if (targetSlot == 40) {
            cir.setReturnValue(false);
            return;
        }

        BalancePackManager.GunConfigurationGroup group = weapon.getConfigurationGroup();

        Set<Integer> forbiddenSlots;
        switch (group) {
            case NONE:
                return;

            case HANDGUN:
            case SIDEARM:
            case REVOLVER:
                forbiddenSlots = new HashSet<>(Arrays.asList(0, 2, 3, 4, 5, 6, 7, 8, 40));
                break;

            default:
                forbiddenSlots = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 40));
                break;
        }

        if (forbiddenSlots.contains(slotIndex)) {
            cir.setReturnValue(false);
        }
    }
}

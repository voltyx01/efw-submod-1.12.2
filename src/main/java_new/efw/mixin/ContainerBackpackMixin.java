package efw.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.quark.oddities.inventory.ContainerBackpack;

@Pseudo
@Mixin(ContainerBackpack.class)
public abstract class ContainerBackpackMixin {
    static {
        System.out.println("[EFW-MIXIN-LOAD] ContainerBackpackMixin class loaded!");
    }
    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/NonNullList;get(I)Ljava/lang/Object;",
                    remap = false
            ),
            remap = false
    )
    private Object redirectGetArmor(NonNullList<ItemStack> list, int index, EntityPlayer player) {
        if (index == 2) { // CHEST
            ItemStack chest = list.get(index);
            return com.voltyx.mwccf.backpack.BackpackBaubles.getBackpackStack(chest, player);
        }
        return list.get(index);
    }
}

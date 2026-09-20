package efw.mixin;

import com.voltyx.mwccf.immersiveui.client.VariableStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.inventory.ContainerRepair$2")
public class MixinContainerRepairOutput {

    @Inject(method = "onTake", at = @At("HEAD"))
    public void immersiveui$onTake(EntityPlayer player, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        VariableStorage.shakeScreen = true;
    }
}

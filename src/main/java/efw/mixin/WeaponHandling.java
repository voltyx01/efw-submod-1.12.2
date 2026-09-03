package efw.mixin; // Adjust to your actual mixin package, e.g., com.yourmod.mixins
import com.paneedah.weaponlib.WeaponEventHandler;
import com.paneedah.weaponlib.Weapon; // Ensure this import is correct
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Pseudo;
@Pseudo
@Mixin(value = com.paneedah.weaponlib.WeaponEventHandler.class)
public abstract class WeaponHandling {
    static {
        System.out.println("[EFW-MIXIN-LOAD] WeaponHandling class loaded!");
    }

    @Inject(method = "onRenderLivingEvent", 
            at = @At(value = "INVOKE", 
                     target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;", // Correct deobfuscated target
                     shift = At.Shift.AFTER), 
            cancellable = true)
    private void mwccf_forceBowAndArrowArmPoseEventHandler(RenderLivingEvent.Pre event, CallbackInfo ci) {
        if (event.isCanceled() || !(event.getEntity() instanceof EntityPlayer)) {
            return;
        }

        ItemStack itemStack = event.getEntity().getHeldItemMainhand(); // Deobfuscated: func_184614_ca() -> getHeldItemMainhand()
        
        if (itemStack != null && itemStack.getItem() instanceof Weapon) { 
            RenderPlayer rp = (RenderPlayer) event.getRenderer();
            
            (rp.getMainModel()).leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW; 
            (rp.getMainModel()).rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW; 
            
            ci.cancel(); // Cancel the original method's execution
        }
    }
}
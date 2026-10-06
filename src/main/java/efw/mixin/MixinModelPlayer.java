package efw.mixin;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelPlayer.class)
public class MixinModelPlayer {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
    public boolean redirectIsSneakingInPlayerRender(Entity entity) {
        if (entity instanceof EntityPlayer && efw.AnimationTickHandler.isBetterCombatAttackActive((EntityPlayer) entity)) {
            return false;
        }
        return entity.isSneaking();
    }
}

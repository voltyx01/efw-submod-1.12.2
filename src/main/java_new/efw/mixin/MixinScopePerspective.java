package efw.mixin;

import com.paneedah.weaponlib.electronics.ScopePerspective;
import com.paneedah.weaponlib.RenderContext;
import com.paneedah.weaponlib.RenderableState;
import com.paneedah.weaponlib.perspective.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.lwjgl.opengl.GL11;

@Mixin(value = ScopePerspective.class, remap = false)
public class MixinScopePerspective {

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V",
            ordinal = 0,
            remap = false
        ),
        remap = false
    )
    private void onBindScopeTexture(int target, int texture) {
        if (texture <= 0) {
            GL11.glBindTexture(target, 0);
        } else {
            GL11.glBindTexture(target, texture);
        }
    }

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lcom/paneedah/weaponlib/perspective/Perspective;getTexture(Lcom/paneedah/weaponlib/RenderContext;)I",
            remap = false
        ),
        remap = false
    )
    private int onGetPerspectiveTexture(Perspective<RenderableState> instance, RenderContext<RenderableState> context) {
        try {
            int texId = instance.getTexture(context);
            if (texId > 0) {
                return texId;
            }
        } catch (Throwable ignored) {}
        return 0;
    }
}

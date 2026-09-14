package efw.mixin;

import com.paneedah.weaponlib.electronics.ScopePerspective;
import com.paneedah.weaponlib.RenderContext;
import com.paneedah.weaponlib.RenderableState;
import com.paneedah.weaponlib.perspective.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Patches ScopePerspective.render() for MWC 0.1.9 compatibility.
 *
 * History:
 *  - onBindScopeTexture: REMOVED — GL11.glBindTexture is no longer called directly in 0.1.9
 *    (texture binding moved to TextureManager). Caused critical Mixin injection failure (0/1 targets).
 *  - onRenderReturn: REMOVED — MWC 0.1.9 already does full GL state cleanup via glPopAttrib +
 *    glPopMatrix + manual texture/blend/alpha restore. Our duplicate cleanup after theirs was
 *    corrupting subsequent HUD rendering (black-and-white, duplicated HUD).
 */
@Mixin(value = ScopePerspective.class, remap = false)
public class MixinScopePerspective {

    /**
     * Null-guards the perspective texture ID returned by the scope lens.
     * Prevents NPE/GL errors when a scope has no texture configured.
     */
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

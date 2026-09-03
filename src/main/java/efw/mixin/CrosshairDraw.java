package efw.mixin;

import com.paneedah.weaponlib.ClientModContext;
import com.paneedah.weaponlib.CustomGui;
import com.paneedah.weaponlib.PlayerWeaponInstance;
import com.paneedah.weaponlib.WeaponState;
import efw.util.ShoulderSurfingCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = { "com.paneedah.weaponlib.CustomGui" })
public abstract class CrosshairDraw {
    static {
        System.out.println("[EFW-MIXIN-LOAD] CrosshairDraw class loaded!");
    }
    private final Minecraft mc = Minecraft.getMinecraft();

    private final ResourceLocation reloadLogo = new ResourceLocation("mwccf", "textures/reloadlogo.png");

    private long inspectStartTime = -1L;

    private static final long INSPECT_DURATION_TICKS = 70L;

    @Shadow(remap = false)
    public abstract void handleAmmoCounter(RenderGameOverlayEvent.Pre paramPre,
            PlayerWeaponInstance paramPlayerWeaponInstance, double paramDouble1, double paramDouble2);

    private boolean isInspectingExtended(PlayerWeaponInstance weaponInstance) {
        if (weaponInstance == null)
            return false;
        WeaponState state = (WeaponState) weaponInstance.getState();
        if (state == WeaponState.INSPECTING && this.inspectStartTime == -1L)
            this.inspectStartTime = this.mc.world.getTotalWorldTime();
        if (this.inspectStartTime != -1L) {
            long elapsed = this.mc.world.getTotalWorldTime() - this.inspectStartTime;
            if (elapsed < 70L)
                return true;
            this.inspectStartTime = -1L;
        }
        return false;
    }

    @Inject(method = { "onRenderHud" }, at = { @At("HEAD") }, remap = false)
    private void handleAllHudElements(RenderGameOverlayEvent.Pre event, CallbackInfo ci) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR)
            return;
        PlayerWeaponInstance weaponInstance = (PlayerWeaponInstance) ClientModContext.getContext()
                .getPlayerItemInstanceRegistry()
                .getMainHandItemInstance((EntityLivingBase) this.mc.player, PlayerWeaponInstance.class);
        if (weaponInstance != null) {
            WeaponState s = (WeaponState) weaponInstance.getState();
            boolean isModifying = (CustomGui.isInModifyingState(weaponInstance)
                    || CustomGui.isInAltModifyingState(weaponInstance));
            boolean isShiftRightClick = (this.mc.player.isSneaking() && Mouse.isButtonDown(1));
            ShoulderSurfingCompat.updateShoulderSurfingLogic(isModifying, isShiftRightClick);

            if (isReloadingState(s)) {
                ScaledResolution scaledResolution = new ScaledResolution(this.mc);
                int ammoX = scaledResolution.getScaledWidth() - 50;
                int ammoY = scaledResolution.getScaledHeight() - 20;
                drawReloadLogo(ammoX, ammoY);
            }
            if (CustomGui.isInModifyingState(weaponInstance) || CustomGui.isInAltModifyingState(weaponInstance))
                return;
            ScaledResolution sr = new ScaledResolution(this.mc);
            GlStateManager.pushMatrix();
            handleAmmoCounter(event, weaponInstance, sr.getScaledWidth_double(), sr.getScaledHeight_double());
            GlStateManager.popMatrix();
        } else if (ShoulderSurfingCompat.isAutoSwitched()) {
            ShoulderSurfingCompat.resetCamera();
        }
    }

    private void drawReloadLogo(int ammoX, int ammoY) {
        this.mc.getTextureManager().bindTexture(this.reloadLogo);
        int width = 16, height = 16;
        int offsetX = -52, offsetY = 40;
        GlStateManager.pushMatrix();
        GlStateManager.translate((ammoX + offsetX), (ammoY - offsetY), 0.0D);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, width, height, width, height);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private boolean isReloadingState(WeaponState state) {
        switch (state) {
            case TACTICAL_RELOAD:
            case COMPOUND_RELOAD:
            case COMPOUND_RELOAD_EMPTY:
            case COMPOUND_RELOAD_FINISH:
            case COMPOUND_RELOAD_FINISHED:
            case LOAD:
            case LOAD_ITERATION:
            case LOAD_ITERATION_COMPLETED:
            case ALL_LOAD_ITERATIONS_COMPLETED:
            case AWAIT_FURTHER_LOAD_INSTRUCTIONS:
            case UNLOAD_PREPARING:
            case UNLOAD_REQUESTED:
            case UNLOAD:
                return true;
        }
        return false;
    }

    @Inject(method = { "onRenderCrosshair" }, at = { @At("HEAD") }, cancellable = true, remap = false)
    private void handleCrosshairVisibility(RenderGameOverlayEvent.Pre event, CallbackInfo ci) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS)
            return;

        PlayerWeaponInstance weaponInstance = (PlayerWeaponInstance) ClientModContext.getContext()
                .getPlayerItemInstanceRegistry()
                .getMainHandItemInstance((EntityLivingBase) this.mc.player, PlayerWeaponInstance.class);

        if (weaponInstance == null && this.mc.player != null && this.mc.player.getHeldItemMainhand().getItem() instanceof com.paneedah.weaponlib.Weapon) {
            weaponInstance = (PlayerWeaponInstance) com.paneedah.weaponlib.Tags.getInstance(this.mc.player.getHeldItemMainhand());
        }

        if (weaponInstance != null) {
            if (shouldHideCrosshair(weaponInstance)) {
                event.setCanceled(true); // Явно скрываем ванильный кроссхейр!
            }
            ci.cancel(); // Отменяем оригинальный метод CustomGui, предотвращая конфликты
        }
    }

    private boolean shouldHideCrosshair(PlayerWeaponInstance weaponInstance) {
        if (ShoulderSurfingCompat.doShoulderSurfing())
            return false;
        if (weaponInstance == null)
            return false;
        if (isInspectingExtended(weaponInstance))
            return true;
        boolean isAiming = weaponInstance.isAimed() || (Mouse.isButtonDown(1) && this.mc.currentScreen == null);
        boolean isSprinting = this.mc.player.isSprinting();
        WeaponState s = (WeaponState) weaponInstance.getState();
        boolean isActionState = (s == WeaponState.READY || s == WeaponState.FIRING || s == WeaponState.RECOILED
                || s == WeaponState.PAUSED || s == WeaponState.DRAWING);
        return isSprinting || (isAiming && this.mc.gameSettings.thirdPersonView == 0) || !isActionState;
    }

    @Inject(method = { "handleAmmoCounter" }, at = { @At("HEAD") }, cancellable = true, remap = false)
    private void cancelOriginalAmmoCounter(RenderGameOverlayEvent.Pre event, PlayerWeaponInstance weaponInstance,
            double sw, double sh, CallbackInfo ci) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR)
            ci.cancel();
    }
}
package efw.mixin;

import com.voltyx.mwccf.immersiveui.ImmersiveUIConfig;
import com.voltyx.mwccf.immersiveui.system.particles.ParticleStorage;
import com.voltyx.mwccf.immersiveui.system.particles.data.GalacticParticleData;
import com.voltyx.mwccf.immersiveui.system.particles.data.ParticleEmitter;
import com.voltyx.mwccf.immersiveui.util.Vector2f;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiEnchantment.class)
public abstract class MixinGuiEnchantment extends GuiContainer {

    @Shadow
    @Final
    private ContainerEnchantment container;

    public MixinGuiEnchantment(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;sendEnchantPacket(II)V"))
    public void immersiveui$onEnchantClick(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        if (!ImmersiveUIConfig.enableEnchantParticles) return;

        Slot slot = this.container.getSlot(0);
        if (slot == null) return;

        ParticleEmitter emitter = new ParticleEmitter(this.guiLeft + slot.xPos, this.guiTop + slot.yPos);

        for (int i = 0; i < 8; i++) {
            float angle = i * 45.0F;

            GalacticParticleData p1 = new GalacticParticleData(3.0F, 52 + i, this.guiLeft + slot.xPos + 8, this.guiTop + slot.yPos + 8, emitter);
            p1.direction = Vector2f.rotate(new Vector2f(0.0F, 1.0F), angle);
            p1.size *= 0.75F;
            p1.speed = 1.5F;
            p1.angularVelocity = -8.0F;
            ParticleStorage.addParticle(emitter, p1);

            GalacticParticleData p2 = new GalacticParticleData(3.0F, 60 + i, this.guiLeft + slot.xPos + 8, this.guiTop + slot.yPos + 8, emitter);
            p2.direction = Vector2f.rotate(new Vector2f(0.0F, 1.0F), angle);
            p2.speed = 0.75F;
            ParticleStorage.addParticle(emitter, p2);
        }
    }
}

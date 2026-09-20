package efw.mixin;

import com.voltyx.mwccf.immersiveui.system.particles.ParticleStorage;
import com.voltyx.mwccf.immersiveui.system.particles.data.FlameParticleData;
import com.voltyx.mwccf.immersiveui.system.particles.data.ParticleEmitter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(GuiFurnace.class)
public abstract class MixinGuiFurnace extends GuiContainer {

    @Shadow
    @Final
    private IInventory tileFurnace;

    @Shadow
    protected abstract int getBurnLeftScaled(int pixels);

    @Unique
    private boolean immersiveui$shouldBurst = false;

    public MixinGuiFurnace(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void immersiveui$onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        boolean isBurning = false;
        if (this.tileFurnace instanceof TileEntityFurnace) {
            isBurning = ((TileEntityFurnace) this.tileFurnace).isBurning();
        } else {
            isBurning = TileEntityFurnace.isBurning(this.tileFurnace);
        }

        if (!isBurning) {
            immersiveui$shouldBurst = false;
            return;
        }

        int burnScaled = this.getBurnLeftScaled(13);
        Random random = new Random();

        if (burnScaled == 12 && !immersiveui$shouldBurst) {
            immersiveui$shouldBurst = true;
            ParticleEmitter emitter = new ParticleEmitter(this.guiLeft, this.guiTop);

            for (int i = 0; i < 8; i++) {
                float startX = this.guiLeft + 56 + 8 + random.nextInt(13) - 6;
                float startY = this.guiTop + 36 + 10 + random.nextInt(13) - 6;
                FlameParticleData particle = new FlameParticleData(startX, startY, random.nextInt(21) + 30, emitter);
                ParticleStorage.addParticle(emitter, particle);
            }
        } else if (burnScaled != 12 && immersiveui$shouldBurst) {
            immersiveui$shouldBurst = false;
        }
    }
}

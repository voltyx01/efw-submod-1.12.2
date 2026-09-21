package efw.mixin;

import com.voltyx.mwccf.immersiveui.ImmersiveUIConfig;
import com.voltyx.mwccf.immersiveui.client.VariableStorage;
import com.voltyx.mwccf.immersiveui.system.particles.ParticleStorage;
import com.voltyx.mwccf.immersiveui.system.particles.data.GenericParticleData;
import com.voltyx.mwccf.immersiveui.system.particles.data.ParticleData;
import com.voltyx.mwccf.immersiveui.system.particles.data.ParticleEmitter;
import com.voltyx.mwccf.immersiveui.util.CommonCode;
import com.voltyx.mwccf.immersiveui.util.Vector2f;
import com.voltyx.mwccf.immersiveui.nea.animations.ItemMoveAnimation;
import com.voltyx.mwccf.immersiveui.nea.animations.ItemPickupThrowAnimation;
import com.voltyx.mwccf.immersiveui.nea.api.IAnimatedScreen;
import com.voltyx.mwccf.immersiveui.nea.api.IItemLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends GuiScreen implements IAnimatedScreen {

    @Shadow
    protected int guiLeft;

    @Shadow
    protected int guiTop;

    @Shadow
    protected int xSize;

    @Shadow
    protected int ySize;

    @Shadow
    private Slot hoveredSlot;

    @Shadow
    private ItemStack draggedStack;

    @Shadow
    protected abstract boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY);

    @Unique
    private final Random immersiveui$random = new Random();

    @Unique
    private final Map<Slot, Float> immersiveui$expandingProgress = new HashMap<>();

    @Unique
    private float immersiveui$deltaX = 0.0F;

    @Unique
    private float immersiveui$deltaY = 0.0F;

    @Unique
    private int immersiveui$oX = Integer.MIN_VALUE;

    @Unique
    private int immersiveui$oY = Integer.MIN_VALUE;

    @Unique
    private int immersiveui$mouseX = 0;

    @Unique
    private int immersiveui$mouseY = 0;

    @Unique
    private long immersiveui$lastNanoTime = 0L;

    @Unique
    private float immersiveui$deltaTime = 0.016F;

    @Unique
    private float immersiveui$currentAngle = 0.0F;

    @Unique
    private float immersiveui$currentAngleVelocity = 0.0F;

    @Unique
    private float immersiveui$timer = 0.0F;

    @Inject(method = "initGui", at = @At("HEAD"))
    public void immersiveui$onInitGui(CallbackInfo ci) {
        immersiveui$oX = Integer.MIN_VALUE;
        immersiveui$oY = Integer.MIN_VALUE;
        immersiveui$lastNanoTime = 0L;
        immersiveui$deltaTime = 0.016F;
        immersiveui$currentAngle = 0.0F;
        immersiveui$currentAngleVelocity = 0.0F;
        immersiveui$expandingProgress.clear();
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void immersiveui$onDrawScreenHead(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.immersiveui$mouseX = mouseX;
        this.immersiveui$mouseY = mouseY;

        long now = System.nanoTime();
        if (immersiveui$lastNanoTime > 0L) {
            immersiveui$deltaTime = (now - immersiveui$lastNanoTime) / 1_000_000_000.0F;
            if (immersiveui$deltaTime <= 0.0F || immersiveui$deltaTime > 0.1F) {
                immersiveui$deltaTime = 0.016F;
            }
        } else {
            immersiveui$deltaTime = 0.016F;
        }
        immersiveui$lastNanoTime = now;

        if (VariableStorage.shakeScreen) {
            VariableStorage.shakeScreen = false;
            immersiveui$timer = ImmersiveUIConfig.shakeTimer;
        }

        if (ImmersiveUIConfig.enableScreenShake && immersiveui$timer > 0) {
            immersiveui$timer = Math.max(0, immersiveui$timer - immersiveui$deltaTime * 20.0F);
            float ratio = immersiveui$timer / (float) ImmersiveUIConfig.shakeTimer;
            float shakeX = (immersiveui$random.nextFloat() * 2.0F - 1.0F) * ImmersiveUIConfig.shakeAmplitude * ratio;
            float shakeY = (immersiveui$random.nextFloat() * 2.0F - 1.0F) * ImmersiveUIConfig.shakeAmplitude * ratio;
            GlStateManager.translate(shakeX, shakeY, 0.0F);
        }

        if (immersiveui$oX != Integer.MIN_VALUE && immersiveui$oY != Integer.MIN_VALUE) {
            immersiveui$deltaX = (immersiveui$oX - mouseX) / (immersiveui$deltaTime * 20.0F);
            immersiveui$deltaY = (immersiveui$oY - mouseY) / (immersiveui$deltaTime * 20.0F);
        }
    }

    @Inject(method = "drawSlot", at = @At("HEAD"))
    public void immersiveui$beforeDrawSlot(Slot slot, CallbackInfo ci) {
        boolean isHovered = slot.isEnabled() && this.isPointInRegion(slot.xPos, slot.yPos, 16, 16, this.immersiveui$mouseX, this.immersiveui$mouseY);
        if (isHovered) {
            this.hoveredSlot = slot;
        }
        GlStateManager.pushMatrix();
        CommonCode.floatingRenderSize(slot, isHovered, immersiveui$expandingProgress, immersiveui$deltaTime);
    }

    @Inject(method = "drawSlot", at = @At("RETURN"))
    public void immersiveui$afterDrawSlot(Slot slot, CallbackInfo ci) {
        GlStateManager.popMatrix();
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGradientRect(IIIIII)V"))
    private void immersiveui$redirectDrawGradientRect(GuiContainer instance, int left, int top, int right, int bottom, int startColor, int endColor) {
        if (ImmersiveUIConfig.enableVanillaSlotHighlighting) {
            this.drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }

    @Inject(method = "drawItemStack", at = @At("HEAD"), cancellable = true)
    private void immersiveui$drawItemStack(ItemStack stack, int x, int y, String altText, CallbackInfo ci) {
        // Intercept rendering floating item on cursor to apply rotation, scale and rarity particles
        if (stack == null || stack.isEmpty()) {
            return;
        }

        float scale = ImmersiveUIConfig.hoveredItemScale;
        float deltaTime = immersiveui$deltaTime;
        float amplitude = ImmersiveUIConfig.floatingItemRotationAmplitude;
        float easingSpeed = ImmersiveUIConfig.floatingItemEasingSpeed;
        float inertiaDamping = 0.75F;

        if (immersiveui$oX != Integer.MIN_VALUE && immersiveui$oY != Integer.MIN_VALUE) {
            float targetAngle = MathHelper.clamp(-immersiveui$deltaX / 8.0F * amplitude, -1.5707963F / (2.0F / amplitude), 1.5707963F / (2.0F / amplitude));
            immersiveui$currentAngleVelocity += (targetAngle - immersiveui$currentAngle) * easingSpeed * deltaTime;
        }

        immersiveui$currentAngle = MathHelper.clamp(immersiveui$currentAngle + immersiveui$currentAngleVelocity * deltaTime, -1.5707963F / (2.0F / amplitude), 1.5707963F / (2.0F / amplitude));
        immersiveui$currentAngleVelocity = immersiveui$currentAngleVelocity * (float) Math.pow(inertiaDamping, deltaTime);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 8.0F, y + 8.0F, 232.0F);
        GlStateManager.scale(scale, scale, 1.0F);

        if (ImmersiveUIConfig.enableFloatingItemRotation) {
            float rot = Math.abs(immersiveui$currentAngle) > 0.01F ? (float) Math.toDegrees(immersiveui$currentAngle) : 0.0F;
            GlStateManager.rotate(rot, 0.0F, 0.0F, 1.0F);
        }

        // Item & Rarity particles
        if (ImmersiveUIConfig.enableRarityParticles) {
            String overrideType = null;
            if (stack.getItem() != null && stack.getItem().getRegistryName() != null) {
                String regName = stack.getItem().getRegistryName().toString().toLowerCase();
                overrideType = ImmersiveUIConfig.itemParticleOverrides.get(regName);
            }

            boolean shouldSpawn = overrideType != null || stack.getRarity() != EnumRarity.COMMON;
            if (shouldSpawn) {
                ParticleEmitter emitter = new ParticleEmitter(this.immersiveui$mouseX, this.immersiveui$mouseY);
                if (!ParticleStorage.EMITTERS.containsKey(emitter) && (Math.abs(immersiveui$deltaX) > 0.1F || Math.abs(immersiveui$deltaY) > 0.1F)) {
                    float moveSpeed = (Math.abs(immersiveui$deltaY) + Math.abs(immersiveui$deltaX)) * ImmersiveUIConfig.particleSpeedMultiplier;
                    int minLife = Math.max(1, ImmersiveUIConfig.particleLifetimeMin);
                    int maxLife = Math.max(minLife, ImmersiveUIConfig.particleLifetimeMax);
                    int lifetime = minLife + immersiveui$random.nextInt(maxLife - minLife + 1);

                    float posX = this.immersiveui$mouseX + immersiveui$random.nextFloat() * 2.0F - 1.0F;
                    float posY = this.immersiveui$mouseY + immersiveui$random.nextFloat() * 2.0F - 1.0F;
                    Vector2f moveDir = new Vector2f(immersiveui$deltaX, immersiveui$deltaY).normalize();

                    ParticleData particle = null;

                    ItemStack crackStack = null;
                    if ("item".equalsIgnoreCase(overrideType) || "self".equalsIgnoreCase(overrideType)) {
                        crackStack = stack;
                    } else if (overrideType != null && (overrideType.contains(":") || net.minecraft.item.Item.REGISTRY.containsKey(new net.minecraft.util.ResourceLocation(overrideType)))) {
                        net.minecraft.item.Item targetItem = net.minecraft.item.Item.REGISTRY.getObject(new net.minecraft.util.ResourceLocation(overrideType));
                        if (targetItem != null) {
                            crackStack = new ItemStack(targetItem);
                        }
                    }

                    if (crackStack != null) {
                        net.minecraft.client.renderer.block.model.IBakedModel model =
                                Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(crackStack, this.mc.world, this.mc.player);
                        net.minecraft.client.renderer.texture.TextureAtlasSprite sprite = model != null ? model.getParticleTexture() : null;
                        if (sprite != null) {
                            com.voltyx.mwccf.immersiveui.system.particles.data.ItemCrackParticleData icp =
                                    new com.voltyx.mwccf.immersiveui.system.particles.data.ItemCrackParticleData(sprite, moveSpeed, lifetime, posX, posY, emitter);
                            icp.direction = moveDir;
                            particle = icp;
                        }
                    } else if ("flame".equalsIgnoreCase(overrideType) || "fire".equalsIgnoreCase(overrideType)) {
                        com.voltyx.mwccf.immersiveui.system.particles.data.FlameParticleData fp =
                                new com.voltyx.mwccf.immersiveui.system.particles.data.FlameParticleData(posX, posY, lifetime, emitter);
                        fp.speed = moveSpeed;
                        fp.direction = moveDir;
                        particle = fp;
                    } else if ("galactic".equalsIgnoreCase(overrideType) || "enchant".equalsIgnoreCase(overrideType)) {
                        com.voltyx.mwccf.immersiveui.system.particles.data.GalacticParticleData gp =
                                new com.voltyx.mwccf.immersiveui.system.particles.data.GalacticParticleData(moveSpeed, lifetime, posX, posY, emitter);
                        gp.direction = moveDir;
                        particle = gp;
                    } else {
                        int color = 0xFFFFFF;
                        if (overrideType != null) {
                            if ("epic".equalsIgnoreCase(overrideType) || "purple".equalsIgnoreCase(overrideType)) color = 0xFF55FF;
                            else if ("rare".equalsIgnoreCase(overrideType) || "aqua".equalsIgnoreCase(overrideType) || "cyan".equalsIgnoreCase(overrideType)) color = 0x55FFFF;
                            else if ("uncommon".equalsIgnoreCase(overrideType) || "yellow".equalsIgnoreCase(overrideType)) color = 0xFFFF55;
                            else if ("common".equalsIgnoreCase(overrideType) || "white".equalsIgnoreCase(overrideType)) color = 0xFFFFFF;
                            else if ("red".equalsIgnoreCase(overrideType)) color = 0xFF5555;
                            else if ("green".equalsIgnoreCase(overrideType)) color = 0x55FF55;
                            else if ("blue".equalsIgnoreCase(overrideType)) color = 0x5555FF;
                            else if ("gold".equalsIgnoreCase(overrideType) || "orange".equalsIgnoreCase(overrideType)) color = 0xFFAA00;
                            else {
                                try {
                                    String hex = overrideType.startsWith("#") ? overrideType.substring(1) :
                                            (overrideType.startsWith("0x") ? overrideType.substring(2) : overrideType);
                                    color = (int) Long.parseLong(hex, 16) & 0xFFFFFF;
                                } catch (Exception ignored) {
                                    color = 0xFFFFFF;
                                }
                            }
                        } else {
                            if (stack.getRarity() == EnumRarity.UNCOMMON) color = 0xFFFF55;
                            else if (stack.getRarity() == EnumRarity.RARE) color = 0x55FFFF;
                            else if (stack.getRarity() == EnumRarity.EPIC) color = 0xFF55FF;
                        }

                        particle = new GenericParticleData(
                                0xFF000000 | color,
                                0x00000000 | color,
                                moveSpeed,
                                posX,
                                posY,
                                immersiveui$random.nextFloat() * 0.4F + 0.6F,
                                lifetime,
                                emitter
                        );
                        particle.direction = moveDir;
                    }

                    if (particle != null) {
                        ParticleStorage.addParticle(emitter, particle);
                    }
                }
            }
        }

        this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, stack, -8, -8);
        this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, stack, -8, -8, altText);
        GlStateManager.popMatrix();

        ci.cancel();
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    public void immersiveui$onDrawScreenTail(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ParticleStorage.renderAll(partialTicks);
        immersiveui$oX = mouseX;
        immersiveui$oY = mouseY;
    }

    @Redirect(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Slot;getStack()Lnet/minecraft/item/ItemStack;"))
    private ItemStack immersiveui$getSlotStack(Slot slot) {
        if (ImmersiveUIConfig.moveAnimationTime > 0) {
            ItemStack virtual = ItemMoveAnimation.getVirtualStack((GuiContainer) (Object) this, slot);
            if (virtual != null) {
                return virtual;
            }
        }
        return slot.getStack();
    }

    @Inject(method = "drawScreen",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V", shift = At.Shift.BEFORE))
    public void immersiveui$drawMovingItems(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.zLevel = 200;
        this.itemRender.zLevel = 200;
        ItemPickupThrowAnimation.drawIndependentAnimations((GuiContainer) (Object) this, this.itemRender, this.fontRenderer);
        ItemMoveAnimation.drawAnimations(this.itemRender, this.fontRenderer);
        this.itemRender.zLevel = 0;
        this.zLevel = 0;
    }

    @ModifyArg(method = "drawScreen",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawItemStack(Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
                        ordinal = 0),
               index = 0)
    public ItemStack immersiveui$injectVirtualCursorStack(ItemStack stack) {
        if (ImmersiveUIConfig.moveAnimationTime > 0) {
            ItemStack virtual = ItemMoveAnimation.getVirtualStack((GuiContainer) (Object) this, IItemLocation.CURSOR);
            return virtual == null ? stack : virtual;
        }
        return stack;
    }

    @Override
    public int nea$getX() {
        return this.guiLeft;
    }

    @Override
    public int nea$getY() {
        return this.guiTop;
    }

    @Override
    public int nea$getWidth() {
        return this.xSize;
    }

    @Override
    public int nea$getHeight() {
        return this.ySize;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ParticleStorage.clear();
    }
}

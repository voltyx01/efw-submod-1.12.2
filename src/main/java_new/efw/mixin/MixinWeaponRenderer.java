package efw.mixin;

import com.paneedah.weaponlib.WeaponRenderer;
import com.paneedah.weaponlib.animation.Transition;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {WeaponRenderer.class}, remap = false)
public class MixinWeaponRenderer {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinWeaponRenderer class loaded!");
    }

    // РҐСЂР°РЅРёС‚ РїРѕСЃР»РµРґРЅРёР№ РІРѕСЃРїСЂРѕРёР·РІРµРґРµРЅРЅС‹Р№ РїРµСЂРµС…РѕРґ РґР»СЏ РєР°Р¶РґРѕР№ СЃСѓС‰РЅРѕСЃС‚Рё, С‡С‚РѕР±С‹ Р·РІСѓРє РЅРµ Р·Р°С†РёРєР»РёРІР°Р»СЃСЏ
    @Unique
    private static final Map<EntityLivingBase, Transition<?>> lastPlayed = new WeakHashMap<>();

    @Inject(method = {"renderPositioning"}, at = {@At("HEAD")}, remap = false, require = 0)
    private void onRenderPositioning(long transitionStart, long duration, long pause, List<Transition<?>> transitions, Object part, EntityLivingBase entity, ItemStack itemStack, CallbackInfo ci) {
        processSound(entity, transitions);
    }

    @Inject(method = {"render"}, at = {@At("HEAD")}, remap = false, require = 0)
    private void onGeneralRender(ItemStack stack, EntityLivingBase entity, CallbackInfo ci) {
        // Method left open for rendering extensions
    }

    @Unique
    private void processSound(EntityLivingBase entity, List<Transition<?>> transitions) {
        if (transitions == null || transitions.isEmpty() || entity == null) return;

        // Р‘РµСЂРµРј РїРµСЂРІС‹Р№ РїРµСЂРµС…РѕРґ РёР· СЃРїРёСЃРєР°
        Transition<?> current = transitions.get(0);
        SoundEvent sound = current.getSound();

        // Р•СЃР»Рё Сѓ РїРµСЂРµС…РѕРґР° РµСЃС‚СЊ Р·РІСѓРє Рё РѕРЅ РµС‰Рµ РЅРµ Р±С‹Р» РїСЂРѕРёРіСЂР°РЅ РґР»СЏ СЌС‚РѕР№ СЃСѓС‰РЅРѕСЃС‚Рё
        if (sound != null) {
            if (lastPlayed.get(entity) != current) {
                // Р’РѕСЃРїСЂРѕРёР·РІРѕРґРёРј Р·РІСѓРє РІ РїРѕР·РёС†РёРё РёРіСЂРѕРєР°
                entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
                
                // Р—Р°РїРѕРјРёРЅР°РµРј, С‡С‚Рѕ СЌС‚РѕС‚ РїРµСЂРµС…РѕРґ СѓР¶Рµ Р°РєС‚РёРІРёСЂРѕРІР°РЅ
                lastPlayed.put(entity, current);
            }
        }
    }

    @Inject(method = "renderLeftArm(Lnet/minecraft/entity/EntityLivingBase;Lcom/paneedah/weaponlib/RenderContext;Lcom/paneedah/weaponlib/animation/MultipartPositioning$Positioner;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPopMatrix()V", remap = false), remap = false)
    private static void onRenderLeftArmMWC(EntityLivingBase player, com.paneedah.weaponlib.RenderContext renderContext, com.paneedah.weaponlib.animation.MultipartPositioning.Positioner positioner, CallbackInfo ci) {
        if (player instanceof net.minecraft.client.entity.AbstractClientPlayer) {
            net.minecraft.client.entity.AbstractClientPlayer clientPlayer = (net.minecraft.client.entity.AbstractClientPlayer) player;
            if (com.voltyx.mwccf.geo.BraceletUI.hasBraceletEquipped(clientPlayer)) {
                boolean isSlim = "slim".equals(clientPlayer.getSkinType());
                com.voltyx.mwccf.geo.GeoArmorModel bracelet = isSlim ? com.voltyx.mwccf.geo.BraceletInspectHandler.getSlimModel() : com.voltyx.mwccf.geo.BraceletInspectHandler.getNormalModel();
                
                if (bracelet != null) {
                    int currentProgram = org.lwjgl.opengl.GL11.glGetInteger(org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM);
                    org.lwjgl.opengl.GL20.glUseProgram(0);

                    net.minecraft.client.renderer.GlStateManager.pushMatrix();
                    net.minecraft.client.renderer.GlStateManager.setActiveTexture(net.minecraft.client.renderer.OpenGlHelper.defaultTexUnit);
                    org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_TEXTURE_2D);
                    org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_ALPHA_TEST);
                    org.lwjgl.opengl.GL11.glAlphaFunc(org.lwjgl.opengl.GL11.GL_GREATER, 0.1F);
                    org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_BLEND);
                    org.lwjgl.opengl.GL11.glBlendFunc(org.lwjgl.opengl.GL11.GL_SRC_ALPHA, org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA);
                    org.lwjgl.opengl.GL11.glDepthMask(true);
                    net.minecraft.client.Minecraft.getMinecraft().getTextureManager().bindTexture(com.voltyx.mwccf.geo.BraceletInspectHandler.getBraceletTexture());
                    org.lwjgl.opengl.GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                    bracelet.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);

                    net.minecraft.client.model.ModelRenderer arm = isSlim ? ((com.voltyx.mwccf.geo.GeoArmorModel)bracelet).bipedLeftArmSlim : bracelet.bipedLeftArm;

                    arm.rotateAngleX = (float)Math.toRadians(-90.0);
                    arm.rotateAngleY = 0.0f;
                    arm.rotateAngleZ = 0.0f;
                    arm.offsetX = -0.375f;
                    arm.offsetY = -0.125f;
                    arm.offsetZ = -0.15f;
                    
                    arm.rotationPointX = 5.0f;
                    arm.rotationPointY = 2.0f;
                    arm.rotationPointZ = 0.0f;

                    org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_CULL_FACE);
                    arm.render(0.0625F);
                    org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_CULL_FACE);
                    net.minecraft.client.renderer.GlStateManager.popMatrix();
                    
                    org.lwjgl.opengl.GL20.glUseProgram(currentProgram);
                }
            }
        }
    }

    @Inject(method = "renderLeftArm(Lnet/minecraft/client/model/ModelBiped;Lnet/minecraft/client/entity/AbstractClientPlayer;)V", at = @At(value = "TAIL"), remap = false)
    private static void onRenderLeftArmModelBiped(net.minecraft.client.model.ModelBiped modelplayer, net.minecraft.client.entity.AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (com.voltyx.mwccf.geo.BraceletUI.hasBraceletEquipped(clientPlayer)) {
            boolean isSlim = "slim".equals(clientPlayer.getSkinType());
            com.voltyx.mwccf.geo.GeoArmorModel bracelet = isSlim ? com.voltyx.mwccf.geo.BraceletInspectHandler.getSlimModel() : com.voltyx.mwccf.geo.BraceletInspectHandler.getNormalModel();
            
            if (bracelet != null) {
                int currentProgram = org.lwjgl.opengl.GL11.glGetInteger(org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM);
                org.lwjgl.opengl.GL20.glUseProgram(0);

                net.minecraft.client.renderer.GlStateManager.pushMatrix();
                net.minecraft.client.renderer.GlStateManager.setActiveTexture(net.minecraft.client.renderer.OpenGlHelper.defaultTexUnit);
                org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_TEXTURE_2D);
                org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_ALPHA_TEST);
                org.lwjgl.opengl.GL11.glAlphaFunc(org.lwjgl.opengl.GL11.GL_GREATER, 0.1F);
                org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_BLEND);
                org.lwjgl.opengl.GL11.glBlendFunc(org.lwjgl.opengl.GL11.GL_SRC_ALPHA, org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA);
                org.lwjgl.opengl.GL11.glDepthMask(true);
                net.minecraft.client.Minecraft.getMinecraft().getTextureManager().bindTexture(com.voltyx.mwccf.geo.BraceletInspectHandler.getBraceletTexture());
                org.lwjgl.opengl.GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                bracelet.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);

                net.minecraft.client.model.ModelRenderer arm = isSlim ? ((com.voltyx.mwccf.geo.GeoArmorModel)bracelet).bipedLeftArmSlim : bracelet.bipedLeftArm;

                arm.rotateAngleX = (float)Math.toRadians(-90.0);
                arm.rotateAngleY = 0.0f;
                arm.rotateAngleZ = 0.0f;
                arm.offsetX = -0.375f;
                arm.offsetY = -0.125f;
                arm.offsetZ = -0.15f;
                
                arm.rotationPointX = 5.0f;
                arm.rotationPointY = 2.0f;
                arm.rotationPointZ = 0.0f;

                org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_CULL_FACE);
                arm.render(0.0625F);
                org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_CULL_FACE);
                net.minecraft.client.renderer.GlStateManager.popMatrix();
                
                org.lwjgl.opengl.GL20.glUseProgram(currentProgram);
            }
        }
    }
}
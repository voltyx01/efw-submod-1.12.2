package com.voltyx.mwccf.geo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class BraceletInspectHandler {

    public static final KeyBinding INSPECT_KEY = new KeyBinding("key.bracelet.inspect", Keyboard.KEY_V, "key.categories.mwccf");
    
    public static boolean isInspecting = false;
    public static boolean isBackgroundRunning = false;
    public static float inspectProgress = 0.0f;
    public static float prevInspectProgress = 0.0f;
    
    public static float targetX = -0.50f;
    public static float targetY = -0.35f;
    public static float targetZ = -0.30f;
    public static float targetRotX = 80.00f;
    public static float targetRotY = 40.00f;
    public static float targetRotZ = -122.00f;

    public static float mwcBootProgress = 0.0f;
    public static float prevMwcBootProgress = 0.0f;
    
    private static boolean prevActiveState = false;
    
    private static com.voltyx.mwccf.geo.GeoArmorModel braceletModel = null;
    private static com.voltyx.mwccf.geo.GeoArmorModel braceletSlimModel = null;
    private static final net.minecraft.util.ResourceLocation BRACELET_TEXTURE = new net.minecraft.util.ResourceLocation("mwccf", "textures/models/armor/bracelet.png");
    private static final net.minecraft.util.ResourceLocation BRACELET_MODEL_PATH = new net.minecraft.util.ResourceLocation("mwccf", "geo/bracelet.geo.json");
    private static final net.minecraft.util.ResourceLocation BRACELET_SLIM_MODEL_PATH = new net.minecraft.util.ResourceLocation("mwccf", "geo/bracelet_slim.geo.json");

    public static com.voltyx.mwccf.geo.GeoArmorModel getNormalModel() {
        if (braceletModel == null) {
            braceletModel = new com.voltyx.mwccf.geo.GeoArmorModel(BRACELET_MODEL_PATH);
        }
        return braceletModel;
    }

    public static com.voltyx.mwccf.geo.GeoArmorModel getSlimModel() {
        if (braceletSlimModel == null) {
            braceletSlimModel = new com.voltyx.mwccf.geo.GeoArmorModel(BRACELET_SLIM_MODEL_PATH);
        }
        return braceletSlimModel;
    }

    public static net.minecraft.util.ResourceLocation getBraceletTexture() {
        return BRACELET_TEXTURE;
    }


    private static long lastBeatInterval = -1;

    // UI Calibration (for FBO 2D Canvas)
    public static float uiOffsetX = -95.0f;
    public static float uiOffsetY = 0.0f;
    public static float uiScale = 0.50f;
    public static float uiRotZ = 90.0f;

    public static void init() {
        ClientRegistry.registerKeyBinding(INSPECT_KEY);
        net.minecraftforge.client.ClientCommandHandler.instance.registerCommand(new CommandHeartbeat());
        com.voltyx.mwccf.geo.BraceletSettings.init(new java.io.File("config/mwccf_bracelet.cfg"));
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) { 
            int key = Keyboard.getEventKey();

            if (isInspecting && inspectProgress >= 2.0f) {
                // FBO calibration removed
            }

            if (isInspecting && Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.currentScreen == null) {
                    mc.displayGuiScreen(new com.voltyx.mwccf.geo.BraceletSettingsGui());
                    return;
                }
            }

            if (INSPECT_KEY.isPressed()) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.player != null) {
                    if (!com.voltyx.mwccf.geo.BraceletUI.hasBraceletEquipped()) return;
                    
                    ItemStack mainHand = mc.player.getHeldItemMainhand();
                    boolean isMWCWeapon = !mainHand.isEmpty() && efw.animation.WeaponTypeHelper.getWeaponType(mainHand) != efw.animation.WeaponTypeHelper.WeaponType.NONE;
                    if (isMWCWeapon) {
                        // Toggle background state when holding MWC weapon instead of hand inspection
                        if (com.voltyx.mwccf.geo.BraceletUI.hasBattery(mc.player)) {
                            isBackgroundRunning = !isBackgroundRunning;
                            if (isBackgroundRunning) {
                                mwcBootProgress = 0.0f;
                                prevMwcBootProgress = 0.0f;
                                mc.getSoundHandler().playSound(net.minecraft.client.audio.PositionedSoundRecord.getMasterRecord(new net.minecraft.util.SoundEvent(new net.minecraft.util.ResourceLocation("mwccf", "bracelet.displayon")), 1.0F));
                            }
                        }
                        return;
                    }
                }
                isInspecting = !isInspecting;
                if (!isInspecting) {
                    isBackgroundRunning = false; // complete turn off on V press
                }
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player == null || mc.world == null || mc.isGamePaused()) return;

            prevInspectProgress = inspectProgress;
            prevMwcBootProgress = mwcBootProgress;
            
            boolean hasBat = com.voltyx.mwccf.geo.BraceletUI.hasBattery(mc.player);
            
            if (mc.player != null) {
                if (!com.voltyx.mwccf.geo.BraceletUI.hasBraceletEquipped()) {
                    isInspecting = false;
                    isBackgroundRunning = false;
                    mwcBootProgress = 0.0f;
                    inspectProgress = 0.0f;
                } else {
                    if (!hasBat) {
                        isInspecting = false;
                        isBackgroundRunning = false;
                    }

                    ItemStack mainHand = mc.player.getHeldItemMainhand();
                    
                    if (isInspecting && (mc.player.isSwingInProgress || mc.gameSettings.keyBindAttack.isKeyDown() || mc.gameSettings.keyBindUseItem.isKeyDown())) {
                        isInspecting = false;
                        if (hasBat) isBackgroundRunning = true;
                    }
                    
                    boolean isMWCWeapon = !mainHand.isEmpty() && efw.animation.WeaponTypeHelper.getWeaponType(mainHand) != efw.animation.WeaponTypeHelper.WeaponType.NONE;
                    if (isMWCWeapon) {
                        if (isInspecting) {
                            isInspecting = false;
                            if (hasBat) isBackgroundRunning = true;
                        }
                        if (isBackgroundRunning) {
                            mwcBootProgress = Math.min(10.0f, mwcBootProgress + 0.05f);
                        } else {
                            mwcBootProgress = 0.0f;
                        }
                    } else {
                        mwcBootProgress = 0.0f;
                    }
                }
            }

            if (isInspecting) {
                if (inspectProgress < 2.0f && (inspectProgress + 0.15f) >= 2.0f) {
                    boolean isMWC = mc.player != null && !mc.player.getHeldItemMainhand().isEmpty() && efw.animation.WeaponTypeHelper.getWeaponType(mc.player.getHeldItemMainhand()) != efw.animation.WeaponTypeHelper.WeaponType.NONE;
                    if (!isMWC && !isBackgroundRunning && hasBat) {
                        Minecraft.getMinecraft().getSoundHandler().playSound(net.minecraft.client.audio.PositionedSoundRecord.getMasterRecord(new net.minecraft.util.SoundEvent(new net.minecraft.util.ResourceLocation("mwccf", "bracelet.displayon")), 1.0F));
                    }
                }
                if (inspectProgress >= 2.0f) {
                    inspectProgress = Math.min(12.0f, inspectProgress + 0.05f); 
                } else {
                    inspectProgress = Math.min(12.0f, inspectProgress + 0.15f); 
                }
            } else {
                if (inspectProgress > 2.0f) {
                    inspectProgress = 2.0f;
                    prevInspectProgress = 2.0f;
                }
                inspectProgress = Math.max(0.0f, inspectProgress - 0.20f); // 10 ticks = 0.5s smooth retraction
            }
            
            // Heartbeat logic
            float currentBootProgress = 0.0f;
            boolean holdingMWCWeapon = mc.player != null && !mc.player.getHeldItemMainhand().isEmpty() && efw.animation.WeaponTypeHelper.getWeaponType(mc.player.getHeldItemMainhand()) != efw.animation.WeaponTypeHelper.WeaponType.NONE;
            if (holdingMWCWeapon && hasBat && isBackgroundRunning) {
                currentBootProgress = mwcBootProgress;
            } else if (isInspecting && inspectProgress >= 2.0f && hasBat) {
                currentBootProgress = inspectProgress - 2.0f;
            }

            HeartbeatManager.update(currentBootProgress > 0.65f, holdingMWCWeapon && isBackgroundRunning, isBackgroundRunning);
            
            // Packet tracking
            boolean currentState = hasBat && (isInspecting || isBackgroundRunning);
            float currentBpm = HeartbeatManager.currentBPM;
            if (currentState != prevActiveState || (currentState && mc.player != null && mc.player.ticksExisted % 20 == 0)) {
                prevActiveState = currentState;
                com.voltyx.mwccf.MwccfMod.PACKET_HANDLER.sendToServer(new com.voltyx.mwccf.network.PacketUpdateDeviceState(currentState, currentBpm));
            }
        }
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            com.voltyx.mwccf.geo.BraceletUI.updateFBO();
        }
    }


    @SubscribeEvent
    public static void onRenderHand(RenderSpecificHandEvent event) {
        float progress = prevInspectProgress + (inspectProgress - prevInspectProgress) * event.getPartialTicks();
        
        if (progress > 0.0f) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player == null) return;
            
            boolean isLeftHand = false;
            if (mc.gameSettings.mainHand == EnumHandSide.RIGHT && event.getHand() == EnumHand.OFF_HAND) isLeftHand = true;
            if (mc.gameSettings.mainHand == EnumHandSide.LEFT && event.getHand() == EnumHand.MAIN_HAND) isLeftHand = true;

            float hideProgress = Math.min(1.0f, progress); 
            float showProgress = Math.max(0.0f, Math.min(1.0f, progress - 1.0f)); 

            if (isLeftHand) {
                event.setCanceled(true); 
                
                // 1. Опускаем текущую левую руку (с предметом или без)
                if (hideProgress > 0 && hideProgress < 1.0f) {
                    mc.getItemRenderer().renderItemInFirstPerson(mc.player, event.getPartialTicks(), event.getInterpolatedPitch(), event.getHand(), event.getSwingProgress(), event.getItemStack(), event.getEquipProgress() + hideProgress * 2.0f);
                }
                
                // 2. Рисуем руку с браслетом, когда hideProgress завершён
                if (showProgress > 0.0f) {
                    GlStateManager.pushMatrix();
                    
                    float startX = -1.0f; 
                    float startY = -1.5f;
                    float startZ = -0.5f;
                    
                    float curX = startX + (targetX - startX) * showProgress;
                    float curY = startY + (targetY - startY) * showProgress;
                    float curZ = startZ + (targetZ - startZ) * showProgress;
                    
                    GlStateManager.translate(curX, curY, curZ);
                    
                    GlStateManager.rotate(targetRotX * showProgress, 1, 0, 0);
                    GlStateManager.rotate(targetRotY * showProgress, 0, 1, 0);
                    GlStateManager.rotate(targetRotZ * showProgress, 0, 0, 1);
                    
                    RenderPlayer renderPlayer = (RenderPlayer) mc.getRenderManager().getSkinMap().get(mc.player.getSkinType());
                    if (renderPlayer != null) {
                        net.minecraft.client.model.ModelBiped mainModel = (net.minecraft.client.model.ModelBiped) renderPlayer.getMainModel();
                        mainModel.swingProgress = 0.0F;
                        mainModel.isSneak = false;
                        mainModel.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, mc.player);
                        
                        mainModel.bipedLeftArm.rotateAngleX = 0.0f;
                        mainModel.bipedLeftArm.rotateAngleY = 0.0f;
                        mainModel.bipedLeftArm.rotateAngleZ = 0.0f;
                        mainModel.bipedLeftArm.rotationPointX = 5.0f;
                        mainModel.bipedLeftArm.rotationPointY = 2.0f;
                        mainModel.bipedLeftArm.rotationPointZ = 0.0f;

                        mc.getTextureManager().bindTexture(mc.player.getLocationSkin());
                        GlStateManager.disableCull();
                        mainModel.bipedLeftArm.render(0.0625f);
                        if (mainModel instanceof net.minecraft.client.model.ModelPlayer) {
                            net.minecraft.client.model.ModelPlayer mp = (net.minecraft.client.model.ModelPlayer) mainModel;
                            mp.bipedLeftArmwear.rotateAngleX = 0.0f;
                            mp.bipedLeftArmwear.rotateAngleY = 0.0f;
                            mp.bipedLeftArmwear.rotateAngleZ = 0.0f;
                            mp.bipedLeftArmwear.rotationPointX = 5.0f;
                            mp.bipedLeftArmwear.rotationPointY = 2.0f;
                            mp.bipedLeftArmwear.rotationPointZ = 0.0f;
                            mp.bipedLeftArmwear.render(0.0625f);
                        }
                        
                        ItemStack chest = mc.player.getItemStackFromSlot(net.minecraft.inventory.EntityEquipmentSlot.CHEST);
                        if (!chest.isEmpty() && chest.getItem() instanceof net.minecraft.item.ItemArmor) {
                            net.minecraft.item.ItemArmor armor = (net.minecraft.item.ItemArmor) chest.getItem();
                            net.minecraft.client.model.ModelBiped armorModel = net.minecraftforge.client.ForgeHooksClient.getArmorModel(mc.player, chest, net.minecraft.inventory.EntityEquipmentSlot.CHEST, null);
                            if (armorModel != null) {
                                String tex = armor.getArmorTexture(chest, mc.player, net.minecraft.inventory.EntityEquipmentSlot.CHEST, null);
                                if (tex != null) {
                                    mc.getTextureManager().bindTexture(new net.minecraft.util.ResourceLocation(tex));
                                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                                    armorModel.bipedLeftArm.rotateAngleX = 0.0f;
                                    armorModel.bipedLeftArm.rotateAngleY = 0.0f;
                                    armorModel.bipedLeftArm.rotateAngleZ = 0.0f;
                                    armorModel.bipedLeftArm.offsetX = 0.0f;
                                    armorModel.bipedLeftArm.offsetY = 0.0f;
                                    armorModel.bipedLeftArm.offsetZ = 0.0f;
                                    armorModel.bipedLeftArm.rotationPointX = 5.0F;
                                    armorModel.bipedLeftArm.rotationPointY = 2.0F;
                                    armorModel.bipedLeftArm.rotationPointZ = 0.0F;
                                    armorModel.bipedLeftArm.render(0.0625F);
                                }
                            }
                        }

                        if (com.voltyx.mwccf.geo.BraceletUI.hasBraceletEquipped()) {
                            boolean isSlim = false;
                            if (mc.player instanceof net.minecraft.client.entity.AbstractClientPlayer) {
                                isSlim = "slim".equals(((net.minecraft.client.entity.AbstractClientPlayer)mc.player).getSkinType());
                            }
                            mc.getTextureManager().bindTexture(BRACELET_TEXTURE);
                            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                            
                            if (isSlim) {
                                if (braceletSlimModel == null) {
                                    braceletSlimModel = new com.voltyx.mwccf.geo.GeoArmorModel(BRACELET_SLIM_MODEL_PATH);
                                }
                                braceletSlimModel.bipedLeftArmSlim.rotateAngleX = 0.0f;
                                braceletSlimModel.bipedLeftArmSlim.rotateAngleY = 0.0f;
                                braceletSlimModel.bipedLeftArmSlim.rotateAngleZ = 0.0f;
                                braceletSlimModel.bipedLeftArmSlim.offsetX = 0.0f;
                                braceletSlimModel.bipedLeftArmSlim.offsetY = 0.0f;
                                braceletSlimModel.bipedLeftArmSlim.offsetZ = 0.0f;
                                braceletSlimModel.bipedLeftArmSlim.rotationPointX = 5.0F;
                                braceletSlimModel.bipedLeftArmSlim.rotationPointY = 2.0F;
                                braceletSlimModel.bipedLeftArmSlim.rotationPointZ = 0.0F;
                                braceletSlimModel.bipedLeftArmSlim.render(0.0625F);
                            } else {
                                if (braceletModel == null) {
                                    braceletModel = new com.voltyx.mwccf.geo.GeoArmorModel(BRACELET_MODEL_PATH);
                                }
                                braceletModel.bipedLeftArm.rotateAngleX = 0.0f;
                                braceletModel.bipedLeftArm.rotateAngleY = 0.0f;
                                braceletModel.bipedLeftArm.rotateAngleZ = 0.0f;
                                braceletModel.bipedLeftArm.offsetX = 0.0f;
                                braceletModel.bipedLeftArm.offsetY = 0.0f;
                                braceletModel.bipedLeftArm.offsetZ = 0.0f;
                                braceletModel.bipedLeftArm.rotationPointX = 5.0F;
                                braceletModel.bipedLeftArm.rotationPointY = 2.0F;
                                braceletModel.bipedLeftArm.rotationPointZ = 0.0F;
                                braceletModel.bipedLeftArm.render(0.0625F);
                            }
                        }

                        GlStateManager.enableCull();
                    }
                    GlStateManager.popMatrix();
                }
            } else {
                // Правая рука
                if (event.getItemStack().isEmpty() || efw.animation.WeaponTypeHelper.getWeaponType(event.getItemStack()) != efw.animation.WeaponTypeHelper.WeaponType.NONE) {
                    event.setCanceled(true);
                    if (event.getItemStack().isEmpty()) {
                        mc.getItemRenderer().renderItemInFirstPerson(mc.player, event.getPartialTicks(), event.getInterpolatedPitch(), event.getHand(), event.getSwingProgress(), event.getItemStack(), event.getEquipProgress() + hideProgress * 2.0f);
                    }
                }
            }
        }
    }
}

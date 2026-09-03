package efw.mixin;

import com.paneedah.weaponlib.PlayerWeaponInstance;
import com.paneedah.weaponlib.Weapon;
import com.paneedah.weaponlib.state.Aspect;
import com.paneedah.weaponlib.state.ExtendedState;
import com.paneedah.weaponlib.state.ManagedState;
import com.paneedah.weaponlib.state.StateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {StateManager.class}, remap = false)
public class MixinStateManager {
    static {
        System.out.println("[EFW-MIXIN-LOAD] MixinStateManager class loaded!");
    }

    /**
     * РџРµСЂРµС…РІР°С‚С‹РІР°РµС‚ СЃРјРµРЅСѓ СЃРѕСЃС‚РѕСЏРЅРёСЏ РѕСЂСѓР¶РёСЏ РґР»СЏ РїСЂРёРЅСѓРґРёС‚РµР»СЊРЅРѕРіРѕ РІРѕСЃРїСЂРѕРёР·РІРµРґРµРЅРёСЏ Р·РІСѓРєРѕРІ РІ С‚СЂРµС‚СЊРµРј Р»РёС†Рµ.
     */
    @Inject(method = {"changeState"}, at = {@At("HEAD")})
    private void onChangeState(Aspect aspect, ExtendedState instance, ManagedState[] states, CallbackInfoReturnable<Boolean> cir) {
        if (states == null || states.length == 0) return;
        
        ManagedState targetState = states[0];
        if (instance instanceof PlayerWeaponInstance) {
            PlayerWeaponInstance weaponInstance = (PlayerWeaponInstance) instance;
            EntityLivingBase living = weaponInstance.getPlayer();
            
            if (living instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) living;
                
                // Block shooting or melee attacking while rolling
                if (targetState != null) {
                    String sName = targetState.toString().toUpperCase();
                    if (sName.contains("FIRING") || sName.contains("ATTACKING") || sName.contains("MELEE")) {
                        com.voltyx.mwccf.dash.DashCapability.IDashData cap = player.getCapability(com.voltyx.mwccf.dash.DashCapability.ROLL_CAP, null);
                        if (cap != null && (cap.isDashing() || efw.AnimationTickHandler.isPlayerRolling(player))) {
                            cir.setReturnValue(false);
                            return;
                        }
                    }
                }

                // РџСЂРѕРІРµСЂРєР°: РЅР° РєР»РёРµРЅС‚Рµ Р»Рё РјС‹, СЏРІР»СЏРµС‚СЃСЏ Р»Рё РёРіСЂРѕРє Р»РѕРєР°Р»СЊРЅС‹Рј Рё РІРєР»СЋС‡РµРЅ Р»Рё РІРёРґ РѕС‚ 3-РіРѕ Р»РёС†Р°
                if (player.world.isRemote && isLocalPlayer(player) && Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
                    handleThirdPersonSounds(weaponInstance, targetState);
                }
            }
        }
    }

    private boolean isLocalPlayer(EntityPlayer player) {
        return Minecraft.getMinecraft().player != null && 
               Minecraft.getMinecraft().player.getUniqueID().equals(player.getUniqueID());
    }

    private void handleThirdPersonSounds(PlayerWeaponInstance instance, ManagedState newState) {
        Weapon weapon = instance.getWeapon();
        if (weapon == null || newState == null) return;
        
        String stateName = newState.toString().toUpperCase();
        
        // Р’РѕСЃРїСЂРѕРёР·РІРµРґРµРЅРёРµ СЃРѕРѕС‚РІРµС‚СЃС‚РІСѓСЋС‰РёС… Р·РІСѓРєРѕРІ РІ Р·Р°РІРёСЃРёРјРѕСЃС‚Рё РѕС‚ СЃРѕСЃС‚РѕСЏРЅРёСЏ
        if (stateName.contains("RELOAD") || stateName.contains("LOAD")) {
            if (weapon.getReloadSound() != null) {
                instance.getPlayer().playSound(weapon.getReloadSound(), 1.0F, 1.0F);
            }
        } else if (stateName.contains("UNLOAD")) {
            if (weapon.getUnloadSound() != null) {
                instance.getPlayer().playSound(weapon.getUnloadSound(), 1.0F, 1.0F);
            }
        } else if (stateName.contains("DRAWING") && weapon.getDrawSound() != null) {
            instance.getPlayer().playSound(weapon.getDrawSound(), 1.0F, 1.0F);
        }
    }
}
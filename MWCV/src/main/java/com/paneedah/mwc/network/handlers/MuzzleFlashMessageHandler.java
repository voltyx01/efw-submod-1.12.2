package com.paneedah.mwc.network.handlers;

import com.paneedah.mwc.network.messages.MuzzleFlashMessage;
import com.paneedah.weaponlib.ClientEventHandler;
import io.redstudioragnarok.redcore.utils.NetworkUtil;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.paneedah.mwc.proxies.ClientProxy.MC;

public final class MuzzleFlashMessageHandler implements IMessageHandler<MuzzleFlashMessage, IMessage> {

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(final MuzzleFlashMessage muzzleFlashMessage, final MessageContext messageContext) {
        NetworkUtil.processMessage(messageContext, () -> {
            if (MC.player == null || MC.player.getEntityId() == muzzleFlashMessage.getEntityID())
                return;

            ClientEventHandler.uploadFlash(muzzleFlashMessage.getEntityID());
            ClientEventHandler.lastShotTimeByEntity.put(muzzleFlashMessage.getEntityID(), System.currentTimeMillis());

            if (MC.world != null) {
                net.minecraft.entity.Entity entity = MC.world.getEntityByID(muzzleFlashMessage.getEntityID());
                if (entity instanceof net.minecraft.entity.player.EntityPlayer) {
                    net.minecraft.entity.player.EntityPlayer shooter = (net.minecraft.entity.player.EntityPlayer) entity;
                    net.minecraft.item.ItemStack held = shooter.getHeldItemMainhand();
                    if (!held.isEmpty() && held.getItem() instanceof com.paneedah.weaponlib.Weapon) {
                        com.paneedah.weaponlib.PlayerItemInstance<?> pii = com.paneedah.mwc.MWC.modContext.getPlayerItemInstanceRegistry().getItemInstance(shooter, held);
                        if (pii instanceof com.paneedah.weaponlib.PlayerWeaponInstance) {
                            com.paneedah.weaponlib.PlayerWeaponInstance pwi = (com.paneedah.weaponlib.PlayerWeaponInstance) pii;
                            pwi.bumpSlidePump();
                            pwi.setLastFireTimestamp(System.currentTimeMillis());
                            int currentAmmo = com.paneedah.weaponlib.Tags.getAmmo(held);
                            if (currentAmmo > 0) {
                                pwi.setAmmo(currentAmmo);
                            } else if (pwi.getAmmo() > 0) {
                                pwi.setAmmo(pwi.getAmmo() - 1);
                            }
                            if (pwi.getAmmo() == 0) {
                                pwi.setSlideLock(true);
                            }
                        }
                    }
                }
            }
        });

        return null;
    }

    public MuzzleFlashMessageHandler() {}

}
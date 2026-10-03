package com.voltyx.mwccf.blood;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Handles client-side blood decay ticks and tracks current rendering player
 * for procedural per-texel blood overlay in both 3rd person and 1st person.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = "mwccf", value = Side.CLIENT)
public class BloodRenderHandler {

    // -------------------------------------------------------------------------
    // Client tick: decay blood for every visible player
    // -------------------------------------------------------------------------

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) return;
        for (EntityPlayer player : mc.world.playerEntities) {
            BloodManager.tickDecay(player);
        }
    }

    // -------------------------------------------------------------------------
    // 3rd Person: mark which player is currently being rendered
    // -------------------------------------------------------------------------

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        BloodTextureManager.setRenderingPlayer(event.getEntityPlayer());
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        BloodTextureManager.clearRenderingPlayer();
    }

    // -------------------------------------------------------------------------
    // 1st Person: mark client player as rendering during hand/weapon rendering
    // (MWC weapons, vanilla items, empty hands)
    // -------------------------------------------------------------------------

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public static void onRenderHandPre(RenderHandEvent event) {
        BloodTextureManager.setRenderingPlayer(Minecraft.getMinecraft().player);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onRenderHandPost(RenderHandEvent event) {
        BloodTextureManager.clearRenderingPlayer();
    }
}

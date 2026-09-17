package com.voltyx.mwccf.walkietalkie;

import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

/**
 * Simple Voice Chat plugin for walkie-talkie audio routing.
 *
 * When a player speaks and has an active (non-muted) walkie-talkie,
 * the audio is broadcast as a "static" (non-directional) packet to all
 * other players who also have an active walkie-talkie on the same channel.
 */
@ForgeVoicechatPlugin
public class WalkieTalkieSvcPlugin implements VoicechatPlugin {

    @Nullable
    public static VoicechatServerApi api;

    @Override
    public String getPluginId() {
        return "mwccf_walkietalkie";
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophone);
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
    }

    private void onServerStarted(VoicechatServerStartedEvent event) {
        api = event.getVoicechat();
    }

    private void onMicrophone(MicrophonePacketEvent event) {
        if (api == null) return;

        VoicechatConnection senderConn = event.getSenderConnection();
        if (senderConn == null) return;

        Object rawPlayer = senderConn.getPlayer().getPlayer();
        if (!(rawPlayer instanceof EntityPlayerMP)) return;
        EntityPlayerMP sender = (EntityPlayerMP) rawPlayer;

        // Find walkie-talkie of sender (in hand or baubles)
        ItemStack senderStack = WalkieTalkieUtil.getActiveWalkieTalkie(sender);
        if (senderStack == null) return;
        if (ItemWalkieTalkie.isMuted(senderStack)) return;

        int channel = ItemWalkieTalkie.getChannel(senderStack);

        // Route to all other players with active WT on the same channel
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null) return;

        for (EntityPlayerMP receiver : server.getPlayerList().getPlayers()) {
            UUID receiverUuid = receiver.getUniqueID();
            if (receiverUuid.equals(sender.getUniqueID())) continue;

            ItemStack recvStack = WalkieTalkieUtil.getActiveWalkieTalkie(receiver);
            if (recvStack == null) continue;
            if (ItemWalkieTalkie.getChannel(recvStack) != channel) continue;

            VoicechatConnection conn = api.getConnectionOf(receiverUuid);
            if (conn == null) continue;

            api.sendStaticSoundPacketTo(conn,
                    event.getPacket().staticSoundPacketBuilder().build());
        }
    }
}

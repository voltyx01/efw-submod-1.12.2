package com.voltyx.mwccf.speech;

import com.voltyx.gender.main.GenderPlayer;
import com.voltyx.gender.main.WildfireGender;
import com.voltyx.mwccf.MwccfMod;
import com.voltyx.mwccf.speech.network.PacketPersonalSpeech;
import com.voltyx.mwccf.speech.network.PacketPublicSpeech;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpeechServerHandler {
    private static final Map<UUID, Long> LAST_MESSAGE_TICK = new HashMap<>();

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        LAST_MESSAGE_TICK.remove(event.player.getUniqueID());
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        EntityPlayerMP sender = event.getPlayer();
        String raw = event.getMessage();
        String text = sanitize(raw);
        if (text.isEmpty()) return;

        event.setCanceled(true);
        long tick = sender.world.getTotalWorldTime();
        Long previousTick = LAST_MESSAGE_TICK.put(sender.getUniqueID(), tick);
        if (previousTick != null && tick - previousTick < 4L) return;

        float pitch = getPlayerPitch(sender.getUniqueID());
        PacketPublicSpeech packet = new PacketPublicSpeech(sender.getEntityId(), sender.getName(), text, pitch);
        double radiusSq = SpeechConfig.publicHiddenDistance * SpeechConfig.publicHiddenDistance;
        int dimension = sender.dimension;
        for (EntityPlayerMP recipient : sender.getServerWorld().getMinecraftServer().getPlayerList().getPlayers()) {
            if (recipient.dimension == dimension && recipient.getDistanceSq(sender) <= radiusSq) {
                MwccfMod.PACKET_HANDLER.sendTo(packet, recipient);
            }
        }
    }

    public static void sendPersonal(EntityPlayerMP player, String translationKey, String... arguments) {
        if (player == null || translationKey == null || translationKey.isEmpty()) return;
        String[] safeArguments = arguments == null ? new String[0] : arguments;
        int count = Math.min(8, safeArguments.length);
        String[] limitedArguments = new String[count];
        System.arraycopy(safeArguments, 0, limitedArguments, 0, count);
        MwccfMod.PACKET_HANDLER.sendTo(new PacketPersonalSpeech(
                translationKey, limitedArguments, getPlayerPitch(player.getUniqueID())), player);
    }

    public static float getPlayerPitch(UUID uuid) {
        long hash = uuid.getMostSignificantBits() ^ Long.rotateLeft(uuid.getLeastSignificantBits(), 23);
        float variation = (((hash >>> 11) & 0xFFFFL) / 65535.0F - 0.5F) * SpeechConfig.publicPitchVariation;
        float pitch = 1.0F + variation;
        try {
            if (WildfireGender.modEnabled) {
                GenderPlayer genderPlayer = WildfireGender.getPlayerById(uuid);
                if (genderPlayer != null && genderPlayer.getGender() == GenderPlayer.Gender.FEMALE) {
                    pitch += SpeechConfig.femalePitchOffset;
                }
            }
        } catch (Throwable ignored) {}
        return Math.max(0.75F, Math.min(1.25F, pitch));
    }

    private static String sanitize(String raw) {
        if (raw == null) return "";
        String cleaned = TextFormatting.getTextWithoutFormattingCodes(raw)
                .replace('\r', ' ').replace('\n', ' ').replace('\u0000', ' ').trim();
        int codePoints = cleaned.codePointCount(0, cleaned.length());
        if (codePoints > SpeechConfig.maxMessageLength) {
            cleaned = cleaned.substring(0, cleaned.offsetByCodePoints(0, SpeechConfig.maxMessageLength));
        }
        return cleaned;
    }
}

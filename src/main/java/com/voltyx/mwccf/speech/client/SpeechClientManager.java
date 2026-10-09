package com.voltyx.mwccf.speech.client;

import com.voltyx.mwccf.ModSounds;
import com.voltyx.mwccf.speech.SpeechConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SideOnly(Side.CLIENT)
public final class SpeechClientManager {
    public static final class TypedLine {
        public final String text;
        public final float pitch;
        public final int entityId;
        public final boolean isPersonal;
        public final boolean isProcedural;
        public int revealedCodePoints;
        public long nextCharacterAt;
        private final boolean playSound;

        private final int[] codePoints;

        private TypedLine(String text, float pitch, int entityId, long startAt, boolean playSound, boolean isPersonal, boolean isProcedural) {
            this.text = text;
            this.pitch = pitch;
            this.entityId = entityId;
            this.playSound = playSound;
            this.isPersonal = isPersonal;
            this.isProcedural = isProcedural;
            this.codePoints = text.codePoints().toArray();
            this.nextCharacterAt = startAt;
        }

        public String getVisibleText() {
            return new String(codePoints, 0, Math.min(revealedCodePoints, codePoints.length));
        }

        public boolean isComplete() {
            return revealedCodePoints >= codePoints.length;
        }
    }

    public static final class SpeechBubble {
        public final int entityId;
        public final String speakerName;
        public final float pitch;
        public final List<TypedLine> lines = new ArrayList<>();
        public long expiresAt;
        public long widthAnimationStart;
        public long widthAnimationEnd;
        public long heightAnimationStart;
        public long heightAnimationEnd;
        public float widthAnimationFrom;
        public float heightAnimationFrom = 12.0F;
        public float renderedWidth;
        public float renderedHeight = 12.0F;

        private SpeechBubble(int entityId, String speakerName, float pitch) {
            this.entityId = entityId;
            this.speakerName = speakerName;
            this.pitch = pitch;
        }

        public String getVisibleText() {
            StringBuilder text = new StringBuilder();
            for (TypedLine line : lines) {
                if (line.revealedCodePoints == 0) continue;
                if (text.length() > 0) text.append('\n');
                text.append('[').append(line.getVisibleText()).append(']');
            }
            return text.toString();
        }

        public String getFullText() {
            StringBuilder text = new StringBuilder();
            for (TypedLine line : lines) {
                if (text.length() > 0) text.append('\n');
                text.append('[').append(line.text).append(']');
            }
            return text.toString();
        }

        public float getWidthProgress(long now) {
            if (widthAnimationEnd <= widthAnimationStart || now >= widthAnimationEnd) return 1.0F;
            return Math.max(0.0F, Math.min(1.0F,
                    (now - widthAnimationStart) / (float) (widthAnimationEnd - widthAnimationStart)));
        }

        public boolean isTyping() {
            for (TypedLine line : lines) if (!line.isComplete()) return true;
            return false;
        }
    }

    public static final class HistoryMessage {
        public final String speakerName;
        public final String text;
        public final long receivedAt;

        private HistoryMessage(String speakerName, String text, long receivedAt) {
            this.speakerName = speakerName;
            this.text = text;
            this.receivedAt = receivedAt;
        }
    }

    private static final Map<Integer, SpeechBubble> BUBBLES = new ConcurrentHashMap<>();
    private static final Deque<HistoryMessage> HISTORY = new ArrayDeque<>();
    private static final Deque<TypedLine> PERSONAL_QUEUE = new ArrayDeque<>();
    private static TypedLine activePersonal;
    private static long personalExpiresAt;

    private SpeechClientManager() {}

    public static void receivePublic(int entityId, String speakerName, String rawText, float pitch) {
        String text = sanitize(rawText);
        if (text.isEmpty()) return;
        long now = System.currentTimeMillis();
        Minecraft mc = Minecraft.getMinecraft();
        boolean isSender = mc.player != null && mc.player.getEntityId() == entityId;
        SpeechBubble bubble = BUBBLES.get(entityId);
        boolean newBubble = bubble == null;
        if (bubble == null) {
            if (BUBBLES.size() >= SpeechConfig.maxBubbles) {
                Integer oldestId = null;
                long earliestExpiry = Long.MAX_VALUE;
                for (Map.Entry<Integer, SpeechBubble> entry : BUBBLES.entrySet()) {
                    if (entry.getValue().expiresAt < earliestExpiry) {
                        earliestExpiry = entry.getValue().expiresAt;
                        oldestId = entry.getKey();
                    }
                }
                if (oldestId != null) BUBBLES.remove(oldestId);
            }
            bubble = new SpeechBubble(entityId, speakerName, pitch);
            bubble.widthAnimationStart = now;
            bubble.widthAnimationEnd = now + 180L;
            bubble.heightAnimationStart = bubble.widthAnimationEnd;
            bubble.heightAnimationEnd = bubble.heightAnimationStart + 120L;
            BUBBLES.put(entityId, bubble);
        } else {
            bubble.widthAnimationFrom = bubble.renderedWidth;
            bubble.widthAnimationStart = now;
            bubble.widthAnimationEnd = now + 180L;
            bubble.heightAnimationFrom = bubble.renderedHeight;
            bubble.heightAnimationStart = now;
            bubble.heightAnimationEnd = now + 120L;
        }
        long startAt = bubble.heightAnimationEnd;
        if (!bubble.lines.isEmpty()) {
            TypedLine last = bubble.lines.get(bubble.lines.size() - 1);
            startAt = Math.max(bubble.widthAnimationEnd, last.nextCharacterAt + estimatedRemainingMs(last));
        }
        bubble.lines.add(new TypedLine(text, pitch, entityId, startAt, !isSender, false, false));
        while (bubble.lines.size() > SpeechConfig.maxLinesPerBubble) bubble.lines.remove(0);
        bubble.expiresAt = 0L;
        HISTORY.addLast(new HistoryMessage(speakerName, text, now));
        while (HISTORY.size() > SpeechConfig.maxHistory) HISTORY.removeFirst();
        if (isSender) {
            PERSONAL_QUEUE.addLast(new TypedLine(text, pitch, entityId, now, true, true, false));
            while (PERSONAL_QUEUE.size() > 8) PERSONAL_QUEUE.removeFirst();
        }
    }

    public static void receivePersonal(String translationKey, String[] arguments, float pitch) {
        Minecraft mc = Minecraft.getMinecraft();
        String text = net.minecraft.client.resources.I18n.format(translationKey, (Object[]) arguments);
        if (text == null || text.isEmpty() || text.equals(translationKey)) {
            if ("speech.mwccf.doll.sorry".equals(translationKey)) {
                text = "Прости меня.";
            }
        }
        text = sanitize(text);
        if (text.isEmpty()) return;
        if (activePersonal != null && text.equals(activePersonal.text)) return;
        for (TypedLine queued : PERSONAL_QUEUE) {
            if (text.equals(queued.text)) return;
        }
        int playerId = mc.player == null ? -1 : mc.player.getEntityId();
        PERSONAL_QUEUE.addLast(new TypedLine(text, pitch, playerId, System.currentTimeMillis(), true, true, true));
        while (PERSONAL_QUEUE.size() > 8) PERSONAL_QUEUE.removeFirst();
    }

    public static void showPersonal(String text) {
        if (text == null || text.isEmpty()) return;
        String clean = sanitize(text);
        if (clean.isEmpty()) return;
        if (activePersonal != null && clean.equals(activePersonal.text)) return;
        for (TypedLine queued : PERSONAL_QUEUE) {
            if (clean.equals(queued.text)) return;
        }
        int playerId = Minecraft.getMinecraft().player == null ? -1 : Minecraft.getMinecraft().player.getEntityId();
        PERSONAL_QUEUE.addLast(new TypedLine(clean, 1.0F, playerId, System.currentTimeMillis(), true, true, true));
        while (PERSONAL_QUEUE.size() > 8) PERSONAL_QUEUE.removeFirst();
    }

    private static boolean personalFadingOut = false;
    private static long personalFadeDurationMs = 350L;

    public static void update() {
        long now = System.currentTimeMillis();
        if (activePersonal == null && !PERSONAL_QUEUE.isEmpty()) {
            activePersonal = PERSONAL_QUEUE.removeFirst();
            personalFadingOut = false;
        }
        if (activePersonal != null) {
            if (!personalFadingOut) {
                boolean completedThisUpdate = advance(activePersonal, now);
                if (activePersonal.isComplete() && (completedThisUpdate || personalExpiresAt == 0L)) {
                    personalFadeDurationMs = Math.max(1L, (long) SpeechConfig.personalFadeMs);
                    personalExpiresAt = now + SpeechConfig.getPersonalHoldDuration(activePersonal.text) + personalFadeDurationMs;
                }
            }
            if (activePersonal.isComplete() && now >= personalExpiresAt) {
                activePersonal = null;
                personalExpiresAt = 0L;
                personalFadingOut = false;
            }
        }

        Iterator<Map.Entry<Integer, SpeechBubble>> it = BUBBLES.entrySet().iterator();
        while (it.hasNext()) {
            SpeechBubble bubble = it.next().getValue();
            for (TypedLine line : bubble.lines) advance(line, now);
            if (!bubble.isTyping()) {
                if (bubble.expiresAt == 0L) bubble.expiresAt = now + SpeechConfig.getPublicHoldDuration(bubble.getFullText()) + SpeechConfig.publicFadeMs;
                if (now >= bubble.expiresAt) it.remove();
            }
        }
    }

    private static boolean advance(TypedLine line, long now) {
        if (line.isComplete() || now < line.nextCharacterAt) return false;
        int codePoint = line.codePoints[line.revealedCodePoints++];
        float speed = line.isPersonal ? SpeechConfig.personalTypingSpeed : SpeechConfig.publicTypingSpeed;
        int baseDelay = line.isPersonal ? SpeechConfig.personalLetterDelayMs : SpeechConfig.publicLetterDelayMs;
        int delay = Math.max(15, (int) (baseDelay / Math.max(0.5F, line.pitch) / Math.max(0.1F, speed)));
        line.nextCharacterAt = now + delay;
        boolean soundEnabled = line.isPersonal ? SpeechConfig.personalSoundEnabled : SpeechConfig.publicSoundEnabled;
        if (line.playSound && soundEnabled && !Character.isWhitespace(codePoint) && ModSounds.SPEECH_LETTER != null) {
            playLetterSound(line);
        }
        return line.isComplete();
    }

    private static void playLetterSound(TypedLine line) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) return;
        Entity source = line.entityId >= 0 ? mc.world.getEntityByID(line.entityId) : mc.player;
        if (source == null) source = mc.player;
        float volume = line.isPersonal ? SpeechConfig.personalSoundVolume : SpeechConfig.publicSoundVolume;
        
        PositionedSoundRecord sound;
        if (line.isPersonal) {
            // Для личных реплик игрока (над хотбаром) воспроизводим напрямую в MASTER без затухания дистанции
            sound = new PositionedSoundRecord(
                    ModSounds.SPEECH_LETTER.getSoundName(),
                    SoundCategory.MASTER,
                    volume * 1.5F, line.pitch, false, 0,
                    PositionedSoundRecord.AttenuationType.NONE,
                    0.0F, 0.0F, 0.0F);
        } else {
            sound = new PositionedSoundRecord(ModSounds.SPEECH_LETTER,
                    SoundCategory.PLAYERS, volume, line.pitch,
                    (float) source.posX, (float) (source.posY + source.height * 0.8D), (float) source.posZ);
        }
        mc.getSoundHandler().playSound(sound);
    }

    private static int estimatedRemainingMs(TypedLine line) {
        int remaining = Math.max(0, line.codePoints.length - line.revealedCodePoints);
        float speed = line.isPersonal ? SpeechConfig.personalTypingSpeed : SpeechConfig.publicTypingSpeed;
        int baseDelay = line.isPersonal ? SpeechConfig.personalLetterDelayMs : SpeechConfig.publicLetterDelayMs;
        return remaining * Math.max(15, (int) (baseDelay / Math.max(0.5F, line.pitch) / Math.max(0.1F, speed)));
    }

    private static int estimateTypingMs(String text, float pitch) {
        int codePoints = text.codePointCount(0, text.length());
        return codePoints * Math.max(15, (int) (SpeechConfig.publicLetterDelayMs / Math.max(0.5F, pitch) / Math.max(0.1F, SpeechConfig.publicTypingSpeed)));
    }

    private static int holdDuration(String text) {
        return SpeechConfig.getPublicHoldDuration(text);
    }

    private static String sanitize(String value) {
        if (value == null) return "";
        String text = TextFormatting.getTextWithoutFormattingCodes(value)
                .replace('\r', ' ').replace('\n', ' ').replace('\u0000', ' ').trim();
        int length = text.codePointCount(0, text.length());
        if (length > SpeechConfig.maxMessageLength) {
            text = text.substring(0, text.offsetByCodePoints(0, SpeechConfig.maxMessageLength));
        }
        return text;
    }

    public static TypedLine getPersonalLine() {
        return activePersonal;
    }

    public static boolean isPersonalProcedural() {
        return activePersonal != null && activePersonal.isProcedural;
    }

    public static void startPersonalFadeOut() {
        if (activePersonal != null && !personalFadingOut) {
            personalFadingOut = true;
            activePersonal.revealedCodePoints = activePersonal.codePoints.length;
            long now = System.currentTimeMillis();
            personalFadeDurationMs = Math.max(400L, (long) SpeechConfig.personalFadeMs);
            personalExpiresAt = now + personalFadeDurationMs;
        }
        PERSONAL_QUEUE.clear();
    }

    public static void clearPersonal() {
        activePersonal = null;
        personalExpiresAt = 0L;
        personalFadingOut = false;
        PERSONAL_QUEUE.clear();
    }

    public static float getPersonalAlpha() {
        if (activePersonal == null) return 0.0F;
        long now = System.currentTimeMillis();
        if (!activePersonal.isComplete()) return 1.0F;
        long fadeDur = Math.max(1L, personalFadeDurationMs);
        return Math.max(0.0F, Math.min(1.0F, (personalExpiresAt - now) / (float) fadeDur));
    }

    public static Collection<SpeechBubble> getBubbles() {
        return new ArrayList<>(BUBBLES.values());
    }

    public static List<HistoryMessage> getHistory() {
        return new ArrayList<>(HISTORY);
    }

    public static void clearWorldState() {
        BUBBLES.clear();
        HISTORY.clear();
        PERSONAL_QUEUE.clear();
        activePersonal = null;
        personalExpiresAt = 0L;
        personalFadingOut = false;
    }
}

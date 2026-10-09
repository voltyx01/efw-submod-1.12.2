package com.voltyx.mwccf.speech;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Обширный конфигурационный файл для обеих систем реплик (mwccf_speech.cfg):
 * 1. Personal (Личные реплики / субтитры над хотбаром, например реплика куклы "[ Прости. ]")
 * 2. Public   (Публичные облачка реплик над головами персонажей в 3D мире)
 * 3. General  (Общие параметры сообщений)
 */
public final class SpeechConfig {

    private static File lastConfigFile = null;
    private static Configuration config = null;

    public static Configuration getConfig() {
        return config;
    }

    // =========================================================================
    //  GENERAL SETTINGS
    // =========================================================================
    public static int maxMessageLength = 160;
    public static int maxHistory = 100;
    public static int maxBubbles = 32;
    public static int maxLinesPerBubble = 8;

    // Legacy fields for backward compatibility
    public static float typingSpeed = 1.0F;
    public static int letterSoundDelayMs = 90;
    public static int fadeMs = 300;
    public static int personalHoldMs = 2200;

    // =========================================================================
    //  PERSONAL REPLICAS (Субтитры над хотбаром)
    // =========================================================================
    public static int personalHoldMsBase = 2200;            // Базовое время удержания в мс
    public static int personalHoldMsPerChar = 35;           // Дополнительное время на каждый символ в мс
    public static int personalHoldMsMax = 8000;             // Максимальное время удержания в мс
    public static int personalFadeMs = 350;                 // Длительность плавного затухания в мс
    public static float personalTypingSpeed = 1.0F;         // Множитель скорости набора букв
    public static int personalLetterDelayMs = 85;           // Базовая задержка между буквами в мс
    public static boolean personalSoundEnabled = true;      // Включен ли звук печати букв
    public static float personalSoundVolume = 1.0F;         // Громкость звука набора букв
    public static float personalScale = 1.0F;               // Масштаб шрифта / реплики
    public static float personalXOffset = 0.0F;             // Горизонтальное смещение от центра экрана
    public static float personalYOffset = 0.0F;             // Вертикальное смещение относительно хотбара
    public static boolean personalShowBrackets = true;      // Показывать ли квадратные скобки [ ]
    public static boolean personalBottomGradient = true;    // Плавный затемняющий градиент снизу до 1/3 экрана
    public static String personalTextColor = "FFFF55";      // HEX цвет текста (RRGGBB)
    private static int personalTextColorRgb = 0x00FFFF55;

    // =========================================================================
    //  PUBLIC REPLICAS (Облачка над головой в 3D мире)
    // =========================================================================
    public static int publicHoldMsBase = 1200;              // Базовое время удержания облачка в мс
    public static int publicHoldMsPerCharacter = 28;        // Дополнительное время на каждый символ в мс
    public static int publicHoldMsMax = 6000;               // Максимальное время удержания облачка в мс
    public static int publicFadeMs = 300;                   // Длительность затухания облачка в мс
    public static float publicTypingSpeed = 1.0F;           // Множитель скорости набора букв
    public static int publicLetterDelayMs = 90;             // Базовая задержка между буквами в мс
    public static boolean publicSoundEnabled = true;        // Включен ли звук печати букв
    public static float publicSoundVolume = 0.95F;          // Громкость звука набора букв
    public static float publicBubbleScale = 0.025F;         // Мировой масштаб облачка
    public static float publicBubbleHeight = 0.45F;         // Высота облачка над макушкой персонажа
    public static float publicBubbleXOffset = 0.0F;         // Горизонтальное смещение облачка в мире
    public static float publicBubbleYOffset = 0.0F;         // Вертикальное смещение облачка в мире
    public static int publicMaxTextWidth = 180;             // Максимальная ширина текста внутри облачка
    public static int publicMaxBubbleHeight = 128;          // Максимальная высота облачка
    public static float publicFullAlphaDistance = 16.0F;    // Дистанция 100% видимости
    public static float publicHiddenDistance = 20.0F;       // Дистанция полного скрытия
    public static float publicPitchVariation = 0.08F;       // Разброс высоты тона звука для игроков
    public static float femalePitchOffset = 0.04F;          // Смещение тона для женских персонажей

    private SpeechConfig() {}

    public static void init(File configDirectory) {
        lastConfigFile = new File(configDirectory, "mwccf_speech.cfg");
        if (config == null) {
            config = new Configuration(lastConfigFile);
        }
        load();
    }

    public static void reload() {
        if (config != null) {
            config.load();
            load();
        } else if (lastConfigFile != null) {
            config = new Configuration(lastConfigFile);
            load();
        }
    }

    public static void load() {
        if (config == null) {
            if (lastConfigFile == null) return;
            config = new Configuration(lastConfigFile);
        }

        String catGeneral = "general";
        String catPersonal = "personal";
        String catPublic = "public";

        config.addCustomCategoryComment(catGeneral, "Общие настройки чата и реплик");
        config.addCustomCategoryComment(catPersonal, "Настройки персональных реплик (субтитров над хотбаром, например фразы Сайи)");
        config.addCustomCategoryComment(catPublic, "Настройки публичных облачков речи над головами персонажей в мире");

        // General
        maxMessageLength = config.getInt("maxMessageLength", catGeneral, 160, 16, 512, "Максимальная длина текста реплики.");
        maxHistory = config.getInt("maxHistory", catGeneral, 100, 10, 500, "Размер локальной истории сообщений.");
        maxBubbles = config.getInt("maxBubbles", catGeneral, 32, 4, 128, "Максимальное число одновременно отображаемых облачков в мире.");
        maxLinesPerBubble = config.getInt("maxLinesPerBubble", catGeneral, 8, 2, 24, "Максимальное число строк в одном облачке.");

        // Personal
        personalHoldMsBase = config.getInt("baseHoldMs", catPersonal, 2200, 200, 10000, "Базовое время показа персональной реплики в мс.");
        personalHoldMsPerChar = config.getInt("extraHoldMsPerChar", catPersonal, 35, 0, 200, "Дополнительное время показа на каждый символ (мс).");
        personalHoldMsMax = config.getInt("maxHoldMs", catPersonal, 8000, 500, 30000, "Максимальное время удержания персональной реплики (мс).");
        personalFadeMs = config.getInt("fadeMs", catPersonal, 350, 50, 2000, "Длительность плавного затухания реплики (мс).");
        personalTypingSpeed = config.getFloat("typingSpeed", catPersonal, 1.0F, 0.2F, 5.0F, "Множитель скорости набора букв (1.0 = стандарт, 2.0 = в 2 раза быстрее).");
        personalLetterDelayMs = config.getInt("letterDelayMs", catPersonal, 85, 20, 300, "Базовая задержка между символами при наборе (мс).");
        personalSoundEnabled = config.getBoolean("soundEnabled", catPersonal, true, "Включить ли звук печати букв для личных реплик.");
        personalSoundVolume = config.getFloat("soundVolume", catPersonal, 1.0F, 0.0F, 2.0F, "Громкость звука печати букв для личных реплик.");
        personalScale = config.getFloat("scale", catPersonal, 1.0F, 0.5F, 3.0F, "Визуальный масштаб (размер) текста реплики над хотбаром.");
        personalXOffset = config.getFloat("xOffset", catPersonal, 0.0F, -1000.0F, 1000.0F, "Горизонтальное смещение реплики от центра экрана.");
        personalYOffset = config.getFloat("yOffset", catPersonal, 0.0F, -500.0F, 500.0F, "Вертикальное смещение реплики относительно хотбара.");
        personalShowBrackets = config.getBoolean("showBrackets", catPersonal, true, "Отображать ли квадратные скобки вокруг реплики [ ... ].");
        personalBottomGradient = config.getBoolean("bottomGradient", catPersonal, true, "Плавный затемняющий градиент снизу до 1/3 высоты экрана для лучшей видимости реплики.");
        personalTextColor = config.getString("textColorHex", catPersonal, "FFFF55", "Цвет текста реплики в формате HEX (RRGGBB, например FFFF55 для золотого, FFFFFF для белого).");

        // Public
        publicHoldMsBase = config.getInt("baseHoldMs", catPublic, 1200, 200, 10000, "Базовое время удержания облачка в мс.");
        publicHoldMsPerCharacter = config.getInt("extraHoldMsPerChar", catPublic, 28, 0, 200, "Дополнительное время показа облачка на каждый символ (мс).");
        publicHoldMsMax = config.getInt("maxHoldMs", catPublic, 6000, 500, 30000, "Максимальное время показа облачка (мс).");
        publicFadeMs = config.getInt("fadeMs", catPublic, 300, 50, 2000, "Длительность плавного затухания облачка (мс).");
        publicTypingSpeed = config.getFloat("typingSpeed", catPublic, 1.0F, 0.2F, 5.0F, "Множитель скорости набора букв в облачке.");
        publicLetterDelayMs = config.getInt("letterDelayMs", catPublic, 90, 20, 300, "Базовая задержка между буквами в облачке (мс).");
        publicSoundEnabled = config.getBoolean("soundEnabled", catPublic, true, "Включить ли звук печати букв в облачках.");
        publicSoundVolume = config.getFloat("soundVolume", catPublic, 0.95F, 0.0F, 2.0F, "Громкость звука печати букв в облачках.");
        publicBubbleScale = config.getFloat("bubbleScale", catPublic, 0.025F, 0.005F, 0.10F, "Мировой масштаб (размер) облачка речи над игроком.");
        publicBubbleHeight = config.getFloat("bubbleHeight", catPublic, 0.45F, -2.0F, 5.0F, "Высота облачка речи над головой игрока.");
        publicBubbleXOffset = config.getFloat("bubbleXOffset", catPublic, 0.0F, -5.0F, 5.0F, "Горизонтальное локальное смещение облачка.");
        publicBubbleYOffset = config.getFloat("bubbleYOffset", catPublic, 0.0F, -5.0F, 5.0F, "Вертикальное локальное смещение облачка.");
        publicMaxTextWidth = config.getInt("maxTextWidth", catPublic, 180, 50, 500, "Максимальная ширина текста внутри облачка (пиксели).");
        publicMaxBubbleHeight = config.getInt("maxBubbleHeight", catPublic, 128, 30, 400, "Максимальная высота облачка (пиксели).");
        publicFullAlphaDistance = config.getFloat("fullAlphaDistance", catPublic, 16.0F, 2.0F, 64.0F, "Дистанция в блоках, на которой облачко видно со 100% прозрачностью.");
        publicHiddenDistance = config.getFloat("hiddenDistance", catPublic, 20.0F, 4.0F, 128.0F, "Дистанция в блоках, после которой облачко полностью скрывается.");
        publicPitchVariation = config.getFloat("pitchVariation", catPublic, 0.08F, 0.0F, 0.5F, "Случайный разброс высоты тона звука для каждого игрока.");
        femalePitchOffset = config.getFloat("femalePitchOffset", catPublic, 0.04F, 0.0F, 0.3F, "Повышение тона для женских персонажей Wildfire Gender.");

        // Legacy mirrors
        personalHoldMs = personalHoldMsBase;
        typingSpeed = personalTypingSpeed;
        letterSoundDelayMs = personalLetterDelayMs;
        fadeMs = personalFadeMs;

        // Parse color
        try {
            String cleanHex = personalTextColor.replace("#", "").trim();
            personalTextColorRgb = Integer.parseInt(cleanHex, 16);
        } catch (Exception e) {
            personalTextColorRgb = 0x00FFFF55;
        }

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static int getPersonalTextColorRgb() {
        return personalTextColorRgb;
    }

    public static int getPersonalHoldDuration(String text) {
        if (text == null || text.isEmpty()) return personalHoldMsBase;
        int length = text.codePointCount(0, text.length());
        return Math.min(personalHoldMsMax, personalHoldMsBase + length * personalHoldMsPerChar);
    }

    public static int getPublicHoldDuration(String text) {
        if (text == null || text.isEmpty()) return publicHoldMsBase;
        int length = text.codePointCount(0, text.length());
        return Math.min(publicHoldMsMax, publicHoldMsBase + length * publicHoldMsPerCharacter);
    }
}

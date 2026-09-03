package efw.biomeinfo;

import net.minecraftforge.common.config.Config;

@Config(modid = "mwccf")
public class MwccfConfig {

    @Config.Name("dash_and_stamina")
    @Config.Comment("Dash and Stamina Settings")
    public static final DashAndStaminaSettings dashAndStamina = new DashAndStaminaSettings();

    public static class DashAndStaminaSettings {
        @Config.Name("stamina")
        @Config.Comment("Stamina Settings")
        public final StaminaSettings stamina = new StaminaSettings();

        @Config.Name("stamina_effects")
        @Config.Comment("Low Stamina Effect Settings")
        public final LowStaminaEffects lowStaminaEffects = new LowStaminaEffects();

        @Config.Name("overlay")
        @Config.Comment("Stamina HUD Overlay Settings")
        public final OverlaySettings overlay = new OverlaySettings();

        @Config.Name("dash")
        @Config.Comment("Dash/Roll Settings")
        public final DashSettings dash = new DashSettings();

        public static class StaminaSettings {
            @Config.Comment("Maximum stamina value")
            @Config.RangeInt(min = 1, max = 1000)
            public int maxStamina = 20;

            @Config.Comment("Sprint drain interval in ticks")
            @Config.RangeInt(min = 0, max = 200)
            public int sprintDrainInterval = 25;

            @Config.Comment("Stamina cost for jumping")
            @Config.RangeInt(min = 0, max = 200)
            public int jumpCost = 0;

            @Config.Comment("Delay before stamina regenerates after a jump")
            @Config.RangeInt(min = 0, max = 100)
            public int jumpRegenDelay = 0;

            @Config.Comment("Delay before stamina regenerates after sprinting")
            @Config.RangeInt(min = 0, max = 200)
            public int sprintRegenDelay = 20;

            @Config.Comment("Interval between stamina regen ticks (Normal)")
            @Config.RangeInt(min = 0, max = 100)
            public int regenIntervalNormal = 6;

            @Config.Comment("Interval between stamina regen ticks (With Boost)")
            @Config.RangeInt(min = 0, max = 100)
            public int regenIntervalBoost = 4;

            @Config.Comment("Amount of stamina drained per tick")
            @Config.RangeInt(min = 0, max = 10)
            public int staminaDrainAmount = 1;

            @Config.Comment("Amount of stamina regenerated per tick")
            @Config.RangeInt(min = 0, max = 10)
            public int staminaRegenAmount = 1;

            @Config.Comment("If true, maximum stamina is limited by player's food level")
            public boolean limitByFood = true;
        }

        public static class LowStaminaEffects {
            @Config.Comment("Max timer for low stamina effects")
            public int lowStaminaTimerMax = 100;

            @Config.Comment("Slowness effect duration in ticks")
            @Config.RangeInt(min = 1, max = 200)
            public int slownessDuration = 10;

            @Config.Comment("Slowness effect amplifier")
            @Config.RangeInt(min = 0, max = 10)
            public int slownessAmplifier = 2;

            @Config.Comment("Mining fatigue duration in ticks")
            @Config.RangeInt(min = 1, max = 200)
            public int fatigueDuration = 10;

            @Config.Comment("Mining fatigue amplifier")
            @Config.RangeInt(min = 0, max = 10)
            public int fatigueAmplifier = 0;

            @Config.Comment("Weakness effect duration in ticks")
            @Config.RangeInt(min = 1, max = 200)
            public int weaknessDuration = 10;

            @Config.Comment("Weakness effect amplifier")
            @Config.RangeInt(min = 0, max = 10)
            public int weaknessAmplifier = 0;
        }

        public static class OverlaySettings {
            @Config.Comment("Use Alternative Stamina HUD")
            public boolean useAlternativeStaminaHUD = false;

            @Config.Comment("X offset of the stamina bar from the right edge of the screen")
            @Config.RangeInt(min = -5000, max = 5000)
            public int overlayBaseX = 0;

            @Config.Comment("Y offset of the stamina bar from the bottom edge of the screen")
            @Config.RangeInt(min = -5000, max = 5000)
            public int overlayBaseY = 0;

            @Config.Comment("X offset of the alternative stamina bar from the screen center")
            @Config.RangeInt(min = -5000, max = 5000)
            public int altOverlayBaseX = 0;

            @Config.Comment("Y offset of the alternative stamina bar from its base position")
            @Config.RangeInt(min = -5000, max = 5000)
            public int altOverlayBaseY = 0;

            @Config.Comment("X offset of the dash cooldown bar from the screen center")
            @Config.RangeInt(min = -5000, max = 5000)
            public int dashBarBaseX = 0;

            @Config.Comment("Y offset of the dash cooldown bar from the screen center")
            @Config.RangeInt(min = -5000, max = 5000)
            public int dashBarBaseY = 0;

            @Config.Comment("Additional Y offset of the alt-HUD dash bar (positive = down, negative = up)")
            @Config.RangeInt(min = -5000, max = 5000)
            public int altDashBarOffsetY = 0;
        }

        public static class DashSettings {
            @Config.Comment("Stamina cost per dash")
            @Config.RangeDouble(min = 0.0, max = 100.0)
            public double staminaCost = 4.0;

            @Config.Comment("Delay before stamina starts regenerating after dash")
            @Config.RangeInt(min = 0, max = 200)
            public int staminaRegenDelay = 20;

            @Config.Comment("Speed multiplier applied during dash")
            @Config.RangeDouble(min = 0.0, max = 10.0)
            public double speed = 1.0;

            @Config.Comment("Post-dash invulnerability duration in ticks")
            @Config.RangeInt(min = 0, max = 100)
            public int postTimerTicks = 10;

            @Config.Comment("Default cooldown ticks after a dash")
            @Config.RangeInt(min = 0, max = 1000)
            public int cooldownTicks = 20;

            @Config.Comment("Number of particles spawned during dash")
            @Config.RangeInt(min = 0, max = 100)
            public int particleCount = 15;

            @Config.Comment("Spread of dash particles")
            @Config.RangeDouble(min = 0.0, max = 10.0)
            public double particleSpread = 0.3;

            @Config.Comment("Height offset of dash particles")
            @Config.RangeDouble(min = 0.0, max = 10.0)
            public double particleHeight = 0.1;

            @Config.Comment("Speed of dash particles")
            @Config.RangeDouble(min = 0.0, max = 10.0)
            public double particleSpeed = 0.05;

            @Config.Comment("Y offset for particle spawn position")
            @Config.RangeDouble(min = 0.0, max = 10.0)
            public double particleYOffset = 0.1;

            @Config.Comment("Volume of dash sound")
            @Config.RangeDouble(min = 0.0, max = 5.0)
            public double soundVolume = 1.0;

            @Config.Comment("Pitch of dash sound")
            @Config.RangeDouble(min = 0.0, max = 5.0)
            public double soundPitch = 1.0;

            @Config.Comment("Resistance effect level during dash (0 = I)")
            @Config.RangeInt(min = 0, max = 255)
            public int resistanceLevel = 2;

            @Config.Comment("Resistance effect duration in ticks during dash")
            @Config.RangeInt(min = 0, max = 200)
            public int resistanceDuration = 10;

            @Config.Comment("Weakness effect level during dash (0 = I)")
            @Config.RangeInt(min = 0, max = 255)
            public int weaknessLevel = 1;

            @Config.Comment("Weakness effect duration in ticks during dash")
            @Config.RangeInt(min = 0, max = 200)
            public int weaknessDuration = 10;

            @Config.Comment("Stamina penalty for hitting while dashing")
            @Config.RangeInt(min = 0, max = 100)
            public int attackExtraStamina = 2;

            @Config.Comment("Reset the cooldown for hitting while dashing")
            public boolean attackExtraCooldown = false;

            @Config.Comment("Allow Dash In Water")
            public boolean allowDashInLiquids = false;
        }
    }

    @Config.Name("biomeinfo")
    @Config.Comment("Biome Info Settings")
    public static final BiomeInfoSettings biomeinfo = new BiomeInfoSettings();

    public static class BiomeInfoSettings {
        @Config.Comment("true if the biome info should be shown, false otherwise")
        public boolean enabled = true;

        @Config.Comment("true if the biome info should fade out shortly after a different biome has been entered. If this is set to false, the biome info will stay visible")
        public boolean fadeOut = true;

        @Config.Comment("true if the biome info should fade in when a different biome has been entered")
        public boolean fadeIn = true;

        @Config.Comment("How long in ticks (20 ticks = 1 second) to display the biome info, if fadeOut = true. If fadeIn = true, the time will be counted from the moment the biome info has finished fading in.")
        @Config.RangeInt(min = 0)
        public int displayTime = 30;

        @Config.Comment("The X position to display the biome info at")
        public int posX = 3;

        @Config.Comment("The Y position to display the biome info at")
        public int posY = 3;

        @Config.Comment("The size of the biome info (multiplier)")
        @Config.RangeDouble(min = 0.0)
        public double scale = 1.0;

        @Config.Comment("true if the biome info should be rendered with a shadow, false otherwise")
        public boolean textShadow = true;

        @Config.Comment("The color to display the biome info in (Format: 0xRRGGBB)")
        public int color = 0xFFFFFF;

        @Config.Comment("If true, hides the mod's info text when the debug screen (F3) is open.")
        public boolean hideOnDebugScreen = true;

        @Config.Comment("If true, hides the mod's info text when the game's UI is also hidden (F1).")
        public boolean hideWithUI = true;

        @Config.Comment("The text alignment of the biome info.")
        public TextAlignment textAlignment = TextAlignment.LEFT;

        @Config.Comment("If true, will automatically format biome names that do not have a proper translation into an English name.")
        public boolean fallbackOnUntranslatableName = true;

        @Config.Comment("If true, will append the mod name the biome is from to the biome name.")
        public boolean appendModName = false;

        @Config.Comment({
                "This lets BiomeInfo automatically determine the correct position of the text without needing to change the posX, posY, or textAlignment configuration settings manually.",
                "If this is set to NONE, then BiomeInfo will use those configuration settings. If this is set to anything other than NONE, they will be ignored."
        })
        public PositionPreset positionPreset = PositionPreset.TOP_LEFT;
    }

    @Config.Name("armor")
    @Config.Comment("Armor Settings")
    public static final ArmorSettings armor = new ArmorSettings();

    public static class ArmorSettings {
        @Config.Comment("Disable registration of marbled armors")
        public boolean disableMarbledArmor = false;

        @Config.Comment("Disable registration of instinct armors")
        public boolean disableInstinctArmor = false;
    }

    @Config.Name("visuals")
    @Config.Comment("Visual Settings (Backported Mods)")
    public static final VisualSettings visuals = new VisualSettings();

    public static class VisualSettings {
        @Config.Comment("Enable the NoHurtFlash mod (Removes red flash when taking damage)")
        public boolean enableNoHurtFlash = true;
    }

    @Config.Name("headshots")
    @Config.Comment("Настройки системы хэдшотов")
    public static final HeadshotSettings headshots = new HeadshotSettings();

    public static class HeadshotSettings {
        @Config.Name("1. Общие настройки (Автоматика)")
        @Config.Comment("Настройки для мобов, которые вычисляются автоматически")
        public final General general = new General();

        @Config.Name("2. Кастомные мобы")
        @Config.Comment({
                "Добавляйте мобов вручную, если автоматика ошибается.",
                "Формат: modid:name, forwardOffset, yOffset, radius",
                "Пример: minecraft:cow, 0.6, 1.1, 0.25"
        })
        public String[] customMobs = new String[] {
                "minecraft:cow, 0.6, 1.1, 0.25",
                "minecraft:sheep, 0.5, 1.0, 0.25",
                "minecraft:pig, 0.5, 0.8, 0.25"
        };

        public static class General {
            @Config.Comment("Урон будет умножаться на это число")
            public float damageMultiplier = 2.5F;

            @Config.Comment("Базовый множитель размера хитбокса головы")
            public double baseRadius = 0.25D;

            @Config.Comment("[Гуманоиды] Смещение по Y (прибавляется к высоте глаз)")
            public double humanoidYOffset = 0.05D;

            @Config.Comment("[Четвероногие] Смещение по Y (в процентах от роста, 0.5 = половина)")
            public double quadrupedYOffsetMult = 0.5D;

            @Config.Comment("[Четвероногие] Доп. смещение по высоте (в блоках)")
            public double quadrupedYOffsetAdd = 0.1D;

            @Config.Comment("[Четвероногие] Смещение вперед (в процентах от ширины, 0.5 = передняя грань)")
            public double quadrupedForwardOffsetMult = 0.5D;
        }
    }

    @Config.Name("techguns")
    @Config.Comment("Techguns2 Port Settings")
    public static final TechgunsSettings techguns = new TechgunsSettings();

    public static class TechgunsSettings {
        @Config.Name("structures")
        @Config.Comment("Настройки шанса спавна структур (0 - выключить)")
        public final StructureSettings structures = new StructureSettings();

        @Config.Name("spawners")
        @Config.Comment("Настройки моб спавнеров")
        public final SpawnerSettings spawners = new SpawnerSettings();

        @Config.Name("crates")
        @Config.Comment("Настройки лута из ящиков. Формат: modid:itemid,chance(от 0.0 до 1.0)")
        public final CrateSettings crates = new CrateSettings();

        public static class StructureSettings {
            @Config.Comment("Modulus chunk frequency for small structures. Default was 20. Lower = more frequent.")
            public int spawnWeightSmall = 6;
            @Config.Comment("Modulus chunk frequency for medium structures. Default was 50. Lower = more frequent.")
            public int spawnWeightMedium = 16;
            @Config.Comment("Modulus chunk frequency for big structures. Default was 100. Lower = more frequent.")
            public int spawnWeightBig = 31;

            public int factoryHouseSmallWeight = 10;
            public int smallTrainstationWeight = 10;
            public int smallMineWeight = 10;
            public int gasStationWeight = 10;

            public int alienBugNestWeight = 20;
            public int policeStationWeight = 10;
            public int survivorHideoutWeight = 10;
            public int oreClusterSpikeWeight = 10;
            public int oreClusterMeteorBasisWeight = 5;
            public int desertOilClusterWeight = 15;

            public int militaryBaseWeight = 1;
            public int castleWeight = 1;
            public int aircraftCarrierWeight = 1;

            public int netherAltarSmallWeight = 10;
            public int netherSoulPlatformWeight = 10;
            public int netherLoot01Weight = 10;
            public int netherAcidHoleWeight = 10;
            public int netherOreClusterSmallWeight = 10;

            public int netherAltarMediumWeight = 10;
            public int netherGhastSpawnerWeight = 10;
            public int netherOreClusterCastleWeight = 10;
        }

        public static class SpawnerSettings {
            @Config.Comment("Список мобов, которые могут спавниться. Формат: modid:entity")
            public String[] spawnerMobs = new String[] {
                    "minecraft:zombie",
                    "minecraft:skeleton"
            };
            @Config.Comment("Максимальное количество живых мобов из одного спавнера одновременно")
            public int maxMobsAlive = 5;
            @Config.Comment("Количество волн мобов до уничтожения спавнера")
            public int waves = 3;
            @Config.Comment("Задержка между спавном волн (в тиках)")
            public int delay = 100;
        }

        public static class CrateSettings {
            @Config.Comment("Медицинский ящик (MEDICAL). Число - вес вероятности.")
            public String[] medicalCrateLoot = new String[] {
                    "minecraft:apple,10"
            };

            @Config.Comment("Оружейный ящик (GUN). Число - вес вероятности.")
            public String[] weaponCrateLoot = new String[] {
                    "minecraft:iron_sword,10"
            };

            @Config.Comment("Ящик с патронами (AMMO). Число - вес вероятности.")
            public String[] ammoCrateLoot = new String[] {
                    "minecraft:arrow,10"
            };

            @Config.Comment("Ящик с броней (ARMOR). Число - вес вероятности.")
            public String[] armorCrateLoot = new String[] {
                    "minecraft:iron_chestplate,10"
            };

            @Config.Comment("Ящик со взрывчаткой (EXPLOSIVE). Число - вес вероятности.")
            public String[] explosiveCrateLoot = new String[] {
                    "minecraft:tnt,10"
            };

            @Config.Comment("Обычный ящик (GENERIC). Число - вес вероятности.")
            public String[] genericCrateLoot = new String[] {
                    "minecraft:coal,10"
            };
        }
    }

    @Config.Name("combat_feedback")
    @Config.Comment("Combat Feedback (Hitmarkers & Hit Sounds)")
    public static final CombatFeedbackSettings combatFeedback = new CombatFeedbackSettings();

    public static class CombatFeedbackSettings {
        @Config.Name("enable_hitmarkers")
        @Config.Comment("Enable visual hitmarkers on hit/headshot/kill")
        public boolean enableHitmarkers = true;

        @Config.Name("enable_hit_sounds")
        @Config.Comment("Enable audio hit and kill sound effects")
        public boolean enableHitSounds = true;
    }

    @Config.Name("item_inspect")
    @Config.Comment("3D Item Inspection & Customization Settings")
    public static final ItemInspectSettings itemInspect = new ItemInspectSettings();

    public static class ItemInspectSettings {
        @Config.Name("global_customization_weapon_scale")
        @Config.Comment("Global scale multiplier for weapons rendered in the weapon modification/customization GUI")
        @Config.RangeDouble(min = 0.1, max = 5.0)
        public double globalCustomizationWeaponScale = 1.0;

        @Config.Name("global_inspect_scale")
        @Config.Comment("Global scale multiplier for all 3D items rendered in the inspect GUI")
        @Config.RangeDouble(min = 0.1, max = 5.0)
        public double globalInspectScale = 1.0;

        @Config.Name("enable_inspect_maker")
        @Config.Comment("Enable the in-game 3D inspect transform maker GUI and settings button [M]")
        public boolean enableInspectMaker = false;
    }
}

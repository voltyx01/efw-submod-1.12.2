package com.voltyx.mwccf.geo;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * HeartbeatManager — полная система сердцебиения.
 * BPM обновляется раз в UPDATE_INTERVAL мс с лёгким хаосом.
 * Данные основаны на heartbeatguide.txt + реальных entity ID из SRP 1.10.7.
 */
@SideOnly(Side.CLIENT)
public class HeartbeatManager {

    // === Публичные данные ===
    public static float currentBPM = 65.0f;
    public static int displayBPM = 65;
    public static final List<Long> spikes = new ArrayList<>();

    // === Взрывы / урон — прямой импульс ===
    /** BPM добавленный взрывом, затухает со временем */
    public static float explosionImpulse = 0.0f;
    /** BPM добавленный ударом, затухает за ~1 сек */
    public static float damageImpulse    = 0.0f;

    // === Внутренние переменные ===
    private static long lastUpdateTime = 0;
    private static long lastBeatTime   = 0;

    // Задержка между обновлениями BPM (0.4–0.6 сек)
    private static final float UPDATE_INTERVAL_BASE = 0.5f;
    private static float nextUpdateIn = 0f;
    private static float displayNoiseUpdateIn = 0f;
    private static final Random rng = new Random();

    // Адаптация
    public static float shortAdaptLow = 0f;
    public static float shortAdaptMid = 0f;
    public static float shortAdaptHigh = 0f;

    public static float longAdaptLow = 0f;
    public static float longAdaptMid = 0f;
    public static float longAdaptHigh = 0f;

    public static long lastSeenLowMs = 0;
    public static long lastSeenMidMs = 0;
    public static long lastSeenHighMs = 0;

    private static boolean loadedAdaptation = false;
    private static long lastSaveTime = 0;

    public static void saveAdaptation() {
        try {
            java.io.File f = new java.io.File("config/mwccf_adaptation.txt");
            f.getParentFile().mkdirs();
            java.io.FileWriter w = new java.io.FileWriter(f);
            w.write(longAdaptLow + "\n" + longAdaptMid + "\n" + longAdaptHigh);
            w.close();
        } catch(Exception e) {}
    }

    public static void loadAdaptation() {
        try {
            java.io.File f = new java.io.File("config/mwccf_adaptation.txt");
            if (f.exists()) {
                java.util.List<String> lines = java.nio.file.Files.readAllLines(f.toPath());
                if (lines.size() >= 3) {
                    longAdaptLow = Float.parseFloat(lines.get(0));
                    longAdaptMid = Float.parseFloat(lines.get(1));
                    longAdaptHigh = Float.parseFloat(lines.get(2));
                }
            }
        } catch(Exception e) {}
    }

    // Оружие и стрельба
    private static int   lastAmmo           = -1;
    private static Object lastWeaponInstance = null;
    private static float shootingHeat       = 0.0f; // 0..8, плавно нарастает
    private static int decayDelayUpdates    = 0; // Для задержки в 2 секунды (4 тика по 0.5с)

    // Первые встречи с мобами SRP
    private static final Set<String> seenSrpMobs = new HashSet<>();

    // =====================================================================
    //  SRP ENTITY CLASSIFICATION
    //  Имена получены из SRPEntities$RegistrationHandler — формат "srparasites.NAME"
    // =====================================================================

    private static final Set<String> SRP_IGNORE = new HashSet<>(Arrays.asList(
        "buglin"
    ));

    /** Tier LOW (слабые, статичные, мелкие): +1 за каждые 2 блока, макс +8 */
    private static final Set<String> SRP_LOW = new HashSet<>(Arrays.asList(
        // Crude (примитивные, слабые)
        "heed", "mes", "quac", "leer", "done", "inhoos", "inhoom",
        // Infected Heads (обычные головы, статичные)
        "inf_cow_head", "inf_horse_head", "inf_pig_head", "inf_sheep_head",
        "inf_wolf_head", "inf_villager_head", "inf_enderman_head",
        // Small infected
        "dorpa",
        // Carrier / swarm
        "carrier_worm", "airscrew",
        // Feral (зараженные животные — слабые)
        "fer_bear", "fer_cow", "fer_horse", "fer_pig", "fer_sheep", "fer_wolf",
        "fer_enderman",
        // Incomplete/Dredge
        "crux_incomplete", "dredge"
    ));

    /** Tier MID (assimara и assimilated, адаптированные): +1 за блок, макс +13 */
    private static final Set<String> SRP_MID = new HashSet<>(Arrays.asList(
        // Moved from LOW
        "crux", "thrall",
        // Adapted (assimara)
        "ada_longarms", "ada_manducater", "ada_reeker", "ada_yelloweye",
        "ada_summoner", "ada_bolster", "ada_tozoon", "ada_arachnida",
        "ada_devourer", "ada_vermin", "ada_viscera", "ada_burrower",
        // Infected (assimilated hosts)
        "inf_bear", "inf_cow", "inf_horse", "inf_pig", "inf_sheep",
        "inf_wolf", "inf_enderman", "inf_human", "inf_villager", "inf_squid",
        "inf_player", "inf_dragon_e",
        // Special (продвинутые заражённые)
        "spe_bear", "spe_cow", "spe_enderman", "spe_human", "spe_sheep", "spe_villager",
        // Infected heads special
        "inf_human_head", "inf_player_head", "inf_dragon_e_head",
        // Deterrent B-class
        "dodt", "leemb", "nak", "rof", "tonro", "unvo",
        // Feral humans/villagers (опаснее)
        "fer_human", "fer_villager",
        // Inborn (born parasites)
        "ata", "buthol", "gothol", "kol", "lodo", "mudo", "nuuh", "rathol", "viin"
    ));

    /** Tier HIGH (высшие, опасные): +2 за блок, макс +19 */
    private static final Set<String> SRP_HIGH = new HashSet<>(Arrays.asList(
        // Primitive (настоящие паразиты первого порядка)
        "pri_longarms", "pri_manducater", "pri_reeker", "pri_yelloweye",
        "pri_summoner", "pri_bolster", "pri_tozoon", "pri_arachnida",
        "pri_devourer", "pri_vermin", "pri_viscera", "pri_burrower",
        // Pure (чистые — самые страшные)
        "overseer", "vigilante", "warden", "bomber_light", "marauder", "monarch",
        "grunt", "bomber_heavy", "wraith", "bogle", "haunter",
        "carrier_colony", "succor", "seeker", "architect",
        // Ancient (древние)
        "anc_dreadnaut", "anc_overlord", "anc_pod", "anc_dreadnaut_ten",
        // Deterrent Nexus (боссы)
        "dod", "dodsii", "dodsiii", "dodsiv",
        "leem", "leemsii", "leemsiii", "leemsiv",
        "venkrol", "venkrolsii", "venkrolsiii", "venkrolsiv",
        // Derived (производные)
        "kirin", "draconite"
    ));

    // =====================================================================
    //  PUBLIC API
    // =====================================================================

    /** Вызывается HeartbeatEventHandler при взрыве рядом */
    public static void notifyExplosion(float damage) {
        // damage ~ 0..20. Спайк 20..30 BPM.
        float spike = 20f + Math.min(1f, damage / 15f) * 10f;
        explosionImpulse = Math.max(explosionImpulse, spike);
    }

    /** Вызывается HeartbeatEventHandler при получении любого урона */
    public static void notifyDamage(float damage) {
        // Урон ~ +20 BPM (из гайда)
        damageImpulse = Math.max(damageImpulse, 20f);
    }

    // =====================================================================
    //  MAIN UPDATE — вызывается каждый тик из BraceletInspectHandler
    // =====================================================================

    public static void update(boolean uiVisible, boolean isMWCWeapon, boolean isBackgroundRunning) {
        long now = System.currentTimeMillis();

        if (lastUpdateTime == 0) {
            lastUpdateTime = now;
            nextUpdateIn = UPDATE_INTERVAL_BASE;
        }

        float dt = Math.min((now - lastUpdateTime) / 1000.0f, 0.1f);
        lastUpdateTime = now;

        Minecraft mc = Minecraft.getMinecraft();
        net.minecraft.entity.player.EntityPlayer player = mc.player;
        if (player == null) return;

        if (!loadedAdaptation) {
            loadAdaptation();
            loadedAdaptation = true;
        }
        if (now - lastSaveTime > 60000) {
            saveAdaptation();
            lastSaveTime = now;
        }

        if (player.getHealth() <= 0 || player.isDead) {
            currentBPM = 0;
            displayBPM = 0;
            shortAdaptLow = 0f;
            shortAdaptMid = 0f;
            shortAdaptHigh = 0f;
            return;
        }

        // ── Оружие ──────────────────────────────────────────────────────
        com.paneedah.weaponlib.PlayerWeaponInstance weaponInstance = null;
        if (isMWCWeapon) {
            try {
                weaponInstance = (com.paneedah.weaponlib.PlayerWeaponInstance)
                    com.paneedah.weaponlib.ClientModContext.getContext()
                        .getPlayerItemInstanceRegistry()
                        .getMainHandItemInstance(player, com.paneedah.weaponlib.PlayerWeaponInstance.class);
            } catch (Throwable t) {}
        }

        // ── Стрельба: накапливаем нагрев ────────────────────────────────
        if (weaponInstance != lastWeaponInstance) {
            lastWeaponInstance = weaponInstance;
            lastAmmo = weaponInstance != null ? weaponInstance.getAmmo() : -1;
        } else if (weaponInstance != null) {
            int curAmmo = weaponInstance.getAmmo();
            if (curAmmo < lastAmmo && lastAmmo >= 0) {
                shootingHeat = Math.min(8f, shootingHeat + 2f);
            }
            lastAmmo = curAmmo;
        }
        // Нагрев остывает если не стрелять
        if (shootingHeat > 0) {
            shootingHeat = Math.max(0f, shootingHeat - 2f * dt);
        }

        // ── Импульсы: BPM сразу, без ceiling ─────────────────────────────
        // Применяем накопившиеся спайки — они прибавляются прямо к BPM
        if (damageImpulse > 0) {
            currentBPM += damageImpulse;
            damageImpulse = 0f;
        }
        if (explosionImpulse > 0) {
            currentBPM = Math.max(currentBPM, 175f);
            decayDelayUpdates = 6; // Задержка 3 секунды после взрыва
            explosionImpulse = 0f;
        }

        // ── Обновление BPM с интервалом (хаотичность) ───────────────────
        nextUpdateIn -= dt;
        if (nextUpdateIn <= 0) {
            // Следующий интервал: 0.35..0.65 секунды
            nextUpdateIn = UPDATE_INTERVAL_BASE * (0.7f + rng.nextFloat() * 0.6f);
            recalcBPM(player, weaponInstance, mc);
        }

        // Обновление цифр на дисплее строго раз в секунду
        displayNoiseUpdateIn -= dt;
        if (displayNoiseUpdateIn <= 0) {
            displayNoiseUpdateIn = 1.0f; // Раз в секунду
            int currentDisplayNoise = (rng.nextInt(7) - 3); // -3 to +3
            if (currentDisplayNoise == 0) currentDisplayNoise = 1; // force some movement
            displayBPM = Math.max(58, (int)currentBPM + currentDisplayNoise);
        }

        // ── Жёсткие границы ─────────────────────────────────────────────
        if (currentBPM > 180f) currentBPM = 180f;
        if (currentBPM < 58f)  currentBPM = 58f;
        if (displayBPM > 180) displayBPM = 180;

        // ── Сердцебиение ─────────────────────────────────────────────────
        long beatInterval = (long)(60000L / currentBPM);
        if (now - lastBeatTime >= beatInterval) {
            lastBeatTime = now;
            spikes.add(now);

            if (uiVisible || isBackgroundRunning || isMWCWeapon) {
                float volume = isMWCWeapon ? BraceletSettings.mwcWeaponVolume
                             : uiVisible   ? BraceletSettings.inspectVolume
                                           : BraceletSettings.backgroundVolume;
                if (volume > 0f) {
                    mc.getSoundHandler().playSound(
                        new PositionedSoundRecord(
                            new ResourceLocation("mwccf", "bracelet.displayhbeat"),
                            net.minecraft.util.SoundCategory.MASTER, volume, 1.0F,
                            false, 0,
                            net.minecraft.client.audio.ISound.AttenuationType.NONE,
                            0f, 0f, 0f
                        )
                    );
                }
            }
        }
        spikes.removeIf(t -> now - t > 3000);

        // ── Плавающий прицел (дрифт) на высоких значениях ───────────────
        if (currentBPM >= 160f && !mc.isGamePaused() && mc.inGameHasFocus) {
            float driftAmp = (currentBPM - 160f) / 20f; // 160→0, 180→1
            float t = player.ticksExisted * 0.05f;
            
            float yawDrift = (float)Math.sin(t * 1.1f + 123.4f) * driftAmp * 0.25f;
            float pitchDrift = (float)Math.cos(t * 0.8f + 567.8f) * driftAmp * 0.25f;
            
            player.rotationYaw += yawDrift;
            player.rotationPitch += pitchDrift;
            
            if (player.rotationPitch > 90f) player.rotationPitch = 90f;
            if (player.rotationPitch < -90f) player.rotationPitch = -90f;
        }

        // ── Паника / Страх из SRP при критическом сердцебиении ─────────
        if (currentBPM >= 165f && player.ticksExisted % 40 == 0) {
            if (net.minecraftforge.fml.common.Loader.isModLoaded("srparasites")) {
                try {
                    net.minecraft.potion.Potion fear = com.dhanantry.scapeandrunparasites.init.SRPPotions.FEAR_E;
                    if (fear != null && !player.isPotionActive(fear)) {
                        player.addPotionEffect(new net.minecraft.potion.PotionEffect(fear, 100, 0, false, false));
                    }
                } catch (Throwable t) {}
            }
        }

        VisualEffectsHandler.updateCameraOverhaul(currentBPM);
    }

    // =====================================================================
    //  RECALC — вычисляет цель BPM и плавно двигает currentBPM к ней
    // =====================================================================

    private static void recalcBPM(
            net.minecraft.entity.player.EntityPlayer player,
            com.paneedah.weaponlib.PlayerWeaponInstance weaponInstance,
            Minecraft mc) {

        float interval = UPDATE_INTERVAL_BASE;
        // Шум (всегда +/- 3 ударов)
        float noise = (rng.nextFloat() - 0.5f) * 6f;

        float growthRate  = 0f;   // BPM/сек от накопительных факторов
        float baseCeiling = 65f;  // потолок от факторов окружения/угроз
        float activeFloor = 65f;
        boolean isAiming  = false;

        // ── Прицеливание ────────────────────────────────────────────────
        if (weaponInstance != null && weaponInstance.isAimed()) {
            isAiming = true;
            activeFloor = 60f;
            if (currentBPM < 90f) {
                growthRate -= 2f;
            } else {
                growthRate += 1f;
                baseCeiling += 10f;
            }
        }

        // ── Спринт ──────────────────────────────────────────────────────
        if (player.isSprinting()) {
            growthRate += 2f;
            baseCeiling += 15f;
        }

        // ── Темнота ──────────────────────────────────────────────────────
        if (mc.world.getLight(player.getPosition()) < 4) {
            growthRate += 2f;
            baseCeiling += 15f;
        }

        // ── Тесное пространство ──────────────────────────────────────────
        if (isEnclosed(player)) {
            growthRate += 1f;
            baseCeiling += 13f;
        }

        // ── Здоровье ────────────────────────────────────────────────────
        float hp = player.getHealth() / player.getMaxHealth();
        if (hp < 0.25f) {
            growthRate += 11f;
            baseCeiling += 65f;
        } else if (hp < 0.50f) {
            growthRate += 3f;
            baseCeiling += 35f;
        }

        // ── Эффекты ──────────────────────────────────────────────────────
        boolean hasMorphine = false;
        for (net.minecraft.potion.PotionEffect eff : player.getActivePotionEffects()) {
            if (eff.getPotion().getRegistryName() == null) continue;
            String n = eff.getPotion().getRegistryName().toString().toLowerCase();
            if (n.contains("panic")) {
                growthRate += 12f;
                baseCeiling += 85f;
            } else if (n.contains("fear")) {
                growthRate += 6f;
                baseCeiling += 55f;
            } else if (n.contains("adrenaline")) {
                growthRate += 10f;
                baseCeiling += 45f;
            } else if (n.contains("energy_boost")) {
                growthRate += 4f;
                baseCeiling += 25f;
            } else if (n.contains("morphine")) {
                hasMorphine = true;
            }
        }

        // ── First Aid переломы ───────────────────────────────────────────
        if (!hasMorphine && net.minecraftforge.fml.common.Loader.isModLoaded("firstaid")) {
            try {
                ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel damageModel = 
                    (ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel) player.getCapability(ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem.INSTANCE, null);
                if (damageModel != null) {
                    int brokenParts = 0;
                    for (ichttt.mods.firstaid.api.damagesystem.AbstractDamageablePart part : damageModel) {
                        if (part.currentHealth <= 1.0f) {
                            brokenParts++;
                        }
                    }
                    if (brokenParts > 0) {
                        growthRate += Math.min(8f, brokenParts * 2.5f);
                        baseCeiling += Math.min(45f, brokenParts * 15f);
                    }
                }
            } catch (Throwable t) {}
        }

        // ── Паразиты / мобы ──────────────────────────────────────────────
        MobStats mob = getMobStats(player);

        if (mob.maxParasiteFactor > 0) {
            growthRate += mob.maxParasiteFactor;
            baseCeiling += mob.maxParasiteFactor * 2.5f;
        }
        if (mob.hasAggro) {
            growthRate += 8f;
            baseCeiling += 50f;
        } else if (mob.hasVisible && mob.maxParasiteFactor == 0) {
            growthRate += 4f;
            baseCeiling += 30f;
        }
        if (mob.newSrpMobSeen) {
            currentBPM += 30f;
        }

        // ── Стрельба — умный cap ─────────────────────────────────────────
        // Стрельба добавляет до +20 BPM ПОВЕРХ baseCeiling от других факторов.
        // Т.е. нельзя одной стрельбой дойти с 65 до 180.
        // Но при уже высоком пульсе от паразитов — стрельба его ещё поднимает.
        float shootingCeiling = baseCeiling + 20f; // макс потолок со стрельбой
        if (shootingCeiling > 160f) shootingCeiling = 160f; // абсолютный лимит стрельбы
        float activeCeiling = baseCeiling; // текущий итоговый потолок

        if (shootingHeat > 0) {
            // shootingHeat 0..8: при максимуме вносит +16/сек что за 0.5 сек = +8 BPM
            growthRate += shootingHeat * 2f;
            activeCeiling = Math.max(activeCeiling, Math.min(shootingCeiling, baseCeiling + shootingHeat * 2.5f));
        } else {
            activeCeiling = baseCeiling;
        }

        // ── Эффект морфина: отнимает 40 от активного BPM/потолка ───────
        if (hasMorphine) {
            activeCeiling = Math.max(65f, activeCeiling - 40f);
            growthRate = Math.min(0f, growthRate - 4f);
        }

        // ── Применяем изменения ──────────────────────────────────────────
        // Быстрый рост (прыжки) вместо медленного накопления
        if (currentBPM < activeCeiling) {
            // Растем очень быстро (сразу почти до потолка)
            float step = Math.max(10f, (activeCeiling - currentBPM) * 0.8f);
            currentBPM += step;
            if (currentBPM > activeCeiling) currentBPM = activeCeiling;
        } else if (hasMorphine && currentBPM > activeCeiling) {
            // При морфине быстро снижаем пульс к новому потолку (не ниже 65)
            float step = Math.max(6f, (currentBPM - activeCeiling) * 0.5f);
            currentBPM = Math.max(Math.max(65f, activeCeiling), currentBPM - step);
        } else if (growthRate < 0 && currentBPM > activeFloor) {
            currentBPM += growthRate * interval;
            if (currentBPM < activeFloor) currentBPM = activeFloor;
        }

        // ── Спад к потолку или к 65 ────
        boolean isActivelyRising = (currentBPM < activeCeiling) || mob.hasAggro || mob.hasVisible || shootingHeat > 0;
        
        if (isActivelyRising) {
            decayDelayUpdates = 6; // Задержка перед спадом (3 секунды)
        } else {
            if (decayDelayUpdates > 0) {
                decayDelayUpdates--;
            } else {
                float decayRate = 3f; // BPM/сек (медленно)
                float decayStep = decayRate * interval;
                
                // Падаем к активному потолку (например 130 при лоу ХП) или к 65
                float targetDecay = Math.max(65f, activeCeiling);
                if (currentBPM > targetDecay) {
                    currentBPM = Math.max(targetDecay, currentBPM - decayStep);
                } else if (currentBPM < 65f && !isAiming) {
                    currentBPM = Math.min(65f, currentBPM + decayStep);
                }
            }
        }
    }

    // =====================================================================
    //  HELPERS
    // =====================================================================

    private static boolean isEnclosed(net.minecraft.entity.player.EntityPlayer p) {
        net.minecraft.util.math.Vec3d eye = p.getPositionEyes(1f);
        for (net.minecraft.util.math.Vec3d d : new net.minecraft.util.math.Vec3d[]{
            new net.minecraft.util.math.Vec3d(0,1,0), new net.minecraft.util.math.Vec3d(0,-1,0),
            new net.minecraft.util.math.Vec3d(1,0,0), new net.minecraft.util.math.Vec3d(-1,0,0),
            new net.minecraft.util.math.Vec3d(0,0,1), new net.minecraft.util.math.Vec3d(0,0,-1)
        }) {
            net.minecraft.util.math.RayTraceResult r = p.world.rayTraceBlocks(eye, eye.add(d.x*5,d.y*5,d.z*5), false, true, false);
            if (r == null || r.typeOfHit != net.minecraft.util.math.RayTraceResult.Type.BLOCK) return false;
        }
        return true;
    }

    private static class MobStats {
        boolean hasVisible       = false;
        boolean hasAggro         = false;
        int     maxParasiteFactor = 0;
        boolean newSrpMobSeen    = false;
    }

    private static MobStats getMobStats(net.minecraft.entity.player.EntityPlayer player) {
        MobStats s = new MobStats();
        net.minecraft.util.math.Vec3d look = player.getLook(1f);

        List<net.minecraft.entity.EntityLivingBase> near =
            player.world.getEntitiesWithinAABB(
                net.minecraft.entity.EntityLivingBase.class,
                player.getEntityBoundingBox().grow(32)); // Увеличен радиус для HIGH тира

        boolean seenLow = false;
        boolean seenMid = false;
        boolean seenHigh = false;

        for (net.minecraft.entity.EntityLivingBase e : near) {
            if (e == player) continue;
            if (!(e instanceof net.minecraft.entity.monster.IMob)) continue;

            // Определяем тир SRP
            String rawId = net.minecraft.entity.EntityList.getEntityString(e);
            if (rawId == null) continue;
            String id = rawId.toLowerCase();

            if (!id.startsWith("srparasites.")) continue;
            String shortName = id.substring("srparasites.".length());

            if (SRP_IGNORE.contains(shortName)) continue;

            int tier;
            if (SRP_HIGH.contains(shortName))      tier = 3;
            else if (SRP_MID.contains(shortName))  tier = 2;
            else if (SRP_LOW.contains(shortName))  tier = 1;
            else                                    tier = 1;

            double dist = player.getDistance(e);
            
            // Если LOW или MID тир и дистанция > 16, игнорируем
            if (dist > 16 && tier != 3) continue;
            
            // Агрессия проверяется без LOS
            boolean aggro = false;
            if (e instanceof net.minecraft.entity.EntityLiving) {
                aggro = (((net.minecraft.entity.EntityLiving) e).getAttackTarget() == player);
            }

            // LOS
            if (!player.canEntityBeSeen(e)) {
                if (aggro) s.hasAggro = true;
                continue;
            }

            // FOV: dot > 0.3 ≈ 72° half-cone
            net.minecraft.util.math.Vec3d toE = new net.minecraft.util.math.Vec3d(
                e.posX - player.posX,
                (e.posY + e.getEyeHeight() * 0.5) - (player.posY + player.getEyeHeight()),
                e.posZ - player.posZ
            ).normalize();
            if (look.dotProduct(toE) < 0.3) {
                if (aggro) s.hasAggro = true;
                continue;
            }

            s.hasVisible = true;
            if (aggro) s.hasAggro = true;
            
            if (tier == 3) seenHigh = true;
            else if (tier == 2) seenMid = true;
            else seenLow = true;

            int steps;
            if (tier == 3) {
                steps = (int)(32.0 - Math.min(dist, 32.0));
            } else {
                steps = (int)(16.0 - Math.min(dist, 16.0));
            }

            float redHigh = shortAdaptHigh * 0.3f + longAdaptHigh * 0.5f;
            float redMid  = shortAdaptMid * 0.3f + longAdaptMid * 0.5f;
            float redLow  = shortAdaptLow * 0.4f + longAdaptLow * 0.6f; // Для LOW тира скидка может дойти до 100%

            int bonus;
            if (tier == 3) {
                float rawHigh = steps * (19.0f / 32.0f); // Max 19 at dist 0
                bonus = (int)(rawHigh * (1f - redHigh));
            } else if (tier == 2) {
                float rawMid = Math.min(13f, steps);
                bonus = (int)(rawMid * (1f - redMid));
            } else {
                float rawLow = Math.min(8f, steps / 2.0f);
                bonus = (int)(rawLow * (1f - redLow));
            }
            if (bonus > s.maxParasiteFactor) s.maxParasiteFactor = bonus;

            // Первая встреча
            String cls = e.getClass().getName();
            if (!seenSrpMobs.contains(cls)) {
                seenSrpMobs.add(cls);
                s.newSrpMobSeen = true;
            }
        }
        
        long nowMs = System.currentTimeMillis();

        if (seenHigh) {
            shortAdaptHigh = Math.min(1f, shortAdaptHigh + 1f / 120f); // 1 минута (120 обновлений)
            longAdaptHigh = Math.min(1f, longAdaptHigh + 1f / 2400f); // 20 минут (2400 обновлений)
            lastSeenHighMs = nowMs;
        } else if (nowMs - lastSeenHighMs > 180000) { // 3 минуты
            shortAdaptHigh = 0f;
        }

        if (seenMid) {
            shortAdaptMid = Math.min(1f, shortAdaptMid + 1f / 120f);
            longAdaptMid = Math.min(1f, longAdaptMid + 1f / 2400f);
            lastSeenMidMs = nowMs;
        } else if (nowMs - lastSeenMidMs > 180000) {
            shortAdaptMid = 0f;
        }

        if (seenLow) {
            shortAdaptLow = Math.min(1f, shortAdaptLow + 1f / 120f);
            longAdaptLow = Math.min(1f, longAdaptLow + 1f / 2400f);
            lastSeenLowMs = nowMs;
        } else if (nowMs - lastSeenLowMs > 180000) {
            shortAdaptLow = 0f;
        }

        return s;
    }

    public static float getHeartScale() {
        long t = System.currentTimeMillis() - lastBeatTime;
        float p = Math.min(1f, t / 800.0f);
        return 1f + 0.4f * (float)Math.exp(-p * 6.0f);
    }
}

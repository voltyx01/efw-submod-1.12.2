package com.voltyx.mwccf.sunmoon;

import net.minecraft.world.World;

/**
 * Complete port of B3M_Core sky math and logic.
 *
 * Implements:
 * - Latitude-tilted sun/moon orbit
 * - Realistic celestial angle curve (getModdedCelestialAngle)
 * - Night curve adjustment (getNightHeightAngle)
 * - Sun azimuth heading for sunrise/sunset (getSunHeading)
 * - Configurable sizes and brightness
 */
public class RealisticSunMoon {

    // ─── Configuration ────────────────────────────────────────────────────────

    /**
     * Observer latitude in degrees.
     * Default = 45.0f (mid-latitude).
     * At 45° latitude, noon sun reaches 45° above the horizon (tilted southward).
     */
    public static float latitude = 45.0f;

    /** Fixed axial tilt of the sun's orbit in degrees (0 = no seasonal tilt). */
    public static float sunTilt = 0.0f;

    /** Sun quad half-size. Vanilla = 30.0f. B3M default = 10.0f. */
    public static float sunSize = 15.0f;

    /** Moon quad half-size. Vanilla = 20.0f. */
    public static float moonSize = 15.0f;

    // ─── Sky/Brightness modifiers ─────────────────────────────────────────────

    /** Multiplier for sky color brightness in World.getSkyColorBody(). */
    public static float skyColorMod = 0.75f;

    /** Multiplier for sun brightness in World.getSunBrightnessFactor(). */
    public static float sunBrightnessFactor = 0.5f;

    /** Intensity of sunrise/sunset glow colors in WorldProvider.calcSunriseSunsetColors(). */
    public static float sunriseSunsetMod = 0.3f;

    // ─── Runtime Orbit Geometry ───────────────────────────────────────────────

    public static double seasonalTilt = 0.0;
    public static double drawDist = 100.0;
    public static double seasonalDist = 0.0;

    // Per-frame cache for getModdedCelestialAngle — invalidated when worldTime changes
    private static long  _cachedWorldTime = Long.MIN_VALUE;
    private static float _cachedPartialTicks = -1.0f;
    private static float _cachedCelestialAngle = 0.0f;

    public static void updateSeasonalGeometry() {
        seasonalTilt = sunTilt;
        drawDist     = 100.0 * Math.cos(Math.toRadians(seasonalTilt));
        seasonalDist = 100.0 * Math.sin(Math.toRadians(seasonalTilt));
    }

    // ─── 3D Vector with B3M Rotation Convention ───────────────────────────────

    private static final class B3MVec3d {
        double x, y, z;

        B3MVec3d(double x, double y, double z) {
            this.x = x; this.y = y; this.z = z;
        }

        void rotateAroundZ(double radians) {
            double c = Math.cos(radians), s = Math.sin(radians);
            double nx = x * c + y * s;
            double ny = y * c - x * s;
            x = nx; y = ny;
        }

        void rotateAroundX(double radians) {
            double c = Math.cos(radians), s = Math.sin(radians);
            double ny = y * c + z * s;
            double nz = z * c - y * s;
            y = ny; z = nz;
        }
    }

    // ─── Core Calculations from B3M_Core ───────────────────────────────────────

    /**
     * Calculates the sun's elevation angle above the horizon in degrees.
     * Port of B3M_Core.getSunHeightAngle().
     */
    public static float getSunHeightAngle(float celestialAngle) {
        updateSeasonalGeometry();
        B3MVec3d v = new B3MVec3d(0.0, drawDist, seasonalDist);
        v.rotateAroundZ(Math.toRadians(celestialAngle * 360.0));
        v.rotateAroundX(Math.toRadians(-latitude));
        double horiz = Math.sqrt(v.x * v.x + v.z * v.z);
        return (float) Math.toDegrees(Math.atan2(v.y, horiz));
    }

    /**
     * Calculates night height curve adjustment for realistic dark nights.
     * Port of B3M_Core.getNightHeightAngle().
     */
    public static float getNightHeightAngle(float sunHeight) {
        if (latitude < 0.0f) {
            float d = sunHeight * (latitude + 90.0f) / (latitude + 90.0f - -23.5f);
            float c = (1.0f - latitude / -66.5f) * 2.0f;
            if (c > 1.0f) {
                c = Math.abs(c - 2.0f);
            }
            d = (d - sunHeight) * c + sunHeight;
            return sunHeight > d ? d : sunHeight;
        }
        float d = sunHeight * (latitude - 90.0f) / (latitude - 90.0f - -23.5f);
        float c = (1.0f - latitude / 66.5f) * 2.0f;
        if (c > 1.0f) {
            c = Math.abs(c - 2.0f);
        }
        d = (d - sunHeight) * c + sunHeight;
        return sunHeight > d ? d : sunHeight;
    }

    /**
     * Replaces World.getCelestialAngle() with B3M's realistic celestial curve.
     * Port of B3M_Core.getModdedCelestialAngle().
     */
    public static float getModdedCelestialAngle(World world, long worldTime, float partialTicks) {
        if (world.provider != null && world.provider.getDimension() != 0) {
            return world.provider.calculateCelestialAngle(worldTime, partialTicks);
        }
        if (worldTime == 0L) {
            worldTime = 6000L;
        }

        // Per-frame cache: avoid redundant trig when called multiple times per frame
        // (Dynamic Surroundings fog, Xaero minimap getSunBrightness, etc.)
        if (worldTime == _cachedWorldTime && partialTicks == _cachedPartialTicks) {
            return _cachedCelestialAngle;
        }

        // Base linear time angle (0 = noon, 0.5 = midnight)
        int j = (int)(worldTime % 24000L);
        float celestialAngle = ((float)j + partialTicks) / 24000.0f - 0.25f;
        if (celestialAngle < 0.0f) celestialAngle += 1.0f;
        if (celestialAngle > 1.0f) celestialAngle -= 1.0f;

        float angle = getSunHeightAngle(celestialAngle);
        float f = 0.0f;

        if (celestialAngle < 0.5f && angle >= 0.0f) {
            f = (90.0f - angle) / 360.0f;
        } else if (celestialAngle >= 0.5f && angle >= 0.0f) {
            f = 1.0f - (90.0f - angle) / 360.0f;
        } else if (celestialAngle >= 0.5f && angle < 0.0f) {
            angle = getNightHeightAngle(angle);
            f = 1.0f - (90.0f - angle) / 360.0f;
        } else if (celestialAngle < 0.5f && angle < 0.0f) {
            angle = getNightHeightAngle(angle);
            f = (90.0f - angle) / 360.0f;
        }

        if (f < 0.0f) f += 1.0f;
        if (f > 1.0f) f -= 1.0f;

        // Store in cache
        _cachedWorldTime = worldTime;
        _cachedPartialTicks = partialTicks;
        _cachedCelestialAngle = f;
        return f;
    }

    /**
     * Computes the sun's compass azimuth in degrees (North = 0°, East = 90°).
     * Used for orienting sunrise/sunset glow.
     * Port of B3M_Core.getSunHeading().
     */
    public static float getSunHeading(float celestialAngle) {
        updateSeasonalGeometry();
        B3MVec3d v = new B3MVec3d(0.0, drawDist, seasonalDist);
        v.rotateAroundZ(Math.toRadians(celestialAngle * 360.0));
        v.rotateAroundX(Math.toRadians(-latitude));
        double deg = Math.toDegrees(Math.atan2(v.x, v.z));
        return (float) ((deg + 180.0) % 360.0);
    }
}

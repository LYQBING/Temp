//
// Decompiled by Jadx - 789ms
//
package com.roaroflove.client;

import com.roaroflove.config.RoarOfLoveConfig;
import java.util.UUID;
import net.minecraft.class_310;

public final class RoarEcg {
    public static final int H = 46;
    private static final long INTRO_MS = 1600;
    private static final long SAMPLE_STEP_MS = 5;
    public static final int W = 240;
    public static final int SAMPLES = 520;
    private static final float[] BUF = new float[SAMPLES];
    private static int head = 0;
    private static boolean active = false;
    private static boolean peakNow = false;
    private static UUID activeInstance = null;
    private static long lastEventMs = 0;
    private static volatile long tailUntil = 0;
    private static volatile long introUntil = 0;
    private static double lastX = Double.NaN;
    private static double lastZ = 0.0d;
    private static double moveSpeed = 0.0d;
    private static double exerciseBpm = 0.0d;
    private static double intensity = 0.0d;
    private static volatile long exerciseUntil = 0;
    private static long stageStart = 0;
    private static long lastStageMs = 0;
    private static long periodMs = 900;
    private static long flatlineUntil = 0;
    private static long burstUntil = 0;
    private static long lastSampleMs = 0;
    private static int bpm = 66;

    private RoarEcg() {
    }

    public static boolean isActive() {
        if (!RoarOfLoveConfig.isEcgEffect()) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = active || RoarSubtitles.isAnimating() || isExercising() || (RoarOfLoveConfig.isEcgTail() && currentTimeMillis < tailUntil);
        if (!z || lastEventMs <= 0 || currentTimeMillis - lastEventMs <= 120000) {
            return z;
        }
        return false;
    }

    public static boolean isPeak() {
        return peakNow;
    }

    public static int bpm() {
        return (int) Math.max(30L, Math.min(200L, 60000 / Math.max(1L, RoarBeat.effectivePeriodMs())));
    }

    public static float[] buffer() {
        return BUF;
    }

    public static int headIndex() {
        return head;
    }

    public static float valueAt(long j) {
        double beat;
        if (j < flatlineUntil) {
            return 0.0f;
        }
        float amplitudeFactor = amplitudeFactor(j, RoarOfLoveConfig.immersionFactor(RoarOfLoveConfig.ecgStrength()));
        if (j < burstUntil) {
            double d = j - flatlineUntil;
            return (float) (amplitudeFactor * 1.5d * (1.0d - (d / 900.0d)) * beat((d % 240.0d) / 240.0d));
        }
        if (!active && RoarOfLoveConfig.isEcgTail() && j < tailUntil) {
            float f = ((float) (tailUntil - j)) / 3200.0f;
            amplitudeFactor *= f * f;
        }
        float breathHoldFactor = amplitudeFactor * RoarFilterState.breathHoldFactor();
        double effectivePeriod = effectivePeriod();
        double phaseAt = RoarBeat.phaseAt(j);
        if (RoarOfLoveConfig.isHrv()) {
            phaseAt += ((((((long) ((j - stageStart) / Math.max(1.0d, effectivePeriod))) * 2654435761L) % 1000) / 1000.0d) - 0.5d) * 0.05d;
        }
        double floor = phaseAt - Math.floor(phaseAt);
        if (peakNow) {
            double d2 = floor * 3.0d;
            beat = beat(d2 - Math.floor(d2)) * 1.15d;
        } else {
            beat = beat(floor);
        }
        return ((float) beat) * breathHoldFactor;
    }

    public static void onStart(UUID uuid) {
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = !active;
        active = true;
        activeInstance = uuid;
        introUntil = INTRO_MS + currentTimeMillis;
        RoarBeat.setEpoch(currentTimeMillis);
        RoarPlayerControl.setInstance(activeInstance);
        lastEventMs = currentTimeMillis;
        stageStart = currentTimeMillis;
        peakNow = false;
        flatlineUntil = 0L;
        burstUntil = 0L;
        if (z) {
            periodMs = 900L;
            lastStageMs = 0L;
            lastSampleMs = 0L;
            bpm = 66;
            for (int i = 0; i < 520; i++) {
                BUF[i] = 0.0f;
            }
        }
    }

    public static void onEnd(UUID uuid) {
        if (activeInstance == null || uuid == null || activeInstance.equals(uuid)) {
            tailUntil = System.currentTimeMillis() + 3200;
            RoarPlayerControl.setInstance((UUID) null);
            active = false;
            activeInstance = null;
            peakNow = false;
        }
    }

    public static void onStage(boolean z) {
        long currentTimeMillis = System.currentTimeMillis();
        if (lastStageMs > 0) {
            long j = currentTimeMillis - lastStageMs;
            if (j >= 250 && j <= 9000) {
                periodMs = (j + (periodMs * 2)) / 3;
            }
        }
        active = true;
        lastEventMs = currentTimeMillis;
        lastStageMs = currentTimeMillis;
        stageStart = currentTimeMillis;
        peakNow = z;
        RoarBeat.setEpoch(currentTimeMillis);
        RoarPlayerControl.setInstance(activeInstance);
        long max = Math.max(180L, periodMs);
        bpm = (int) Math.max(30L, Math.min(200L, 60000 / max));
        if (isExercising()) {
            bpm = (int) Math.max(30L, Math.min(200L, 60000 / Math.min(max, exercisePeriod())));
        }
    }

    public static void onReflux() {
        long currentTimeMillis = System.currentTimeMillis();
        active = true;
        lastEventMs = currentTimeMillis;
        flatlineUntil = currentTimeMillis + 300;
        burstUntil = flatlineUntil + 900;
    }

    public static void sample() {
        if (active || isExercising() || RoarSubtitles.isAnimating()) {
            long currentTimeMillis = System.currentTimeMillis();
            if (lastSampleMs == 0) {
                lastSampleMs = currentTimeMillis;
            }
            int i = 0;
            while (currentTimeMillis - lastSampleMs >= SAMPLE_STEP_MS && i < 200) {
                lastSampleMs += SAMPLE_STEP_MS;
                BUF[head] = valueAt(lastSampleMs);
                head = (head + 1) % SAMPLES;
                i++;
            }
            if (i >= 200) {
                lastSampleMs = currentTimeMillis;
            }
        }
    }

    public static void tick() {
        try {
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 == null || method_1551.field_1724 == null) {
                lastX = Double.NaN;
                return;
            }
            double method_23317 = method_1551.field_1724.method_23317();
            double method_23321 = method_1551.field_1724.method_23321();
            if (!Double.isNaN(lastX)) {
                double d = method_23317 - lastX;
                double d2 = method_23321 - lastZ;
                moveSpeed = (Math.sqrt((d * d) + (d2 * d2)) * 0.3d) + (moveSpeed * 0.7d);
            }
            lastX = method_23317;
            lastZ = method_23321;
            long currentTimeMillis = System.currentTimeMillis();
            double min = moveSpeed > 0.2d ? Math.min(1.0d, (moveSpeed - 0.2d) / 0.14d) : 0.0d;
            intensity = ((min > intensity ? 0.008d : 0.005d) * (min - intensity)) + intensity;
            if (intensity < 0.01d && min == 0.0d) {
                intensity = 0.0d;
            }
            if (intensity > 0.02d) {
                exerciseBpm = 45.0d + (11.0d * intensity);
                exerciseUntil = 5000 + currentTimeMillis;
            } else {
                exerciseBpm = 0.0d;
            }
        } catch (Throwable th) {
        }
    }

    public static float exerciseLevel() {
        if (exerciseBpm <= 0.0d) {
            return 0.0f;
        }
        return (float) intensity;
    }

    private static float amplitudeFactor(long j, float f) {
        float exerciseLevel = 0.62f + (0.55f * f) + (0.35f * exerciseLevel());
        if (active || RoarSubtitles.isAnimating()) {
            exerciseLevel *= 1.3f;
        }
        float introFactor = exerciseLevel * introFactor(j);
        if (peakNow) {
            introFactor *= 1.2f;
        }
        float breathHoldFactor = introFactor * RoarFilterState.breathHoldFactor();
        if (!active && RoarOfLoveConfig.isEcgTail() && j < tailUntil) {
            float f2 = ((float) (tailUntil - j)) / 3200.0f;
            return breathHoldFactor * f2 * f2;
        }
        return breathHoldFactor;
    }

    public static boolean animationActive() {
        return active || RoarSubtitles.isAnimating();
    }

    public static boolean isExercising() {
        return exerciseBpm > 0.0d && System.currentTimeMillis() < exerciseUntil + 12000;
    }

    public static long exercisePeriod() {
        if (exerciseBpm <= 0.0d) {
            return 100000L;
        }
        return (long) (60000.0d / Math.max(45.0d, Math.min(56.0d, exerciseBpm)));
    }

    private static long effectivePeriod() {
        long max = Math.max(180L, periodMs);
        return isExercising() ? Math.min(max, exercisePeriod()) : max;
    }

    private static float introFactor(long j) {
        if (introUntil <= 0) {
            return 1.0f;
        }
        long j2 = introUntil - j;
        if (j2 <= 0) {
            return 1.0f;
        }
        float f = ((float) j2) / 1600.0f;
        return 1.0f + (f * 0.65f * f);
    }

    private static double gauss(double d, double d2, double d3) {
        double d4 = (d - d2) / d3;
        return Math.exp(d4 * (-d4));
    }

    private static double beat(double d) {
        return (gauss(d, 0.6d, 0.06d) * 0.42d) + ((((0.0d + (gauss(d, 0.17d, 0.035d) * 0.2d)) - (gauss(d, 0.29d, 0.01d) * 0.1d)) + (gauss(d, 0.34d, 0.022d) * 1.0d)) - (gauss(d, 0.39d, 0.022d) * 0.3d));
    }
}

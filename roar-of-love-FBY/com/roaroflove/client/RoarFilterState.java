//
// Decompiled by Jadx - 854ms
//
package com.roaroflove.client;

import com.nonid.effect.NonStatusEffects;
import com.roaroflove.RoarOfLove;
import com.roaroflove.config.RoarOfLoveConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1293;
import net.minecraft.class_310;
import net.minecraft.class_746;
import net.minecraft.class_7923;

public final class RoarFilterState {
    private static final long BLACK_FADE_IN_MS = 800;
    private static final float BLACK_PEAK = 0.8f;
    private static final long DIP_MS = 5200;
    private static final long FLASH_HOLD_MS = 150;
    private static final long FLASH_MS = 750;
    private static final float FLASH_PEAK = 0.82f;
    private static final long HOLD_MS = 6000;
    private static final long REFLUX_MS = 4200;
    private static final long SHAKE_MS = 700;
    private static final float SUSTAIN_LEVEL = 0.32f;
    private static final long SUSTAIN_SAFETY_MS = 45000;
    private static final Object LOCK = new Object();
    private static final List<Heart> HEARTS = new ArrayList();
    private static volatile long sustainUntil = 0;
    private static volatile long flashUntil = 0;
    private static volatile long blackUntil = 0;
    private static volatile long shakeUntil = 0;
    private static volatile float shakeStrength = 0.0f;
    private static boolean hadPregnant = false;
    private static long lastFrameMs = 0;
    private static int lastFilledAmp = -1;
    private static long forceHeartsUntil = 0;
    private static int heartSpawnLogs = 0;
    private static volatile long refluxUntil = 0;
    private static volatile int cachedLust = -1;
    private static int lastLust = -1;
    private static long lastLustMs = 0;
    private static volatile long dipUntil = 0;
    private static volatile long holdUntil = 0;

    private RoarFilterState() {
    }

    private static long blackTotalMs() {
        return RoarOfLoveConfig.blackSeconds() * 1000;
    }

    public static void triggerFlash() {
        flashUntil = System.currentTimeMillis() + FLASH_MS;
    }

    public static void triggerBlack() {
        blackUntil = System.currentTimeMillis() + blackTotalMs();
    }

    public static void triggerShake(float f) {
        if (RoarOfLoveConfig.isShakeEffect()) {
            shakeStrength = f;
            shakeUntil = System.currentTimeMillis() + SHAKE_MS;
        }
    }

    public static void startSustain() {
        sustainUntil = System.currentTimeMillis() + SUSTAIN_SAFETY_MS;
    }

    public static void stopSustain() {
        sustainUntil = 0L;
    }

    public static int cachedLust() {
        return cachedLust;
    }

    public static void setCachedLust(int i) {
        cachedLust = i;
    }

    public static void triggerReflux() {
        refluxUntil = System.currentTimeMillis() + REFLUX_MS;
    }

    public static float refluxAlpha() {
        long currentTimeMillis = refluxUntil - System.currentTimeMillis();
        if (currentTimeMillis <= 0) {
            return 0.0f;
        }
        float f = ((float) currentTimeMillis) / 4200.0f;
        return f * 0.3f * f;
    }

    public static float refluxProgress() {
        long currentTimeMillis = refluxUntil - System.currentTimeMillis();
        if (currentTimeMillis <= 0) {
            return 1.0f;
        }
        return 1.0f - (((float) currentTimeMillis) / 4200.0f);
    }

    public static void triggerPostPeak() {
        dipUntil = System.currentTimeMillis() + DIP_MS;
    }

    public static float postPeakFactor() {
        if (!RoarOfLoveConfig.isPostPeakDip()) {
            return 1.0f;
        }
        long currentTimeMillis = dipUntil - System.currentTimeMillis();
        if (currentTimeMillis > 0) {
            return ((1.0f - (((float) currentTimeMillis) / 5200.0f)) * 0.45f) + 0.55f;
        }
        return 1.0f;
    }

    public static float fovBreathFactor() {
        if (cachedLust < 0) {
            return 1.0f;
        }
        double immersionFactor = (RoarOfLoveConfig.immersionFactor(RoarOfLoveConfig.breathStrength()) * 0.016d) + 0.006d;
        if (isBreathHolding()) {
            immersionFactor *= 1.6d;
        }
        double currentTimeMillis = System.currentTimeMillis();
        return (float) ((immersionFactor * Math.sin(currentTimeMillis / (RoarOfLoveConfig.isHrv() ? 880.0d + (120.0d * Math.sin(currentTimeMillis / 5200.0d)) : 900.0d))) + 1.0d + (0.005d * RoarBeat.pulse()));
    }

    public static void triggerBreathHold() {
        if (RoarOfLoveConfig.isBreathHold()) {
            holdUntil = System.currentTimeMillis() + HOLD_MS;
        }
    }

    public static void stopBreathHold() {
        holdUntil = 0L;
    }

    public static boolean isBreathHolding() {
        return RoarOfLoveConfig.isBreathHold() && System.currentTimeMillis() < holdUntil;
    }

    public static float breathHoldFactor() {
        return isBreathHolding() ? 0.72f : 1.0f;
    }

    public static float breathFactor() {
        if (!RoarOfLoveConfig.isBreathEffect() || cachedLust < 0) {
            return 1.0f;
        }
        double immersionFactor = 0.03d + (RoarOfLoveConfig.immersionFactor(RoarOfLoveConfig.breathStrength()) * 0.15d);
        double currentTimeMillis = System.currentTimeMillis();
        return (float) ((Math.sin(currentTimeMillis / (RoarOfLoveConfig.isHrv() ? 820.0d + (160.0d * Math.sin(currentTimeMillis / 4300.0d)) : 900.0d)) * immersionFactor) + 1.0d + (0.005d * RoarBeat.pulse()));
    }

    private static double gauss(double d, double d2, double d3) {
        double d4 = (d - d2) / d3;
        return Math.exp(d4 * (-d4));
    }

    public static float heartPulse() {
        double gauss;
        double min;
        if (!RoarOfLoveConfig.isPulseEffect()) {
            return 1.0f;
        }
        double immersionFactor = 0.08d + (RoarOfLoveConfig.immersionFactor(RoarOfLoveConfig.pulseStrength()) * 0.3d);
        double phase = RoarBeat.phase();
        if (RoarOfLoveConfig.pulseStyle() == 3) {
            min = (Math.sin(phase * 6.2831853d) * 0.5d) + 0.5d;
        } else {
            if (RoarOfLoveConfig.pulseStyle() == 1) {
                gauss = gauss(phase, 0.08d, 0.07d);
            } else if (RoarOfLoveConfig.pulseStyle() == 2) {
                gauss = (gauss(phase, 0.29d, 0.05d) * 0.7d) + gauss(phase, 0.05d, 0.05d) + (gauss(phase, 0.17d, 0.05d) * 0.85d);
            } else {
                gauss = (gauss(phase, 0.22d, 0.06d) * 0.55d) + gauss(phase, 0.06d, 0.055d);
            }
            min = Math.min(1.0d, gauss);
        }
        return (float) (1.0d - ((1.0d - min) * immersionFactor));
    }

    private static float flashScale() {
        return RoarOfLoveConfig.strengthScale(RoarOfLoveConfig.flashStrength());
    }

    private static float blackScale() {
        return RoarOfLoveConfig.strengthScale(RoarOfLoveConfig.blackStrength());
    }

    private static float sustainAlpha() {
        if (sustainUntil - System.currentTimeMillis() <= 0) {
            return 0.0f;
        }
        return Math.min(1.0f, ((((float) Math.sin(System.currentTimeMillis() / 240.0d)) * 0.18f) + FLASH_PEAK) * SUSTAIN_LEVEL * flashScale());
    }

    public static void triggerShakeForced(float f) {
        shakeStrength = f;
        shakeUntil = System.currentTimeMillis() + SHAKE_MS;
    }

    private static float shakeFade() {
        long currentTimeMillis = shakeUntil - System.currentTimeMillis();
        if (currentTimeMillis <= 0) {
            return 0.0f;
        }
        float min = Math.min(1.0f, ((float) currentTimeMillis) / 700.0f);
        return min * min;
    }

    public static float shakeYaw() {
        float shakeFade = shakeFade();
        if (shakeFade <= 0.0f) {
            return 0.0f;
        }
        return (float) (shakeFade * Math.sin(System.currentTimeMillis() / 19.0d) * shakeStrength);
    }

    public static float shakePitch() {
        float shakeFade = shakeFade();
        if (shakeFade <= 0.0f) {
            return 0.0f;
        }
        return (float) (shakeFade * Math.cos(System.currentTimeMillis() / 15.0d) * shakeStrength * 0.55d);
    }

    public static float flashAlpha() {
        return Math.max(burstAlpha(), sustainAlpha());
    }

    private static float burstAlpha() {
        long currentTimeMillis = flashUntil - System.currentTimeMillis();
        if (currentTimeMillis <= 0) {
            return 0.0f;
        }
        long j = FLASH_MS - currentTimeMillis;
        if (j <= FLASH_HOLD_MS) {
            return Math.min(1.0f, flashScale() * FLASH_PEAK);
        }
        float max = Math.max(0.0f, Math.min(1.0f, 1.0f - (((float) (j - FLASH_HOLD_MS)) / 600.0f)));
        return Math.min(1.0f, max * flashScale() * FLASH_PEAK * max);
    }

    public static float blackAlpha() {
        long blackTotalMs = blackTotalMs();
        long currentTimeMillis = blackUntil - System.currentTimeMillis();
        if (currentTimeMillis <= 0) {
            return 0.0f;
        }
        long j = blackTotalMs - currentTimeMillis;
        if (j < BLACK_FADE_IN_MS) {
            return Math.min(1.0f, blackScale() * BLACK_PEAK * (((float) j) / 800.0f));
        }
        float f = (((float) currentTimeMillis) / ((float) blackTotalMs)) / 0.15f;
        return Math.min(1.0f, Math.max(0.0f, Math.min(1.0f, f)) * blackScale() * BLACK_PEAK);
    }

    public static void tick() {
        frame();
    }

    public static void frame() {
        int i = -1;
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            long currentTimeMillis = System.currentTimeMillis();
            class_746 class_746Var = method_1551.field_1724;
            if (class_746Var == null) {
                hadPregnant = false;
                lastFrameMs = 0L;
                lastFilledAmp = -1;
                synchronized (LOCK) {
                    HEARTS.clear();
                }
                return;
            }
            float min = lastFrameMs == 0 ? 0.05f : Math.min(0.25f, ((float) (currentTimeMillis - lastFrameMs)) / 1000.0f);
            lastFrameMs = currentTimeMillis;
            try {
                boolean z = class_746Var.method_6112(class_7923.field_41174.method_47983(NonStatusEffects.PREGNANT)) != null;
                if (hadPregnant && !z && RoarOfLoveConfig.isBlackFilter()) {
                    RoarOfLove.LOGGER.info("[roar_of_love] 检测到生产结束 -> 触发黑色滤镜");
                    triggerBlack();
                }
                if (hadPregnant && !z) {
                    stopSustain();
                }
                hadPregnant = z;
            } catch (Throwable th) {
                RoarOfLove.LOGGER.debug("[roar_of_love] 怀孕状态检测异常", th);
            }
            try {
                class_1293 method_6112 = class_746Var.method_6112(class_7923.field_41174.method_47983(NonStatusEffects.FILLED));
                int method_5578 = method_6112 == null ? -1 : method_6112.method_5578();
                if (lastFilledAmp >= 0 && method_5578 > lastFilledAmp && RoarOfLoveConfig.isFlashEffect()) {
                    RoarOfLove.LOGGER.info("[roar_of_love] 液体充盈等级 {} -> {} 触发白色闪屏", Integer.valueOf(lastFilledAmp), Integer.valueOf(method_5578));
                    triggerFlash();
                }
                if (method_5578 > lastFilledAmp && method_5578 >= 3) {
                    triggerBreathHold();
                }
                lastFilledAmp = method_5578;
            } catch (Throwable th2) {
                RoarOfLove.LOGGER.debug("[roar_of_love] 液体充盈检测异常", th2);
            }
            try {
                class_1293 method_61122 = class_746Var.method_6112(class_7923.field_41174.method_47983(NonStatusEffects.ENERGIZED));
                if (method_61122 != null) {
                    i = Math.max(0, method_61122.method_5578());
                }
            } catch (Throwable th3) {
            }
            if (i < 0 && lastLust >= 0 && currentTimeMillis - lastLustMs < 2600) {
                i = lastLust;
            }
            if (i >= 0) {
                lastLust = i;
                lastLustMs = currentTimeMillis;
            }
            cachedLust = i;
            updateHearts(class_746Var, min, currentTimeMillis);
        }
    }

    private static void updateHearts(class_746 class_746Var, float f, long j) {
        int i;
        synchronized (LOCK) {
            HEARTS.removeIf(heart -> {
                return j - heart.born >= heart.life;
            });
            boolean z = j < forceHeartsUntil;
            if (RoarOfLoveConfig.isPinkFilter() || z) {
                try {
                    class_1293 method_6112 = class_746Var.method_6112(class_7923.field_41174.method_47983(NonStatusEffects.ENERGIZED));
                    if (method_6112 != null) {
                        r0 = Math.max(0, method_6112.method_5578());
                    } else if (!z) {
                        r0 = -1;
                    }
                    i = r0;
                } catch (Throwable th) {
                    i = z ? 0 : -1;
                }
            } else {
                i = -1;
            }
            if (i < 0) {
                if (!HEARTS.isEmpty()) {
                    HEARTS.clear();
                }
                return;
            }
            if (HEARTS.size() >= Math.min(18, (i * 2) + 4)) {
                return;
            }
            if (ThreadLocalRandom.current().nextDouble() < (z ? 6.0d : 0.8d + Math.min(2.4d, i * 0.5d)) * f) {
                spawnHeart(j, i);
            }
        }
    }

    private static void spawnHeart(long j, int i) {
        Heart heart = new Heart();
        heart.born = j;
        heart.life = 2400 + ThreadLocalRandom.current().nextLong(1400L);
        heart.x = 0.08f + (ThreadLocalRandom.current().nextFloat() * 0.84f);
        heart.y = 0.94f + (ThreadLocalRandom.current().nextFloat() * 0.16f);
        heart.rise = (ThreadLocalRandom.current().nextFloat() * 0.18f) + 0.3f;
        heart.sway = 0.015f + (ThreadLocalRandom.current().nextFloat() * 0.035f);
        heart.phase = ThreadLocalRandom.current().nextFloat() * 6.28318f;
        heart.size = 13.0f + Math.min(11.0f, i * 1.8f) + (ThreadLocalRandom.current().nextFloat() * 4.0f);
        heart.alpha = 0.36f + Math.min(0.3f, i * 0.05f);
        HEARTS.add(heart);
        if (heartSpawnLogs < 5) {
            heartSpawnLogs++;
            RoarOfLove.LOGGER.info("[roar_of_love] 生成爱心 #{}(等级={} 共{}个 位置={},{} 大小={})", new Object[]{Integer.valueOf(heartSpawnLogs), Integer.valueOf(i), Integer.valueOf(HEARTS.size()), Float.valueOf(heart.x), Float.valueOf(heart.y), Float.valueOf(heart.size)});
        }
    }

    public static void debugBurstHearts() {
        forceHeartsUntil = System.currentTimeMillis() + HOLD_MS;
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (LOCK) {
            for (int i = 0; i < 8; i++) {
                spawnHeart(currentTimeMillis - ThreadLocalRandom.current().nextLong(SHAKE_MS), 1);
            }
        }
    }

    public static List<Heart> hearts() {
        return HEARTS;
    }
}

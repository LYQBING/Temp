//
// Decompiled by Jadx - 608ms
//
package com.roaroflove.client;

import com.roaroflove.config.RoarOfLoveConfig;
import com.roaroflove.sound.RoLSounds;
import net.minecraft.class_1109;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3414;

public final class RoarAmbience {
    private static final long BREATH_PERIOD_MS = 5650;
    private static final class_2960 HEARTBEAT = class_2960.method_60655("roar_of_love", "ambience/heartbeat");
    private static final class_2960 BREATH = class_2960.method_60655("roar_of_love", "ambience/breath");
    private static int lastBeat = -1;
    private static long lastBreathMs = 0;

    private RoarAmbience() {
    }

    public static void tick() {
        class_310 method_1551;
        try {
            if (RoarOfLoveConfig.isAmbienceSound() && (method_1551 = class_310.method_1551()) != null && method_1551.field_1724 != null && method_1551.method_1483() != null) {
                if (RoarFilterState.cachedLust() < 0) {
                    lastBeat = -1;
                    return;
                }
                float lust = RoarFilterState.cachedLust();
                float min = ((Math.min(1.0f, (lust + 1) / 6.0f) * 0.5f) + 0.22f) * ((RoarOfLoveConfig.immersionFactor(RoarOfLoveConfig.ambienceStrength()) * 0.85f) + 0.35f);
                int beatIndex = RoarBeat.beatIndex();
                if (beatIndex != lastBeat) {
                    lastBeat = beatIndex;
                    play("roar_of_love:ambience/heartbeat", (RoarEcg.isPeak() ? 1.25f : 1.0f) * min);
                }
                long currentTimeMillis = System.currentTimeMillis();
                if (lastBreathMs == 0) {
                    lastBreathMs = currentTimeMillis;
                } else if (currentTimeMillis - lastBreathMs >= BREATH_PERIOD_MS) {
                    lastBreathMs = currentTimeMillis;
                    play("roar_of_love:ambience/breath", min * 0.75f);
                }
            }
        } catch (Throwable th) {
        }
    }

    private static void play(String str, float f) {
        class_3414 class_3414Var;
        try {
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null && method_1551.method_1483() != null && method_1551.method_1478() != null) {
                if (method_1551.method_1478().method_14486(class_2960.method_12829(str)).isPresent() && (class_3414Var = RoLSounds.get(str)) != null) {
                    method_1551.method_1483().method_4873(class_1109.method_4757(class_3414Var, 1.0f, Math.max(0.0f, Math.min(1.0f, f))));
                }
            }
        } catch (Throwable th) {
        }
    }
}

//
// Decompiled by Jadx - 617ms
//
package com.roaroflove.client;

import com.roaroflove.config.RoarOfLoveConfig;
import net.minecraft.class_1113;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_638;

public final class RoarSoundDistance {
    private static final float FADE_MAX = 0.82f;
    private static final double FADE_RANGE = 14.0d;
    private static final double FADE_START = 16.0d;
    private static final float WALL_PITCH = 0.9f;
    private static final float WALL_VOLUME = 0.42f;

    private RoarSoundDistance() {
    }

    public static boolean isModSound(class_2960 class_2960Var) {
        if (class_2960Var == null) {
            return false;
        }
        String method_12836 = class_2960Var.method_12836();
        return "roar_of_love".equals(method_12836) || "needsofnature".equals(method_12836);
    }

    public static class_1113 adjust(class_1113 class_1113Var) {
        class_310 method_1551;
        float f;
        float f2;
        if (class_1113Var != null) {
            try {
                if (!(class_1113Var instanceof RoarQuietSound)) {
                    if ((RoarOfLoveConfig.isDistantFade() || RoarOfLoveConfig.isOcclusion()) && isModSound(class_1113Var.method_4775()) && (method_1551 = class_310.method_1551()) != null && method_1551.field_1724 != null) {
                        double method_23317 = method_1551.field_1724.method_23317();
                        double method_23318 = method_1551.field_1724.method_23318() + method_1551.field_1724.method_17682();
                        double method_23321 = method_1551.field_1724.method_23321();
                        double method_4784 = class_1113Var.method_4784();
                        double method_4779 = class_1113Var.method_4779();
                        double method_4778 = class_1113Var.method_4778();
                        double d = method_4784 - method_23317;
                        double d2 = method_4779 - method_23318;
                        double d3 = method_4778 - method_23321;
                        double sqrt = Math.sqrt((d * d) + (d2 * d2) + (d3 * d3));
                        float f3 = 1.0f;
                        if (RoarOfLoveConfig.isDistantFade() && sqrt > FADE_START) {
                            f3 = 1.0f * (1.0f - (((float) Math.min(1.0d, (sqrt - FADE_START) / FADE_RANGE)) * FADE_MAX));
                        }
                        float pitchScale = 1.0f * RoarStyle.pitchScale();
                        if (RoarOfLoveConfig.isOcclusion() && sqrt > 2.5d && blocked(method_1551, method_23317, method_23318, method_23321, method_4784, method_4779, method_4778)) {
                            f2 = WALL_VOLUME * f3;
                            f = pitchScale * WALL_PITCH;
                        } else {
                            f = pitchScale;
                            f2 = f3;
                        }
                        float postPeakFactor = RoarFilterState.postPeakFactor() * f2 * RoarFilterState.breathHoldFactor() * (1.0f + (0.25f * RoarFilterState.overloadLevel())) * (0.96f + (0.08f * RoarBeat.pulse())) * RoarStyle.muffle();
                        float soundVolumeSteps = "roar_of_love".equals(class_1113Var.method_4775().method_12836()) ? postPeakFactor * (RoarOfLoveConfig.soundVolumeSteps(class_1113Var.method_4775().method_12832()) / 10.0f) : postPeakFactor;
                        return (soundVolumeSteps < 0.999f || f < 0.999f) ? new RoarQuietSound(class_1113Var, soundVolumeSteps, f) : class_1113Var;
                    }
                    return class_1113Var;
                }
                return class_1113Var;
            } catch (Throwable th) {
                return class_1113Var;
            }
        }
        return class_1113Var;
    }

    private static boolean blocked(class_310 class_310Var, double d, double d2, double d3, double d4, double d5, double d6) {
        class_638 class_638Var = class_310Var.field_1687;
        if (class_638Var == null) {
            return false;
        }
        for (int i = 1; i <= 3; i++) {
            double d7 = i / 4.0d;
            class_2680 method_8320 = class_638Var.method_8320(new class_2338((int) Math.floor(((d4 - d) * d7) + d), (int) Math.floor(((d5 - d2) * d7) + d2), (int) Math.floor((d7 * (d6 - d3)) + d3)));
            if (method_8320 != null && !method_8320.method_26215()) {
                return true;
            }
        }
        return false;
    }
}

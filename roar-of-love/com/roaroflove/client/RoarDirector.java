//
// Decompiled by Jadx - 518ms
//
package com.roaroflove.client;

import com.roaroflove.config.RoarOfLoveConfig;

public final class RoarDirector {
    private static long startMs = 0;
    private static long learnedMs = 0;
    private static boolean peaked = false;
    private static boolean armed = false;

    private RoarDirector() {
    }

    public static void onAnimationStart() {
        startMs = System.currentTimeMillis();
        peaked = false;
        armed = false;
    }

    public static void onPeak() {
        if (startMs > 0 && !peaked) {
            learnedMs = System.currentTimeMillis() - startMs;
            peaked = true;
        }
    }

    public static void tick() {
        if (RoarOfLoveConfig.isDirector() && learnedMs > 0 && startMs > 0 && !peaked && !armed && RoarEcg.animationActive() && System.currentTimeMillis() - startMs >= learnedMs - 2000) {
            armed = true;
            RoarFilterState.triggerBreathHold();
        }
    }
}

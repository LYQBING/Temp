//
// Decompiled by Jadx - 673ms
//
package com.roaroflove.client;

import com.roaroflove.config.RoarOfLoveConfig;
import net.minecraft.class_332;

public final class RoarPlayerHud {
    private static String toast = "";
    private static long toastUntil = 0;
    private static int drawErrors = 0;

    private RoarPlayerHud() {
    }

    public static void notify(String str) {
        if (str == null) {
            str = "";
        }
        toast = str;
        toastUntil = System.currentTimeMillis() + 1000;
    }

    public static void render(class_332 class_332Var, int i, int i2) {
    }

    public static String statusText() {
        return RoarOfLoveConfig.isEnabled() ? RoarPlayerControl.isFrozen() ? "定住" : "播放" : "关闭";
    }
}

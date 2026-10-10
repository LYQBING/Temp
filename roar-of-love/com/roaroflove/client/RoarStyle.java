//
// Decompiled by Jadx - 742ms
//
package com.roaroflove.client;

import com.roaroflove.config.RoarOfLoveConfig;

public final class RoarStyle {
    public static final int NONE = 0;
    public static final int ORAL = 1;
    public static final int POLE = 3;
    public static final int SELF = 2;
    private static volatile int style = 0;

    private RoarStyle() {
    }

    public static void setGroup(String str) {
        if (str == null || !RoarOfLoveConfig.isAdaptHud()) {
            style = 0;
            return;
        }
        if ("oral".equals(str)) {
            style = 1;
            return;
        }
        if ("self".equals(str)) {
            style = 2;
        } else if ("pole".equals(str)) {
            style = 3;
        } else {
            style = 0;
        }
    }

    public static void clear() {
        style = 0;
    }

    public static int current() {
        if (RoarOfLoveConfig.isAdaptHud()) {
            return style;
        }
        return 0;
    }

    public static float subtitleRate() {
        switch (current()) {
            case ORAL:
                return 1.7f;
            case SELF:
            default:
                return 1.0f;
            case POLE:
                return 0.0f;
        }
    }

    public static float beatScale() {
        return current() == 2 ? 1.3f : 1.0f;
    }

    public static float muffle() {
        switch (current()) {
            case ORAL:
                return 0.78f;
            case SELF:
            default:
                return 1.0f;
            case POLE:
                return 0.86f;
        }
    }

    public static float pitchScale() {
        switch (current()) {
            case ORAL:
                return 0.94f;
            case SELF:
            default:
                return 1.0f;
            case POLE:
                return 0.97f;
        }
    }

    public static boolean cool() {
        return current() == 2;
    }

    public static int tintAlpha() {
        return current() == 3 ? 38 : 0;
    }
}

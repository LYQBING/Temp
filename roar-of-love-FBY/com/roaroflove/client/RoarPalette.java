//
// Decompiled by Jadx - 581ms
//
package com.roaroflove.client;

import com.roaroflove.config.RoarOfLoveConfig;

public final class RoarPalette {
    public static final int BLOOD = 3;
    public static final int COUNT = 4;
    public static final int CYAN = 1;
    public static final int PINK = 0;
    public static final int PURPLE = 2;

    private RoarPalette() {
    }

    public static int accent() {
        if (RoarStyle.cool()) {
            return 6737151;
        }
        switch (RoarOfLoveConfig.palette()) {
            case CYAN:
                return 5888255;
            case PURPLE:
                return 12160255;
            case BLOOD:
                return 16729424;
            default:
                return 16740264;
        }
    }

    public static int soft() {
        if (RoarStyle.cool()) {
            return 11068671;
        }
        switch (RoarOfLoveConfig.palette()) {
            case CYAN:
                return 10152191;
            case PURPLE:
                return 14073599;
            case BLOOD:
                return 16747146;
            default:
                return 16751320;
        }
    }

    public static int danmaku() {
        if (RoarStyle.cool()) {
            return 13168383;
        }
        switch (RoarOfLoveConfig.palette()) {
            case CYAN:
                return 12381951;
            case PURPLE:
                return 14930943;
            case BLOOD:
                return 16756912;
            default:
                return 16759010;
        }
    }

    public static int frame() {
        return accent();
    }

    public static String labelKey() {
        return "roar_of_love.ui.palette_" + RoarOfLoveConfig.palette();
    }
}

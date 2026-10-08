//
// Decompiled by Jadx - 543ms
//
package com.roaroflove.client;

import com.roaroflove.config.RoarOfLoveConfig;

public final class RoarLayout {
    public static final int COUNT = 4;
    private static final int DEF_ECG_X = 6;
    private static final int DEF_ECG_Y = 85;
    private static final int DEF_LUST_X = 2;
    private static final int DEF_LUST_Y = 3;
    private static final int DEF_PREG_X = 2;
    private static final int DEF_PREG_Y = 8;
    private static final int DEF_SUB_X = 97;
    private static final int DEF_SUB_Y = 77;
    public static final int ECG = 2;
    public static final int LUST_BAR = 0;
    public static final int LUST_H = 6;
    public static final int LUST_W = 74;
    public static final int PREGNANCY = 3;
    public static final int PREG_H = 11;
    public static final int PREG_W = 82;
    public static final int SUBTITLE = 1;

    private RoarLayout() {
    }

    public static String labelKey(int i) {
        if (i == 0) {
            return "roar_of_love.ui.pos_lustbar";
        }
        if (i == 1) {
            return "roar_of_love.ui.pos_subtitles";
        }
        return i == 2 ? "roar_of_love.ui.pos_ecg" : "roar_of_love.ui.pos_pregnancy";
    }

    public static int lustBarWidth() {
        return Math.max(28, Math.round(((RoarOfLoveConfig.immersionFactor(RoarOfLoveConfig.lustBarStrength()) * 0.6f) + 0.7f) * 74.0f));
    }

    public static int ecgWidth(int i) {
        return Math.min(240, Math.max(120, i - 24));
    }

    public static int width(int i, int i2) {
        if (i == 0) {
            return lustBarWidth();
        }
        if (i == 1) {
            return RoarSubtitles.boxWidth(i2);
        }
        if (i == 2) {
            return ecgWidth(i2);
        }
        return 82;
    }

    public static int height(int i, int i2) {
        if (i == 0) {
            return 6;
        }
        if (i == 1) {
            return RoarSubtitles.boxHeight();
        }
        return i == 2 ? 46 : 11;
    }

    public static int percentX(int i) {
        if (i == 0) {
            return RoarOfLoveConfig.lustBarX();
        }
        if (i == 1) {
            return RoarOfLoveConfig.subtitleX();
        }
        return i == 2 ? RoarOfLoveConfig.ecgX() : RoarOfLoveConfig.pregX();
    }

    public static int percentY(int i) {
        if (i == 0) {
            return RoarOfLoveConfig.lustBarY();
        }
        if (i == 1) {
            return RoarOfLoveConfig.subtitleY();
        }
        return i == 2 ? RoarOfLoveConfig.ecgY() : RoarOfLoveConfig.pregY();
    }

    public static void setPercent(int i, int i2, int i3) {
        if (i == 0) {
            RoarOfLoveConfig.setLustBarPos(i2, i3);
            return;
        }
        if (i == 1) {
            RoarOfLoveConfig.setSubtitlePos(i2, i3);
        } else if (i == 2) {
            RoarOfLoveConfig.setEcgPos(i2, i3);
        } else {
            RoarOfLoveConfig.setPregPos(i2, i3);
        }
    }

    public static void reset(int i) {
        if (i == 0) {
            RoarOfLoveConfig.setLustBarPos(2, 3);
            return;
        }
        if (i == 1) {
            RoarOfLoveConfig.setSubtitlePos(DEF_SUB_X, DEF_SUB_Y);
        } else if (i == 2) {
            RoarOfLoveConfig.setEcgPos(6, DEF_ECG_Y);
        } else {
            RoarOfLoveConfig.setPregPos(2, DEF_PREG_Y);
        }
    }

    public static void resetAll() {
        for (int i = 0; i < 4; i++) {
            reset(i);
        }
    }

    public static int x(int i, int i2, int i3) {
        return percentToPos(percentX(i), i2 - width(i, i2));
    }

    public static int y(int i, int i2, int i3) {
        return percentToPos(percentY(i), i3 - height(i, i3));
    }

    public static int posToPercent(int i, int i2) {
        if (i2 <= 0) {
            return 0;
        }
        return Math.max(0, Math.min(100, Math.round((100.0f * i) / i2)));
    }

    public static int percentToPos(int i, int i2) {
        if (i2 <= 0) {
            return 0;
        }
        return Math.round((Math.max(0, Math.min(100, i)) * i2) / 100.0f);
    }
}

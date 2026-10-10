//
// Decompiled by Jadx - 1161ms
//
package com.roaroflove.client;

import com.nonid.effect.NonStatusEffects;
import com.roaroflove.RoarOfLove;
import com.roaroflove.client.RoarFilterState;
import com.roaroflove.config.RoarOfLoveConfig;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_5250;
import net.minecraft.class_746;
import net.minecraft.class_7923;
import net.minecraft.class_9779;

public final class RoarVignetteHud {
    private static final float HEART_GROW = 1.18f;
    private static final int PINK = 16738740;
    private static final int STEPS = 26;
    private static final float WIDEN = 1.75f;
    public static final class_2960 ID = class_2960.method_60655("roar_of_love", "pink_filter");
    private static final int BLACK = 0;
    private static int heartDrawLogs = BLACK;
    private static int flashDrawLogs = BLACK;
    private static boolean frameErrorLogged = false;
    private static boolean drawErrorLogged = false;
    private static float lustShown = -1.0f;
    private static long lustShownMs = 0;
    private static int lastLustSegments = -1;
    private static long lustSegFlashUntil = 0;
    private static final float[][] HEART_ROWS = {new float[]{0.5f, 0.13f}, new float[]{0.5f, 0.1f}, new float[]{0.5f, 0.06f}, new float[]{0.5f, 0.06f}, new float[]{0.5f, 0.0f}, new float[]{0.5f, 0.0f}, new float[]{0.5f, 0.0f}, new float[]{0.5f, 0.0f}, new float[]{0.5f, 0.0f}, new float[]{0.45f, 0.0f}, new float[]{0.37f, 0.0f}, new float[]{0.28f, 0.0f}, new float[]{0.19f, 0.0f}, new float[]{0.11f, 0.0f}, new float[]{0.05f, 0.0f}};
    private static volatile long lastHudFrameMs = 0;
    private static int hudSkipTicks = BLACK;
    private static boolean hudSkipReported = false;

    private RoarVignetteHud() {
    }

    private static void frame(class_332 class_332Var, int i, int i2, int i3, int i4) {
        if (i3 > 0) {
            int max = Math.max(2, (Math.min(i, i2) / 3) / STEPS);
            int i5 = BLACK;
            while (true) {
                int i6 = i5;
                if (i6 < STEPS) {
                    float f = 1.0f - (i6 / 26.0f);
                    int i7 = (int) (f * i3 * f);
                    if (i7 > 0) {
                        int i8 = (i7 << 24) | i4;
                        int i9 = i6 * max;
                        class_332Var.method_25294(BLACK, i9, i, i9 + max, i8);
                        class_332Var.method_25294(BLACK, (i2 - i9) - max, i, i2 - i9, i8);
                        class_332Var.method_25294(i9, BLACK, i9 + max, i2, i8);
                        class_332Var.method_25294((i - i9) - max, BLACK, i - i9, i2, i8);
                    }
                    i5 = i6 + 1;
                } else {
                    return;
                }
            }
        }
    }

    private static void reflux(class_332 class_332Var, int i, int i2) {
        float refluxAlpha = RoarFilterState.refluxAlpha();
        if (refluxAlpha > 0.0f && RoarOfLoveConfig.isRefluxEffect()) {
            float min = Math.min(1.0f, refluxAlpha * ((RoarOfLoveConfig.immersionFactor(RoarOfLoveConfig.refluxStrength()) * 1.3f) + 0.35f));
            float refluxProgress = RoarFilterState.refluxProgress();
            int max = Math.max(2, (Math.min(i, i2) / 4) / 9);
            int i3 = (int) (refluxProgress * max * 0.8f);
            for (int i4 = BLACK; i4 < 9; i4++) {
                float f = 1.0f - (i4 / 9);
                int i5 = (int) (f * 255.0f * min * f);
                if (i5 > 0) {
                    int i6 = (i5 << 24) | 16777215;
                    int i7 = i3 + (i4 * max);
                    class_332Var.method_25294(BLACK, i7, i, i7 + max, i6);
                    class_332Var.method_25294(BLACK, (i2 - i7) - max, i, i2 - i7, i6);
                    class_332Var.method_25294(i7, BLACK, i7 + max, i2, i6);
                    class_332Var.method_25294((i - i7) - max, BLACK, i - i7, i2, i6);
                }
            }
        }
    }

    private static int lighten(int i, float f) {
        float max = Math.max(0.0f, Math.min(1.0f, f));
        int i2 = (i >> 16) & 255;
        int i3 = (i >> 8) & 255;
        int i4 = i & 255;
        return ((int) ((max * (255.0f - i4)) + i4)) | (((int) (((255.0f - i2) * max) + i2)) << 16) | (((int) (((255.0f - i3) * max) + i3)) << 8);
    }

    private static float smoothLustFraction(int i) {
        float max = Math.max(0.0f, Math.min(1.0f, i / 3.0f));
        long currentTimeMillis = System.currentTimeMillis();
        if (lustShown < 0.0f) {
            lustShown = max;
            lustShownMs = currentTimeMillis;
            return max;
        }
        float max2 = Math.max(0.0f, Math.min(0.5f, ((float) (currentTimeMillis - lustShownMs)) / 1000.0f));
        lustShownMs = currentTimeMillis;
        if (max > lustShown) {
            lustShown = Math.min(max, (max2 * 4.0f) + lustShown);
        } else {
            lustShown = Math.max(max, lustShown - (max2 * 0.3f));
        }
        return lustShown;
    }

    private static void lustBar(class_332 class_332Var, int i, int i2) {
        if (RoarOfLoveConfig.isLustBar()) {
            int cachedLust = RoarFilterState.cachedLust();
            if (cachedLust < 0) {
                lustShown = -1.0f;
                return;
            }
            float immersionFactor = RoarOfLoveConfig.immersionFactor(RoarOfLoveConfig.lustBarStrength());
            int lustBarWidth = RoarLayout.lustBarWidth();
            int x = RoarLayout.x(BLACK, i, i2);
            int y = RoarLayout.y(BLACK, i, i2);
            class_332Var.method_25294(x - 1, y - 1, x + lustBarWidth + 1, y + 6 + 1, -1610612736);
            class_332Var.method_25294(x, y, x + lustBarWidth, y + 6, 0x50ffffff);
            float smoothLustFraction = smoothLustFraction(cachedLust);
            int i3 = (int) ((lustBarWidth * smoothLustFraction) + 0.5f);
            float heartPulse = RoarFilterState.heartPulse();
            int max = Math.max(110, Math.min(255, (int) (255.0f * (1.0f - ((0.3f + (0.35f * immersionFactor)) * (1.0f - Math.min(1.0f, heartPulse)))))));
            int accent = RoarPalette.accent();
            class_332Var.method_25294(x, y, x + i3, y + 6, (max << 24) | accent);
            if (i3 > 0) {
                long currentTimeMillis = System.currentTimeMillis();
                int min = (int) ((((0.5f + (0.5f * Math.min(1.0f, heartPulse))) * 0.25f) + 0.75f) * max);
                int max2 = (x + i3) - ((int) ((currentTimeMillis / 6) % Math.max(1, i3 + r3)));
                int max3 = max2 + Math.max(6, lustBarWidth / 4);
                int max4 = Math.max(x, max2);
                int min2 = Math.min(x + i3, max3);
                if (min2 > max4) {
                    class_332Var.method_25294(max4, y, min2, y + 6, ((min / 3) << 24) | 16777215);
                }
                if (smoothLustFraction >= 0.999f) {
                    int sin = (int) (((0.5f + (0.5f * ((float) Math.sin(currentTimeMillis / 150.0d)))) * 120.0f) + 60.0f);
                    class_332Var.method_25294(x, y - 1, x + lustBarWidth, y, (Math.min(255, sin) << 24) | accent);
                    class_332Var.method_25294(x, y + 6, x + lustBarWidth, y + 6 + 1, (Math.min(255, sin) << 24) | accent);
                }
                int max5 = Math.max(1, 3);
                int max6 = Math.max(1, lustBarWidth / max5);
                for (int i4 = 1; i4 < max5; i4++) {
                    int i5 = x + (i4 * max6);
                    class_332Var.method_25294(i5, y, i5 + 1, y + 6, ((((float) i4) / ((float) max5)) > (0.001f + smoothLustFraction) ? 1 : ((((float) i4) / ((float) max5)) == (0.001f + smoothLustFraction) ? 0 : -1)) <= 0 ? -1862270977 : 0x50000000);
                }
                int floor = (int) Math.floor((smoothLustFraction * max5) + 1.0E-4d);
                if (floor != lastLustSegments) {
                    if (floor > lastLustSegments) {
                        lustSegFlashUntil = 420 + currentTimeMillis;
                    }
                    lastLustSegments = floor;
                }
                if (currentTimeMillis < lustSegFlashUntil) {
                    class_332Var.method_25294(x, y - 2, x + lustBarWidth, y + 6 + 2, (((int) ((((float) (lustSegFlashUntil - currentTimeMillis)) / 420.0f) * 150.0f)) << 24) | 16777215);
                }
                class_332Var.method_25294(x, y, x + i3, y + 1, 0x66ffffff);
            }
        }
    }

    private static void ecgStrip(class_332 class_332Var, int i, int i2) {
        if (RoarEcg.isActive()) {
            RoarEcg.sample();
            int width = RoarLayout.width(2, i);
            int height = RoarLayout.height(2, i2);
            int x = RoarLayout.x(2, i, i2);
            int y = RoarLayout.y(2, i, i2);
            class_332Var.method_25294(x - 1, y - 1, x + width + 1, y + height + 1, 0x38000000);
            class_332Var.method_25294(x, y, x + width, y + height, 0x1c000000);
            class_332Var.method_25294(x, y, x + width, y + 1, 0x3c000000 | RoarPalette.frame());
            int i3 = y + (height / 2);
            for (int i4 = x + 3; i4 < (x + width) - 6; i4 += 12) {
                class_332Var.method_25294(i4, i3, i4 + 6, i3 + 1, 0x16ffffff);
            }
            float f = (height / 2) - 3;
            float f2 = (width - 4) / 520.0f;
            float[] buffer = RoarEcg.buffer();
            int headIndex = RoarEcg.headIndex();
            int i5 = BLACK;
            int i6 = i3;
            while (i5 < 520) {
                int i7 = x + 2 + ((int) (i5 * f2));
                int max = Math.max(y + 2, Math.min((y + height) - 2, i3 - ((int) (buffer[(headIndex + i5) % 520] * f))));
                class_332Var.method_25294(i7, Math.min(i6, max), i7 + 1, Math.max(i6, max) + 1, (Math.min(255, ((int) ((200.0f * i5) / 519.0f)) + 36) << 24) | RoarPalette.accent());
                i5++;
                i6 = max;
            }
            int max2 = Math.max(y + 2, Math.min((y + height) - 2, i3 - ((int) (RoarEcg.valueAt(System.currentTimeMillis()) * f))));
            class_332Var.method_25294((x + width) - 3, max2 - 1, (x + width) - 1, max2 + 2, -1);
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null && method_1551.field_1772 != null) {
                class_5250 method_43470 = class_2561.method_43470("♥ " + RoarEcg.bpm());
                class_332Var.method_27535(method_1551.field_1772, method_43470, ((x + width) - 4) - method_1551.field_1772.method_27525(method_43470), y + 2, -25896);
            }
        }
    }

    private static void pregnancyBadge(class_332 class_332Var, int i, int i2) {
        int level;
        if (RoarOfLoveConfig.isPregnancyHint() && (level = RoarPregnancy.level()) >= 0) {
            int width = RoarLayout.width(3, i);
            int height = RoarLayout.height(3, i2);
            int x = RoarLayout.x(3, i, i2);
            int y = RoarLayout.y(3, i, i2);
            class_332Var.method_25294(x - 1, y - 1, width + x + 1, y + height + 1, -1879048192);
            drawHeart(class_332Var, x + 2, y + 1, 8, (((int) (((Math.max(0.0f, Math.min(1.0f, RoarFilterState.heartPulse())) * 0.28f) + 0.72f) * 255.0f)) << 24) | lighten(RoarPalette.accent(), 0.55f));
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null && method_1551.field_1772 != null) {
                class_332Var.method_27535(method_1551.field_1772, class_2561.method_43469("roar_of_love.ui.pregnancy_badge", new Object[]{Integer.valueOf(level + 1)}), x + 12, y + 2, -14108);
            }
        }
    }

    private static void heatBar(class_332 class_332Var, int i, int i2) {
        if (RoarOfLoveConfig.isHeatBar()) {
            float max = Math.max(RoarFilterState.heatLevel(), RoarFilterState.overloadLevel() * 0.6f);
            if (max > 0.01f) {
                int lustBarWidth = RoarLayout.lustBarWidth();
                int x = RoarLayout.x(BLACK, i, i2);
                int y = RoarLayout.y(BLACK, i, i2) + 6 + 3;
                class_332Var.method_25294(x, y, ((int) (lustBarWidth * max)) + x, ((int) (3.0f * max)) + 2 + y, ((((int) (165.0f * max)) + 90) << 16) | (-872415232) | ((120 - ((int) (80.0f * max))) << 8) | (200 - ((int) (max * 150.0f))));
            }
        }
    }

    private static void debugRawHeart(class_332 class_332Var, int i, int i2) {
        int max = Math.max(4, Math.round(140 / WIDEN));
        int i3 = (i / 2) - 70;
        int max2 = Math.max(4, (i2 / 2) - 170);
        class_332Var.method_25294(i3 - 4, max2 - 4, i3 + 140 + 4, max + max2 + 4, -1879048192);
        drawHeart(class_332Var, i3, max2, 140, (-16777216) | lighten(RoarPalette.accent(), 0.22f));
    }

    private static void drawHeart(class_332 class_332Var, int i, int i2, int i3, int i4) {
        int i5;
        if (i3 < 6) {
            i3 = 6;
        }
        int i6 = (i4 >>> 24) & 255;
        if (i6 > 4) {
            int i7 = (i6 << 24) | (16777215 & i4);
            int length = HEART_ROWS.length;
            float f = i3 * HEART_GROW;
            int max = Math.max(4, Math.round(f / WIDEN));
            float f2 = (i3 - max) / 2.0f;
            float f3 = (i3 - 1.0f) / 2.0f;
            int i8 = BLACK;
            while (true) {
                int i9 = i8;
                if (i9 < i3) {
                    int round = Math.round(i9 - f2);
                    if (round >= 0 && round < max) {
                        int i10 = (round * length) / max;
                        if (i10 >= length) {
                            i10 = length - 1;
                        }
                        float f4 = HEART_ROWS[i10][BLACK] * f;
                        float f5 = HEART_ROWS[i10][1] * f;
                        int i11 = i2 + i9;
                        int i12 = -1;
                        int i13 = BLACK;
                        while (i13 <= i3) {
                            boolean z = false;
                            if (i13 < i3) {
                                float abs = Math.abs(i13 - f3);
                                z = abs <= f4 && (f5 <= 0.0f || abs >= f5);
                            }
                            if (z) {
                                if (i12 < 0) {
                                    i5 = i13;
                                }
                                i5 = i12;
                            } else {
                                if (i12 >= 0) {
                                    class_332Var.method_25294(i12 + i, i11, i + i13, i11 + 1, i7);
                                    i5 = -1;
                                }
                                i5 = i12;
                            }
                            i13++;
                            i12 = i5;
                        }
                    }
                    i8 = i9 + 1;
                } else {
                    return;
                }
            }
        }
    }

    private static void fillSpan(class_332 class_332Var, int i, int i2, int i3, int i4, int i5, int i6) {
        if (i4 < 0) {
            i4 = BLACK;
        }
        if (i5 <= i2) {
            i2 = i5;
        }
        if (i2 > i4) {
            class_332Var.method_25294(i + i4, i3, i + i2, i3 + 1, i6);
        }
    }

    private static void debugScaleLadder(class_332 class_332Var, int i, int i2) {
        int[] iArr = {64, 40, 28, 18};
        int length = 14 * (iArr.length - 1);
        int length2 = iArr.length;
        for (int i3 = BLACK; i3 < length2; i3++) {
            length += iArr[i3];
        }
        int max = Math.max(4, (i - length) / 2);
        int max2 = Math.max(4, (i2 / 2) + 30);
        int length3 = iArr.length;
        int i4 = BLACK;
        int i5 = max;
        while (i4 < length3) {
            int i6 = iArr[i4];
            class_332Var.method_25294(i5 - 2, max2 - 2, i5 + i6 + 2, Math.max(4, Math.round(i6 / WIDEN)) + max2 + 2, 0x60ffffff);
            drawHeart(class_332Var, i5, max2, i6, (-16777216) | lighten(RoarPalette.accent(), 0.22f));
            i4++;
            i5 += i6 + 14;
        }
    }

    private static void renderHearts(class_332 class_332Var, int i, int i2) {
        int i3;
        int i4;
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (RoarFilterState.hearts()) {
            i3 = BLACK;
            for (RoarFilterState.Heart heart : RoarFilterState.hearts()) {
                long j = currentTimeMillis - heart.born;
                if (j >= 0 && j < heart.life) {
                    float f = ((float) j) / ((float) heart.life);
                    float min = Math.min(1.0f, f / 0.15f) * heart.alpha * Math.min(1.0f, (1.0f - f) / 0.28f);
                    if (min > 0.01f) {
                        float sin = heart.x + (((float) Math.sin((j / 460.0d) + heart.phase)) * heart.sway);
                        float f2 = heart.y - ((((float) j) / 1000.0f) * heart.rise);
                        int max = Math.max(6, (int) ((heart.size * (0.8f + (f * 0.4f))) + 0.5f));
                        int i5 = ((int) (i * sin)) - (max / 2);
                        int i6 = ((int) (i2 * f2)) - (max / 2);
                        if (i6 >= (-max) && i6 <= i2 + max && i5 >= (-max) && i5 <= i + max) {
                            int max2 = Math.max(BLACK, Math.min(255, (int) (min * 255.0f)));
                            if (RoarFilterState.isDebugHearts()) {
                                max2 = Math.min(255, max2 + 90);
                            }
                            int lighten = lighten(RoarPalette.accent(), 0.22f);
                            if (max >= 16 && (i4 = (int) (max2 * 0.22f)) > 6) {
                                drawHeart(class_332Var, i5 - 2, i6 - 2, max + 4, (i4 << 24) | lighten);
                            }
                            drawHeart(class_332Var, i5, i6, max, (max2 << 24) | lighten);
                            i3++;
                        }
                    }
                }
            }
        }
        if (i3 > 0 && heartDrawLogs < 3) {
            heartDrawLogs++;
            RoarOfLove.LOGGER.info("[roar_of_love] 绘制爱心 {} 个（画面 {}x{}）", new Object[]{Integer.valueOf(i3), Integer.valueOf(i), Integer.valueOf(i2)});
        }
    }

    private static void noteFrame() {
        lastHudFrameMs = System.currentTimeMillis();
    }

    public static long lastFrameMs() {
        return lastHudFrameMs;
    }

    public static void watchdog() {
        try {
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null && method_1551.field_1724 != null && method_1551.field_1687 != null && method_1551.field_1755 == null) {
                long currentTimeMillis = System.currentTimeMillis();
                if (lastHudFrameMs == 0) {
                    lastHudFrameMs = currentTimeMillis;
                    return;
                }
                if ((RoarEcg.isActive() || RoarFilterState.cachedLust() >= 0 || RoarOfLoveConfig.isPinkFilter()) && currentTimeMillis - lastHudFrameMs > 900) {
                    hudSkipTicks++;
                    if (!hudSkipReported && hudSkipTicks >= 20) {
                        hudSkipReported = true;
                        RoarOfLove.LOGGER.warn("[roar_of_love] 检测到 HUD 绘制被跳过 {} 次（约 {} 秒），本体动画可能整个隐藏了 HUD", Integer.valueOf(hudSkipTicks), Integer.valueOf(hudSkipTicks / 20));
                        method_1551.field_1724.method_7353(class_2561.method_43469("roar_of_love.chat.hud_skipped", new Object[]{Integer.valueOf(hudSkipTicks / 20)}), false);
                    }
                }
            }
        } catch (Throwable th) {
        }
    }

    public static void render(class_332 class_332Var, class_9779 class_9779Var) {
        class_746 class_746Var;
        noteFrame();
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null && (class_746Var = method_1551.field_1724) != null) {
            int method_51421 = class_332Var.method_51421();
            int method_51443 = class_332Var.method_51443();
            if (method_51421 > 0 && method_51443 > 0) {
                try {
                    try {
                        RoarFilterState.frame();
                        RoarSubtitles.update();
                    } catch (Throwable th) {
                        if (!frameErrorLogged) {
                            frameErrorLogged = true;
                            RoarOfLove.LOGGER.warn("[roar_of_love] 滤镜状态更新异常", th);
                        }
                    }
                    try {
                        float heartPulse = RoarFilterState.heartPulse() * RoarFilterState.breathFactor() * RoarFilterState.breathHoldFactor();
                        float blackAlpha = RoarFilterState.blackAlpha();
                        if (blackAlpha > 0.0f && RoarOfLoveConfig.isBlackFilter()) {
                            int min = Math.min(208, (int) (blackAlpha * 255.0f * heartPulse));
                            int min2 = Math.min(64, (int) (blackAlpha * 62.0f));
                            if (min2 > 0) {
                                class_332Var.method_25294(BLACK, BLACK, method_51421, method_51443, min2 << 24);
                            }
                            frame(class_332Var, method_51421, method_51443, min, BLACK);
                        }
                        if (RoarOfLoveConfig.isPinkFilter()) {
                            if (class_746Var.method_6112(class_7923.field_41174.method_47983(NonStatusEffects.ENERGIZED)) != null) {
                                frame(class_332Var, method_51421, method_51443, Math.min(235, (int) (((Math.max(BLACK, r0.method_5578()) * 34) + 44) * RoarOfLoveConfig.strengthScale(RoarOfLoveConfig.pinkStrength()) * heartPulse)), PINK);
                            }
                            renderHearts(class_332Var, method_51421, method_51443);
                            if (RoarFilterState.isDebugHearts()) {
                                try {
                                    debugRawHeart(class_332Var, method_51421, method_51443);
                                    debugScaleLadder(class_332Var, method_51421, method_51443);
                                } catch (Throwable th2) {
                                }
                            }
                        }
                        reflux(class_332Var, method_51421, method_51443);
                    } catch (Throwable th3) {
                        if (!drawErrorLogged) {
                            drawErrorLogged = true;
                            RoarOfLove.LOGGER.warn("[roar_of_love] 滤镜绘制异常", th3);
                        }
                    }
                    try {
                        float flashAlpha = RoarFilterState.flashAlpha();
                        if (flashAlpha > 0.0f && RoarOfLoveConfig.isFlashEffect()) {
                            int min3 = Math.min(255, (int) (flashAlpha * 255.0f));
                            class_332Var.method_25294(BLACK, BLACK, method_51421, method_51443, 16777215 | (min3 << 24));
                            if (flashDrawLogs < 3) {
                                flashDrawLogs++;
                                RoarOfLove.LOGGER.info("[roar_of_love] 绘制白色闪屏 alpha={}/255", Integer.valueOf(min3));
                            }
                        }
                    } catch (Throwable th4) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 闪屏绘制异常", th4);
                    }
                    try {
                        RoarSubtitles.render(class_332Var, method_51421, method_51443);
                    } catch (Throwable th5) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 字幕绘制异常", th5);
                    }
                    try {
                        lustBar(class_332Var, method_51421, method_51443);
                    } catch (Throwable th6) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 性欲条绘制异常", th6);
                    }
                    try {
                        heatBar(class_332Var, method_51421, method_51443);
                    } catch (Throwable th7) {
                    }
                    try {
                        pregnancyBadge(class_332Var, method_51421, method_51443);
                    } catch (Throwable th8) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 怀孕提示绘制异常", th8);
                    }
                    try {
                        ecgStrip(class_332Var, method_51421, method_51443);
                    } catch (Throwable th9) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 心电绘制异常", th9);
                    }
                    try {
                        int tintAlpha = RoarStyle.tintAlpha();
                        if (tintAlpha > 0) {
                            class_332Var.method_25294(BLACK, BLACK, method_51421, method_51443, 674956 | (tintAlpha << 24));
                        }
                    } catch (Throwable th10) {
                    }
                    try {
                        RoarPlayerHud.render(class_332Var, method_51421, method_51443);
                    } catch (Throwable th11) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 玩家操纵提示绘制异常", th11);
                    }
                } catch (Throwable th12) {
                    try {
                        float flashAlpha2 = RoarFilterState.flashAlpha();
                        if (flashAlpha2 > 0.0f && RoarOfLoveConfig.isFlashEffect()) {
                            int min4 = Math.min(255, (int) (flashAlpha2 * 255.0f));
                            class_332Var.method_25294(BLACK, BLACK, method_51421, method_51443, 16777215 | (min4 << 24));
                            if (flashDrawLogs < 3) {
                                flashDrawLogs++;
                                RoarOfLove.LOGGER.info("[roar_of_love] 绘制白色闪屏 alpha={}/255", Integer.valueOf(min4));
                            }
                        }
                    } catch (Throwable th13) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 闪屏绘制异常", th13);
                    }
                    try {
                        RoarSubtitles.render(class_332Var, method_51421, method_51443);
                    } catch (Throwable th14) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 字幕绘制异常", th14);
                    }
                    try {
                        lustBar(class_332Var, method_51421, method_51443);
                    } catch (Throwable th15) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 性欲条绘制异常", th15);
                    }
                    try {
                        heatBar(class_332Var, method_51421, method_51443);
                    } catch (Throwable th16) {
                    }
                    try {
                        pregnancyBadge(class_332Var, method_51421, method_51443);
                    } catch (Throwable th17) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 怀孕提示绘制异常", th17);
                    }
                    try {
                        ecgStrip(class_332Var, method_51421, method_51443);
                    } catch (Throwable th18) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 心电绘制异常", th18);
                    }
                    try {
                        int tintAlpha2 = RoarStyle.tintAlpha();
                        if (tintAlpha2 > 0) {
                            class_332Var.method_25294(BLACK, BLACK, method_51421, method_51443, 674956 | (tintAlpha2 << 24));
                        }
                    } catch (Throwable th19) {
                    }
                    try {
                        RoarPlayerHud.render(class_332Var, method_51421, method_51443);
                        throw th12;
                    } catch (Throwable th20) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 玩家操纵提示绘制异常", th20);
                        throw th12;
                    }
                }
            }
        }
    }
}

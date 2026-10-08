//
// Decompiled by Jadx - 692ms
//
package com.roaroflove.client;

import com.nonid.effect.NonStatusEffects;
import com.roaroflove.RoarOfLove;
import com.roaroflove.client.RoarFilterState;
import com.roaroflove.config.RoarOfLoveConfig;
import net.minecraft.class_10799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_5250;
import net.minecraft.class_746;
import net.minecraft.class_7923;
import net.minecraft.class_9779;

public final class RoarVignetteHud {
    private static final int HEART_TEX_SIZE = 96;
    private static final int PINK = 16738740;
    private static final int STEPS = 26;
    public static final class_2960 ID = class_2960.method_60655("roar_of_love", "pink_filter");
    private static final class_2960 HEART = class_2960.method_60655("roar_of_love", "textures/gui/heart.png");
    private static final int BLACK = 0;
    private static int heartDrawLogs = BLACK;
    private static int flashDrawLogs = BLACK;
    private static boolean frameErrorLogged = false;
    private static boolean drawErrorLogged = false;
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

    private static void lustBar(class_332 class_332Var, int i, int i2) {
        int cachedLust;
        if (RoarOfLoveConfig.isLustBar() && (cachedLust = RoarFilterState.cachedLust()) >= 0) {
            float immersionFactor = RoarOfLoveConfig.immersionFactor(RoarOfLoveConfig.lustBarStrength());
            int lustBarWidth = RoarLayout.lustBarWidth();
            int x = RoarLayout.x(BLACK, i, i2);
            int y = RoarLayout.y(BLACK, i, i2);
            class_332Var.method_25294(x - 1, y - 1, x + lustBarWidth + 1, y + 6 + 1, -1610612736);
            class_332Var.method_25294(x, y, x + lustBarWidth, y + 6, 0x50ffffff);
            int max = Math.max(1, (int) (Math.min(1.0f, (cachedLust + 1) / 6.0f) * lustBarWidth));
            class_332Var.method_25294(x, y, x + max, y + 6, (Math.max(110, Math.min(255, (int) ((1.0f - ((1.0f - Math.min(1.0f, RoarFilterState.heartPulse())) * (0.3f + (0.35f * immersionFactor)))) * 255.0f))) << 24) | RoarPalette.accent());
            class_332Var.method_25294(x, y, x + max, y + 1, 0x66ffffff);
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
            class_332Var.method_25291(class_10799.field_56883, HEART, x + 1, y + 1, 0.0f, 0.0f, 9, 9, HEART_TEX_SIZE, HEART_TEX_SIZE, 16777215 | (((int) (((Math.max(0.0f, Math.min(1.0f, RoarFilterState.heartPulse())) * 0.28f) + 0.72f) * 255.0f)) << 24));
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null && method_1551.field_1772 != null) {
                class_332Var.method_27535(method_1551.field_1772, class_2561.method_43469("roar_of_love.ui.pregnancy_badge", new Object[]{Integer.valueOf(level + 1)}), x + 12, y + 2, -14108);
            }
        }
    }

    private static void renderHearts(class_332 class_332Var, int i, int i2) {
        int i3;
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (RoarFilterState.hearts()) {
            i3 = BLACK;
            for (RoarFilterState.Heart heart : RoarFilterState.hearts()) {
                long j = currentTimeMillis - heart.born;
                if (j >= 0 && j < heart.life) {
                    float f = ((float) j) / ((float) heart.life);
                    float min = Math.min(1.0f, (1.0f - f) / 0.28f) * Math.min(1.0f, f / 0.15f) * heart.alpha;
                    if (min > 0.01f) {
                        float sin = heart.x + (((float) Math.sin((j / 460.0d) + heart.phase)) * heart.sway);
                        float f2 = heart.y - ((((float) j) / 1000.0f) * heart.rise);
                        int max = Math.max(6, (int) (heart.size * ((f * 0.4f) + 0.8f)));
                        int i4 = ((int) (i * sin)) - (max / 2);
                        int i5 = ((int) (i2 * f2)) - (max / 2);
                        if (i5 >= (-max) && i5 <= i2 + max && i4 >= (-max) && i4 <= i + max) {
                            class_332Var.method_25291(class_10799.field_56883, HEART, i4, i5, 0.0f, 0.0f, max, max, HEART_TEX_SIZE, HEART_TEX_SIZE, (Math.max(BLACK, Math.min(255, (int) (255.0f * min))) << 24) | 16777215);
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
                    if (RoarMarquee.isLocked()) {
                        RoarMarquee.render(class_332Var, method_51421, method_51443);
                        return;
                    }
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
                        }
                        reflux(class_332Var, method_51421, method_51443);
                    } catch (Throwable th2) {
                        if (!drawErrorLogged) {
                            drawErrorLogged = true;
                            RoarOfLove.LOGGER.warn("[roar_of_love] 滤镜绘制异常", th2);
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
                    } catch (Throwable th3) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 闪屏绘制异常", th3);
                    }
                    try {
                        RoarSubtitles.render(class_332Var, method_51421, method_51443);
                    } catch (Throwable th4) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 字幕绘制异常", th4);
                    }
                    try {
                        lustBar(class_332Var, method_51421, method_51443);
                    } catch (Throwable th5) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 性欲条绘制异常", th5);
                    }
                    try {
                        pregnancyBadge(class_332Var, method_51421, method_51443);
                    } catch (Throwable th6) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 怀孕提示绘制异常", th6);
                    }
                    try {
                        ecgStrip(class_332Var, method_51421, method_51443);
                    } catch (Throwable th7) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 心电绘制异常", th7);
                    }
                    try {
                        int tintAlpha = RoarStyle.tintAlpha();
                        if (tintAlpha > 0) {
                            class_332Var.method_25294(BLACK, BLACK, method_51421, method_51443, 674956 | (tintAlpha << 24));
                        }
                    } catch (Throwable th8) {
                    }
                    try {
                        RoarMarquee.render(class_332Var, method_51421, method_51443);
                    } catch (Throwable th9) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 公告绘制异常", th9);
                    }
                } catch (Throwable th10) {
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
                    } catch (Throwable th11) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 闪屏绘制异常", th11);
                    }
                    try {
                        RoarSubtitles.render(class_332Var, method_51421, method_51443);
                    } catch (Throwable th12) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 字幕绘制异常", th12);
                    }
                    try {
                        lustBar(class_332Var, method_51421, method_51443);
                    } catch (Throwable th13) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 性欲条绘制异常", th13);
                    }
                    try {
                        pregnancyBadge(class_332Var, method_51421, method_51443);
                    } catch (Throwable th14) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 怀孕提示绘制异常", th14);
                    }
                    try {
                        ecgStrip(class_332Var, method_51421, method_51443);
                    } catch (Throwable th15) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 心电绘制异常", th15);
                    }
                    try {
                        int tintAlpha2 = RoarStyle.tintAlpha();
                        if (tintAlpha2 > 0) {
                            class_332Var.method_25294(BLACK, BLACK, method_51421, method_51443, 674956 | (tintAlpha2 << 24));
                        }
                    } catch (Throwable th16) {
                    }
                    try {
                        RoarMarquee.render(class_332Var, method_51421, method_51443);
                        throw th10;
                    } catch (Throwable th17) {
                        RoarOfLove.LOGGER.debug("[roar_of_love] 公告绘制异常", th17);
                        throw th10;
                    }
                }
            }
        }
    }
}

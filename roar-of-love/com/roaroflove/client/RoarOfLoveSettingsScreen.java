//
// Decompiled by Jadx - 1009ms
//
package com.roaroflove.client;

import com.roaroflove.RoarOfLove;
import com.roaroflove.client.audio.AudioPackLoader;
import com.roaroflove.client.ui.RoarButton;
import com.roaroflove.client.ui.RoarTooltip;
import com.roaroflove.config.RoarOfLoveConfig;
import com.roaroflove.sound.RoLSounds;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_10799;
import net.minecraft.class_1109;
import net.minecraft.class_1140;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_5250;

public class RoarOfLoveSettingsScreen extends class_437 {
    public static final String AUTHOR = "哔哩哔哩：佳佳不好";
    private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
    private static final int COL_GAP = 8;
    private static final int COL_W = 150;
    private static final int CONTENT_TOP = 38;
    private static final int FOOTER_GAP = 30;
    private static final int FOOTER_H = 34;
    private static final int HEADER_H = 34;
    private static final int PV_MAIN_W = 112;
    private static final int PV_ROW_W = 150;
    private static final int PV_SWITCH_W = 34;
    private static final int ROW_H = 24;
    private static final int SECTION_H = 26;
    public static final String TITLE = "音频设置";
    private String cachedStatus;
    private int cachedStatusWidth;
    private int checkBtnX;
    private int checkBtnY;
    private int maxScroll;
    private final class_437 parent;
    private int scrollY;
    private final List<String[]> sectionHeaderRects;
    private String statusLine;
    private String statusText;

    public RoarOfLoveSettingsScreen(class_437 class_437Var) {
        super(class_2561.method_43469("roar_of_love.ui.title", new Object[0]));
        this.statusText = "";
        this.cachedStatus = null;
        this.statusLine = "";
        this.cachedStatusWidth = -1;
        this.sectionHeaderRects = new ArrayList();
        this.scrollY = 0;
        this.maxScroll = 0;
        this.checkBtnX = COL_GAP;
        this.checkBtnY = 0;
        this.parent = class_437Var;
        this.statusText = AudioPackLoader.lastStatus();
    }

    private static String tr(String str, Object... objArr) {
        return class_2561.method_43469(str, objArr).getString();
    }

    private void refresh() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.execute(() -> {
                method_25426();
            });
        }
    }

    private void scrollBy(int i) {
        int max = Math.max(0, Math.min(this.maxScroll, this.scrollY + i));
        if (max != this.scrollY) {
            this.scrollY = max;
            refresh();
        }
    }

    protected void method_25426() {
        method_37067();
        RoarButton.resetVariants();
        this.sectionHeaderRects.clear();
        int i = this.field_22789 / 2;
        int i2 = (i - 150) - 4;
        int i3 = i + 4;
        List<Action> buildPreviews = buildPreviews();
        int size = (buildPreviews.size() + 1) / 2;
        this.maxScroll = Math.max(0, (((((size * ROW_H) + SECTION_H) + 220) + 98) + 16) - ((this.field_22790 - 34) - 64));
        if (this.scrollY > this.maxScroll) {
            this.scrollY = this.maxScroll;
        }
        RoarTooltip.reset();
        int section = section("roar_of_love.ui.section_fx", i, 38 - this.scrollY);
        toggle(i2, section, 150, RoarOfLoveConfig.isFlashEffect(), "flash", () -> {
            RoarOfLoveConfig.setFlashEffect(!RoarOfLoveConfig.isFlashEffect());
        }, "roar_of_love.tip.flash");
        toggle(i3, section, 150, RoarOfLoveConfig.isShakeEffect(), "shake", () -> {
            RoarOfLoveConfig.setShakeEffect(!RoarOfLoveConfig.isShakeEffect());
        }, "roar_of_love.tip.shake");
        int i4 = section + ROW_H;
        toggle(i2, i4, 150, RoarOfLoveConfig.isPushEffect(), "push", () -> {
            RoarOfLoveConfig.setPushEffect(!RoarOfLoveConfig.isPushEffect());
        }, "roar_of_love.tip.push");
        toggle(i3, i4, 150, RoarOfLoveConfig.isHeatBar(), "heat", () -> {
            RoarOfLoveConfig.setHeatBar(!RoarOfLoveConfig.isHeatBar());
        }, "roar_of_love.tip.heat");
        int i5 = i4 + ROW_H;
        toggle(i2, i5, 150, RoarOfLoveConfig.isOverload(), "overload", () -> {
            RoarOfLoveConfig.setOverload(!RoarOfLoveConfig.isOverload());
        }, "roar_of_love.tip.overload");
        toggle(i3, i5, 150, RoarOfLoveConfig.isDirector(), "director", () -> {
            RoarOfLoveConfig.setDirector(!RoarOfLoveConfig.isDirector());
        }, "roar_of_love.tip.director");
        int section2 = section("roar_of_love.ui.section_audio", i, i5 + ROW_H);
        toggle(i2, section2, 150, RoarOfLoveConfig.isSoundEnabled("hurt"), "hurt", () -> {
            RoarOfLoveConfig.setSoundEnabled("hurt", !RoarOfLoveConfig.isSoundEnabled("hurt"));
        }, "roar_of_love.tip.hurt");
        toggle(i3, section2, 150, RoarOfLoveConfig.isOtherMobCalls(), "mob_calls", () -> {
            RoarOfLoveConfig.setOtherMobCalls(!RoarOfLoveConfig.isOtherMobCalls());
        }, "roar_of_love.tip.mob_calls");
        int i6 = section2 + ROW_H;
        toggle(i2, i6, 150, RoarOfLoveConfig.isMuteOtherHurt(), "mute_hurt", () -> {
            RoarOfLoveConfig.setMuteOtherHurt(!RoarOfLoveConfig.isMuteOtherHurt());
        }, "roar_of_love.tip.mute_hurt");
        toggle(i3, i6, 150, RoarOfLoveConfig.isAlignMoans(), "align", () -> {
            RoarOfLoveConfig.setAlignMoans(!RoarOfLoveConfig.isAlignMoans());
        }, "roar_of_love.tip.align");
        int i7 = i6 + ROW_H;
        toggle(i2, i7, 150, RoarOfLoveConfig.isDistantFade(), "distant", () -> {
            RoarOfLoveConfig.setDistantFade(!RoarOfLoveConfig.isDistantFade());
        }, "");
        toggle(i3, i7, 150, RoarOfLoveConfig.isOcclusion(), "occlusion", () -> {
            RoarOfLoveConfig.setOcclusion(!RoarOfLoveConfig.isOcclusion());
        }, "");
        int i8 = i7 + ROW_H;
        toggle(i2, i8, 150, RoarOfLoveConfig.isPostPeakDip(), "postpeak", () -> {
            RoarOfLoveConfig.setPostPeakDip(!RoarOfLoveConfig.isPostPeakDip());
        }, "roar_of_love.tip.postpeak");
        toggle(i3, i8, 150, RoarOfLoveConfig.isAmbienceSound(), "ambience", () -> {
            RoarOfLoveConfig.setAmbienceSound(!RoarOfLoveConfig.isAmbienceSound());
        }, "roar_of_love.tip.ambience");
        int section3 = section("roar_of_love.ui.center_preview", i, i8 + ROW_H);
        int i9 = 0;
        while (true) {
            int i10 = i9;
            if (i10 >= buildPreviews.size()) {
                break;
            }
            int i11 = i10 / 2;
            int i12 = i10 % 2 == 0 ? i2 : i3;
            int i13 = section3 + (i11 * ROW_H);
            Action action = buildPreviews.get(i10);
            method_37063(btn(class_2561.method_43470(action.label()), class_4185Var -> {
                try {
                    action.run().run();
                } catch (Throwable th) {
                    RoarOfLove.LOGGER.warn("[roar_of_love] 试听处理异常：{}", action.label(), th);
                }
            }, i12, i13, PV_MAIN_W, 20));
            method_37063(btn(class_2561.method_43469(RoarOfLoveConfig.isSoundEnabled(action.key()) ? "roar_of_love.ui.snd_on" : "roar_of_love.ui.snd_off", new Object[0]), class_4185Var2 -> {
                RoarOfLoveConfig.setSoundEnabled(action.key(), !RoarOfLoveConfig.isSoundEnabled(action.key()));
                refresh();
            }, i12 + PV_MAIN_W + 4, i13, 34, 20));
            i9 = i10 + 1;
        }
        int section4 = section("roar_of_love.ui.section_tools", i, (size * ROW_H) + section3);
        link(i2, section4, 150, "roar_of_love.ui.filters_open", this::openFilters);
        link(i3, section4, 150, "roar_of_love.ui.subtitles_open", this::openSubtitles);
        int i14 = section4 + ROW_H;
        link(i2, i14, 150, "roar_of_love.ui.immersion_open", this::openImmersion);
        link(i3, i14, 150, "roar_of_love.ui.layout_open", this::openLayout);
        int i15 = i14 + ROW_H;
        link(i2, i15, 150, "roar_of_love.ui.handbook_open", this::openHandbook);
        link(i3, i15, 150, "roar_of_love.ui.open_audio_folder", AudioPackLoader::openAudioFolder);
        int i16 = i15 + ROW_H;
        this.checkBtnX = COL_GAP;
        this.checkBtnY = this.field_22790 - 26;
        RoarButton roarButton = new RoarButton(this.checkBtnX, this.checkBtnY, 130, 20, class_2561.method_43469("roar_of_love.ui.builtin_check", new Object[0]), class_4185Var3 -> {
            checkBuiltin();
        });
        roarButton.field_22763 = this.scrollY >= this.maxScroll + (-1);
        method_37063(roarButton);
        tip(this.checkBtnX, this.checkBtnY, 130, 20, "roar_of_love.tip.builtin_check");
        RoarOfLove.LOGGER.debug("[roar_of_love] 设置界面重建：试听 {} 项，滚动 {}/{}", new Object[]{Integer.valueOf(buildPreviews.size()), Integer.valueOf(this.scrollY), Integer.valueOf(this.maxScroll)});
    }

    private int section(String str, int i, int i2) {
        this.sectionHeaderRects.add(new String[]{str, String.valueOf(i2)});
        return i2 + SECTION_H;
    }

    private void toggle(int i, int i2, int i3, boolean z, String str, Runnable runnable) {
        toggle(i, i2, i3, z, str, runnable, "");
    }

    private void toggle(int i, int i2, int i3, boolean z, String str, Runnable runnable, String str2) {
        method_37063(btn(class_2561.method_43469("roar_of_love.ui." + str + (z ? "_on" : "_off"), new Object[0]), class_4185Var -> {
            runnable.run();
            refresh();
        }, i, i2, i3, 20));
        tip(i, i2, i3, 20, str2);
    }

    private void tip(int i, int i2, int i3, int i4, String str) {
        if (str != null && !str.isEmpty()) {
            RoarTooltip.register(i, i2, i3, i4, str);
        }
    }

    private void link(int i, int i2, int i3, String str, Runnable runnable) {
        link(i, i2, i3, str, runnable, "");
    }

    private void link(int i, int i2, int i3, String str, Runnable runnable, String str2) {
        method_37063(btn(class_2561.method_43469(str, new Object[0]), class_4185Var -> {
            try {
                runnable.run();
            } catch (Throwable th) {
                RoarOfLove.LOGGER.warn("[roar_of_love] 界面跳转失败：{}", str, th);
            }
        }, i, i2, i3, 20));
        tip(i, i2, i3, 20, str2);
    }

    private List<Action> buildPreviews() {
        ArrayList arrayList = new ArrayList();
        for (String str : RoLSounds.CATEGORIES) {
            arrayList.add(new Action(tr("roar_of_love.cat." + str, new Object[0]), "cat:" + str, () -> {
                openAudioScreen("cat:" + str);
            }));
        }
        arrayList.add(new Action(tr("roar_of_love.ui.hurt_preview", new Object[0]), "hunt:hurt", () -> {
            openAudioScreen("hunt:hurt");
        }));
        arrayList.add(new Action(tr("roar_of_love.call.group.default", new Object[0]), "call:default", () -> {
            openAudioScreen("call:default");
        }));
        for (String str2 : RoLSounds.CALL_GROUPS) {
            if (!"default".equals(str2)) {
                arrayList.add(new Action(tr("roar_of_love.call.group." + str2, new Object[0]), "call:" + str2, () -> {
                    openAudioScreen("call:" + str2);
                }));
            }
        }
        return arrayList;
    }

    private class_4185 btn(class_2561 class_2561Var, class_4185.class_4241 class_4241Var, int i, int i2, int i3, int i4) {
        return new RoarButton(i, i2, i3, i4, class_2561Var, class_4241Var);
    }

    private void openAudioScreen(String str) {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.method_1507(new RoarAudioScreen(this, str));
        }
    }

    private void openFilters() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.method_1507(new RoarFilterScreen(this));
        }
    }

    private void openSubtitles() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.method_1507(new RoarSubtitleScreen(this));
        }
    }

    private void openImmersion() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.method_1507(new RoarImmersionScreen(this));
        }
    }

    private void openLayout() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.method_1507(new RoarLayoutScreen(this));
        }
    }

    private void openHandbook() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.method_1507(new RoarHandbookScreen(this));
        }
    }

    private void previewHurt() {
        try {
            class_3414 class_3414Var = RoLSounds.get("minecraft:entity.player.hurt");
            if (class_3414Var == null) {
                AudioPackLoader.sendChat(tr("roar_of_love.chat.preview_fail", tr("roar_of_love.ui.hurt_preview", new Object[0])));
            } else {
                playLocal(class_3414Var);
            }
        } catch (Throwable th) {
            AudioPackLoader.sendChat(tr("roar_of_love.chat.preview_error", th));
        }
    }

    private void previewCall(String str, boolean z) {
        try {
            class_3414 pickCallPreview = AudioPackLoader.pickCallPreview(str, z);
            if (pickCallPreview == null) {
                AudioPackLoader.sendChat(tr("roar_of_love.chat.no_group_call", tr("roar_of_love.call.group." + str, new Object[0])));
            } else {
                playLocal(pickCallPreview);
            }
        } catch (Throwable th) {
            AudioPackLoader.sendChat(tr("roar_of_love.chat.preview_error", th));
        }
    }

    private void playLocal(class_3414 class_3414Var) {
        try {
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null) {
                if (method_1551.field_1687 != null && method_1551.field_1724 != null) {
                    method_1551.field_1687.method_8486(method_1551.field_1724.method_23317(), method_1551.field_1724.method_23318() + (method_1551.field_1724.method_17682() * 0.5d), method_1551.field_1724.method_23321(), class_3414Var, class_3419.field_15250, RoarOfLoveConfig.volume(), 1.0f, false);
                } else if (method_1551.method_1483() != null) {
                    class_1140.class_11518 method_4873 = method_1551.method_1483().method_4873(class_1109.method_4757(class_3414Var, 1.0f, RoarOfLoveConfig.volume()));
                    if (method_4873 == class_1140.class_11518.field_60956) {
                        AudioPackLoader.sendChat(tr("roar_of_love.chat.not_loaded", class_3414Var.comp_3319()));
                    } else if (method_4873 == class_1140.class_11518.field_60955) {
                        AudioPackLoader.sendChat(tr("roar_of_love.chat.silent", new Object[0]));
                    }
                }
            }
        } catch (Throwable th) {
            AudioPackLoader.sendChat(tr("roar_of_love.chat.play_error", th));
        }
    }

    private void checkBuiltin() {
        try {
            List missingDefaultAssets = AudioPackLoader.missingDefaultAssets(class_310.method_1551());
            if (missingDefaultAssets.isEmpty()) {
                AudioPackLoader.sendChat(tr("roar_of_love.chat.builtin_ok", new Object[0]));
                class_310.method_1551().method_1507(new NoticeScreen(this, new String[]{tr("roar_of_love.chat.builtin_ok", new Object[0])}));
                return;
            }
            AudioPackLoader.sendChat(tr("roar_of_love.chat.builtin_missing", Integer.valueOf(missingDefaultAssets.size())));
            int min = Math.min(9, missingDefaultAssets.size());
            String[] strArr = new String[min + 1];
            strArr[0] = tr("roar_of_love.chat.builtin_missing", Integer.valueOf(missingDefaultAssets.size()));
            for (int i = 0; i < min; i++) {
                strArr[i + 1] = (String) missingDefaultAssets.get(i);
            }
            class_310.method_1551().method_1507(new NoticeScreen(this, strArr));
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 内置音频自检异常", th);
        }
    }

    public boolean method_25401(double d, double d2, double d3, double d4) {
        if (this.maxScroll > 0 && d4 != 0.0d) {
            scrollBy(d4 > 0.0d ? -24 : ROW_H);
            return true;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:55:0x0041, code lost:
    
        r0 = super.method_25402(r9, r10);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean method_25402(class_11909 class_11909Var, boolean z) {
        boolean method_25402;
        List method_25396;
        try {
            method_25396 = method_25396();
        } catch (Throwable th) {
            RoarOfLove.LOGGER.debug("[roar_of_love] 点击分发异常", th);
            method_25402 = super.method_25402(class_11909Var, z);
        }
        if (class_11909Var.comp_4799() >= ((double) (this.field_22790 + (-34)))) {
            int size = method_25396.size() - 1;
            while (true) {
                if (size < 0) {
                    method_25402 = true;
                    break;
                }
                Object obj = method_25396.get(size);
                if (obj instanceof class_339) {
                    class_339 class_339Var = (class_339) obj;
                    if (class_339Var.field_22763 && class_339Var.method_25405(class_11909Var.comp_4798(), class_11909Var.comp_4799())) {
                        try {
                            if (class_339Var.method_25402(class_11909Var, z)) {
                                method_25402 = true;
                                break;
                            }
                        } catch (Throwable th2) {
                        }
                    }
                }
                size--;
            }
        } else {
            int size2 = method_25396.size() - 1;
            while (true) {
                if (size2 >= 0) {
                    Object obj2 = method_25396.get(size2);
                    if (obj2 instanceof class_339) {
                        class_339 class_339Var2 = (class_339) obj2;
                        if (class_339Var2.field_22763 && class_339Var2.method_25405(class_11909Var.comp_4798(), class_11909Var.comp_4799())) {
                            try {
                                if (class_339Var2.method_25402(class_11909Var, z)) {
                                    method_25402 = true;
                                    break;
                                }
                            } catch (Throwable th3) {
                            }
                        }
                    }
                    size2--;
                }
                method_25402 = super.method_25402(class_11909Var, z);
            }
        }
        return method_25402;
    }

    public void method_25394(class_332 class_332Var, int i, int i2, float f) {
        class_5250 method_43469;
        class_332Var.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
        class_332Var.method_25294(0, 0, this.field_22789, 34, -1072689136);
        class_332Var.method_25294(0, this.field_22790 - 34, this.field_22789, this.field_22790, -1072689136);
        super.method_25394(class_332Var, i, i2, f);
        if (this.field_22787 != null && this.field_22793 != null) {
            centered(class_332Var, class_2561.method_43469("roar_of_love.ui.title", new Object[0]), this.field_22789 / 2, 4, 16777215);
            String watermarkText = RoarMarquee.watermarkText();
            if (watermarkText != null && !watermarkText.isEmpty()) {
                int i3 = this.field_22789 - 16;
                if (widthOf(watermarkText) > i3) {
                    int length = watermarkText.length();
                    while (length > 1 && widthOf(watermarkText.substring(0, length) + "…") > i3) {
                        length--;
                    }
                    watermarkText = watermarkText.substring(0, length) + "…";
                }
                centered(class_332Var, class_2561.method_43470(watermarkText), this.field_22789 / 2, 14, RoarMarquee.isLockedNow() ? -65536 : -6381922);
            }
            centered(class_332Var, class_2561.method_43470(statusLine()), this.field_22789 / 2, 25, -4144960);
            int i4 = ((this.field_22790 - 34) - 30) + SECTION_H;
            for (String[] strArr : this.sectionHeaderRects) {
                int parseInt = Integer.parseInt(strArr[1]);
                if (parseInt >= ROW_H && parseInt + 10 <= i4) {
                    centered(class_332Var, class_2561.method_43469(strArr[0], new Object[0]), this.field_22789 / 2, parseInt + 4, -2039553);
                }
            }
            int i5 = (this.field_22790 - 34) + 4;
            class_5250 method_434692 = class_2561.method_43469("roar_of_love.ui.author", new Object[0]);
            class_332Var.method_27535(this.field_22793, method_434692, (this.field_22789 / 2) - (this.field_22793.method_27525(method_434692) / 2), i5 + 2, -6381922);
            if (AudioPackLoader.isPackDirty()) {
                class_5250 method_434693 = class_2561.method_43469("roar_of_love.ui.apply_now_dirty", new Object[0]);
                class_332Var.method_27535(this.field_22793, method_434693, (this.field_22789 - 8) - this.field_22793.method_27525(method_434693), i5 + 2, -15797);
            } else if (this.maxScroll > 0) {
                if (this.scrollY >= this.maxScroll - 1) {
                    method_43469 = class_2561.method_43469("roar_of_love.ui.scroll_end", new Object[0]);
                } else {
                    method_43469 = class_2561.method_43469("roar_of_love.ui.scroll_hint", new Object[0]);
                }
                class_332Var.method_27535(this.field_22793, method_43469, (this.field_22789 - 8) - this.field_22793.method_27525(method_43469), i5 + 2, -6381828);
            }
            RoarTooltip.render(class_332Var, i, i2, this.field_22789, this.field_22790);
        }
    }

    private void centered(class_332 class_332Var, class_2561 class_2561Var, int i, int i2, int i3) {
        class_332Var.method_27535(this.field_22793, class_2561Var, i - (this.field_22793.method_27525(class_2561Var) / 2), i2, i3);
    }

    private int widthOf(String str) {
        if (this.field_22793 == null) {
            return 0;
        }
        return this.field_22793.method_27525(class_2561.method_43470(str));
    }

    private String statusLine() {
        int i;
        if (this.cachedStatus != null && this.cachedStatus.equals(this.statusText) && this.cachedStatusWidth == this.field_22789) {
            return this.statusLine;
        }
        String str = this.statusText == null ? "" : this.statusText;
        if (!str.isEmpty() && this.field_22793 != null && widthOf(str) > this.field_22789 - 12) {
            int length = str.length();
            while (length > 1 && widthOf(str.substring(0, length) + "…") > i) {
                length--;
            }
            str = str.substring(0, length) + "…";
        }
        this.cachedStatus = this.statusText;
        this.cachedStatusWidth = this.field_22789;
        this.statusLine = str;
        return str;
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }
}

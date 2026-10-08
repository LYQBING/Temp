//
// Decompiled by Jadx - 1029ms
//
package com.roaroflove.client;

import com.roaroflove.RoarOfLove;
import com.roaroflove.client.audio.AudioPackLoader;
import com.roaroflove.client.ui.RoarButton;
import com.roaroflove.config.RoarOfLoveConfig;
import com.roaroflove.sound.RoLSounds;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_10799;
import net.minecraft.class_1109;
import net.minecraft.class_1140;
import net.minecraft.class_11909;
import net.minecraft.class_124;
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
    public static final String TITLE = "音频设置";
    private final List<int[]> buttonRects;
    private String cachedStatus;
    private int cachedStatusWidth;
    private int listTopY;
    private int maxScroll;
    private String missingNote;
    private final class_437 parent;
    private String pendingZip;
    private int previewTop;
    private int scrollY;
    private String statusLine;
    private String statusText;
    private int zipShown;

    public RoarOfLoveSettingsScreen(class_437 class_437Var) {
        super(class_2561.method_43469("roar_of_love.ui.title", new Object[0]));
        this.statusText = "";
        this.missingNote = "";
        this.cachedStatus = null;
        this.statusLine = "";
        this.cachedStatusWidth = -1;
        this.previewTop = 76;
        this.listTopY = 190;
        this.zipShown = 0;
        this.pendingZip = null;
        this.buttonRects = new ArrayList();
        this.scrollY = 0;
        this.maxScroll = 0;
        this.parent = class_437Var;
        this.statusText = AudioPackLoader.lastStatus();
        this.pendingZip = AudioPackLoader.activeZipName();
        List missingDefaultAssets = AudioPackLoader.missingDefaultAssets(class_310.method_1551());
        if (!missingDefaultAssets.isEmpty()) {
            StringBuilder sb = new StringBuilder(tr("roar_of_love.ui.missing_prefix", new Object[0]));
            for (int i = 0; i < Math.min(3, missingDefaultAssets.size()); i++) {
                if (i > 0) {
                    sb.append(tr("roar_of_love.ui.missing_sep", new Object[0]));
                }
                sb.append((String) missingDefaultAssets.get(i));
            }
            if (missingDefaultAssets.size() > 3) {
                sb.append(tr("roar_of_love.ui.missing_more", Integer.valueOf(missingDefaultAssets.size())));
            }
            this.missingNote = sb.toString();
        }
    }

    private static String tr(String str, Object... objArr) {
        return class_2561.method_43469(str, objArr).getString();
    }

    private static String previewGroupLabel(String str, String str2) {
        return tr("roar_of_love.call.group." + str, new Object[0]);
    }

    private static String previewLabel(String str, String str2) {
        return tr("roar_of_love.cat." + str, new Object[0]);
    }

    private boolean isDirty() {
        String activeZipName = AudioPackLoader.activeZipName();
        return this.pendingZip == null ? activeZipName != null : !this.pendingZip.equals(activeZipName);
    }

    private void commitPending() {
        for (String str : AudioPackLoader.detectedZipNames()) {
            AudioPackLoader.setZipEnabled(str, str.equals(this.pendingZip));
        }
        refreshAndRebuild();
    }

    private int zipCapacity(int i, int i2, int i3, int i4, int i5, int i6) {
        return Math.max(1, ((this.field_22790 - 44) - (((i2 * i3) + i) + i4)) / i5) * i6;
    }

    protected void method_25426() {
        String str;
        method_37067();
        RoarButton.resetVariants();
        this.buttonRects.clear();
        int i = this.field_22789 / 2;
        ArrayList arrayList = new ArrayList();
        for (String str2 : RoLSounds.CATEGORIES) {
            arrayList.add(new Action(previewLabel(str2, str2), str2, () -> {
                previewCategory(str2);
            }));
        }
        arrayList.add(new Action(tr("roar_of_love.ui.hurt_preview", new Object[0]), "hurt", () -> {
            previewHurt();
        }));
        arrayList.add(new Action(previewGroupLabel("default", "begin"), "default", () -> {
            previewCall("default", true);
        }));
        arrayList.add(new Action(previewGroupLabel("default", "end"), "default", () -> {
            previewCall("default", false);
        }));
        for (String str3 : RoLSounds.CALL_GROUPS) {
            if (!"default".equals(str3)) {
                arrayList.add(new Action(previewGroupLabel(str3, str3), str3, () -> {
                    previewCall(str3, true);
                }));
            }
        }
        List detectedZipNames = AudioPackLoader.detectedZipNames();
        int size = (((arrayList.size() + 1) / 2) * 24) + 186 + 18;
        int i2 = size + 14;
        this.maxScroll = Math.max(0, (((detectedZipNames.size() * 23) + i2) + 20) - ((this.field_22790 - 36) - 32));
        if (this.scrollY > this.maxScroll) {
            this.scrollY = this.maxScroll;
        }
        int i3 = 32 - this.scrollY;
        int i4 = (i - 150) - 4;
        int i5 = i4 + 150 + 8;
        method_37063(btn(class_2561.method_43469("roar_of_love.ui.rescan", new Object[0]), class_4185Var -> {
            commitPending();
        }, i - 158, i3, 100, 20));
        method_37063(btn(class_2561.method_43469("roar_of_love.ui.pack_check", new Object[0]), class_4185Var2 -> {
            checkPack();
        }, i - 54, i3, 108, 20));
        method_37063(btn(class_2561.method_43469("roar_of_love.ui.open_folder", new Object[0]), class_4185Var3 -> {
            RoarOfLove.LOGGER.info("[roar_of_love] 打开音效包文件夹");
            AudioPackLoader.openAudioFolder();
        }, i + 58, i3, 100, 20));
        int i6 = i3 + 30;
        method_37063(btn(class_2561.method_43469("roar_of_love.ui.filters_open", new Object[0]), class_4185Var4 -> {
            openFilters();
        }, i - 190, i6, 92, 20));
        method_37063(btn(class_2561.method_43469("roar_of_love.ui.subtitles_open", new Object[0]), class_4185Var5 -> {
            openSubtitles();
        }, i - 94, i6, 92, 20));
        method_37063(btn(class_2561.method_43469("roar_of_love.ui.immersion_open", new Object[0]), class_4185Var6 -> {
            openImmersion();
        }, i + 2, i6, 92, 20));
        method_37063(btn(class_2561.method_43469("roar_of_love.ui.layout_open", new Object[0]), class_4185Var7 -> {
            openLayout();
        }, i + 98, i6, 92, 20));
        toggleBtn(i4, i3 + 68, 150, RoarOfLoveConfig.isOtherMobCalls() ? "roar_of_love.ui.mob_calls_on" : "roar_of_love.ui.mob_calls_off", () -> {
            toggleMobCalls();
        });
        toggleBtn(i5, i3 + 68, 150, RoarOfLoveConfig.isMuteOtherHurt() ? "roar_of_love.ui.mute_hurt_on" : "roar_of_love.ui.mute_hurt_off", () -> {
            toggleMuteHurt();
        });
        toggleBtn(i4, i3 + 94, 150, RoarOfLoveConfig.isSoundEnabled("hurt") ? "roar_of_love.ui.hurt_on" : "roar_of_love.ui.hurt_off", () -> {
            toggleHurtSound();
        });
        toggleBtn(i5, i3 + 94, 150, RoarOfLoveConfig.isPostPeakDip() ? "roar_of_love.ui.postpeak_on" : "roar_of_love.ui.postpeak_off", () -> {
            togglePostPeak();
        });
        toggleBtn(i4, i3 + 120, 150, RoarOfLoveConfig.isDistantFade() ? "roar_of_love.ui.distant_on" : "roar_of_love.ui.distant_off", () -> {
            toggleDistant();
        });
        toggleBtn(i5, i3 + 120, 150, RoarOfLoveConfig.isOcclusion() ? "roar_of_love.ui.occlusion_on" : "roar_of_love.ui.occlusion_off", () -> {
            toggleOcclusion();
        });
        toggleBtn(i4, i3 + 146, 308, RoarOfLoveConfig.isAlignMoans() ? "roar_of_love.ui.align_on" : "roar_of_love.ui.align_off", () -> {
            toggleAlign();
        });
        int i7 = 0;
        while (true) {
            int i8 = i7;
            if (i8 >= arrayList.size()) {
                break;
            }
            int i9 = i8 / 2;
            int i10 = i8 % 2 == 0 ? i4 : i5;
            int i11 = i3 + 186 + (i9 * 24);
            Action action = (Action) arrayList.get(i8);
            method_37063(btn(class_2561.method_43470(action.label()), class_4185Var8 -> {
                RoarOfLove.LOGGER.info("[roar_of_love] 点击：{}", action.label());
                try {
                    action.run().run();
                } catch (Throwable th) {
                    RoarOfLove.LOGGER.warn("[roar_of_love] 点击处理异常：{}", action.label(), th);
                }
            }, i10, i11, 112, 20));
            method_37063(btn(class_2561.method_43469(RoarOfLoveConfig.isSoundEnabled(action.key()) ? "roar_of_love.ui.snd_on" : "roar_of_love.ui.snd_off", new Object[0]), class_4185Var9 -> {
                RoarOfLoveConfig.setSoundEnabled(action.key(), !RoarOfLoveConfig.isSoundEnabled(action.key()));
                rebuild();
            }, i10 + 116, i11, 36, 20));
            i7 = i8 + 1;
        }
        int min = Math.min(560, this.field_22789 - 60);
        int i12 = i - (min / 2);
        this.zipShown = detectedZipNames.size();
        int i13 = 0;
        while (true) {
            int i14 = i13;
            if (i14 < detectedZipNames.size()) {
                String str4 = (String) detectedZipNames.get(i14);
                boolean isZipEnabled = AudioPackLoader.isZipEnabled(str4);
                boolean equals = str4.equals(this.pendingZip);
                if (equals != isZipEnabled) {
                    str = equals ? "roar_of_love.ui.zip_pending_on" : "roar_of_love.ui.zip_pending_off";
                } else {
                    str = isZipEnabled ? "roar_of_love.ui.zip_on" : "roar_of_love.ui.zip_off";
                }
                method_37063(btn(class_2561.method_43469(str, new Object[0]).method_27692(equals ? class_124.field_1060 : class_124.field_1061).method_10852(class_2561.method_43470(trim(str4, min - 40)).method_27692(class_124.field_1068)), class_4185Var10 -> {
                    RoarOfLove.LOGGER.info("[roar_of_love] 点击：选择压缩包 {} -> 待{}", str4, equals ? "停用" : "启用");
                    if (equals) {
                        r4 = null;
                    }
                    this.pendingZip = r4;
                    rebuild();
                }, i12, i3 + i2 + (i14 * 23), min, 20));
                i13 = i14 + 1;
            } else {
                method_37063(btn(class_2561.method_43469("roar_of_love.ui.apply", new Object[0]), class_4185Var11 -> {
                    commitPending();
                }, i - 104, this.field_22790 - 28, 100, 20));
                method_37063(btn(class_2561.method_43469("roar_of_love.ui.done", new Object[0]), class_4185Var12 -> {
                    method_25419();
                }, i + 4, this.field_22790 - 28, 100, 20));
                this.listTopY = i3 + size;
                RoarOfLove.LOGGER.info("[roar_of_love] 设置界面 init：widgets={} zips={} 试听={} 滚动={}/{}", new Object[]{Integer.valueOf(method_25396().size()), Integer.valueOf(detectedZipNames.size()), Integer.valueOf(arrayList.size()), Integer.valueOf(this.scrollY), Integer.valueOf(this.maxScroll)});
                return;
            }
        }
    }

    public boolean method_25401(double d, double d2, double d3, double d4) {
        if (this.maxScroll > 0 && d4 != 0.0d) {
            scrollBy(d4 > 0.0d ? -24 : 24);
            return true;
        }
        return false;
    }

    private void scrollBy(int i) {
        int max = Math.max(0, Math.min(this.maxScroll, this.scrollY + i));
        if (max != this.scrollY) {
            this.scrollY = max;
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null) {
                method_1551.execute(() -> {
                    method_25426();
                });
            }
        }
    }

    private void checkPack() {
        try {
            List missingDefaultAssets = AudioPackLoader.missingDefaultAssets(class_310.method_1551());
            if (missingDefaultAssets.isEmpty()) {
                AudioPackLoader.sendChat(tr("roar_of_love.chat.pack_ok", new Object[0]));
                class_310.method_1551().method_1507(new NoticeScreen(this, new String[]{tr("roar_of_love.chat.pack_ok", new Object[0])}));
                return;
            }
            AudioPackLoader.sendChat(tr("roar_of_love.chat.pack_missing", Integer.valueOf(missingDefaultAssets.size())));
            int min = Math.min(9, missingDefaultAssets.size());
            String[] strArr = new String[min + 1];
            strArr[0] = tr("roar_of_love.chat.pack_missing", Integer.valueOf(missingDefaultAssets.size()));
            for (int i = 0; i < min; i++) {
                strArr[i + 1] = (String) missingDefaultAssets.get(i);
            }
            class_310.method_1551().method_1507(new NoticeScreen(this, strArr));
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 音效包检查异常", th);
        }
    }

    private void toggleBtn(int i, int i2, int i3, String str, Runnable runnable) {
        method_37063(btn(class_2561.method_43469(str, new Object[0]), class_4185Var -> {
            runnable.run();
        }, i, i2, i3, 20));
    }

    private void rebuild() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.execute(() -> {
                method_25426();
            });
        }
    }

    private void toggleHurtSound() {
        RoarOfLoveConfig.setSoundEnabled("hurt", !RoarOfLoveConfig.isSoundEnabled("hurt"));
        rebuild();
    }

    private void togglePostPeak() {
        RoarOfLoveConfig.setPostPeakDip(!RoarOfLoveConfig.isPostPeakDip());
        rebuild();
    }

    private void toggleDistant() {
        RoarOfLoveConfig.setDistantFade(!RoarOfLoveConfig.isDistantFade());
        rebuild();
    }

    private void toggleOcclusion() {
        RoarOfLoveConfig.setOcclusion(!RoarOfLoveConfig.isOcclusion());
        rebuild();
    }

    private void toggleAlign() {
        RoarOfLoveConfig.setAlignMoans(!RoarOfLoveConfig.isAlignMoans());
        rebuild();
    }

    private void previewHurt() {
        try {
            class_3414 class_3414Var = RoLSounds.get("minecraft:entity.player.hurt");
            if (class_3414Var == null) {
                AudioPackLoader.sendChat(tr("roar_of_love.chat.preview_fail", tr("roar_of_love.ui.hurt_preview", new Object[0])));
            } else {
                RoarOfLove.LOGGER.info("[roar_of_love] 试听受击音效 {}", class_3414Var.comp_3319());
                playLocal(class_3414Var);
            }
        } catch (Throwable th) {
            AudioPackLoader.sendChat(tr("roar_of_love.chat.preview_error", th));
        }
    }

    private class_4185 btn(class_2561 class_2561Var, class_4185.class_4241 class_4241Var, int i, int i2, int i3, int i4) {
        this.buttonRects.add(new int[]{i, i2, i3, i4});
        return new RoarButton(i, i2, i3, i4, class_2561Var, class_4241Var);
    }

    private void toggleMuteHurt() {
        RoarOfLoveConfig.setMuteOtherHurt(!RoarOfLoveConfig.isMuteOtherHurt());
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.execute(() -> {
                method_25426();
            });
        }
    }

    private void toggleMobCalls() {
        RoarOfLoveConfig.setOtherMobCalls(!RoarOfLoveConfig.isOtherMobCalls());
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.execute(() -> {
                method_25426();
            });
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

    private void openSubtitles() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.method_1507(new RoarSubtitleScreen(this));
        }
    }

    private void openFilters() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.method_1507(new RoarFilterScreen(this));
        }
    }

    private void togglePinkFilter() {
        RoarOfLoveConfig.setPinkFilter(!RoarOfLoveConfig.isPinkFilter());
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.execute(() -> {
                method_25426();
            });
        }
    }

    private void drawGlass(class_332 class_332Var) {
        for (int[] iArr : this.buttonRects) {
            int i = iArr[0];
            int i2 = iArr[1];
            int i3 = iArr[2] + i;
            int i4 = i2 + iArr[3];
            class_332Var.method_25294(i, i2, i3, i4, 0x33ffffff);
            class_332Var.method_25294(i, i2, i3, i2 + 1, -1929379841);
            class_332Var.method_25294(i, i4 - 1, i3, i4, -1929379841);
            class_332Var.method_25294(i, i2, i + 1, i4, -1929379841);
            class_332Var.method_25294(i3 - 1, i2, i3, i4, -1929379841);
        }
    }

    private static String trim(String str, int i) {
        int max = Math.max(6, i / 6);
        return str.length() <= max ? str : str.substring(0, Math.max(4, max - 1)) + "…";
    }

    private void refreshAndRebuild() {
        AudioPackLoader.refreshNow(() -> {
            class_310.method_1551().execute(() -> {
                this.statusText = AudioPackLoader.lastStatus();
                AudioPackLoader.sendChat(tr("roar_of_love.chat.refresh_done", this.statusText));
                method_25426();
            });
        });
    }

    private void previewCategory(String str) {
        int i;
        RoarOfLove.LOGGER.info("[roar_of_love] previewCategory 开始：{}", str);
        try {
            class_3414 pickCategoryPreview = AudioPackLoader.pickCategoryPreview(str);
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 == null || method_1551.method_1478() == null) {
                i = 0;
            } else {
                Iterator it = ((List) RoLSounds.CATEGORY_FILES.getOrDefault(str, List.of())).iterator();
                i = 0;
                while (it.hasNext()) {
                    i = method_1551.method_1478().method_14486(class_2960.method_60655("roar_of_love", "sounds/actionsounds/" + str + "/" + ((String) it.next()) + ".ogg")).isPresent() ? i + 1 : i;
                }
            }
            RoarOfLove.LOGGER.info("[roar_of_love] previewCategory 事件={} 可用文件={}", pickCategoryPreview == null ? "null" : pickCategoryPreview.comp_3319(), Integer.valueOf(i));
            if (pickCategoryPreview == null) {
                AudioPackLoader.sendChat(tr("roar_of_love.chat.preview_fail", str));
            } else {
                playLocal(pickCategoryPreview);
            }
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] previewCategory 异常", th);
            AudioPackLoader.sendChat(tr("roar_of_love.chat.preview_error", th));
        }
    }

    private void previewCall(String str, boolean z) {
        RoarOfLove.LOGGER.info("[roar_of_love] previewCall 开始：group={} begin={}", str, Boolean.valueOf(z));
        try {
            class_3414 pickCallPreview = AudioPackLoader.pickCallPreview(str, z);
            RoarOfLove.LOGGER.info("[roar_of_love] previewCall 事件={}", pickCallPreview == null ? "null" : pickCallPreview.comp_3319());
            if (pickCallPreview == null) {
                AudioPackLoader.sendChat(tr("roar_of_love.chat.no_group_call", tr("roar_of_love.call.group." + str, new Object[0])));
            } else {
                playLocal(pickCallPreview);
            }
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] previewCall 异常", th);
            AudioPackLoader.sendChat(tr("roar_of_love.chat.preview_error", th));
        }
    }

    private void playLocal(class_3414 class_3414Var) {
        RoarOfLove.LOGGER.info("[roar_of_love] playLocal 开始：{}", class_3414Var == null ? "null" : class_3414Var.comp_3319());
        try {
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null) {
                if (method_1551.field_1687 != null && method_1551.field_1724 != null) {
                    method_1551.field_1687.method_8486(method_1551.field_1724.method_23317(), method_1551.field_1724.method_23318() + (method_1551.field_1724.method_17682() * 0.5d), method_1551.field_1724.method_23321(), class_3414Var, class_3419.field_15250, RoarOfLoveConfig.volume(), 1.0f, false);
                    RoarOfLove.LOGGER.info("[roar_of_love] 试听(世界内播放) {} ok", class_3414Var.comp_3319());
                } else if (method_1551.method_1483() != null) {
                    class_1140.class_11518 method_4873 = method_1551.method_1483().method_4873(class_1109.method_4757(class_3414Var, 1.0f, RoarOfLoveConfig.volume()));
                    RoarOfLove.LOGGER.info("[roar_of_love] 试听(菜单) {} -> {}", class_3414Var.comp_3319(), method_4873);
                    if (method_4873 == class_1140.class_11518.field_60956) {
                        AudioPackLoader.sendChat(tr("roar_of_love.chat.not_loaded", class_3414Var.comp_3319()));
                    } else if (method_4873 == class_1140.class_11518.field_60955) {
                        AudioPackLoader.sendChat(tr("roar_of_love.chat.silent", new Object[0]));
                    }
                }
            }
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] playLocal 异常", th);
            AudioPackLoader.sendChat(tr("roar_of_love.chat.play_error", th));
        }
    }

    public boolean method_25402(class_11909 class_11909Var, boolean z) {
        RoarOfLove.LOGGER.info("[roar_of_love] 设置界面 mouseClicked x={} y={} button={}", new Object[]{Double.valueOf(class_11909Var.comp_4798()), Double.valueOf(class_11909Var.comp_4799()), Integer.valueOf(class_11909Var.method_74245())});
        try {
            List method_25396 = method_25396();
            for (int size = method_25396.size() - 1; size >= 0; size--) {
                Object obj = method_25396.get(size);
                if (obj instanceof class_339) {
                    class_339 class_339Var = (class_339) obj;
                    if (class_339Var.field_22763 && class_339Var.method_25405(class_11909Var.comp_4798(), class_11909Var.comp_4799()) && class_339Var.method_25402(class_11909Var, z)) {
                        return true;
                    }
                }
            }
        } catch (Throwable th) {
            RoarOfLove.LOGGER.debug("[roar_of_love] 点击分发异常", th);
        }
        return super.method_25402(class_11909Var, z);
    }

    public void method_25394(class_332 class_332Var, int i, int i2, float f) {
        class_5250 method_43469;
        class_332Var.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
        int i3 = this.field_22790 - 36;
        class_332Var.method_25294(0, 0, this.field_22789, 32, -1072689136);
        class_332Var.method_25294(0, i3, this.field_22789, this.field_22790, -1072689136);
        super.method_25394(class_332Var, i, i2, f);
        if (this.field_22787 != null && this.field_22793 != null) {
            centered(class_332Var, class_2561.method_43469("roar_of_love.ui.title", new Object[0]), this.field_22789 / 2, 6, 16777215);
            centered(class_332Var, class_2561.method_43470(statusLine()), this.field_22789 / 2, 18, -4144960);
            int i4 = 32 - this.scrollY;
            drawRegionText(class_332Var, class_2561.method_43469("roar_of_love.ui.section_ui", new Object[0]), i4 + 18, 32, i3, -2039553);
            drawRegionText(class_332Var, class_2561.method_43469("roar_of_love.ui.section_audio", new Object[0]), i4 + 56, 32, i3, -2039553);
            drawRegionText(class_332Var, class_2561.method_43469("roar_of_love.ui.preview_header", new Object[0]), i4 + 172, 32, i3, -2039553);
            List detectedZipNames = AudioPackLoader.detectedZipNames();
            if (isDirty()) {
                method_43469 = class_2561.method_43469("roar_of_love.ui.group_header_dirty", new Object[0]);
            } else {
                method_43469 = class_2561.method_43469("roar_of_love.ui.group_header", new Object[0]);
            }
            drawRegionText(class_332Var, method_43469, this.listTopY, 32, i3, -2039553);
            if (detectedZipNames.isEmpty()) {
                drawRegionText(class_332Var, class_2561.method_43469("roar_of_love.ui.no_pack", new Object[0]), this.listTopY + 18, 32, i3, -7303024);
            } else if (!this.missingNote.isEmpty()) {
                drawRegionText(class_332Var, class_2561.method_43470(this.missingNote), this.listTopY + (detectedZipNames.size() * 23) + 8, 32, i3, -39322);
            }
            drawScrollbar(class_332Var, 32, i3);
            class_332Var.method_27535(this.field_22793, class_2561.method_43469("roar_of_love.ui.author", new Object[0]), 6, this.field_22790 - 11, -6381922);
            if (this.maxScroll > 0) {
                class_5250 method_434692 = class_2561.method_43469("roar_of_love.ui.scroll_hint", new Object[0]);
                class_332Var.method_27535(this.field_22793, method_434692, (this.field_22789 - 6) - this.field_22793.method_27525(method_434692), this.field_22790 - 11, -6381922);
            }
        }
    }

    private void drawRegionText(class_332 class_332Var, class_2561 class_2561Var, int i, int i2, int i3, int i4) {
        if (i >= i2 - 8 && i + 9 <= i3) {
            centered(class_332Var, class_2561Var, this.field_22789 / 2, i, i4);
        }
    }

    private void drawScrollbar(class_332 class_332Var, int i, int i2) {
        if (this.maxScroll > 0) {
            int i3 = i2 - i;
            int max = Math.max(16, (i3 * i3) / (this.maxScroll + i3));
            int i4 = i + (((i3 - max) * this.scrollY) / this.maxScroll);
            int i5 = this.field_22789 - 6;
            class_332Var.method_25294(i5, i, i5 + 3, i2, -8355712);
            class_332Var.method_25294(i5, i4, i5 + 3, i4 + max, -3158065);
        }
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

    private void centered(class_332 class_332Var, class_2561 class_2561Var, int i, int i2, int i3) {
        class_332Var.method_27535(this.field_22793, class_2561Var, i - (this.field_22793.method_27525(class_2561Var) / 2), i2, i3);
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }
}

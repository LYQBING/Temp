//
// Decompiled by Jadx - 1029ms
//
package com.roaroflove.client;

import com.roaroflove.RoarOfLove;
import com.roaroflove.client.audio.AudioPackLoader;
import com.roaroflove.client.ui.RoarButton;
import com.roaroflove.client.ui.RoarHoverTooltip;
import com.roaroflove.config.RoarOfLoveConfig;
import com.roaroflove.sound.RoLSounds;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_10799;
import net.minecraft.class_11909;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_5250;

public class RoarOfLoveSettingsScreen extends class_437 {
    public static final String AUTHOR = "哔哩哔哩：佳佳不好";
    private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
    public static final String TITLE = "音频设置";
    private final List<String[]> sectionHeaderRects;
    private String cachedStatus;
    private int cachedStatusWidth;
    private int maxScroll;
    private String missingNote;
    private final class_437 parent;
    private String pendingZip;
    private int scrollY;
    private String statusLine;
    private String statusText;

    public RoarOfLoveSettingsScreen(class_437 class_437Var) {
        super(class_2561.method_43469("roar_of_love.ui.title", new Object[0]));
        this.statusText = "";
        this.missingNote = "";
        this.cachedStatus = null;
        this.statusLine = "";
        this.cachedStatusWidth = -1;
        this.pendingZip = null;
        this.sectionHeaderRects = new ArrayList();
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

    private void openHandbook() {
        class_310 client = class_310.method_1551();
        if (client != null) client.method_1507(new RoarHandbookScreen(this));
    }

    private void openAudioEditor(String slotId) {
        class_310 client = class_310.method_1551();
        if (client != null) client.method_1507(new RoarAudioSlotScreen(this, slotId));
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

    protected void method_25426() {
        method_37067();
        RoarButton.resetVariants();
        this.sectionHeaderRects.clear();
        RoarHoverTooltip.reset();
        int center = this.field_22789 / 2;
        int left = center - 154;
        int right = center + 4;
        List<RoarOfLoveSettingsScreen$Action> previews = new ArrayList<>();
        for (String str2 : RoLSounds.CATEGORIES) {
            previews.add(new RoarOfLoveSettingsScreen$Action(tr("roar_of_love.cat." + str2), "cat:" + str2, () -> {
                openAudioEditor("cat:" + str2);
            }));
        }
        previews.add(new RoarOfLoveSettingsScreen$Action(tr("roar_of_love.ui.hurt_preview"), "hunt:hurt", () -> {
            openAudioEditor("hunt:hurt");
        }));
        previews.add(new RoarOfLoveSettingsScreen$Action(tr("roar_of_love.call.group.default"), "call:default", () -> {
            openAudioEditor("call:default");
        }));
        for (String str3 : RoLSounds.CALL_GROUPS) {
            if (!"default".equals(str3)) {
                previews.add(new RoarOfLoveSettingsScreen$Action(tr("roar_of_love.call.group." + str3), "call:" + str3, () -> {
                    openAudioEditor("call:" + str3);
                }));
            }
        }
        List<String> detectedZipNames = AudioPackLoader.detectedZipNames();
        int previewRows = (previews.size() + 1) / 2;
        int zipTopOffset = 38 + 26 + (4 * 24) + 26 + (4 * 24) + 26 + (previewRows * 24) + 26 + (3 * 24) + 24 + 26;
        int contentBottom = zipTopOffset + (Math.max(1, detectedZipNames.size()) * 23) + (this.missingNote.isEmpty() ? 0 : 10) + (detectedZipNames.isEmpty() ? 10 : 0);
        this.maxScroll = Math.max(0, contentBottom - (this.field_22790 - 64));
        if (this.scrollY > this.maxScroll) this.scrollY = this.maxScroll;

        int top = 38 - this.scrollY;
        int y = section("roar_of_love.ui.section_fx", center, top);
        toggle(left, y, 150, RoarOfLoveConfig.isFlashEffect(), "flash", () -> RoarOfLoveConfig.setFlashEffect(!RoarOfLoveConfig.isFlashEffect()), "roar_of_love.tip.flash");
        toggle(right, y, 150, RoarOfLoveConfig.isShakeEffect(), "shake", () -> RoarOfLoveConfig.setShakeEffect(!RoarOfLoveConfig.isShakeEffect()), "roar_of_love.tip.shake");
        y += 24;
        toggle(left, y, 150, RoarOfLoveConfig.isPushEffect(), "push", () -> RoarOfLoveConfig.setPushEffect(!RoarOfLoveConfig.isPushEffect()), "roar_of_love.tip.push");
        toggle(right, y, 150, RoarOfLoveConfig.isHeatBar(), "heat", () -> RoarOfLoveConfig.setHeatBar(!RoarOfLoveConfig.isHeatBar()), "roar_of_love.tip.heat");
        y += 24;
        toggle(left, y, 150, RoarOfLoveConfig.isOverload(), "overload", () -> RoarOfLoveConfig.setOverload(!RoarOfLoveConfig.isOverload()), "roar_of_love.tip.overload");
        toggle(right, y, 150, RoarOfLoveConfig.isDirector(), "director", () -> RoarOfLoveConfig.setDirector(!RoarOfLoveConfig.isDirector()), "roar_of_love.tip.director");
        y += 24;
        toggle(left, y, 308, RoarOfLoveConfig.isDirSub(), "dirsub", () -> RoarOfLoveConfig.setDirSub(!RoarOfLoveConfig.isDirSub()), "");

        y = section("roar_of_love.ui.section_audio", center, y + 24);
        toggle(left, y, 150, RoarOfLoveConfig.isSoundEnabled("hurt"), "hurt", () -> RoarOfLoveConfig.setSoundEnabled("hurt", !RoarOfLoveConfig.isSoundEnabled("hurt")), "roar_of_love.tip.hurt");
        toggle(right, y, 150, RoarOfLoveConfig.isOtherMobCalls(), "mob_calls", () -> RoarOfLoveConfig.setOtherMobCalls(!RoarOfLoveConfig.isOtherMobCalls()), "roar_of_love.tip.mob_calls");
        y += 24;
        toggle(left, y, 150, RoarOfLoveConfig.isMuteOtherHurt(), "mute_hurt", () -> RoarOfLoveConfig.setMuteOtherHurt(!RoarOfLoveConfig.isMuteOtherHurt()), "roar_of_love.tip.mute_hurt");
        toggle(right, y, 150, RoarOfLoveConfig.isAlignMoans(), "align", () -> RoarOfLoveConfig.setAlignMoans(!RoarOfLoveConfig.isAlignMoans()), "roar_of_love.tip.align");
        y += 24;
        toggle(left, y, 150, RoarOfLoveConfig.isDistantFade(), "distant", () -> RoarOfLoveConfig.setDistantFade(!RoarOfLoveConfig.isDistantFade()), "");
        toggle(right, y, 150, RoarOfLoveConfig.isOcclusion(), "occlusion", () -> RoarOfLoveConfig.setOcclusion(!RoarOfLoveConfig.isOcclusion()), "");
        y += 24;
        toggle(left, y, 150, RoarOfLoveConfig.isPostPeakDip(), "postpeak", () -> RoarOfLoveConfig.setPostPeakDip(!RoarOfLoveConfig.isPostPeakDip()), "roar_of_love.tip.postpeak");
        toggle(right, y, 150, RoarOfLoveConfig.isAmbienceSound(), "ambience", () -> RoarOfLoveConfig.setAmbienceSound(!RoarOfLoveConfig.isAmbienceSound()), "roar_of_love.tip.ambience");

        int previewTop = section("roar_of_love.ui.center_preview", center, y + 24);
        for (int index = 0; index < previews.size(); index++) {
            RoarOfLoveSettingsScreen$Action action = previews.get(index);
            int x = index % 2 == 0 ? left : right;
            int rowY = previewTop + (index / 2) * 24;
            method_37063(btn(class_2561.method_43470(action.label()), button -> action.run().run(), x, rowY, 112, 20));
            method_37063(btn(class_2561.method_43469(RoarOfLoveConfig.isSoundEnabled(action.key()) ? "roar_of_love.ui.snd_on" : "roar_of_love.ui.snd_off", new Object[0]), button -> {
                RoarOfLoveConfig.setSoundEnabled(action.key(), !RoarOfLoveConfig.isSoundEnabled(action.key()));
                rebuild();
            }, x + 116, rowY, 34, 20));
        }

        int toolTop = section("roar_of_love.ui.section_tools", center, previewTop + ((previews.size() + 1) / 2) * 24);
        link(left, toolTop, 150, "roar_of_love.ui.filters_open", this::openFilters);
        link(right, toolTop, 150, "roar_of_love.ui.subtitles_open", this::openSubtitles);
        toolTop += 24;
        link(left, toolTop, 150, "roar_of_love.ui.immersion_open", this::openImmersion);
        link(right, toolTop, 150, "roar_of_love.ui.layout_open", this::openLayout);
        toolTop += 24;
        method_37063(btn(class_2561.method_43469("roar_of_love.ui.audio_editor", new Object[0]), button -> openAudioEditor(""), left, toolTop, 150, 20));
        link(right, toolTop, 150, "roar_of_love.ui.handbook_open", this::openHandbook);
        toolTop += 24;
        method_37063(btn(class_2561.method_43469("roar_of_love.ui.open_audio_folder", new Object[0]), button -> AudioPackLoader.openAudioFolder(), left, toolTop, 150, 20));
        method_37063(btn(class_2561.method_43469("roar_of_love.ui.pack_check", new Object[0]), button -> checkPack(), right, toolTop, 150, 20));

        int zipTop = section(isDirty() ? "roar_of_love.ui.group_header_dirty" : "roar_of_love.ui.group_header", center, toolTop + 24);
        int zipWidth = Math.min(308, this.field_22789 - 24);
        int zipX = center - zipWidth / 2;
        for (int index = 0; index < detectedZipNames.size(); index++) {
            String zipName = detectedZipNames.get(index);
            boolean selected = zipName.equals(this.pendingZip);
            boolean enabled = AudioPackLoader.isZipEnabled(zipName);
            String key = selected != enabled ? (selected ? "roar_of_love.ui.zip_pending_on" : "roar_of_love.ui.zip_pending_off") : (enabled ? "roar_of_love.ui.zip_on" : "roar_of_love.ui.zip_off");
            String nextZip = selected ? null : zipName;
            method_37063(btn(class_2561.method_43469(key, new Object[0]).method_27692(selected ? class_124.field_1060 : class_124.field_1061).method_10852(class_2561.method_43470(trim(zipName, zipWidth - 40)).method_27692(class_124.field_1068)), button -> {
                this.pendingZip = nextZip;
                rebuild();
            }, zipX, zipTop + index * 23, zipWidth, 20));
        }
        int footerY = this.field_22790 - 26;
        int footerWidth = Math.min(130, (this.field_22789 - 20) / 3);
        int footerGap = 4;
        int footerX = Math.max(4, center - ((footerWidth * 3 + footerGap * 2) / 2));
        RoarButton checkButton = new RoarButton(footerX, footerY, footerWidth, 20, class_2561.method_43469("roar_of_love.ui.builtin_check", new Object[0]), button -> checkPack());
        checkButton.field_22763 = this.scrollY >= this.maxScroll - 1;
        method_37063(checkButton);
        method_37063(new RoarButton(footerX + footerWidth + footerGap, footerY, footerWidth, 20, class_2561.method_43469("roar_of_love.ui.apply", new Object[0]), button -> commitPending()));
        method_37063(new RoarButton(footerX + (footerWidth + footerGap) * 2, footerY, footerWidth, 20, class_2561.method_43469("roar_of_love.ui.done", new Object[0]), button -> method_25419()));
        RoarHoverTooltip.register(footerX, footerY, footerWidth, 20, "roar_of_love.tip.builtin_check");
        RoarOfLove.LOGGER.debug("[roar_of_love] 设置界面重建：试听 {} 项，音频包 {} 个，滚动 {}/{}", previews.size(), detectedZipNames.size(), this.scrollY, this.maxScroll);
    }

    private int section(String key, int center, int y) {
        // Align stored section header Y to the same drawing baseline used by the scrollbar (32),
        // so helpers that lookup section positions and the scrollbar use the same coordinates.
        this.sectionHeaderRects.add(new String[]{key, String.valueOf(y - 6)});
        return y + 26;
    }

    private void toggle(int x, int y, int width, boolean enabled, String key, Runnable action, String tooltip) {
        method_37063(btn(class_2561.method_43469("roar_of_love.ui." + key + (enabled ? "_on" : "_off"), new Object[0]), button -> {
            action.run();
            rebuild();
        }, x, y, width, 20));
        RoarHoverTooltip.register(x, y, width, 20, tooltip);
    }

    private void link(int x, int y, int width, String key, Runnable action) {
        method_37063(btn(class_2561.method_43469(key, new Object[0]), button -> action.run(), x, y, width, 20));
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
                class_310.method_1551().method_1507(new RoarOfLoveSettingsScreen$NoticeScreen(this, tr("roar_of_love.chat.pack_ok", new Object[0])));
                return;
            }
            AudioPackLoader.sendChat(tr("roar_of_love.chat.pack_missing", Integer.valueOf(missingDefaultAssets.size())));
            int min = Math.min(9, missingDefaultAssets.size());
            String[] strArr = new String[min + 1];
            strArr[0] = tr("roar_of_love.chat.pack_missing", Integer.valueOf(missingDefaultAssets.size()));
            for (int i = 0; i < min; i++) {
                strArr[i + 1] = (String) missingDefaultAssets.get(i);
            }
            class_310.method_1551().method_1507(new RoarOfLoveSettingsScreen$NoticeScreen(this, strArr));
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 音效包检查异常", th);
        }
    }

    private void rebuild() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.execute(() -> {
                method_25426();
            });
        }
    }

    private class_4185 btn(class_2561 class_2561Var, class_4185.class_4241 class_4241Var, int i, int i2, int i3, int i4) {
        return new RoarButton(i, i2, i3, i4, class_2561Var, class_4241Var);
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
        class_332Var.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
        class_332Var.method_25294(0, 0, this.field_22789, 32, -1072689136);
        class_332Var.method_25294(0, this.field_22790 - 34, this.field_22789, this.field_22790, -1072689136);
        super.method_25394(class_332Var, i, i2, f);
        if (this.field_22787 != null && this.field_22793 != null) {
            int center = this.field_22789 / 2;
            int contentBottom = this.field_22790 - 34;
            centered(class_332Var, class_2561.method_43469("roar_of_love.ui.title", new Object[0]), center, 4, 16777215);
            centered(class_332Var, class_2561.method_43470(statusLine()), center, 14, -4144960);
            for (String[] section : this.sectionHeaderRects) {
                int y = Integer.parseInt(section[1]);
                if (y >= 32 && y + 10 <= contentBottom) {
                    centered(class_332Var, class_2561.method_43469(section[0], new Object[0]), center, y + 4, -2039553);
                }
            }
            List<String> detectedZipNames = AudioPackLoader.detectedZipNames();
            int zipHeaderY = findSectionY("roar_of_love.ui.group_header", "roar_of_love.ui.group_header_dirty");
            if (zipHeaderY >= 0 && detectedZipNames.isEmpty()) {
                drawRegionText(class_332Var, class_2561.method_43469("roar_of_love.ui.no_pack", new Object[0]), zipHeaderY + 26, 32, contentBottom, -7303024);
            }
            if (zipHeaderY >= 0 && !this.missingNote.isEmpty()) {
                int noteY = zipHeaderY + 26 + (Math.max(1, detectedZipNames.size()) * 23);
                drawRegionText(class_332Var, class_2561.method_43470(this.missingNote), noteY, 32, contentBottom, -39322);
            }
            drawScrollbar(class_332Var, 32, contentBottom);
            class_332Var.method_27535(this.field_22793, class_2561.method_43469("roar_of_love.ui.author", new Object[0]), 6, this.field_22790 - 11, -6381922);
            if (isDirty()) {
                class_5250 dirtyText = class_2561.method_43469("roar_of_love.ui.apply_now_dirty", new Object[0]);
                class_332Var.method_27535(this.field_22793, dirtyText, (this.field_22789 - 8) - this.field_22793.method_27525(dirtyText), this.field_22790 - 11, -15797);
            } else if (this.maxScroll > 0) {
                class_5250 scrollHint = class_2561.method_43469(this.scrollY >= this.maxScroll - 1 ? "roar_of_love.ui.scroll_end" : "roar_of_love.ui.scroll_hint", new Object[0]);
                class_332Var.method_27535(this.field_22793, scrollHint, (this.field_22789 - 8) - this.field_22793.method_27525(scrollHint), this.field_22790 - 11, -6381828);
            }
            RoarHoverTooltip.render(class_332Var, i, i2, this.field_22789, this.field_22790);
        }
    }

    private int findSectionY(String... keys) {
        for (String[] section : this.sectionHeaderRects) {
            for (String key : keys) {
                if (section[0].equals(key)) return Integer.parseInt(section[1]);
            }
        }
        return -1;
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
            int maxWidth = this.field_22789 - 12;
            while (length > 1 && widthOf(str.substring(0, length) + "…") > maxWidth) {
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

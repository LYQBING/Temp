//
// Decompiled by Jadx - 792ms
//
package com.roaroflove.client;

import com.roaroflove.RoarOfLove;
import com.roaroflove.client.audio.AudioPackLoader;
import com.roaroflove.client.ui.RoarButton;
import com.roaroflove.client.ui.RoarTooltip;
import com.roaroflove.config.RoarOfLoveConfig;
import com.roaroflove.sound.RoLSounds;
import com.roaroflove.util.AudioPaths;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.class_10799;
import net.minecraft.class_1109;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_3414;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_5250;

public class RoarAudioScreen extends class_437 {
    private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
    private static final int COL_GAP = 6;
    private static final int LEFT_W = 124;
    private static final int ROW_H = 14;
    private final String category;
    private final List<Column> columns;
    private int listBottom;
    private int listTop;
    private int maxScroll;
    private final class_437 parent;
    private int scrollY;
    private int selectedColumn;
    private int selectedRow;
    private final String slotId;
    private String toast;
    private long toastUntil;

    public RoarAudioScreen(class_437 class_437Var, String str) {
        super(class_2561.method_43469("roar_of_love.ui.audio_title", new Object[]{class_2561.method_43469(RoLSounds.labelKeyFor(str), new Object[0])}));
        this.columns = new ArrayList();
        this.scrollY = 0;
        this.maxScroll = 0;
        this.selectedColumn = 0;
        this.selectedRow = 0;
        this.listTop = 26;
        this.listBottom = 200;
        this.toast = "";
        this.toastUntil = 0L;
        this.parent = class_437Var;
        this.slotId = str;
        this.category = AudioPackLoader.slotName(str);
        reloadEntries();
    }

    private static String tr(String str, Object... objArr) {
        return class_2561.method_43469(str, objArr).getString();
    }

    private String trimTo(String str, int i) {
        if (this.field_22793 == null || str == null) {
            return "";
        }
        if (this.field_22793.method_27525(class_2561.method_43470(str)) > i) {
            while (str.length() > 1 && this.field_22793.method_27525(class_2561.method_43470(str + "…")) > i) {
                str = str.substring(0, str.length() - 1);
            }
            return str + "…";
        }
        return str;
    }

    private void reloadEntries() {
        this.columns.clear();
        String slotKind = AudioPackLoader.slotKind(this.slotId);
        char c = 65535;
        switch (slotKind.hashCode()) {
            case 98262:
                if (slotKind.equals("cat")) {
                    c = 0;
                    break;
                }
                break;
            case 3045982:
                if (slotKind.equals("call")) {
                    c = 1;
                    break;
                }
                break;
            case 3214227:
                if (slotKind.equals("hunt")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                if (RoLSounds.isSplitCategory(this.category)) {
                    Column column = new Column("roar_of_love.ui.col_shot_in");
                    column.files.addAll(RoLSounds.shotInFiles());
                    Column column2 = new Column("roar_of_love.ui.col_shot_out");
                    column2.files.addAll(RoLSounds.shotOutFiles());
                    this.columns.add(column);
                    this.columns.add(column2);
                    break;
                } else {
                    Column column3 = new Column("roar_of_love.ui.col_single");
                    column3.files.addAll((Collection) RoLSounds.CATEGORY_FILES.getOrDefault(this.category, List.of()));
                    this.columns.add(column3);
                    break;
                }
            case 1:
                Column column4 = new Column("roar_of_love.ui.col_single");
                column4.files.addAll(RoLSounds.callSlotsFor(this.category));
                this.columns.add(column4);
                break;
            case 2:
                Column column5 = new Column("roar_of_love.ui.col_single");
                column5.files.addAll(RoLSounds.hurtSlots());
                this.columns.add(column5);
                break;
        }
        if (this.selectedColumn >= this.columns.size()) {
            this.selectedColumn = 0;
        }
        if (selectedFile() == null) {
            this.selectedRow = 0;
        }
    }

    private Column columnAt(int i) {
        if (i < 0 || i >= this.columns.size()) {
            return null;
        }
        return this.columns.get(i);
    }

    private Column activeColumn() {
        return columnAt(this.selectedColumn);
    }

    private String selectedFile() {
        Column activeColumn = activeColumn();
        if (activeColumn == null || activeColumn.files.isEmpty()) {
            return null;
        }
        return (String) activeColumn.files.get(Math.max(0, Math.min(activeColumn.files.size() - 1, this.selectedRow)));
    }

    private String keyOf(String str) {
        return RoLSounds.eventKeyFor(this.slotId, str);
    }

    private void refresh() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.execute(() -> {
                method_25426();
            });
        }
    }

    private void notice(String str) {
        if (str == null) {
            str = "";
        }
        this.toast = str;
        this.toastUntil = System.currentTimeMillis() + 3000;
        if (!this.toast.isEmpty()) {
            AudioPackLoader.sendChat(this.toast);
        }
    }

    protected void method_25426() {
        method_37067();
        RoarButton.resetVariants();
        reloadEntries();
        this.listTop = 48;
        this.listBottom = this.field_22790 - 58;
        Column activeColumn = activeColumn();
        this.maxScroll = Math.max(0, (activeColumn == null ? 0 : activeColumn.files.size()) - Math.max(1, (this.listBottom - this.listTop) / ROW_H));
        if (this.scrollY > this.maxScroll) {
            this.scrollY = this.maxScroll;
        }
        RoarTooltip.reset();
        btnTip("roar_of_love.ui.preview_one", 8, 26, LEFT_W, "", class_4185Var -> {
            previewSelected();
        });
        String selectedFile = selectedFile();
        btnTip(selectedFile == null || RoarOfLoveConfig.isSoundEnabled(keyOf(selectedFile)) ? "roar_of_love.ui.snd_on" : "roar_of_love.ui.snd_off", 8, 50, LEFT_W, "", class_4185Var2 -> {
            toggleSelected();
        });
        btnTip("roar_of_love.ui.replace_one", 8, 74, LEFT_W, "roar_of_love.tip.replace", class_4185Var3 -> {
            pickFile();
        });
        btnTip("roar_of_love.ui.reset_one", 8, 98, LEFT_W, "roar_of_love.tip.reset", class_4185Var4 -> {
            resetSelected();
        });
        btnTip((class_2561) class_2561.method_43469("roar_of_love.ui.strength", new Object[]{Integer.valueOf(Math.round(volumeOfSelected() * 100.0f))}), 8, 122, LEFT_W, "roar_of_love.tip.volume", class_4185Var5 -> {
            volumeSelected(-0.1f);
        });
        btnTip((class_2561) class_2561.method_43469("roar_of_love.ui.strength", new Object[]{Integer.valueOf(Math.round(volumeOfSelected() * 100.0f))}), 8, 146, LEFT_W, "", class_4185Var6 -> {
            volumeSelected(0.1f);
        });
        btnTip("roar_of_love.ui.open_slot_folder", 8, 174, LEFT_W, "roar_of_love.tip.slot_folder", class_4185Var7 -> {
            openSlotFolder();
        });
        btnTip(AudioPackLoader.isPackDirty() ? "roar_of_love.ui.apply_now_dirty" : "roar_of_love.ui.apply_now", 8, 198, LEFT_W, "roar_of_love.tip.apply", class_4185Var8 -> {
            notice(AudioPackLoader.applyNow());
        });
        btnTip("roar_of_love.ui.open_audio_folder", 8, 222, LEFT_W, "", class_4185Var9 -> {
            AudioPackLoader.openAudioFolder();
        });
        btnTip("roar_of_love.ui.back", 8, this.field_22790 - 24, LEFT_W, "", class_4185Var10 -> {
            method_25419();
        });
    }

    private void openSlotFolder() {
        Path customDirFor = AudioPackLoader.customDirFor(this.slotId);
        if (customDirFor == null) {
            notice(tr("roar_of_love.chat.replace_unsupported", new Object[0]));
            return;
        }
        try {
            Files.createDirectories(customDirFor, new FileAttribute[0]);
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 建立槽位目录失败 {}", customDirFor, th);
        }
        AudioPackLoader.openFolder(customDirFor);
        notice(tr("roar_of_love.chat.opened_slot", customDirFor.toAbsolutePath().toString()));
    }

    private void pickFile() {
        class_310 method_1551;
        String selectedFile = selectedFile();
        if (selectedFile != null && (method_1551 = class_310.method_1551()) != null) {
            Path customDirFor = AudioPackLoader.customDirFor(this.slotId);
            if (customDirFor == null || !Files.isDirectory(customDirFor, new LinkOption[0])) {
                customDirFor = AudioPaths.audioFolder();
            }
            method_1551.method_1507(new RoarFilePickerScreen(this, customDirFor, selectedFile, path -> {
                notice(AudioPackLoader.replaceFrom(this.slotId, selectedFile, path));
                refresh();
            }));
        }
    }

    private void resetSelected() {
        String selectedFile = selectedFile();
        if (selectedFile != null) {
            notice(AudioPackLoader.resetSlot(this.slotId, selectedFile));
            refresh();
        }
    }

    private class_4185 btn(String str, int i, int i2, int i3, class_4185.class_4241 class_4241Var) {
        return btnTip((class_2561) class_2561.method_43469(str, new Object[0]), i, i2, i3, "", class_4241Var);
    }

    private class_4185 btnTip(String str, int i, int i2, int i3, String str2, class_4185.class_4241 class_4241Var) {
        return btnTip((class_2561) class_2561.method_43469(str, new Object[0]), i, i2, i3, str2, class_4241Var);
    }

    private class_4185 btnTip(class_2561 class_2561Var, int i, int i2, int i3, String str, class_4185.class_4241 class_4241Var) {
        RoarButton roarButton = new RoarButton(i, i2, i3, 20, class_2561Var, class_4241Var);
        method_37063(roarButton);
        if (str != null && !str.isEmpty()) {
            RoarTooltip.register(i, i2, i3, 20, str);
        }
        return roarButton;
    }

    private int volumeSteps(String str) {
        return RoarOfLoveConfig.soundVolumeSteps(str);
    }

    private float volumeOfSelected() {
        if (selectedFile() == null) {
            return 1.0f;
        }
        return volumeSteps(keyOf(r0)) / 10.0f;
    }

    private void volumeSelected(float f) {
        String selectedFile = selectedFile();
        if (selectedFile != null) {
            String keyOf = keyOf(selectedFile);
            RoarOfLoveConfig.setSoundVolumeSteps(keyOf, (f > 0.0f ? 1 : -1) + volumeSteps(keyOf));
            refresh();
        }
    }

    private void toggleSelected() {
        String selectedFile = selectedFile();
        if (selectedFile != null) {
            String keyOf = keyOf(selectedFile);
            RoarOfLoveConfig.setSoundEnabled(keyOf, !RoarOfLoveConfig.isSoundEnabled(keyOf));
            refresh();
        }
    }

    private void previewSelected() {
        String selectedFile = selectedFile();
        if (selectedFile == null) {
            notice(tr("roar_of_love.chat.preview_no_slot", new Object[0]));
            return;
        }
        try {
            class_3414 class_3414Var = RoLSounds.get(keyOf(selectedFile));
            if (class_3414Var == null) {
                notice(tr("roar_of_love.chat.preview_fail", selectedFile));
            } else {
                class_310 method_1551 = class_310.method_1551();
                if (method_1551 != null && method_1551.method_1483() != null) {
                    method_1551.method_1483().method_4873(class_1109.method_4757(class_3414Var, 1.0f, RoarOfLoveConfig.volume() * volumeOfSelected()));
                }
            }
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 试听失败", th);
        }
    }

    public boolean method_25401(double d, double d2, double d3, double d4) {
        Column activeColumn;
        if (d4 == 0.0d || (activeColumn = activeColumn()) == null || activeColumn.files.isEmpty()) {
            return false;
        }
        int max = Math.max(0, Math.min(activeColumn.files.size() - 1, (d4 > 0.0d ? -1 : 1) + this.selectedRow));
        if (max != this.selectedRow) {
            this.selectedRow = max;
            int max2 = Math.max(1, (this.listBottom - this.listTop) / ROW_H);
            if (this.selectedRow < this.scrollY) {
                this.scrollY = this.selectedRow;
            }
            if (this.selectedRow >= this.scrollY + max2) {
                this.scrollY = (this.selectedRow - max2) + 1;
            }
            refresh();
        }
        return true;
    }

    public boolean method_25402(class_11909 class_11909Var, boolean z) {
        for (int size = method_25396().size() - 1; size >= 0; size--) {
            Object obj = method_25396().get(size);
            if (obj instanceof class_339) {
                class_339 class_339Var = (class_339) obj;
                if (class_339Var.field_22763 && class_339Var.method_25405(class_11909Var.comp_4798(), class_11909Var.comp_4799())) {
                    try {
                        if (class_339Var.method_25402(class_11909Var, z)) {
                            return true;
                        }
                    } catch (Throwable th) {
                    }
                }
            }
        }
        int comp_4799 = (int) class_11909Var.comp_4799();
        if (comp_4799 >= this.listTop && comp_4799 <= this.listBottom) {
            int i = this.scrollY + ((comp_4799 - this.listTop) / ROW_H);
            for (int i2 = 0; i2 < this.columns.size(); i2++) {
                int[] columnSpan = columnSpan(i2);
                if (class_11909Var.comp_4798() >= columnSpan[0] && class_11909Var.comp_4798() <= columnSpan[1]) {
                    Column column = this.columns.get(i2);
                    if (i >= 0 && i < column.files.size()) {
                        this.selectedColumn = i2;
                        this.selectedRow = i;
                        refresh();
                        return true;
                    }
                }
            }
        }
        return super.method_25402(class_11909Var, z);
    }

    private int[] columnSpan(int i) {
        int i2 = (this.field_22789 / 2) - 20;
        int i3 = this.field_22789 - 8;
        int i4 = i3 - i2;
        int max = Math.max(1, this.columns.size());
        if (max == 1) {
            return new int[]{i2, i3};
        }
        int i5 = (i4 - 6) / max;
        int i6 = i2 + ((i5 + COL_GAP) * i);
        if (i != max - 1) {
            i3 = i6 + i5;
        }
        return new int[]{i6, i3};
    }

    public void method_25394(class_332 class_332Var, int i, int i2, float f) {
        int i3;
        String tr;
        class_332Var.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
        super.method_25394(class_332Var, i, i2, f);
        if (this.field_22787 != null && this.field_22793 != null) {
            class_5250 method_43469 = class_2561.method_43469("roar_of_love.ui.audio_title", new Object[]{class_2561.method_43469(RoLSounds.labelKeyFor(this.slotId), new Object[0])});
            class_332Var.method_27535(this.field_22793, method_43469, (this.field_22789 / 2) - (this.field_22793.method_27525(method_43469) / 2), COL_GAP, -1);
            int max = Math.max(1, (this.listBottom - this.listTop) / ROW_H);
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 >= this.columns.size()) {
                    break;
                }
                Column column = this.columns.get(i5);
                int[] columnSpan = columnSpan(i5);
                class_332Var.method_25294(columnSpan[0] - 2, this.listTop - 2, columnSpan[1] + 2, this.listBottom + 2, Integer.MIN_VALUE);
                if (this.columns.size() > 1) {
                    class_332Var.method_27535(this.field_22793, class_2561.method_43469(column.titleKey, new Object[0]), columnSpan[0] + 2, 15, -9754);
                }
                for (int i6 = 0; i6 < max; i6++) {
                    int i7 = this.scrollY + i6;
                    if (i7 >= column.files.size()) {
                        break;
                    }
                    String str = (String) column.files.get(i7);
                    String keyOf = keyOf(str);
                    boolean isSoundEnabled = RoarOfLoveConfig.isSoundEnabled(keyOf);
                    boolean hasCustom = AudioPackLoader.hasCustom(this.slotId, str);
                    boolean hasBuiltin = AudioPackLoader.hasBuiltin(this.slotId, str);
                    int i8 = this.listTop + (i6 * ROW_H);
                    if (i5 == this.selectedColumn && i7 == this.selectedRow) {
                        class_332Var.method_25294(columnSpan[0] - 1, i8 - 1, columnSpan[1] + 1, i8 + 11, 0x50ffffff);
                    }
                    if (!isSoundEnabled) {
                        i3 = -40864;
                        tr = tr("roar_of_love.ui.tag_off", new Object[0]);
                    } else if (hasCustom) {
                        i3 = -9445152;
                        tr = tr("roar_of_love.ui.tag_custom", new Object[0]);
                    } else if (hasBuiltin) {
                        i3 = -8658806;
                        tr = tr("roar_of_love.ui.tag_builtin", new Object[0]);
                    } else {
                        i3 = -6643540;
                        tr = tr("roar_of_love.ui.tag_empty", new Object[0]);
                    }
                    class_332Var.method_27535(this.field_22793, class_2561.method_43470(trimTo(str + "  " + (isSoundEnabled ? "ON" : "OFF") + "  " + tr + "  " + (volumeSteps(keyOf) * 10) + "%", (columnSpan[1] - columnSpan[0]) - 6)), columnSpan[0] + 3, i8 + 2, i3);
                }
                i4 = i5 + 1;
            }
            if (this.maxScroll > 0) {
                class_5250 method_434692 = class_2561.method_43469("roar_of_love.ui.scroll_hint", new Object[0]);
                class_332Var.method_27535(this.field_22793, method_434692, (this.field_22789 - 8) - this.field_22793.method_27525(method_434692), this.field_22790 - 12, -6381828);
            }
            class_332Var.method_27535(this.field_22793, class_2561.method_43470(tr("roar_of_love.ui.legend", new Object[0])), 8, 26, -6643540);
            if (AudioPackLoader.isPackDirty()) {
                class_332Var.method_27535(this.field_22793, class_2561.method_43469("roar_of_love.ui.dirty_hint", new Object[0]), 8, this.field_22790 - 24, -15797);
            }
            Path customDirFor = AudioPackLoader.customDirFor(this.slotId);
            if (customDirFor != null) {
                String path = customDirFor.toAbsolutePath().toString();
                if (this.field_22793.method_27525(class_2561.method_43470(path)) > this.field_22789 - 16) {
                    path = "…" + path.substring(Math.max(0, path.length() - 60));
                }
                class_332Var.method_27535(this.field_22793, class_2561.method_43470(path), 8, 36, -9459512);
            }
            if (System.currentTimeMillis() < this.toastUntil && !this.toast.isEmpty()) {
                int method_27525 = this.field_22793.method_27525(class_2561.method_43470(this.toast));
                int i9 = (this.field_22789 / 2) - (method_27525 / 2);
                int i10 = (this.field_22790 / 2) + 30;
                class_332Var.method_25294(i9 - 4, i10 - 3, method_27525 + i9 + 4, i10 + 11, -1073741824);
                class_332Var.method_27535(this.field_22793, class_2561.method_43470(this.toast), i9, i10, -1);
            }
            RoarTooltip.render(class_332Var, i, i2, this.field_22789, this.field_22790);
        }
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            try {
                AudioPackLoader.applyOnExit();
            } catch (Throwable th) {
                RoarOfLove.LOGGER.warn("[roar_of_love] 退出音频界面时应用改动失败", th);
            }
            this.field_22787.method_1507(this.parent);
        }
    }
}

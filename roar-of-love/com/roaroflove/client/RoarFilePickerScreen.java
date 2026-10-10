//
// Decompiled by Jadx - 987ms
//
package com.roaroflove.client;

import com.roaroflove.RoarOfLove;
import com.roaroflove.client.ui.RoarButton;
import com.roaroflove.util.AudioPaths;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.class_10799;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_437;

public class RoarFilePickerScreen extends class_437 {
    private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
    private static final int FOOTER_H = 34;
    private static final int HEADER_H = 44;
    private static final int ROW_H = 16;
    private static final int VISIBLE_ROWS = 10;
    private Path dir;
    private List<Path> dirs;
    private List<Path> files;
    private String note;
    private final Consumer<Path> onPick;
    private final class_437 parent;
    private int scrollRow;
    private int selected;
    private final String slotLabel;

    public RoarFilePickerScreen(class_437 class_437Var, Path path, String str, Consumer<Path> consumer) {
        super(class_2561.method_43469("roar_of_love.ui.pick_title", new Object[0]));
        this.dirs = new ArrayList();
        this.files = new ArrayList();
        this.selected = 0;
        this.scrollRow = 0;
        this.note = "";
        this.parent = class_437Var;
        this.slotLabel = str == null ? "" : str;
        this.onPick = consumer;
        this.dir = path;
        reload();
    }

    private void reload() {
        this.dirs = new ArrayList();
        this.files = new ArrayList();
        this.selected = 0;
        this.scrollRow = 0;
        try {
            if (this.dir == null || !Files.isDirectory(this.dir, new LinkOption[0])) {
                this.note = tr("roar_of_love.ui.pick_bad_dir", new Object[0]);
                return;
            }
            Stream<Path> list = Files.list(this.dir);
            try {
                Objects.requireNonNull(list);
                Iterable<Path> iterable = list::iterator;
                for (Path path : iterable) {
                    try {
                        if (Files.isDirectory(path, new LinkOption[0])) {
                            this.dirs.add(path);
                        } else if (path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".ogg")) {
                            this.files.add(path);
                        }
                    } catch (Throwable th) {
                    }
                }
                if (list != null) {
                    list.close();
                }
                this.dirs.sort((path2, path3) -> {
                    return path2.getFileName().toString().compareToIgnoreCase(path3.getFileName().toString());
                });
                this.files.sort((path4, path5) -> {
                    return path4.getFileName().toString().compareToIgnoreCase(path5.getFileName().toString());
                });
                this.note = "";
            } finally {
            }
        } catch (Throwable th2) {
            this.note = tr("roar_of_love.ui.pick_read_fail", String.valueOf(th2.getMessage()));
            RoarOfLove.LOGGER.warn("[roar_of_love] 读取目录失败 {}", this.dir, th2);
        }
    }

    private static String tr(String str, Object... objArr) {
        return class_2561.method_43469(str, objArr).getString();
    }

    private Path itemAt(int i) {
        if (i < this.dirs.size()) {
            return this.dirs.get(i);
        }
        int size = i - this.dirs.size();
        if (size < this.files.size()) {
            return this.files.get(size);
        }
        return null;
    }

    private int itemCount() {
        return this.dirs.size() + this.files.size();
    }

    private boolean isDirItem(int i) {
        return i < this.dirs.size();
    }

    private void ensureVisible() {
        if (this.selected < this.scrollRow) {
            this.scrollRow = this.selected;
        }
        if (this.selected >= this.scrollRow + VISIBLE_ROWS) {
            this.scrollRow = (this.selected - 10) + 1;
        }
        this.scrollRow = Math.max(0, Math.min(Math.max(0, itemCount() - 10), this.scrollRow));
    }

    protected void method_25426() {
        method_37067();
        RoarButton.resetVariants();
        int i = this.field_22789 / 2;
        int i2 = this.field_22790 - 26;
        method_37063(new RoarButton(8, i2, 70, 20, class_2561.method_43469("roar_of_love.ui.pick_up", new Object[0]), class_4185Var -> {
            Path parent = this.dir == null ? null : this.dir.getParent();
            if (parent != null) {
                this.dir = parent;
                reload();
                method_25426();
            }
        }));
        method_37063(new RoarButton(82, i2, 110, 20, class_2561.method_43469("roar_of_love.ui.pick_goto_audio", new Object[0]), class_4185Var2 -> {
            this.dir = AudioPaths.audioFolder();
            reload();
            method_25426();
        }));
        method_37063(new RoarButton(i - 60, i2, 120, 20, class_2561.method_43469("roar_of_love.ui.pick_confirm", new Object[0]), class_4185Var3 -> {
            confirm();
        }));
        method_37063(new RoarButton(this.field_22789 - 78, i2, 70, 20, class_2561.method_43469("roar_of_love.ui.back", new Object[0]), class_4185Var4 -> {
            method_25419();
        }));
        ensureVisible();
    }

    private void confirm() {
        Path itemAt = itemAt(this.selected);
        if (itemAt == null || isDirItem(this.selected)) {
            if (itemAt != null) {
                this.dir = itemAt;
                reload();
                method_25426();
                return;
            }
            return;
        }
        try {
            this.onPick.accept(itemAt);
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 选择文件后处理失败", th);
        }
        method_25419();
    }

    public boolean method_25401(double d, double d2, double d3, double d4) {
        if (d4 == 0.0d || itemCount() == 0) {
            return false;
        }
        int max = Math.max(0, Math.min(itemCount() - 1, (d4 > 0.0d ? -1 : 1) + this.selected));
        if (max != this.selected) {
            this.selected = max;
            ensureVisible();
        }
        return true;
    }

    public boolean method_25402(class_11909 class_11909Var, boolean z) {
        int i;
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
        }
        int comp_4798 = (int) class_11909Var.comp_4798();
        int comp_4799 = (int) class_11909Var.comp_4799();
        if (comp_4799 >= HEADER_H && comp_4799 < 204 && comp_4798 >= 8 && comp_4798 <= this.field_22789 - 8 && (i = this.scrollRow + ((comp_4799 - HEADER_H) / ROW_H)) >= 0 && i < itemCount()) {
            boolean z2 = i == this.selected;
            this.selected = i;
            ensureVisible();
            if (!z) {
                return true;
            }
            if (!z2 && !isDirItem(i)) {
                return true;
            }
            confirm();
            return true;
        }
        return super.method_25402(class_11909Var, z);
    }

    public void method_25394(class_332 class_332Var, int i, int i2, float f) {
        int i3;
        class_332Var.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
        class_332Var.method_25294(0, 0, this.field_22789, 36, -1072689132);
        class_332Var.method_25294(0, this.field_22790 - 34, this.field_22789, this.field_22790, -1072689132);
        super.method_25394(class_332Var, i, i2, f);
        if (this.field_22787 != null && this.field_22793 != null) {
            centered(class_332Var, class_2561.method_43469("roar_of_love.ui.pick_title", new Object[0]), 6, -1);
            centered(class_332Var, class_2561.method_43470(tr("roar_of_love.ui.pick_slot", this.slotLabel)), 17, -9754);
            class_332Var.method_27535(this.field_22793, class_2561.method_43470(trim(this.dir == null ? "-" : this.dir.toAbsolutePath().toString(), this.field_22789 - 16)), 8, 28, -6633217);
            if (!this.note.isEmpty()) {
                class_332Var.method_27535(this.field_22793, class_2561.method_43470(this.note), 8, 38, -32640);
            }
            class_332Var.method_25294(6, 42, this.field_22789 - 6, 206, Integer.MIN_VALUE);
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 >= VISIBLE_ROWS || (i3 = this.scrollRow + i5) >= itemCount()) {
                    break;
                }
                Path itemAt = itemAt(i3);
                if (itemAt != null) {
                    boolean isDirItem = isDirItem(i3);
                    String path = isDirItem ? "[ " + itemAt.getFileName().toString() + " ]" : itemAt.getFileName().toString();
                    int i6 = HEADER_H + (i5 * ROW_H);
                    if (i3 == this.selected) {
                        class_332Var.method_25294(7, i6 - 1, this.field_22789 - 7, (i6 + ROW_H) - 3, 0x50ffffff);
                    }
                    class_332Var.method_27535(this.field_22793, class_2561.method_43470(trim(path, this.field_22789 - 40)), 12, i6, isDirItem ? -6625120 : -9754);
                }
                i4 = i5 + 1;
            }
            centered(class_332Var, class_2561.method_43469("roar_of_love.ui.pick_hint", new Object[0]), 212, -7695450);
            if (itemCount() > VISIBLE_ROWS) {
                String str = (this.scrollRow + 1) + "-" + Math.min(itemCount(), this.scrollRow + VISIBLE_ROWS) + " / " + itemCount();
                class_332Var.method_27535(this.field_22793, class_2561.method_43470(str), (this.field_22789 - 8) - this.field_22793.method_27525(class_2561.method_43470(str)), (this.field_22790 - 34) - 12, -5185281);
            }
        }
    }

    private void centered(class_332 class_332Var, class_2561 class_2561Var, int i, int i2) {
        class_332Var.method_27535(this.field_22793, class_2561Var, (this.field_22789 / 2) - (this.field_22793.method_27525(class_2561Var) / 2), i, i2);
    }

    private String trim(String str, int i) {
        if (this.field_22793 == null || str == null) {
            return "";
        }
        if (this.field_22793.method_27525(class_2561.method_43470(str)) > i) {
            int length = str.length();
            while (length > 1 && this.field_22793.method_27525(class_2561.method_43470("…" + str.substring(str.length() - length))) > i) {
                length--;
            }
            return "…" + str.substring(str.length() - length);
        }
        return str;
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }
}

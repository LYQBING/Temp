//
// Decompiled by Jadx - 534ms
//
package com.roaroflove.client;

import com.roaroflove.RoarOfLove;
import com.roaroflove.client.ui.RoarButton;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_10799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class RoarSubtitleFileScreen extends class_437 {
    private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
    private static final int VISIBLE = 6;
    private final class_437 parent;
    private int scroll;

    public RoarSubtitleFileScreen(class_437 class_437Var) {
        super(class_2561.method_43469("roar_of_love.ui.sub_file_title", new Object[0]));
        this.scroll = 0;
        this.parent = class_437Var;
    }

    private static Path audioFolder() {
        try {
            return FabricLoader.getInstance().getGameDir().resolve("RoarOfLove_Audio");
        } catch (Throwable th) {
            return null;
        }
    }

    private static List<Path> findFiles() {
        ArrayList arrayList = new ArrayList();
        Path audioFolder = audioFolder();
        if (audioFolder != null && Files.isDirectory(audioFolder, new LinkOption[0])) {
            try {
                Stream<Path> walk = Files.walk(audioFolder, 4, new FileVisitOption[0]);
                try {
                    walk.filter(path -> {
                        return Files.isRegularFile(path, new LinkOption[0]) && path.getFileName().toString().toLowerCase().endsWith(".txt");
                    }).forEach(path2 -> {
                        String lowerCase = audioFolder.relativize(path2).toString().replace('\\', '/').toLowerCase();
                        if (!lowerCase.endsWith("readme.txt") && !lowerCase.contains("template")) {
                            arrayList.add(path2);
                        }
                    });
                    if (walk != null) {
                        walk.close();
                    }
                } finally {
                }
            } catch (Throwable th) {
                RoarOfLove.LOGGER.debug("[roar_of_love] 扫描字幕文件失败", th);
            }
            arrayList.sort((path3, path4) -> {
                return path3.getFileName().toString().compareToIgnoreCase(path4.getFileName().toString());
            });
        }
        return arrayList;
    }

    private static int lineCount(Path path) {
        try {
            return (int) Files.lines(path, StandardCharsets.UTF_8).filter(str -> {
                return (str == null || str.trim().isEmpty()) ? false : true;
            }).count();
        } catch (Throwable th) {
            return -1;
        }
    }

    public boolean method_25401(double d, double d2, double d3, double d4) {
        if (findFiles().size() <= VISIBLE || d4 == 0.0d) {
            return false;
        }
        int max = Math.max(0, Math.min(r0.size() - 6, (d4 > 0.0d ? 1 : -1) + this.scroll));
        if (max != this.scroll) {
            this.scroll = max;
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null) {
                method_1551.execute(() -> {
                    method_25426();
                });
            }
            return true;
        }
        return false;
    }

    protected void method_25426() {
        method_37067();
        RoarButton.resetVariants();
        int i = this.field_22789 / 2;
        List<Path> findFiles = findFiles();
        int max = Math.max(0, findFiles.size() - 6);
        if (this.scroll > max) {
            this.scroll = max;
        }
        for (int i2 = 0; i2 < VISIBLE && this.scroll + i2 < findFiles.size(); i2++) {
            Path path = findFiles.get(this.scroll + i2);
            int lineCount = lineCount(path);
            String path2 = path.getFileName().toString();
            method_37063(new RoarButton(i - 160, (i2 * 22) + 40, 320, 20, class_2561.method_43470(path2 + "  (" + class_2561.method_43469("roar_of_love.ui.sub_file_lines", new Object[]{Integer.valueOf(lineCount)}).getString() + ")"), class_4185Var -> {
                RoarOfLove.LOGGER.info("[roar_of_love] 加载字幕文件 {} -> {} 行", path2, Integer.valueOf(RoarSubtitles.loadCustom(path)));
                class_310 method_1551 = class_310.method_1551();
                if (method_1551 != null) {
                    method_1551.method_1507(this.parent);
                }
            }));
        }
        method_37063(new RoarButton(i - 160, 178, 155, 20, class_2561.method_43469("roar_of_love.ui.sub_file_clear", new Object[0]), class_4185Var2 -> {
            RoarSubtitles.clearCustom();
            method_25426();
        }));
        method_37063(new RoarButton(i + 5, 178, 155, 20, class_2561.method_43469("roar_of_love.ui.back", new Object[0]), class_4185Var3 -> {
            method_25419();
        }));
    }

    private void line(class_332 class_332Var, class_2561 class_2561Var, int i, int i2, int i3) {
        if (this.field_22793 != null) {
            class_332Var.method_27535(this.field_22793, class_2561Var, i - (this.field_22793.method_27525(class_2561Var) / 2), i2, i3);
        }
    }

    public void method_25394(class_332 class_332Var, int i, int i2, float f) {
        class_332Var.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
        super.method_25394(class_332Var, i, i2, f);
        if (this.field_22787 != null && this.field_22793 != null) {
            int i3 = this.field_22789 / 2;
            line(class_332Var, class_2561.method_43469("roar_of_love.ui.sub_file_title", new Object[0]), i3, 12, -1);
            line(class_332Var, class_2561.method_43469("roar_of_love.ui.sub_file_hint", new Object[0]), i3, 24, -5185281);
            if (findFiles().isEmpty()) {
                line(class_332Var, class_2561.method_43469("roar_of_love.ui.sub_file_none", new Object[0]), i3, 90, -32640);
            }
        }
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }
}

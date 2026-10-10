//
// Decompiled by Jadx - 771ms
//
package com.roaroflove.client;

import com.roaroflove.client.ui.RoarButton;
import com.roaroflove.config.RoarOfLoveConfig;
import net.minecraft.class_10799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_5250;

public class RoarGuideScreen extends class_437 {
    private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
    private final class_437 parent;

    public RoarGuideScreen(class_437 class_437Var) {
        super(class_2561.method_43469("roar_of_love.guide.title", new Object[0]));
        this.parent = class_437Var;
    }

    private static String tr(String str) {
        return class_2561.method_43469(str, new Object[0]).getString();
    }

    private void rebuild() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.execute(() -> {
                method_25426();
            });
        }
    }

    private void openSettings() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.method_1507(new RoarOfLoveSettingsScreen(this));
        }
    }

    private void openFilters() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.method_1507(new RoarFilterScreen(this));
        }
    }

    protected void method_25426() {
        method_37067();
        RoarButton.resetVariants();
        int i = this.field_22789 / 2;
        method_37063(new RoarButton((i - 190) - 4, 116, 190, 20, class_2561.method_43469("roar_of_love.guide.step1", new Object[0]), class_4185Var -> {
            openSettings();
        }));
        method_37063(new RoarButton(i + 4, 116, 190, 20, class_2561.method_43469("roar_of_love.guide.step2", new Object[0]), class_4185Var2 -> {
            openFilters();
        }));
        boolean isFilterViewed = RoarOfLoveConfig.isFilterViewed();
        RoarButton roarButton = new RoarButton(i - 90, 150, 180, 20, class_2561.method_43469(isFilterViewed ? "roar_of_love.guide.close_ready" : "roar_of_love.guide.close_locked", new Object[0]), class_4185Var3 -> {
            method_25419();
        });
        roarButton.field_22763 = isFilterViewed;
        method_37063(roarButton);
    }

    public boolean method_25422() {
        return RoarOfLoveConfig.isFilterViewed();
    }

    private void line(class_332 class_332Var, String str, int i, int i2, int i3) {
        if (this.field_22793 != null) {
            class_5250 method_43469 = class_2561.method_43469(str, new Object[0]);
            class_332Var.method_27535(this.field_22793, method_43469, i - (this.field_22793.method_27525(method_43469) / 2), i2, i3);
        }
    }

    public void method_25394(class_332 class_332Var, int i, int i2, float f) {
        class_332Var.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
        class_332Var.method_25294(0, 0, this.field_22789, this.field_22790, -1342177280);
        super.method_25394(class_332Var, i, i2, f);
        if (this.field_22787 != null && this.field_22793 != null) {
            int i3 = this.field_22789 / 2;
            line(class_332Var, "roar_of_love.guide.title", i3, 40, -9882);
            line(class_332Var, "roar_of_love.guide.line1", i3, 62, -1);
            line(class_332Var, "roar_of_love.guide.line2", i3, 74, -1);
            line(class_332Var, "roar_of_love.guide.line3", i3, 90, -20304);
            line(class_332Var, "roar_of_love.guide.line4", i3, 102, -20304);
            line(class_332Var, RoarOfLoveConfig.isFilterViewed() ? "roar_of_love.guide.hint_ok" : "roar_of_love.guide.close_locked", i3, 182, RoarOfLoveConfig.isFilterViewed() ? -7536756 : -32640);
            class_332Var.method_27535(this.field_22793, class_2561.method_43470("Roar of Love 2.0.23 · " + tr("roar_of_love.ui.author")), 6, this.field_22790 - 11, -6381922);
        }
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }
}

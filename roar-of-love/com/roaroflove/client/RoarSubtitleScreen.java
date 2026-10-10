//
// Decompiled by Jadx - 688ms
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

public class RoarSubtitleScreen extends class_437 {
    private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
    private final class_437 parent;

    public RoarSubtitleScreen(class_437 class_437Var) {
        super(class_2561.method_43469("roar_of_love.ui.subtitles_title", new Object[0]));
        this.parent = class_437Var;
    }

    private void refresh() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.execute(() -> {
                method_25426();
            });
        }
    }

    protected void method_25426() {
        method_37067();
        RoarButton.resetVariants();
        int i = this.field_22789 / 2;
        cat(i, 30, "lust", "roar_of_love.ui.sub_lust");
        cat(i, 52, "anim", "roar_of_love.ui.sub_anim");
        cat(i, 74, "peak", "roar_of_love.ui.sub_peak");
        cat(i, 96, "self", "roar_of_love.ui.sub_self");
        cat(i, 118, "oral", "roar_of_love.ui.sub_oral");
        method_37063(new RoarButton(i - 120, 142, 240, 20, class_2561.method_43469("roar_of_love.ui.sub_form_" + RoarOfLoveConfig.subForm(), new Object[0]), class_4185Var -> {
            RoarOfLoveConfig.setSubForm((RoarOfLoveConfig.subForm() + 1) % 3);
            refresh();
        }));
        String subCustomFile = RoarOfLoveConfig.subCustomFile();
        method_37063(new RoarButton(i - 120, 164, 240, 20, class_2561.method_43469(subCustomFile.isEmpty() ? "roar_of_love.ui.sub_custom_none" : "roar_of_love.ui.sub_custom_loaded", new Object[]{subCustomFile}), class_4185Var2 -> {
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null) {
                method_1551.method_1507(new RoarSubtitleFileScreen(this));
            }
        }));
        method_37063(new RoarButton(i - 120, 186, 118, 20, class_2561.method_43469(RoarOfLoveConfig.isTypewriter() ? "roar_of_love.ui.typewriter_on" : "roar_of_love.ui.typewriter_off", new Object[0]), class_4185Var3 -> {
            RoarOfLoveConfig.setTypewriter(!RoarOfLoveConfig.isTypewriter());
            refresh();
        }));
        method_37063(new RoarButton(i + 2, 186, 118, 20, class_2561.method_43469(RoarSubtitleLang.labelKey(), new Object[0]), class_4185Var4 -> {
            RoarOfLoveConfig.setSubLang((RoarOfLoveConfig.subLang() + 1) % 4);
            refresh();
        }));
        method_37063(new RoarButton(i - 120, 208, 240, 20, class_2561.method_43469("roar_of_love.ui.subtitle_gender_" + RoarOfLoveConfig.subtitleGender(), new Object[0]), class_4185Var5 -> {
            RoarOfLoveConfig.setSubtitleGender((RoarOfLoveConfig.subtitleGender() + 1) % 3);
            refresh();
        }));
        method_37063(new RoarButton(i - 50, this.field_22790 - 24, 100, 20, class_2561.method_43469("roar_of_love.ui.back", new Object[0]), class_4185Var5 -> {
            method_25419();
        }));
    }

    private void cat(int i, int i2, String str, String str2) {
        method_37063(new RoarButton(i - 120, i2, 240, 20, class_2561.method_43469(RoarSubtitles.isEnabled(str) ? str2 + "_on" : str2 + "_off", new Object[0]), class_4185Var -> {
            RoarSubtitles.setEnabled(str, !RoarSubtitles.isEnabled(str));
            refresh();
        }));
    }

    private void line(class_332 class_332Var, String str, int i, int i2, int i3) {
        if (this.field_22793 != null) {
            class_5250 method_43469 = class_2561.method_43469(str, new Object[0]);
            class_332Var.method_27535(this.field_22793, method_43469, i - (this.field_22793.method_27525(method_43469) / 2), i2, i3);
        }
    }

    public void method_25394(class_332 class_332Var, int i, int i2, float f) {
        class_332Var.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
        super.method_25394(class_332Var, i, i2, f);
        if (this.field_22787 != null && this.field_22793 != null) {
            line(class_332Var, "roar_of_love.ui.subtitles_title", this.field_22789 / 2, 5, -1);
            line(class_332Var, "roar_of_love.ui.sub_hint", this.field_22789 / 2, 16, -5185281);
            if (RoarSubtitles.isMalePlayer()) {
                line(class_332Var, "roar_of_love.ui.gender_male_state", this.field_22789 / 2, this.field_22790 - 34, -40864);
            } else {
                line(class_332Var, "roar_of_love.ui.gender_female_state", this.field_22789 / 2, this.field_22790 - 34, -6625120);
            }
        }
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }
}

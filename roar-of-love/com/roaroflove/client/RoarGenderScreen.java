//
// Decompiled by Jadx - 617ms
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

public class RoarGenderScreen extends class_437 {
    private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
    private final class_437 parent;

    public RoarGenderScreen(class_437 class_437Var) {
        super(class_2561.method_43469("roar_of_love.ui.gender_pick_title", new Object[0]));
        this.parent = class_437Var;
    }

    protected void method_25426() {
        method_37067();
        RoarButton.resetVariants();
        int i = this.field_22789 / 2;
        int i2 = (this.field_22790 / 2) - 10;
        method_37063(new RoarButton(i - 120, i2, 240, 20, class_2561.method_43469("roar_of_love.ui.gender_pick_male", new Object[0]), class_4185Var -> {
            pick(1);
        }));
        method_37063(new RoarButton(i - 120, i2 + 24, 240, 20, class_2561.method_43469("roar_of_love.ui.gender_pick_female", new Object[0]), class_4185Var2 -> {
            pick(2);
        }));
    }

    private void pick(int i) {
        RoarOfLoveConfig.setSubtitleGender(i);
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null) {
            method_1551.method_1507(this.parent);
        }
    }

    public void method_25394(class_332 class_332Var, int i, int i2, float f) {
        class_332Var.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
        class_332Var.method_25294(0, 0, this.field_22789, this.field_22790, -1342177280);
        super.method_25394(class_332Var, i, i2, f);
        if (this.field_22793 != null) {
            class_5250 method_43469 = class_2561.method_43469("roar_of_love.ui.gender_pick_title", new Object[0]);
            class_332Var.method_27535(this.field_22793, method_43469, (this.field_22789 / 2) - (this.field_22793.method_27525(method_43469) / 2), (this.field_22790 / 2) - 40, -1);
            class_5250 method_434692 = class_2561.method_43469("roar_of_love.ui.gender_pick_hint", new Object[0]);
            class_332Var.method_27535(this.field_22793, method_434692, (this.field_22789 / 2) - (this.field_22793.method_27525(method_434692) / 2), (this.field_22790 / 2) - 28, -5185281);
        }
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }
}

//
// Decompiled by Jadx - 657ms
//
package com.roaroflove.client;

import com.roaroflove.client.ui.RoarButton;
import java.util.List;
import net.minecraft.class_10799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_5250;

public class RoarHandbookScreen extends class_437 {
    private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
    private int page;
    private final class_437 parent;
    private List<String> rows;

    public RoarHandbookScreen(class_437 class_437Var) {
        super(class_2561.method_43469("roar_of_love.ui.handbook_title", new Object[0]));
        this.page = 0;
        this.rows = List.of();
        this.parent = class_437Var;
        this.rows = RoarHandbook.summary();
    }

    private int pages() {
        return Math.max(1, (this.rows.size() + 9) / 10);
    }

    protected void method_25426() {
        method_37067();
        RoarButton.resetVariants();
        int i = this.field_22789 / 2;
        int i2 = this.field_22790 - 26;
        method_37063(new RoarButton(i - 130, i2, 80, 20, class_2561.method_43469("roar_of_love.ui.page_prev", new Object[0]), class_4185Var -> {
            turn(-1);
        }));
        method_37063(new RoarButton(i + 50, i2, 80, 20, class_2561.method_43469("roar_of_love.ui.page_next", new Object[0]), class_4185Var2 -> {
            turn(1);
        }));
        method_37063(new RoarButton(i - 40, i2, 80, 20, class_2561.method_43469("roar_of_love.ui.back", new Object[0]), class_4185Var3 -> {
            method_25419();
        }));
    }

    public boolean method_25401(double d, double d2, double d3, double d4) {
        if (d4 == 0.0d) {
            return false;
        }
        turn(d4 > 0.0d ? -1 : 1);
        return true;
    }

    private void turn(int i) {
        int max = Math.max(0, Math.min(pages() - 1, this.page + i));
        if (max != this.page) {
            this.page = max;
            method_25426();
        }
    }

    public void method_25394(class_332 class_332Var, int i, int i2, float f) {
        int i3;
        class_332Var.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
        class_332Var.method_25294(20, 20, this.field_22789 - 20, this.field_22790 - 34, -1879048192);
        class_332Var.method_25294(20, 20, this.field_22789 - 20, 21, -1862270977);
        super.method_25394(class_332Var, i, i2, f);
        if (this.field_22793 != null) {
            class_5250 method_43469 = class_2561.method_43469("roar_of_love.ui.handbook_title", new Object[0]);
            class_332Var.method_27535(this.field_22793, method_43469, (this.field_22789 / 2) - (this.field_22793.method_27525(method_43469) / 2), 6, -1);
            if (this.rows.isEmpty()) {
                class_5250 method_434692 = class_2561.method_43469("roar_of_love.ui.handbook_empty", new Object[0]);
                class_332Var.method_27535(this.field_22793, method_434692, (this.field_22789 / 2) - (this.field_22793.method_27525(method_434692) / 2), 60, -5185281);
            }
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 >= 10 || (i3 = (this.page * 10) + i5) >= this.rows.size()) {
                    break;
                }
                class_332Var.method_27535(this.field_22793, class_2561.method_43470(this.rows.get(i3)), 32, (i5 * 16) + 30, -9754);
                i4 = i5 + 1;
            }
            class_5250 method_43470 = class_2561.method_43470((this.page + 1) + " / " + pages());
            class_332Var.method_27535(this.field_22793, method_43470, (this.field_22789 / 2) - (this.field_22793.method_27525(method_43470) / 2), this.field_22790 - 40, -5185281);
        }
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }
}

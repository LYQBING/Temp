//
// Decompiled by Jadx - 684ms
//
package com.roaroflove.client;

import com.roaroflove.client.ui.RoarButton;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_5250;

public class RoarOfLoveSettingsScreen$NoticeScreen extends class_437 {
    private final List<String> lines;
    private final class_437 parent;

    public RoarOfLoveSettingsScreen$NoticeScreen(class_437 class_437Var, String... strArr) {
        super(class_2561.method_43470("Roar of Love"));
        this.parent = class_437Var;
        this.lines = List.of((Object[]) strArr);
    }

    protected void method_25426() {
        method_37067();
        RoarButton.resetVariants();
        method_37063(new RoarButton((this.field_22789 / 2) - 60, (this.field_22790 / 2) + 24, 120, 20, class_2561.method_43469("roar_of_love.ui.ok", new Object[0]), class_4185Var -> {
            method_25419();
        }));
    }

    public void method_25394(class_332 class_332Var, int i, int i2, float f) {
        class_332Var.method_25294(0, 0, this.field_22789, this.field_22790, -1072689136);
        super.method_25394(class_332Var, i, i2, f);
        if (this.field_22787 != null && this.field_22793 != null) {
            int i3 = (this.field_22790 / 2) - 30;
            Iterator<String> it = this.lines.iterator();
            while (it.hasNext()) {
                class_5250 method_43470 = class_2561.method_43470(it.next());
                class_332Var.method_27535(this.field_22793, method_43470, (this.field_22789 / 2) - (this.field_22793.method_27525(method_43470) / 2), i3, -39322);
                i3 += 12;
            }
        }
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }
}

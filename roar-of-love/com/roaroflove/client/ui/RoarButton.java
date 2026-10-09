//
// Decompiled by Jadx - 697ms
//
package com.roaroflove.client.ui;

import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4185;

public class RoarButton extends class_4185 {
    private static final int BORDER = -1612515329;
    private static final int BORDER_DISABLED = 0x55afc3cf;
    private static final int BORDER_HOVER = -588186369;
    private static final int RADIUS = 3;
    private static final int TEXT = -1;
    private static final int TEXT_DISABLED = -4934476;
    private static final int TINT = 0x2ab9e6f5;
    private static final int TINT_DISABLED = 0x1a8c9aa6;
    private static final int TINT_HOVER = 0x4acff0fb;
    private final int bh;
    private final int bw;
    private final int bx;
    private final int by;

    public RoarButton(int i, int i2, int i3, int i4, class_2561 class_2561Var, class_4185.class_4241 class_4241Var) {
        super(i, i2, i3, i4, class_2561Var, class_4241Var, field_40754);
        this.bx = i;
        this.by = i2;
        this.bw = i3;
        this.bh = i4;
    }

    public static void resetVariants() {
    }

    private static void rounded(class_332 class_332Var, int i, int i2, int i3, int i4, int i5, int i6) {
        if (i3 > 0 && i4 > 0 && (i6 >>> 24) != 0) {
            int max = Math.max(0, Math.min(i5, Math.min(i3 / 2, i4 / 2)));
            for (int i7 = 0; i7 < i4; i7++) {
                int min = Math.min(i7, (i4 + TEXT) - i7);
                int i8 = min < max ? max - min : 0;
                if (i3 - (i8 * 2) <= 0) {
                    i8 = 0;
                }
                class_332Var.method_25294(i + i8, i2 + i7, (i + i3) - i8, i2 + i7 + 1, i6);
            }
        }
    }

    protected void method_75752(class_332 class_332Var, int i, int i2, float f) {
        int i3;
        int i4;
        class_327 class_327Var;
        int i5 = this.bx;
        int i6 = this.by;
        int i7 = this.bw;
        int i8 = this.bh;
        boolean z = this.field_22763;
        boolean z2 = z && i >= i5 && i2 >= i6 && i < i5 + i7 && i2 < i6 + i8;
        if (z) {
            i3 = z2 ? BORDER_HOVER : BORDER;
        } else {
            i3 = BORDER_DISABLED;
        }
        if (z) {
            i4 = z2 ? TINT_HOVER : TINT;
        } else {
            i4 = TINT_DISABLED;
        }
        rounded(class_332Var, i5, i6, i7, i8, RADIUS, i3);
        rounded(class_332Var, i5 + 1, i6 + 1, i7 - 2, i8 - 2, Math.max(0, 2), i4);
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null && (class_327Var = method_1551.field_1772) != null) {
            class_332Var.method_27535(class_327Var, this.field_22754, i5 + ((i7 - class_327Var.method_27525(this.field_22754)) / 2), i6 + ((i8 - 8) / 2), z ? TEXT : TEXT_DISABLED);
        }
    }
}

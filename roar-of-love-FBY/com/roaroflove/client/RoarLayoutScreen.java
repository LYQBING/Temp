//
// Decompiled by Jadx - 962ms
//
package com.roaroflove.client;

import com.roaroflove.client.ui.RoarButton;
import java.util.List;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_437;
import net.minecraft.class_5250;

public class RoarLayoutScreen extends class_437 {
    private static final int CANVAS_TOP = 84;
    private static final int SNAP = 4;
    private int dragId;
    private int grabDX;
    private int grabDY;
    private int livePctX;
    private int livePctY;
    private final class_437 parent;
    private int selected;

    public RoarLayoutScreen(class_437 class_437Var) {
        super(class_2561.method_43469("roar_of_love.ui.layout_title", new Object[0]));
        this.selected = 0;
        this.dragId = -1;
        this.grabDX = 0;
        this.grabDY = 0;
        this.livePctX = 0;
        this.livePctY = 0;
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
        int min = Math.min(150, ((this.field_22789 - 24) / Math.max(1, SNAP)) - 6);
        int i2 = i - (((SNAP * min) + 18) / 2);
        int i3 = 0;
        while (i3 < SNAP) {
            method_37063(new RoarButton(((min + 6) * i3) + i2, 28, min, 20, class_2561.method_43469(RoarLayout.labelKey(i3) + (this.selected == i3 ? "_sel" : ""), new Object[0]), class_4185Var -> {
                this.selected = i3;
                refresh();
            }));
            i3++;
        }
        method_37063(new RoarButton(6, this.field_22790 - 24, 74, 20, class_2561.method_43469("roar_of_love.ui.back", new Object[0]), class_4185Var2 -> {
            method_25419();
        }));
        method_37063(new RoarButton(this.field_22789 - 116, this.field_22790 - 24, 110, 20, class_2561.method_43469("roar_of_love.ui.pos_reset_one", new Object[0]), class_4185Var3 -> {
            RoarLayout.reset(this.selected);
            this.dragId = -1;
            refresh();
        }));
    }

    private int posX(int i, int i2, int i3) {
        return i == this.dragId ? RoarLayout.percentToPos(this.livePctX, i2 - RoarLayout.width(i, i2)) : RoarLayout.x(i, i2, i3);
    }

    private int posY(int i, int i2, int i3) {
        return i == this.dragId ? RoarLayout.percentToPos(this.livePctY, i3 - RoarLayout.height(i, i3)) : RoarLayout.y(i, i2, i3);
    }

    private int pctX(int i) {
        return i == this.dragId ? this.livePctX : RoarLayout.percentX(i);
    }

    private int pctY(int i) {
        return i == this.dragId ? this.livePctY : RoarLayout.percentY(i);
    }

    private static int snap(int i, int i2) {
        if (i2 > 0 && Math.abs(i) > SNAP) {
            if (Math.abs(i - i2) > SNAP) {
                if (Math.abs(i - (i2 / 2)) <= SNAP) {
                    return i2 / 2;
                }
                return Math.max(0, Math.min(i2, i));
            }
            return i2;
        }
        return 0;
    }

    private void updateDrag(int i, int i2, int i3, int i4) {
        if (this.dragId >= 0) {
            int i5 = this.dragId;
            int max = Math.max(0, i3 - RoarLayout.width(i5, i3));
            int max2 = Math.max(0, i4 - RoarLayout.height(i5, i4));
            this.livePctX = RoarLayout.posToPercent(snap(i - this.grabDX, max), max);
            this.livePctY = RoarLayout.posToPercent(snap(i2 - this.grabDY, max2), max2);
        }
    }

    private boolean tryGrab(double d, double d2) {
        for (int i = 3; i >= 0; i--) {
            int posX = posX(i, this.field_22789, this.field_22790);
            int posY = posY(i, this.field_22789, this.field_22790);
            int width = RoarLayout.width(i, this.field_22789);
            int height = RoarLayout.height(i, this.field_22790);
            if (d >= posX - 3 && d <= width + posX + 3 && d2 >= posY - 3 && d2 <= posY + height + 3) {
                this.selected = i;
                this.dragId = i;
                this.grabDX = ((int) d) - posX;
                this.grabDY = ((int) d2) - posY;
                this.livePctX = RoarLayout.percentX(i);
                this.livePctY = RoarLayout.percentY(i);
                return true;
            }
        }
        return false;
    }

    public boolean method_25402(class_11909 class_11909Var, boolean z) {
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
        if (class_11909Var.method_74245() == 0 && tryGrab(class_11909Var.comp_4798(), class_11909Var.comp_4799())) {
            return true;
        }
        return super.method_25402(class_11909Var, z);
    }

    public boolean method_25403(class_11909 class_11909Var, double d, double d2) {
        if (this.dragId < 0) {
            return super.method_25403(class_11909Var, d, d2);
        }
        updateDrag((int) class_11909Var.comp_4798(), (int) class_11909Var.comp_4799(), this.field_22789, this.field_22790);
        return true;
    }

    public boolean method_25406(class_11909 class_11909Var) {
        if (this.dragId < 0) {
            return super.method_25406(class_11909Var);
        }
        updateDrag((int) class_11909Var.comp_4798(), (int) class_11909Var.comp_4799(), this.field_22789, this.field_22790);
        RoarLayout.setPercent(this.dragId, this.livePctX, this.livePctY);
        this.dragId = -1;
        return true;
    }

    private static double g(double d, double d2, double d3) {
        double d4 = (d - d2) / d3;
        return Math.exp(d4 * (-d4));
    }

    private static String truncate(class_327 class_327Var, String str, int i) {
        if (class_327Var.method_27525(class_2561.method_43470(str)) > i) {
            while (str.length() > 1 && class_327Var.method_27525(class_2561.method_43470(str + "…")) > i) {
                str = str.substring(0, str.length() - 1);
            }
            return str + "…";
        }
        return str;
    }

    private void drawSample(class_332 class_332Var, int i, int i2, int i3) {
        int posX = posX(i, i2, i3);
        int posY = posY(i, i2, i3);
        int i4 = this.selected == i ? -1 : 0x66ffffff;
        if (i == 2) {
            int width = RoarLayout.width(i, i2);
            int height = RoarLayout.height(i, i3);
            class_332Var.method_25294(posX - 1, posY - 1, posX + width + 1, posY + height + 1, Integer.MIN_VALUE);
            class_332Var.method_25294(posX, posY, posX + width, posY + height, 0x48000000);
            int i5 = posY + (height / 2);
            int i6 = 0;
            int i7 = i5;
            while (i6 < width - 4) {
                double d = (i6 * 0.02d) % 1.0d;
                int g = i5 - ((int) (((g(d, 0.56d, 0.055d) * 0.3d) + ((((0.14d * g(d, 0.16d, 0.03d)) - (g(d, 0.29d, 0.01d) * 0.1d)) + g(d, 0.32d, 0.011d)) - (g(d, 0.36d, 0.014d) * 0.26d))) * ((height / 2) - 4)));
                class_332Var.method_25294(posX + 2 + i6, Math.min(i7, g), posX + 3 + i6, Math.max(i7, g) + 1, -788566104);
                i6++;
                i7 = g;
            }
            class_332Var.method_25294(posX - 2, posY - 2, posX + width + 2, posY - 1, i4);
            class_332Var.method_25294(posX - 2, posY + height + 1, posX + width + 2, posY + height + 2, i4);
            class_332Var.method_25294(posX - 2, posY - 2, posX - 1, posY + height + 2, i4);
            class_332Var.method_25294(posX + width + 1, posY - 2, posX + width + 2, posY + height + 2, i4);
            return;
        }
        if (i == 3) {
            int width2 = RoarLayout.width(i, i2);
            int height2 = RoarLayout.height(i, i3);
            class_332Var.method_25294(posX - 1, posY - 1, posX + width2 + 1, posY + height2 + 1, -1879048192);
            class_332Var.method_25294(posX + 1, posY + 1, posX + 8, posY + 8, -38476);
            class_327 class_327Var = this.field_22793;
            if (class_327Var != null) {
                class_332Var.method_27535(class_327Var, class_2561.method_43470(class_2561.method_43469("roar_of_love.ui.pregnancy_badge", new Object[]{1}).getString()), posX + 12, posY + 2, -14108);
            }
            class_332Var.method_25294(posX - 2, posY - 2, posX + width2 + 2, posY - 1, i4);
            class_332Var.method_25294(posX - 2, posY + height2 + 1, posX + width2 + 2, posY + height2 + 2, i4);
            class_332Var.method_25294(posX - 2, posY - 2, posX - 1, posY + height2 + 2, i4);
            class_332Var.method_25294(posX + width2 + 1, posY - 2, posX + width2 + 2, posY + height2 + 2, i4);
            return;
        }
        if (i == 0) {
            int width3 = RoarLayout.width(i, i2);
            int height3 = RoarLayout.height(i, i3);
            int cachedLust = RoarFilterState.cachedLust();
            int i8 = cachedLust >= 0 ? cachedLust : 3;
            class_332Var.method_25294(posX - 1, posY - 1, posX + width3 + 1, posY + height3 + 1, -1073741824);
            class_332Var.method_25294(posX, posY, posX + width3, posY + height3, 0x50ffffff);
            int max = Math.max(1, (int) (width3 * Math.min(1.0f, (i8 + 1) / 6.0f)));
            class_332Var.method_25294(posX, posY, posX + max, posY + height3, -38476);
            class_332Var.method_25294(posX, posY, posX + max, posY + 1, 0x66ffffff);
            class_332Var.method_25294(posX - 2, posY - 2, posX + width3 + 2, posY - 1, i4);
            class_332Var.method_25294(posX - 2, posY + height3 + 1, posX + width3 + 2, posY + height3 + 2, i4);
            class_332Var.method_25294(posX - 2, posY - 2, posX - 1, posY + height3 + 2, i4);
            class_332Var.method_25294(posX + width3 + 1, posY - 2, posX + width3 + 2, posY + height3 + 2, i4);
            return;
        }
        int width4 = RoarLayout.width(i, i2);
        int height4 = RoarLayout.height(i, i3);
        class_332Var.method_25294(posX, posY, posX + width4, posY + height4, 0x40000000);
        class_332Var.method_25294(posX, posY, posX + width4, posY + 1, i4);
        class_332Var.method_25294(posX, (posY + height4) - 1, posX + width4, posY + height4, i4);
        class_332Var.method_25294(posX, posY, posX + 1, posY + height4, i4);
        class_332Var.method_25294((posX + width4) - 1, posY, posX + width4, posY + height4, i4);
        class_327 class_327Var2 = this.field_22793;
        if (class_327Var2 != null) {
            long currentTimeMillis = System.currentTimeMillis();
            int i9 = 0;
            while (true) {
                int i10 = i9;
                if (i10 < 3) {
                    String sampleText = truncate(class_327Var2, class_2561.method_43469("roar_of_love.ui.pos_sample" + (i10 + 1), new Object[0]).getString(), width4 - 10);
                    class_332Var.method_51439(class_327Var2, class_2561.method_43470(sampleText), posX + 5 + ((int) (((width4 - 10) - class_327Var2.method_27525(class_2561.method_43470(sampleText))) * (1.0f - (((float) (currentTimeMillis % 3000)) / 3000.0f)))), (i10 * 19) + posY + 7, -251676446, true);
                    i9 = i10 + 1;
                } else {
                    return;
                }
            }
        }
    }

    public void method_25394(class_332 class_332Var, int i, int i2, float f) {
        int i3 = this.field_22789;
        int i4 = this.field_22790;
        if (this.dragId >= 0) {
            updateDrag(i, i2, i3, i4);
        }
        class_332Var.method_25294(0, 0, i3, i4, -15723750);
        for (int i5 = 0; i5 < i3; i5 += 40) {
            class_332Var.method_25294(i5, CANVAS_TOP, i5 + 1, i4, 0x10ffffff);
        }
        for (int i6 = CANVAS_TOP; i6 < i4; i6 += 40) {
            class_332Var.method_25294(0, i6, i3, i6 + 1, 0x10ffffff);
        }
        class_332Var.method_25294(0, 83, i3, CANVAS_TOP, 0x40ffffff);
        for (int i7 = 0; i7 < SNAP; i7++) {
            if (i7 != this.selected) {
                drawSample(class_332Var, i7, i3, i4);
            }
        }
        if (this.selected >= 0 && this.selected < SNAP) {
            drawSample(class_332Var, this.selected, i3, i4);
        }
        super.method_25394(class_332Var, i, i2, f);
        if (this.field_22793 != null && this.field_22787 != null) {
            class_5250 method_43469 = class_2561.method_43469("roar_of_love.ui.layout_title", new Object[0]);
            class_332Var.method_27535(this.field_22793, method_43469, (i3 / 2) - (this.field_22793.method_27525(method_43469) / 2), 8, -1);
            line(class_332Var, "roar_of_love.ui.pos_hint1", 54, -4602152);
            line(class_332Var, "roar_of_love.ui.pos_hint2", 65, -7695450);
            class_5250 method_434692 = class_2561.method_43469("roar_of_love.ui.pos_readout", new Object[]{class_2561.method_43469(RoarLayout.labelKey(this.selected), new Object[0]).getString(), Integer.valueOf(pctX(this.selected)), Integer.valueOf(pctY(this.selected))});
            class_332Var.method_27535(this.field_22793, method_434692, (i3 / 2) - (this.field_22793.method_27525(method_434692) / 2), 74, -25896);
        }
    }

    private void line(class_332 class_332Var, String str, int i, int i2) {
        if (this.field_22793 != null) {
            class_5250 method_43469 = class_2561.method_43469(str, new Object[0]);
            class_332Var.method_27535(this.field_22793, method_43469, (this.field_22789 / 2) - (this.field_22793.method_27525(method_43469) / 2), i, i2);
        }
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }
}

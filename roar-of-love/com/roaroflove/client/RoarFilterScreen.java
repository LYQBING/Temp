//
// Decompiled by Jadx - 676ms
//
package com.roaroflove.client;

import com.roaroflove.client.ui.RoarButton;
import com.roaroflove.config.RoarOfLoveConfig;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import net.minecraft.class_10799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_5250;

public class RoarFilterScreen extends class_437 {
    private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
    private int maxScroll;
    private final class_437 parent;
    private int scrollY;

    public RoarFilterScreen(class_437 class_437Var) {
        super(class_2561.method_43469("roar_of_love.ui.filters_title", new Object[0]));
        this.scrollY = 0;
        this.maxScroll = 0;
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

    public boolean method_25401(double d, double d2, double d3, double d4) {
        if (this.maxScroll <= 0 || d4 == 0.0d) {
            return false;
        }
        int max = Math.max(0, Math.min(this.maxScroll, (d4 > 0.0d ? -26 : 26) + this.scrollY));
        if (max != this.scrollY) {
            this.scrollY = max;
            refresh();
        }
        return true;
    }

    protected void method_25426() {
        method_37067();
        RoarButton.resetVariants();
        if (!RoarOfLoveConfig.isFilterViewed()) {
            RoarOfLoveConfig.setFilterViewed(true);
        }
        int i = this.field_22789 / 2;
        this.maxScroll = Math.max(0, 214 - ((this.field_22790 - 36) - 32));
        if (this.scrollY > this.maxScroll) {
            this.scrollY = this.maxScroll;
        }
        int i2 = 32 - this.scrollY;
        row(i, i2 + 40, "roar_of_love.ui.filter_pink", RoarOfLoveConfig.isPinkFilter(), () -> {
            RoarOfLoveConfig.setPinkFilter(!RoarOfLoveConfig.isPinkFilter());
        }, RoarOfLoveConfig.pinkStrength(), i3 -> {
            RoarOfLoveConfig.setPinkStrength(i3);
        });
        row(i, i2 + 62, "roar_of_love.ui.filter_black", RoarOfLoveConfig.isBlackFilter(), () -> {
            RoarOfLoveConfig.setBlackFilter(!RoarOfLoveConfig.isBlackFilter());
        }, RoarOfLoveConfig.blackStrength(), i4 -> {
            RoarOfLoveConfig.setBlackStrength(i4);
        });
        row(i, i2 + 84, "roar_of_love.ui.filter_flash", RoarOfLoveConfig.isFlashEffect(), () -> {
            RoarOfLoveConfig.setFlashEffect(!RoarOfLoveConfig.isFlashEffect());
        }, RoarOfLoveConfig.flashStrength(), i5 -> {
            RoarOfLoveConfig.setFlashStrength(i5);
        });
        method_37063(new RoarButton(i - 120, i2 + 106, 240, 20, class_2561.method_43469(RoarOfLoveConfig.isShakeEffect() ? "roar_of_love.ui.filter_shake_on" : "roar_of_love.ui.filter_shake_off", new Object[0]), class_4185Var -> {
            RoarOfLoveConfig.setShakeEffect(!RoarOfLoveConfig.isShakeEffect());
            refresh();
        }));
        method_37063(new RoarButton(i - 50, this.field_22790 - 24, 100, 20, class_2561.method_43469("roar_of_love.ui.back", new Object[0]), class_4185Var2 -> {
            method_25419();
        }));
    }

    private void plain(int i, int i2, String str, boolean z, Consumer<Boolean> consumer) {
        method_37063(new RoarButton(i - 120, i2, 240, 20, class_2561.method_43469(z ? str + "_on" : str + "_off", new Object[0]), class_4185Var -> {
            consumer.accept(Boolean.valueOf(!z));
            refresh();
        }));
    }

    private void row(int i, int i2, String str, boolean z, Runnable runnable, int i3, IntConsumer intConsumer) {
        method_37063(new RoarButton(i - 120, i2, 176, 20, class_2561.method_43469(z ? str + "_on" : str + "_off", new Object[0]), class_4185Var -> {
            runnable.run();
            refresh();
        }));
        int i4 = i3 >= 10 ? 1 : i3 + 1;
        method_37063(new RoarButton(i + 60, i2, 60, 20, class_2561.method_43469("roar_of_love.ui.strength", new Object[]{Integer.valueOf(i3)}), class_4185Var2 -> {
            intConsumer.accept(i4);
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
        int i3 = this.field_22789 / 2;
        int i4 = 32 - this.scrollY;
        int i5 = i4 + 134;
        class_332Var.method_25294(10, i5, this.field_22789 - 10, i4 + 204, -1073741824);
        class_332Var.method_25294(10, i5, this.field_22789 - 10, i5 + 1, -32640);
        super.method_25394(class_332Var, i, i2, f);
        if (this.field_22787 != null && this.field_22793 != null) {
            line(class_332Var, "roar_of_love.ui.filters_title", i3, 10, -1);
            line(class_332Var, "roar_of_love.filters.warn_title", i3, i4 + 140, -40864);
            line(class_332Var, "roar_of_love.filters.warn1", i3, i4 + 152, -9824);
            line(class_332Var, "roar_of_love.filters.warn2", i3, i4 + 164, -9824);
            line(class_332Var, "roar_of_love.filters.warn3", i3, i4 + 176, -9824);
            line(class_332Var, "roar_of_love.filters.warn4", i3, i4 + 188, -5185281);
            if (this.maxScroll > 0) {
                class_5250 method_43469 = class_2561.method_43469("roar_of_love.ui.scroll_hint", new Object[0]);
                class_332Var.method_27535(this.field_22793, method_43469, (this.field_22789 - 6) - this.field_22793.method_27525(method_43469), this.field_22790 - 11, -6381922);
            }
        }
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }
}

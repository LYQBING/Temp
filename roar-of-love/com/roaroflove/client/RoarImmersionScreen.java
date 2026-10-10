//
// Decompiled by Jadx - 602ms
//
package com.roaroflove.client;

import com.roaroflove.client.ui.RoarButton;
import com.roaroflove.client.ui.RoarHoverTooltip;
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

public class RoarImmersionScreen extends class_437 {
    private static final class_2960 BACKGROUND = class_2960.method_60655("roar_of_love", "textures/gui/settings_background.png");
    private static final int CONTENT_HEIGHT = 382;
    private static final int FOOTER_H = 34;
    private static final int HEADER_H = 30;
    private static final int ROW_STEP = 22;
    private int maxScroll;
    private final class_437 parent;
    private int scrollY;

    public RoarImmersionScreen(class_437 class_437Var) {
        super(class_2561.method_43469("roar_of_love.ui.immersion_title", new Object[0]));
        this.scrollY = 0;
        this.maxScroll = 0;
        this.parent = class_437Var;
    }

    private void scrollBy(int amount) {
        int next = Math.max(0, Math.min(this.maxScroll, this.scrollY + amount));
        if (next != this.scrollY) {
            this.scrollY = next;
            refresh();
        }
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
        this.maxScroll = Math.max(0, CONTENT_HEIGHT - ((this.field_22790 - HEADER_H) - FOOTER_H));
        if (this.scrollY > this.maxScroll) this.scrollY = this.maxScroll;
        int extraScroll = (414 - this.maxScroll) - (this.field_22790 - FOOTER_H);
        if (extraScroll > 0) this.maxScroll += extraScroll;
        int i = this.field_22789 / 2;
        int i2 = 32 - this.scrollY;
        row(i, i2 + 34, "roar_of_love.ui.breath", RoarOfLoveConfig.isBreathEffect(), bool -> {
            RoarOfLoveConfig.setBreathEffect(bool.booleanValue());
        }, RoarOfLoveConfig.breathStrength(), i3 -> {
            RoarOfLoveConfig.setBreathStrength(i3);
        }, "roar_of_love.ui.fov", RoarOfLoveConfig.isFovBreath(), bool2 -> {
            RoarOfLoveConfig.setFovBreath(bool2.booleanValue());
        });
        row(i, i2 + 56, "roar_of_love.ui.pulse", RoarOfLoveConfig.isPulseEffect(), bool3 -> {
            RoarOfLoveConfig.setPulseEffect(bool3.booleanValue());
        }, RoarOfLoveConfig.pulseStrength(), i4 -> {
            RoarOfLoveConfig.setPulseStrength(i4);
        }, "roar_of_love.ui.hrv", RoarOfLoveConfig.isHrv(), bool4 -> {
            RoarOfLoveConfig.setHrv(bool4.booleanValue());
        });
        row(i, i2 + 78, "roar_of_love.ui.reflux", RoarOfLoveConfig.isRefluxEffect(), bool5 -> {
            RoarOfLoveConfig.setRefluxEffect(bool5.booleanValue());
        }, RoarOfLoveConfig.refluxStrength(), i5 -> {
            RoarOfLoveConfig.setRefluxStrength(i5);
        }, "roar_of_love.ui.breathhold", RoarOfLoveConfig.isBreathHold(), bool6 -> {
            RoarOfLoveConfig.setBreathHold(bool6.booleanValue());
        });
        row(i, i2 + 100, "roar_of_love.ui.lustbar", RoarOfLoveConfig.isLustBar(), bool7 -> {
            RoarOfLoveConfig.setLustBar(bool7.booleanValue());
        }, RoarOfLoveConfig.lustBarStrength(), i6 -> {
            RoarOfLoveConfig.setLustBarStrength(i6);
        }, RoarPalette.labelKey(), true, bool8 -> {
        });
        row(i, i2 + 122, "roar_of_love.ui.ecg", RoarOfLoveConfig.isEcgEffect(), bool9 -> {
            RoarOfLoveConfig.setEcgEffect(bool9.booleanValue());
        }, RoarOfLoveConfig.ecgStrength(), i7 -> {
            RoarOfLoveConfig.setEcgStrength(i7);
        }, "roar_of_love.ui.ecgtail", RoarOfLoveConfig.isEcgTail(), bool10 -> {
            RoarOfLoveConfig.setEcgTail(bool10.booleanValue());
        });
        row(i, i2 + 152, "roar_of_love.ui.ambience", RoarOfLoveConfig.isAmbienceSound(), bool11 -> {
            RoarOfLoveConfig.setAmbienceSound(bool11.booleanValue());
        }, RoarOfLoveConfig.ambienceStrength(), i8 -> {
            RoarOfLoveConfig.setAmbienceStrength(i8);
        }, "roar_of_love.ui.pregnancy", RoarOfLoveConfig.isPregnancyHint(), bool12 -> {
            RoarOfLoveConfig.setPregnancyHint(bool12.booleanValue());
        });
        method_37063(new RoarButton(i - 120, i2 + 182, 240, 20, class_2561.method_43469("roar_of_love.ui.pulse_style_" + RoarOfLoveConfig.pulseStyle(), new Object[0]), class_4185Var -> {
            RoarOfLoveConfig.setPulseStyle((RoarOfLoveConfig.pulseStyle() + 1) % 4);
            refresh();
        }));
        method_37063(new RoarButton(i - 120, i2 + 212, 240, 20, class_2561.method_43469(RoarOfLoveConfig.isAdaptHud() ? "roar_of_love.ui.adapt_on" : "roar_of_love.ui.adapt_off", new Object[0]), class_4185Var2 -> {
            RoarOfLoveConfig.setAdaptHud(!RoarOfLoveConfig.isAdaptHud());
            refresh();
        }));
        method_37063(new RoarButton(i - 120, i2 + 236, 240, 20, class_2561.method_43469(RoarOfLoveConfig.isPushEffect() ? "roar_of_love.ui.push_on" : "roar_of_love.ui.push_off", new Object[0]), class_4185Var3 -> {
            RoarOfLoveConfig.setPushEffect(!RoarOfLoveConfig.isPushEffect());
            refresh();
        }));
        method_37063(new RoarButton(i - 120, i2 + 258, 240, 20, class_2561.method_43469(RoarOfLoveConfig.isHeatBar() ? "roar_of_love.ui.heat_on" : "roar_of_love.ui.heat_off", new Object[0]), class_4185Var4 -> {
            RoarOfLoveConfig.setHeatBar(!RoarOfLoveConfig.isHeatBar());
            refresh();
        }));
        method_37063(new RoarButton(i - 120, i2 + 280, 240, 20, class_2561.method_43469(RoarOfLoveConfig.isOverload() ? "roar_of_love.ui.overload_on" : "roar_of_love.ui.overload_off", new Object[0]), class_4185Var5 -> {
            RoarOfLoveConfig.setOverload(!RoarOfLoveConfig.isOverload());
            refresh();
        }));
        method_37063(new RoarButton(i - 120, i2 + 302, 240, 20, class_2561.method_43469(RoarOfLoveConfig.isDirector() ? "roar_of_love.ui.director_on" : "roar_of_love.ui.director_off", new Object[0]), class_4185Var6 -> {
            RoarOfLoveConfig.setDirector(!RoarOfLoveConfig.isDirector());
            refresh();
        }));
        method_37063(new RoarButton(i - 120, i2 + 324, 240, 20, class_2561.method_43469(RoarOfLoveConfig.isDirSub() ? "roar_of_love.ui.dirsub_on" : "roar_of_love.ui.dirsub_off", new Object[0]), class_4185Var7 -> {
            RoarOfLoveConfig.setDirSub(!RoarOfLoveConfig.isDirSub());
            refresh();
        }));
        method_37063(new RoarButton(i - 120, i2 + 346, 240, 20, class_2561.method_43469(RoarOfLoveConfig.isTooltips() ? "roar_of_love.ui.tooltips_on" : "roar_of_love.ui.tooltips_off", new Object[0]), class_4185Var8 -> {
            RoarOfLoveConfig.setTooltips(!RoarOfLoveConfig.isTooltips());
            refresh();
        }));
        RoarHoverTooltip.reset();
        RoarHoverTooltip.register(i - 120, i2 + 236, 240, 20, "roar_of_love.tip.push");
        RoarHoverTooltip.register(i - 120, i2 + 258, 240, 20, "roar_of_love.tip.heat");
        RoarHoverTooltip.register(i - 120, i2 + 280, 240, 20, "roar_of_love.tip.overload");
        RoarHoverTooltip.register(i - 120, i2 + 302, 240, 20, "roar_of_love.tip.director");
        method_37063(new RoarButton(i - 50, this.field_22790 - 26, 100, 20, class_2561.method_43469("roar_of_love.ui.back", new Object[0]), class_4185Var3 -> {
            method_25419();
        }));
    }

    public boolean method_25401(double x, double y, double horizontalAmount, double verticalAmount) {
        if (this.maxScroll > 0 && verticalAmount != 0.0d) {
            scrollBy(verticalAmount > 0.0d ? -22 : ROW_STEP);
            return true;
        }
        return false;
    }

    private void row(int i, int i2, String str, boolean z, Consumer<Boolean> consumer, int i3, IntConsumer intConsumer, String str2, boolean z2, Consumer<Boolean> consumer2) {
        method_37063(new RoarButton(i - 180, i2, 150, 20, class_2561.method_43469(z ? str + "_on" : str + "_off", new Object[0]), class_4185Var -> {
            consumer.accept(Boolean.valueOf(!z));
            refresh();
        }));
        int i4 = i3 >= 10 ? 1 : i3 + 1;
        method_37063(new RoarButton(i - 26, i2, 52, 20, class_2561.method_43469("roar_of_love.ui.strength", new Object[]{Integer.valueOf(i3)}), class_4185Var2 -> {
            intConsumer.accept(i4);
            refresh();
        }));
        if ("roar_of_love.ui.palette".equals(str2)) {
            method_37063(new RoarButton(i + 30, i2, 150, 20, class_2561.method_43469(RoarPalette.labelKey(), new Object[0]), class_4185Var3 -> {
                RoarOfLoveConfig.setPalette((RoarOfLoveConfig.palette() + 1) % 4);
                refresh();
            }));
        } else if (!str2.isEmpty()) {
            method_37063(new RoarButton(i + 30, i2, 150, 20, class_2561.method_43469(z2 ? str2 + "_on" : str2 + "_off", new Object[0]), class_4185Var4 -> {
                consumer2.accept(Boolean.valueOf(!z2));
                refresh();
            }));
        }
    }

    private void line(class_332 class_332Var, String str, int i, int i2, int i3) {
        if (this.field_22793 != null) {
            class_5250 method_43469 = class_2561.method_43469(str, new Object[0]);
            class_332Var.method_27535(this.field_22793, method_43469, i - (this.field_22793.method_27525(method_43469) / 2), i2, i3);
        }
    }

    public void method_25394(class_332 class_332Var, int i, int i2, float f) {
        class_332Var.method_25290(class_10799.field_56883, BACKGROUND, 0, 0, 0.0f, 0.0f, this.field_22789, this.field_22790, this.field_22789, this.field_22790);
        int i3 = this.field_22790 - 34;
        class_332Var.method_25294(0, 0, this.field_22789, 30, -1072689132);
        class_332Var.method_25294(0, i3, this.field_22789, this.field_22790, -1072689132);
        super.method_25394(class_332Var, i, i2, f);
        RoarHoverTooltip.render(class_332Var, i, i2, this.field_22789, this.field_22790);
        if (this.field_22787 != null && this.field_22793 != null) {
            int i4 = 32 - this.scrollY;
            line(class_332Var, "roar_of_love.ui.immersion_title", this.field_22789 / 2, 10, -1);
            line(class_332Var, "roar_of_love.ui.immersion_hint", this.field_22789 / 2, i4 + 372, -5185281);
            line(class_332Var, "roar_of_love.ui.immersion_hint2", this.field_22789 / 2, i4 + 386, -7695450);
            if (this.maxScroll > 0) {
                int trackHeight = i3 - HEADER_H;
                int thumbHeight = Math.max(16, (trackHeight * trackHeight) / (this.maxScroll + trackHeight));
                int thumbY = HEADER_H + (((trackHeight - thumbHeight) * this.scrollY) / this.maxScroll);
                int scrollbarX = this.field_22789 - 6;
                class_332Var.method_25294(scrollbarX, HEADER_H, scrollbarX + 3, i3, 0x60ffffff);
                class_332Var.method_25294(scrollbarX, thumbY, scrollbarX + 3, thumbY + thumbHeight, -1056964609);
                class_5250 method_43469 = class_2561.method_43469("roar_of_love.ui.scroll_hint", new Object[0]);
                class_332Var.method_27535(this.field_22793, method_43469, (this.field_22789 - 12) - this.field_22793.method_27525(method_43469), this.field_22790 - 11, -6381922);
            }
        }
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }
}

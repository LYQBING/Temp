//
// Decompiled by Jadx - 680ms
//
package cn.blockforge.generated.semensyringe;

import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_357;

final class SyringeConfigScreen$DoseSlider extends class_357 {
    final SyringeConfigScreen this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    SyringeConfigScreen$DoseSlider(SyringeConfigScreen syringeConfigScreen, int x, int y, int width) {
        super(x, y, width, 20, class_2561.method_43473(), toSliderValue(SyringeConfig.clampAmount(SyringeConfig.get().injectionAmount)));
        this.this$0 = syringeConfigScreen;
        method_25346();
    }

    private static double toSliderValue(int ml) {
        return (ml - 1) / 999.0d;
    }

    private int ml() {
        return ((int) Math.round(this.field_22753 * 999)) + 1;
    }

    protected void method_25346() {
        method_25355(this.this$0.doseLabel(ml()));
    }

    protected void method_25344() {
        SyringeConfig.get().injectionAmount = ml();
    }

    public void method_25357(class_11909 click) {
        super.method_25357(click);
        SyringeConfig.get().save();
    }
}

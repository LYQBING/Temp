//
// Decompiled by Jadx - 555ms
//
package cn.blockforge.generated.semensyringe;

import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_4185;
import net.minecraft.class_437;

public final class SyringeConfigScreen extends class_437 {
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_WIDTH = 230;
    private final class_437 parent;

    public SyringeConfigScreen(class_437 parent) {
        super(SyringeLang.text("semen_syringe.config.title", new Object[0]));
        this.parent = parent;
    }

    protected void method_25426() {
        int x = (this.field_22789 / 2) - 115;
        method_37063(class_4185.method_46430(subtitleLabel(), b -> {
            SyringeConfig.get().injectionSubtitles = !SyringeConfig.get().injectionSubtitles;
            SyringeConfig.get().save();
            method_41843();
        }).method_46434(x, (this.field_22790 / 2) - 68, BUTTON_WIDTH, BUTTON_HEIGHT).method_46431());
        method_37063(class_4185.method_46430(languageLabel(), b2 -> {
            SyringeConfig.get().nextLanguage();
            method_41843();
        }).method_46434(x, (this.field_22790 / 2) - 44, BUTTON_WIDTH, BUTTON_HEIGHT).method_46431());
        method_37063(new DoseSlider(this, x, (this.field_22790 / 2) - 20, BUTTON_WIDTH));
        method_37063(class_4185.method_46430(SyringeLang.text("semen_syringe.config.done", new Object[0]), b3 -> {
            method_25419();
        }).method_46434(x, (this.field_22790 / 2) + 10, BUTTON_WIDTH, BUTTON_HEIGHT).method_46431());
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        super.method_25394(context, mouseX, mouseY, delta);
        context.method_27534(this.field_22793, SyringeLang.text("semen_syringe.config.hint", new Object[0]), this.field_22789 / 2, (this.field_22790 / 2) + 36, 10526880);
        context.method_27534(this.field_22793, SyringeLang.text("semen_syringe.config.dose.hint", new Object[0]), this.field_22789 / 2, (this.field_22790 / 2) + 50, 8421504);
    }

    private class_2561 subtitleLabel() {
        return SyringeLang.text("semen_syringe.config.subtitle", new Object[0]).method_27693(": ").method_10852(SyringeLang.text(SyringeConfig.subtitlesEnabled() ? "semen_syringe.config.on" : "semen_syringe.config.off", new Object[0]));
    }

    private class_2561 languageLabel() {
        String key;
        String languageCode = SyringeConfig.languageCode();
        char c = 65535;
        switch (languageCode.hashCode()) {
            case 96647668:
                if (languageCode.equals("en_us")) {
                    c = 0;
                    break;
                }
                break;
            case 115862300:
                if (languageCode.equals("zh_cn")) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                key = "semen_syringe.config.lang.en";
                break;
            case 1:
                key = "semen_syringe.config.lang.zh";
                break;
            default:
                key = "semen_syringe.config.lang.auto";
                break;
        }
        return SyringeLang.text("semen_syringe.config.language", new Object[0]).method_27693(": ").method_10852(SyringeLang.text(key, new Object[0]));
    }

    private class_2561 doseLabel(int ml) {
        return SyringeLang.text("semen_syringe.config.dose", new Object[0]).method_27693(": ").method_10852(class_2561.method_43470(ml + " mL"));
    }

    public void method_25419() {
        SyringeConfig.get().save();
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }
}

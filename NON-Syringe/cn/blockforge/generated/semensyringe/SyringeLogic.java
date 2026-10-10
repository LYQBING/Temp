//
// Decompiled by Jadx - 627ms
//
package cn.blockforge.generated.semensyringe;

import java.util.Optional;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_3417;
import org.jetbrains.annotations.Nullable;

public final class SyringeLogic {
    public static final int SYRINGE_CAPACITY = 1000;

    private SyringeLogic() {
    }

    public static class_1799 makeFilled(class_1799 syringeStack, class_1799 bottleStack) {
        class_2960 currentDonor;
        class_1799 result = syringeStack.method_46651(1);
        SyringeContents current = SyringeContents.read(result);
        int currentMl = current == null ? 0 : current.ml();
        class_2960 bottleDonor = NonNatureBridge.bottleDonor(bottleStack);
        int gain = NonNatureBridge.bottleMl(bottleDonor);
        class_2960 mergedDonor = bottleDonor;
        if (current != null && (currentDonor = current.donorId()) != null && !currentDonor.equals(bottleDonor)) {
            mergedDonor = null;
        }
        int total = Math.min(SYRINGE_CAPACITY, currentMl + gain);
        SyringeContents.write(result, new SyringeContents(Optional.ofNullable(mergedDonor), total, NonNatureBridge.liquidColor(mergedDonor)));
        return result;
    }

    public static boolean inject(class_3222 injector, class_3222 target, class_1268 hand) {
        class_1799 held = injector.method_5998(hand);
        if (!(held.method_7909() instanceof SyringeItem)) {
            return false;
        }
        SyringeContents contents = SyringeContents.read(held);
        if (contents == null) {
            feedback(injector, SyringeLang.text("semen_syringe.msg.empty", new Object[0]));
            return false;
        }
        if (!NonNatureBridge.tankEnabled()) {
            feedback(injector, SyringeLang.text("semen_syringe.msg.tank_disabled", new Object[0]));
            return false;
        }
        int dose = SyringeConfig.doseFor(contents.ml());
        int got = NonNatureBridge.inject(target, contents.donorId(), dose);
        if (got <= 0) {
            feedback(injector, SyringeLang.text("semen_syringe.msg.rejected", new Object[0]));
            return false;
        }
        consume(injector, hand, contents, got);
        injector.method_6104(hand);
        target.method_5783(class_3417.field_14810, 0.5f, 1.7f);
        feedback(injector, SyringeLang.text("semen_syringe.msg.injected", new Object[]{donorText(contents.donorId()), Integer.valueOf(got)}));
        if (target != injector) {
            feedback(target, SyringeLang.text("semen_syringe.msg.injected_by", new Object[]{injector.method_5477(), Integer.valueOf(got)}));
        }
        return true;
    }

    private static void consume(class_3222 injector, class_1268 hand, SyringeContents contents, int usedMl) {
        class_1799 held = injector.method_5998(hand);
        if (held.method_7909() instanceof SyringeItem) {
            int left = contents.ml() - usedMl;
            if (left > 0) {
                SyringeContents.write(held, new SyringeContents(contents.donor(), left, contents.tint()));
            } else {
                SyringeContents.clear(held);
            }
            injector.field_7512.method_7623();
        }
    }

    public static class_2561 donorText(@Nullable class_2960 donor) {
        return SyringeLang.donorText(donor);
    }

    public static void feedback(class_3222 player, class_2561 text) {
        if (SyringeConfig.subtitlesEnabled()) {
            player.method_7353(text, true);
        }
    }
}

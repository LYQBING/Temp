//
// Decompiled by Jadx - 455ms
//
package cn.blockforge.generated.semensyringe;

import java.util.function.Consumer;
import net.minecraft.class_10712;
import net.minecraft.class_124;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_1937;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_3222;

public final class SyringeItem extends class_1792 {
    public SyringeItem(class_1792.class_1793 settings) {
        super(settings);
    }

    public class_1269 method_7836(class_1937 world, class_1657 user, class_1268 hand) {
        if (world.method_8608()) {
            return class_1269.field_5811;
        }
        if (!(user instanceof class_3222)) {
            return class_1269.field_5811;
        }
        class_3222 serverPlayer = (class_3222) user;
        class_1799 stack = serverPlayer.method_5998(hand);
        SyringeContents contents = SyringeContents.read(stack);
        if (contents != null) {
            return SyringeLogic.inject(serverPlayer, serverPlayer, hand) ? class_1269.field_5812 : class_1269.field_5814;
        }
        SyringeLogic.feedback(serverPlayer, SyringeLang.text("semen_syringe.msg.empty", new Object[0]));
        return class_1269.field_5814;
    }

    public class_2561 method_7864(class_1799 stack) {
        return SyringeLang.text("item.semen_syringe.syringe", new Object[0]);
    }

    public void method_67187(class_1799 stack, class_1792.class_9635 context, class_10712 display, Consumer<class_2561> consumer, class_1836 type) {
        SyringeContents contents = SyringeContents.read(stack);
        if (contents == null) {
            consumer.accept(SyringeLang.text("semen_syringe.tooltip.empty", new Object[0]).method_10862(class_2583.field_24360.method_10977(class_124.field_1080)));
            consumer.accept(SyringeLang.text("semen_syringe.tooltip.usage", new Object[0]).method_10862(class_2583.field_24360.method_10977(class_124.field_1063)));
        } else {
            consumer.accept(SyringeLang.text("semen_syringe.tooltip.donor", new Object[]{SyringeLang.donorText(contents.donorId())}).method_10862(class_2583.field_24360.method_36139(contents.tint() | (-16777216))));
            consumer.accept(SyringeLang.text("semen_syringe.tooltip.amount", new Object[]{Integer.valueOf(contents.ml())}).method_10862(class_2583.field_24360.method_10977(class_124.field_1080)));
            consumer.accept(SyringeLang.text("semen_syringe.tooltip.dose", new Object[]{Integer.valueOf(SyringeConfig.doseFor(contents.ml()))}).method_10862(class_2583.field_24360.method_10977(class_124.field_1080)));
            consumer.accept(SyringeLang.text("semen_syringe.tooltip.usage", new Object[0]).method_10862(class_2583.field_24360.method_10977(class_124.field_1063)));
        }
    }
}

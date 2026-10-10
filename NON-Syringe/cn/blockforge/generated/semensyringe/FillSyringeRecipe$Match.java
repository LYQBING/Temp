//
// Decompiled by Jadx - 699ms
//
package cn.blockforge.generated.semensyringe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import net.minecraft.class_1799;

final class FillSyringeRecipe$Match extends Record {
    private final class_1799 bottle;
    private final int bottleSlot;
    private final class_1799 syringe;
    private final int syringeSlot;

    private FillSyringeRecipe$Match(class_1799 syringe, int syringeSlot, class_1799 bottle, int bottleSlot) {
        this.syringe = syringe;
        this.syringeSlot = syringeSlot;
        this.bottle = bottle;
        this.bottleSlot = bottleSlot;
    }

    public class_1799 bottle() {
        return this.bottle;
    }

    public int bottleSlot() {
        return this.bottleSlot;
    }

    @Override
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, FillSyringeRecipe$Match.class, Object.class), FillSyringeRecipe$Match.class, "syringe;syringeSlot;bottle;bottleSlot", "FIELD:Lcn/blockforge/generated/semensyringe/FillSyringeRecipe$Match;->syringe:Lnet/minecraft/class_1799;", "FIELD:Lcn/blockforge/generated/semensyringe/FillSyringeRecipe$Match;->syringeSlot:I", "FIELD:Lcn/blockforge/generated/semensyringe/FillSyringeRecipe$Match;->bottle:Lnet/minecraft/class_1799;", "FIELD:Lcn/blockforge/generated/semensyringe/FillSyringeRecipe$Match;->bottleSlot:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    @Override
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, FillSyringeRecipe$Match.class), FillSyringeRecipe$Match.class, "syringe;syringeSlot;bottle;bottleSlot", "FIELD:Lcn/blockforge/generated/semensyringe/FillSyringeRecipe$Match;->syringe:Lnet/minecraft/class_1799;", "FIELD:Lcn/blockforge/generated/semensyringe/FillSyringeRecipe$Match;->syringeSlot:I", "FIELD:Lcn/blockforge/generated/semensyringe/FillSyringeRecipe$Match;->bottle:Lnet/minecraft/class_1799;", "FIELD:Lcn/blockforge/generated/semensyringe/FillSyringeRecipe$Match;->bottleSlot:I").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    public class_1799 syringe() {
        return this.syringe;
    }

    public int syringeSlot() {
        return this.syringeSlot;
    }

    @Override
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, FillSyringeRecipe$Match.class), FillSyringeRecipe$Match.class, "syringe;syringeSlot;bottle;bottleSlot", "FIELD:Lcn/blockforge/generated/semensyringe/FillSyringeRecipe$Match;->syringe:Lnet/minecraft/class_1799;", "FIELD:Lcn/blockforge/generated/semensyringe/FillSyringeRecipe$Match;->syringeSlot:I", "FIELD:Lcn/blockforge/generated/semensyringe/FillSyringeRecipe$Match;->bottle:Lnet/minecraft/class_1799;", "FIELD:Lcn/blockforge/generated/semensyringe/FillSyringeRecipe$Match;->bottleSlot:I").dynamicInvoker().invoke(this) /* invoke-custom */;
    }
}

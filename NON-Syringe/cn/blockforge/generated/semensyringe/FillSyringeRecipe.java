//
// Decompiled by Jadx - 1822ms
//
package cn.blockforge.generated.semensyringe;

import java.util.List;
import java.util.Optional;
import net.minecraft.class_10295;
import net.minecraft.class_10301;
import net.minecraft.class_10302;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1852;
import net.minecraft.class_1856;
import net.minecraft.class_1865;
import net.minecraft.class_1935;
import net.minecraft.class_1937;
import net.minecraft.class_2371;
import net.minecraft.class_2378;
import net.minecraft.class_3955;
import net.minecraft.class_7225;
import net.minecraft.class_7710;
import net.minecraft.class_7923;
import net.minecraft.class_9694;
import net.minecraft.class_9695;
import net.minecraft.class_9887;
import org.jetbrains.annotations.Nullable;

public final class FillSyringeRecipe implements class_3955 {
    private static final int DISPLAY_TINT = 15721430;
    public static final class_1865<FillSyringeRecipe> SERIALIZER = (class_1865) class_2378.method_10230(class_7923.field_41189, SemenSyringeMod.id("crafting_fill_syringe"), new class_1852.class_1866(FillSyringeRecipe::new));
    private final class_7710 category;
    private class_9887 placement;

    public boolean method_8115(class_9695 class_9695Var, class_1937 class_1937Var) {
        return matches((class_9694) class_9695Var, class_1937Var);
    }

    public class_1799 method_8116(class_9695 class_9695Var, class_7225.class_7874 class_7874Var) {
        return craft((class_9694) class_9695Var, class_7874Var);
    }

    public FillSyringeRecipe(class_7710 category) {
        this.category = category;
    }

    public boolean matches(class_9694 input, class_1937 world) {
        return find(input) != null;
    }

    public class_1799 craft(class_9694 input, class_7225.class_7874 lookup) {
        Match match = find(input);
        return match == null ? class_1799.field_8037 : SyringeLogic.makeFilled(match.syringe(), match.bottle());
    }

    public class_2371<class_1799> method_17704(class_9694 input) {
        class_2371<class_1799> remainders = class_2371.method_10213(input.method_59983(), class_1799.field_8037);
        Match match = find(input);
        if (match != null) {
            remainders.set(match.bottleSlot(), new class_1799(class_1802.field_8469));
        }
        return remainders;
    }

    public class_1865<? extends class_3955> method_8119() {
        return SERIALIZER;
    }

    public class_7710 method_45441() {
        return this.category;
    }

    public class_9887 method_61671() {
        if (this.placement == null) {
            this.placement = class_9887.method_61686(ingredients());
        }
        return this.placement;
    }

    public List<class_10295> method_64664() {
        List<class_1856> ingredients = ingredients();
        return List.of(new class_10301(List.of(ingredients.get(0).method_64673(), ingredients.get(1).method_64673()), new class_10302.class_10307(displayResult()), new class_10302.class_10306(class_1802.field_8465)));
    }

    private static List<class_1856> ingredients() {
        return List.of(class_1856.method_8091(new class_1935[]{SemenSyringeMod.SYRINGE}), class_1856.method_8091(new class_1935[]{class_1802.field_8574}));
    }

    private static class_1799 displayResult() {
        class_1799 stack = new class_1799(SemenSyringeMod.SYRINGE);
        stack.method_57379(SyringeContents.type(), new SyringeContents(Optional.empty(), 1000, DISPLAY_TINT));
        return stack;
    }

    @Nullable
    private static Match find(class_9694 input) {
        class_1799 syringe = null;
        class_1799 bottle = null;
        int syringeSlot = -1;
        int bottleSlot = -1;
        for (int i = 0; i < input.method_59983(); i++) {
            class_1799 stack = input.method_59984(i);
            if (!stack.method_7960()) {
                if (syringe == null && stack.method_31574(SemenSyringeMod.SYRINGE)) {
                    syringe = stack;
                    syringeSlot = i;
                } else {
                    if (bottle != null || !NonNatureBridge.isLiquidBottle(stack)) {
                        return null;
                    }
                    bottle = stack;
                    bottleSlot = i;
                }
            }
        }
        if (syringe == null || bottle == null) {
            return null;
        }
        SyringeContents current = SyringeContents.read(syringe);
        if (current == null || current.ml() < 1000) {
            return new Match(syringe, syringeSlot, bottle, bottleSlot);
        }
        return null;
    }
}

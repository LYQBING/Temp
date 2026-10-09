//
// Decompiled by Jadx - 628ms
//
package com.roaroflove.client;

import com.nonid.effect.NonStatusEffects;
import com.roaroflove.RoarOfLove;
import com.roaroflove.config.RoarOfLoveConfig;
import net.minecraft.class_1293;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_746;
import net.minecraft.class_7923;

public final class RoarPregnancy {
    private static int lastLevel = -1;

    private RoarPregnancy() {
    }

    public static int level() {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 == null || method_1551.field_1724 == null) {
            return -1;
        }
        return levelOf(method_1551.field_1724);
    }

    private static int levelOf(class_746 class_746Var) {
        try {
            class_1293 method_6112 = class_746Var.method_6112(class_7923.field_41174.method_47983(NonStatusEffects.PREGNANT));
            if (method_6112 == null) {
                return -1;
            }
            return Math.max(0, method_6112.method_5578());
        } catch (Throwable th) {
            return -1;
        }
    }

    public static void tick() {
        int level = level();
        if (level != lastLevel) {
            if (level >= 0 && lastLevel < 0) {
                notify("roar_of_love.chat.pregnant", Integer.valueOf(level + 1));
            } else if (level >= 0) {
                notify("roar_of_love.chat.pregnant_stage", Integer.valueOf(level + 1));
            } else {
                notify("roar_of_love.chat.pregnant_end", new Object[0]);
            }
            lastLevel = level;
        }
    }

    public static void reset() {
        lastLevel = -1;
    }

    private static void notify(String str, Object... objArr) {
        class_310 method_1551;
        try {
            if (RoarOfLoveConfig.isPregnancyHint() && (method_1551 = class_310.method_1551()) != null && method_1551.field_1724 != null) {
                method_1551.field_1724.method_7353(class_2561.method_43469(str, objArr), false);
            }
        } catch (Throwable th) {
            RoarOfLove.LOGGER.debug("[roar_of_love] 怀孕提示异常", th);
        }
    }
}

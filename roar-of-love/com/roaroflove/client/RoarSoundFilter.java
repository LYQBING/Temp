//
// Decompiled by Jadx - 745ms
//
package com.roaroflove.client;

import com.roaroflove.config.RoarOfLoveConfig;
import java.util.Locale;
import net.minecraft.class_2960;

public final class RoarSoundFilter {
    private RoarSoundFilter() {
    }

    public static boolean isBlocked(class_2960 class_2960Var) {
        String method_12836;
        if (class_2960Var == null || (method_12836 = class_2960Var.method_12836()) == null) {
            return false;
        }
        String lowerCase = method_12836.toLowerCase(Locale.ROOT);
        if ("roar_of_love".equals(lowerCase)) {
            String method_12832 = class_2960Var.method_12832();
            String substring = method_12832.indexOf(46) > 0 ? method_12832.substring(0, method_12832.indexOf(46)) : method_12832;
            if (!RoarOfLoveConfig.isSoundEnabled(method_12832) || !RoarOfLoveConfig.isSoundEnabled(substring)) {
                return true;
            }
        }
        String lowerCase2 = class_2960Var.method_12832().toLowerCase(Locale.ROOT);
        boolean z = lowerCase2.contains("hurt") || lowerCase2.contains("damage") || lowerCase2.contains("pain") || lowerCase2.contains("injured");
        if ("minecraft".equals(lowerCase)) {
            return z && !RoarOfLoveConfig.isSoundEnabled("hurt");
        }
        if (!RoarOfLoveConfig.isMuteOtherHurt() || "roar_of_love".equals(lowerCase) || "needsofnature".equals(lowerCase)) {
            return false;
        }
        return z;
    }
}

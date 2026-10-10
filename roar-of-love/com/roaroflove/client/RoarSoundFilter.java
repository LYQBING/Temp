//
// Decompiled by Jadx - 936ms
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
            String event = class_2960Var.method_12832();
            int separator = event.indexOf('.');
            String group = separator > 0 ? event.substring(0, separator) : event;
            if (!RoarOfLoveConfig.isSoundEnabled(event) || !RoarOfLoveConfig.isSoundEnabled(group)) {
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

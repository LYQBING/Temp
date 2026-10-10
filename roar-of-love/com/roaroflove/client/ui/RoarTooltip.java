//
// Decompiled by Jadx - 797ms
//
package com.roaroflove.client.ui;

import com.roaroflove.config.RoarOfLoveConfig;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;

public final class RoarTooltip {
    private static final List<Item> ITEMS = new ArrayList();
    private static final int LINE_H = 11;
    private static final int PAD = 4;
    private static final int WRAP_W = 168;

    private RoarTooltip() {
    }

    public static void reset() {
        ITEMS.clear();
    }

    public static void register(int i, int i2, int i3, int i4, String str) {
        if (str != null && !str.isEmpty()) {
            ITEMS.add(new Item(i, i2, i3, i4, str));
        }
    }

    public static void render(class_332 class_332Var, int i, int i2, int i3, int i4) {
        if (RoarOfLoveConfig.isTooltips() && !ITEMS.isEmpty()) {
            try {
                class_310 method_1551 = class_310.method_1551();
                if (method_1551 != null && method_1551.field_1772 != null) {
                    class_327 class_327Var = method_1551.field_1772;
                    for (Item item : ITEMS) {
                        if (i >= item.x && i <= item.x + item.w && i2 >= item.y && i2 <= item.y + item.h) {
                            String string = class_2561.method_43469(item.key, new Object[0]).getString();
                            if (string != null && !string.isEmpty() && !string.equals(item.key)) {
                                List<String> wrap = wrap(class_327Var, string, WRAP_W);
                                Iterator<String> it = wrap.iterator();
                                int i5 = 0;
                                while (it.hasNext()) {
                                    i5 = Math.max(i5, class_327Var.method_27525(class_2561.method_43470(it.next())));
                                }
                                int size = (wrap.size() * LINE_H) + 8;
                                int min = Math.min(i + 10, ((i3 - i5) - 8) - 4);
                                int i6 = i2 + 12;
                                if (i6 + size > i4 - 4) {
                                    i6 = (i2 - size) - 6;
                                }
                                int max = Math.max(PAD, min);
                                int max2 = Math.max(PAD, i6);
                                class_332Var.method_25294(max - 4, max2 - 4, max + i5 + PAD, ((max2 + size) - 4) + 2, -267382764);
                                class_332Var.method_25294(max - 4, max2 - 4, max + i5 + PAD, (max2 - 4) + 1, 0x60ffffff);
                                int i7 = (max2 - 4) + 2;
                                Iterator<String> it2 = wrap.iterator();
                                while (it2.hasNext()) {
                                    class_332Var.method_27535(class_327Var, class_2561.method_43470(it2.next()), max, i7, -1513240);
                                    i7 += LINE_H;
                                }
                                return;
                            }
                            return;
                        }
                    }
                }
            } catch (Throwable th) {
            }
        }
    }

    private static List<String> wrap(class_327 class_327Var, String str, int i) {
        ArrayList arrayList = new ArrayList();
        while (true) {
            if (str.isEmpty()) {
                break;
            }
            if (class_327Var.method_27525(class_2561.method_43470(str)) <= i) {
                arrayList.add(str);
                break;
            }
            int length = str.length();
            while (length > 1 && class_327Var.method_27525(class_2561.method_43470(str.substring(0, length))) > i) {
                length--;
            }
            int lastIndexOf = str.lastIndexOf(32, Math.max(1, length));
            if (lastIndexOf <= 0 || lastIndexOf < length - 12) {
                lastIndexOf = length;
            }
            arrayList.add(str.substring(0, lastIndexOf).trim());
            str = str.substring(lastIndexOf).trim();
        }
        return arrayList;
    }
}

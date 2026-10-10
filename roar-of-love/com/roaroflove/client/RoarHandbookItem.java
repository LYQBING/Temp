//
// Decompiled by Jadx - 588ms
//
package com.roaroflove.client;

import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1937;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_638;

public class RoarHandbookItem extends class_1792 {
    public RoarHandbookItem(class_1792.class_1793 class_1793Var) {
        super(class_1793Var);
    }

    public class_1269 method_7836(class_1937 class_1937Var, class_1657 class_1657Var, class_1268 class_1268Var) {
        class_310 method_1551;
        try {
            if ((class_1937Var instanceof class_638) && (method_1551 = class_310.method_1551()) != null) {
                method_1551.method_1507(new RoarHandbookScreen((class_437) null));
            }
        } catch (Throwable th) {
        }
        return super.method_7836(class_1937Var, class_1657Var, class_1268Var);
    }
}

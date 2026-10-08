//
// Decompiled by Jadx - 439ms
//
package com.roaroflove.client;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;

final class RoarOfLoveSettingsScreen$Action extends Record {
    private final String key;
    private final String label;
    private final Runnable run;

    private RoarOfLoveSettingsScreen$Action(String str, String str2, Runnable runnable) {
        this.label = str;
        this.key = str2;
        this.run = runnable;
    }

    @Override
    public final boolean equals(Object obj) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, RoarOfLoveSettingsScreen$Action.class, Object.class), RoarOfLoveSettingsScreen$Action.class, "label;key;run", "FIELD:Lcom/roaroflove/client/RoarOfLoveSettingsScreen$Action;->label:Ljava/lang/String;", "FIELD:Lcom/roaroflove/client/RoarOfLoveSettingsScreen$Action;->key:Ljava/lang/String;", "FIELD:Lcom/roaroflove/client/RoarOfLoveSettingsScreen$Action;->run:Ljava/lang/Runnable;").dynamicInvoker().invoke(this, obj) /* invoke-custom */;
    }

    @Override
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, RoarOfLoveSettingsScreen$Action.class), RoarOfLoveSettingsScreen$Action.class, "label;key;run", "FIELD:Lcom/roaroflove/client/RoarOfLoveSettingsScreen$Action;->label:Ljava/lang/String;", "FIELD:Lcom/roaroflove/client/RoarOfLoveSettingsScreen$Action;->key:Ljava/lang/String;", "FIELD:Lcom/roaroflove/client/RoarOfLoveSettingsScreen$Action;->run:Ljava/lang/Runnable;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    public String key() {
        return this.key;
    }

    public String label() {
        return this.label;
    }

    public Runnable run() {
        return this.run;
    }

    @Override
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, RoarOfLoveSettingsScreen$Action.class), RoarOfLoveSettingsScreen$Action.class, "label;key;run", "FIELD:Lcom/roaroflove/client/RoarOfLoveSettingsScreen$Action;->label:Ljava/lang/String;", "FIELD:Lcom/roaroflove/client/RoarOfLoveSettingsScreen$Action;->key:Ljava/lang/String;", "FIELD:Lcom/roaroflove/client/RoarOfLoveSettingsScreen$Action;->run:Ljava/lang/Runnable;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }
}

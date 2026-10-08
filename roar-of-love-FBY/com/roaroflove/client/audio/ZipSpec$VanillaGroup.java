//
// Decompiled by Jadx - 436ms
//
package com.roaroflove.client.audio;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.List;

public final class ZipSpec$VanillaGroup extends Record {
    private final String display;
    private final List<String> events;
    private final List<String> fileNames;
    private final String key;

    public ZipSpec$VanillaGroup(String str, String str2, List<String> list, List<String> list2) {
        this.key = str;
        this.display = str2;
        this.events = list;
        this.fileNames = list2;
    }

    public String display() {
        return this.display;
    }

    @Override
    public final boolean equals(Object obj) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ZipSpec$VanillaGroup.class, Object.class), ZipSpec$VanillaGroup.class, "key;display;events;fileNames", "FIELD:Lcom/roaroflove/client/audio/ZipSpec$VanillaGroup;->key:Ljava/lang/String;", "FIELD:Lcom/roaroflove/client/audio/ZipSpec$VanillaGroup;->display:Ljava/lang/String;", "FIELD:Lcom/roaroflove/client/audio/ZipSpec$VanillaGroup;->events:Ljava/util/List;", "FIELD:Lcom/roaroflove/client/audio/ZipSpec$VanillaGroup;->fileNames:Ljava/util/List;").dynamicInvoker().invoke(this, obj) /* invoke-custom */;
    }

    public List<String> events() {
        return this.events;
    }

    public List<String> fileNames() {
        return this.fileNames;
    }

    @Override
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ZipSpec$VanillaGroup.class), ZipSpec$VanillaGroup.class, "key;display;events;fileNames", "FIELD:Lcom/roaroflove/client/audio/ZipSpec$VanillaGroup;->key:Ljava/lang/String;", "FIELD:Lcom/roaroflove/client/audio/ZipSpec$VanillaGroup;->display:Ljava/lang/String;", "FIELD:Lcom/roaroflove/client/audio/ZipSpec$VanillaGroup;->events:Ljava/util/List;", "FIELD:Lcom/roaroflove/client/audio/ZipSpec$VanillaGroup;->fileNames:Ljava/util/List;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    public String key() {
        return this.key;
    }

    @Override
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ZipSpec$VanillaGroup.class), ZipSpec$VanillaGroup.class, "key;display;events;fileNames", "FIELD:Lcom/roaroflove/client/audio/ZipSpec$VanillaGroup;->key:Ljava/lang/String;", "FIELD:Lcom/roaroflove/client/audio/ZipSpec$VanillaGroup;->display:Ljava/lang/String;", "FIELD:Lcom/roaroflove/client/audio/ZipSpec$VanillaGroup;->events:Ljava/util/List;", "FIELD:Lcom/roaroflove/client/audio/ZipSpec$VanillaGroup;->fileNames:Ljava/util/List;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }
}

//
// Decompiled by Jadx - 674ms
//
package com.roaroflove.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class CallsAvailability {
    private static volatile Map<String, Set<String>> bundledByGroup = Collections.emptyMap();

    private CallsAvailability() {
    }

    public static void setBundledCallFiles(Map<String, Set<String>> map) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (map != null) {
            for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
                linkedHashMap.put(entry.getKey(), Collections.unmodifiableSet(new LinkedHashSet(entry.getValue())));
            }
        }
        bundledByGroup = Collections.unmodifiableMap(linkedHashMap);
    }

    public static Set<String> availableCallFiles(String str) {
        if (str == null || str.isEmpty()) {
            str = "default";
        }
        Set<String> set = bundledByGroup.get(str);
        return set == null ? Collections.emptySet() : new LinkedHashSet(set);
    }

    public static Set<String> availableCallFiles() {
        return availableCallFiles("default");
    }

    public static boolean anyCallsAvailable(String str) {
        return !availableCallFiles(str).isEmpty();
    }

    public static boolean anyCallsAvailable() {
        Iterator<Set<String>> it = bundledByGroup.values().iterator();
        while (it.hasNext()) {
            if (!it.next().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}

//
// Decompiled by Jadx - 648ms
//
package com.roaroflove.util;

import com.roaroflove.sound.RoLSounds;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public final class CallsAvailability {
    private static volatile Map<String, Set<String>> bundledByGroup = Collections.emptyMap();

    private CallsAvailability() {
    }

    public static void setBundledCallFiles(Map<String, Set<String>> map) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (map != null) {
            for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
                LinkedHashSet linkedHashSet2 = new LinkedHashSet(entry.getValue());
                linkedHashMap.put(entry.getKey(), Collections.unmodifiableSet(linkedHashSet2));
            }
        }
        bundledByGroup = Collections.unmodifiableMap(linkedHashMap);
    }

    private static void scanOgg(Path path, Set<String> set) {
        if (Files.isDirectory(path, new LinkOption[0])) {
            try {
                Stream<Path> list = Files.list(path);
                try {
                    list.filter(path2 -> {
                        return Files.isRegularFile(path2, new LinkOption[0]) && path2.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".ogg");
                    }).forEach(path3 -> {
                        String fileName = path3.getFileName().toString();
                        set.add(fileName.substring(0, fileName.length() - 4));
                    });
                    if (list != null) {
                        list.close();
                    }
                } finally {
                }
            } catch (IOException e) {
            }
        }
    }

    private static Map<String, Set<String>> diskPackCallFiles() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Path resolve = AudioPaths.packCacheDir().resolve("assets").resolve("roar_of_love").resolve("sounds").resolve("calls");
        if (!Files.isDirectory(resolve, new LinkOption[0])) {
            return linkedHashMap;
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        scanOgg(resolve, linkedHashSet);
        if (!linkedHashSet.isEmpty()) {
            linkedHashMap.put("default", linkedHashSet);
        }
        for (String str : RoLSounds.CALL_GROUPS) {
            if (!"default".equals(str)) {
                LinkedHashSet linkedHashSet2 = new LinkedHashSet();
                scanOgg(resolve.resolve(str), linkedHashSet2);
                if (!linkedHashSet2.isEmpty()) {
                    linkedHashMap.put(str, linkedHashSet2);
                }
            }
        }
        return linkedHashMap;
    }

    public static Set<String> availableCallFiles(String str) {
        if (str == null || str.isEmpty()) {
            str = "default";
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Set<String> set = bundledByGroup.get(str);
        if (set != null) {
            linkedHashSet.addAll(set);
        }
        Set<String> set2 = diskPackCallFiles().get(str);
        if (set2 != null) {
            linkedHashSet.addAll(set2);
        }
        return linkedHashSet;
    }

    public static Set<String> availableCallFiles() {
        return availableCallFiles("default");
    }

    public static boolean anyCallsAvailable(String str) {
        return !availableCallFiles(str).isEmpty();
    }

    public static boolean anyCallsAvailable() {
        for (Set<String> calls : bundledByGroup.values()) {
            if (!calls.isEmpty()) {
                return true;
            }
        }
        Map<String, Set<String>> diskCalls = diskPackCallFiles();
        for (String group : RoLSounds.CALL_GROUPS) {
            Set<String> bundledCalls = bundledByGroup.get(group);
            Set<String> diskGroupCalls = diskCalls.get(group);
            if ((bundledCalls != null && !bundledCalls.isEmpty()) || (diskGroupCalls != null && !diskGroupCalls.isEmpty())) {
                return true;
            }
        }
        return false;
    }
}

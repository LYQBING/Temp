//
// Decompiled by Jadx - 705ms
//
package com.roaroflove.client;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_310;
import net.minecraft.class_746;

public final class RoarHandbook {
    private static final int CLOSE = 8;
    private static final Map<String, Integer> AFFECTION = new LinkedHashMap();
    private static int creatureCount = 0;
    private static boolean loaded = false;

    private RoarHandbook() {
    }

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("roar_of_love_handbook.txt");
    }

    public static synchronized void load() {
        synchronized (RoarHandbook.class) {
            if (!loaded) {
                loaded = true;
                try {
                    Path file = file();
                    if (Files.exists(file, new LinkOption[0])) {
                        Iterator<String> it = Files.readAllLines(file, StandardCharsets.UTF_8).iterator();
                        while (it.hasNext()) {
                            String[] split = it.next().split("\t");
                            if (split.length >= 2 && "P".equals(split[0])) {
                                AFFECTION.put(split[1], Integer.valueOf(Integer.parseInt(split[2])));
                            } else if (split.length >= 2 && "C".equals(split[0])) {
                                creatureCount = Integer.parseInt(split[1]);
                            }
                        }
                    }
                } catch (Throwable th) {
                }
            }
        }
    }

    public static synchronized void save() {
        synchronized (RoarHandbook.class) {
            try {
                Path file = file();
                Files.createDirectories(file.getParent(), new FileAttribute[0]);
                ArrayList arrayList = new ArrayList();
                for (Map.Entry<String, Integer> entry : AFFECTION.entrySet()) {
                    arrayList.add("P\t" + entry.getKey() + "\t" + String.valueOf(entry.getValue()));
                }
                arrayList.add("C\t" + creatureCount);
                Files.write(file, arrayList, StandardCharsets.UTF_8, new OpenOption[0]);
            } catch (Throwable th) {
            }
        }
    }

    public static void noteAnimation(List<UUID> list) {
        int i = 0;
        if (list != null && !list.isEmpty()) {
            load();
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null && method_1551.field_1687 != null && method_1551.field_1724 != null) {
                try {
                    for (class_746 class_746Var : method_1551.field_1687.method_18456()) {
                        if (list.contains(class_746Var.method_5667())) {
                            i++;
                            if (class_746Var != method_1551.field_1724) {
                                String string = class_746Var.method_5477().getString();
                                AFFECTION.put(string, Integer.valueOf(Math.min(50, AFFECTION.getOrDefault(string, 0).intValue() + 1)));
                            }
                        }
                        i = i;
                    }
                } catch (Throwable th) {
                }
                if (list.size() > Math.max(1, i)) {
                    creatureCount = Math.min(9999, (list.size() - Math.max(1, i)) + creatureCount);
                }
                save();
            }
        }
    }

    public static synchronized int affection(String str) {
        int intValue;
        synchronized (RoarHandbook.class) {
            load();
            intValue = AFFECTION.getOrDefault(str, 0).intValue();
        }
        return intValue;
    }

    public static synchronized boolean isClose(String str) {
        boolean z;
        synchronized (RoarHandbook.class) {
            z = affection(str) >= CLOSE;
        }
        return z;
    }

    public static synchronized int creatureCount() {
        int i;
        synchronized (RoarHandbook.class) {
            load();
            i = creatureCount;
        }
        return i;
    }

    public static synchronized List<String> summary() {
        ArrayList arrayList;
        synchronized (RoarHandbook.class) {
            load();
            arrayList = new ArrayList();
            for (Map.Entry<String, Integer> entry : AFFECTION.entrySet()) {
                arrayList.add(entry.getKey() + "   " + String.valueOf(entry.getValue()));
            }
            arrayList.add("creatures   " + creatureCount);
        }
        return arrayList;
    }
}

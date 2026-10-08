//
// Decompiled by Jadx - 719ms
//
package com.roaroflove.sound;

import com.roaroflove.RoarOfLove;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3414;
import net.minecraft.class_7923;

public final class RoLSounds {
    public static final List<String> CALL_BEGIN_FILES;
    public static final String CALL_END_FILE = "end";
    public static final int MAX_BEGIN = 10;
    public static final List<String> CATEGORIES = List.of("dryimpacts", "motions", "retract", "shots", "wet", "wetimpacts");
    public static final Map<String, List<String>> CATEGORY_FILES = new LinkedHashMap();
    public static final String CALL_GROUP_DEFAULT = "default";
    public static final List<String> CALL_GROUPS = List.of(CALL_GROUP_DEFAULT, "self", "pole", "oral");
    private static final Map<String, List<String>> CALL_GROUP_TAGS = new LinkedHashMap();
    private static final Map<String, class_3414> BY_ID = new LinkedHashMap();
    private static boolean registered = false;

    static {
        CATEGORY_FILES.put("dryimpacts", names("impactdry", 1, 12));
        CATEGORY_FILES.put("motions", names("motion", 1, 10));
        CATEGORY_FILES.put("retract", names("retract", 1, 3));
        CATEGORY_FILES.put("shots", merge(names("shot_in", 1, 3), names("shot_out", 1, 6)));
        CATEGORY_FILES.put("wet", names("wet", 1, 12));
        CATEGORY_FILES.put("wetimpacts", names("impactwet", 1, 16));
        ArrayList arrayList = new ArrayList();
        for (int i = 1; i <= 10; i++) {
            arrayList.add("begin" + i);
        }
        CALL_BEGIN_FILES = Collections.unmodifiableList(arrayList);
        CALL_GROUP_TAGS.put("self", List.of("masturbation", "solo", "handjob"));
        CALL_GROUP_TAGS.put("pole", List.of("poledance", "polein", "poleout"));
        CALL_GROUP_TAGS.put("oral", List.of("blowjob", "oral", "facefk"));
    }

    private RoLSounds() {
    }

    private static List<String> names(String str, int i, int i2) {
        ArrayList arrayList = new ArrayList();
        while (i <= i2) {
            arrayList.add(str + String.format("%02d", Integer.valueOf(i)));
            i++;
        }
        return Collections.unmodifiableList(arrayList);
    }

    private static List<String> merge(List<String> list, List<String> list2) {
        ArrayList arrayList = new ArrayList(list);
        arrayList.addAll(list2);
        return Collections.unmodifiableList(arrayList);
    }

    public static void init() {
        if (!registered) {
            for (String str : CATEGORIES) {
                List<String> orDefault = CATEGORY_FILES.getOrDefault(str, List.of());
                register(str);
                Iterator<String> it = orDefault.iterator();
                while (it.hasNext()) {
                    register(str + "." + it.next());
                }
            }
            register("call_begin");
            register("call_end");
            Iterator<String> it2 = CALL_BEGIN_FILES.iterator();
            while (it2.hasNext()) {
                register("call." + it2.next());
            }
            register("call.end");
            for (String str2 : CALL_GROUPS) {
                if (!CALL_GROUP_DEFAULT.equals(str2)) {
                    Iterator<String> it3 = CALL_BEGIN_FILES.iterator();
                    while (it3.hasNext()) {
                        register("call." + str2 + "." + it3.next());
                    }
                    register("call." + str2 + ".end");
                }
            }
            registered = true;
            RoarOfLove.LOGGER.info("[roar_of_love] 已注册 {} 个声音事件（含 {} 个叫声分组）", Integer.valueOf(BY_ID.size()), Integer.valueOf(CALL_GROUPS.size()));
        }
    }

    private static void register(String str) {
        class_2960 method_60655 = class_2960.method_60655("roar_of_love", str);
        class_3414 method_47908 = class_3414.method_47908(method_60655);
        class_2378.method_10230(class_7923.field_41172, method_60655, method_47908);
        BY_ID.put(method_60655.toString(), method_47908);
    }

    public static class_3414 get(String str) {
        if (str == null) {
            return null;
        }
        if (!str.contains(":")) {
            str = "roar_of_love:" + str;
        }
        class_3414 class_3414Var = BY_ID.get(str);
        return class_3414Var == null ? (class_3414) class_7923.field_41172.method_63535(class_2960.method_12829(str)) : class_3414Var;
    }

    public static boolean isRegistered(String str) {
        if (!str.contains(":")) {
            str = "roar_of_love:" + str;
        }
        return BY_ID.containsKey(str);
    }

    public static String callEventKey(String str, String str2) {
        if (str == null || str.isEmpty()) {
            str = CALL_GROUP_DEFAULT;
        }
        return CALL_GROUP_DEFAULT.equals(str) ? "call." + str2 : "call." + str + "." + str2;
    }

    public static String normalizeTag(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char lowerCase = Character.toLowerCase(str.charAt(i));
            if (lowerCase != ' ' && lowerCase != '_' && lowerCase != '-' && lowerCase != '.') {
                sb.append(lowerCase);
            }
        }
        return sb.toString();
    }

    public static String callGroupForTags(Iterable<String> iterable) {
        if (iterable != null) {
            for (Map.Entry<String, List<String>> entry : CALL_GROUP_TAGS.entrySet()) {
                for (String str : iterable) {
                    if (str != null && entry.getValue().contains(normalizeTag(str))) {
                        return entry.getKey();
                    }
                }
            }
        }
        return CALL_GROUP_DEFAULT;
    }

    public static List<String> callGroups() {
        return CALL_GROUPS;
    }

    public static List<String> callGroupTags(String str) {
        return CALL_GROUP_TAGS.getOrDefault(str, List.of());
    }
}

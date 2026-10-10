//
// Decompiled by Jadx - 660ms
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
    public static final List<String> CALL_SLOTS;
    public static final int HURT_BUILTIN_COUNT = 9;
    public static final List<String> HURT_SLOTS;
    public static final int MAX_BEGIN = 20;
    public static final List<String> CATEGORIES = List.of("dryimpacts", "motions", "retract", "shots", "wet", "wetimpacts");
    public static final Map<String, List<String>> CATEGORY_FILES = new LinkedHashMap();
    public static final String CALL_GROUP_DEFAULT = "default";
    public static final List<String> CALL_GROUPS = List.of(CALL_GROUP_DEFAULT, "self", "pole", "oral");
    private static final Map<String, List<String>> CALL_GROUP_TAGS = new LinkedHashMap();
    private static final Map<String, class_3414> BY_ID = new LinkedHashMap();
    private static boolean registered = false;

    static {
        CATEGORY_FILES.put("dryimpacts", names("impactdry", 1, 20));
        CATEGORY_FILES.put("motions", names("motion", 1, 20));
        CATEGORY_FILES.put("retract", names("retract", 1, 20));
        CATEGORY_FILES.put("shots", merge(names("shot_in", 1, 20), names("shot_out", 1, 20)));
        CATEGORY_FILES.put("shots_in", names("shot_in", 1, 20));
        CATEGORY_FILES.put("shots_out", names("shot_out", 1, 20));
        CATEGORY_FILES.put("wet", names("wet", 1, 20));
        CATEGORY_FILES.put("wetimpacts", names("impactwet", 1, 16));
        HURT_SLOTS = Collections.unmodifiableList(names("hurt", 1, 20, false));
        ArrayList arrayList = new ArrayList();
        for (int i = 1; i <= 20; i++) {
            arrayList.add("begin" + i);
        }
        CALL_BEGIN_FILES = Collections.unmodifiableList(arrayList);
        ArrayList arrayList2 = new ArrayList(arrayList);
        arrayList2.add(CALL_END_FILE);
        CALL_SLOTS = Collections.unmodifiableList(arrayList2);
        CALL_GROUP_TAGS.put("self", List.of("masturbation", "solo", "handjob"));
        CALL_GROUP_TAGS.put("pole", List.of("poledance", "polein", "poleout"));
        CALL_GROUP_TAGS.put("oral", List.of("blowjob", "oral", "facefk"));
    }

    private RoLSounds() {
    }

    private static List<String> names(String str, int i, int i2) {
        return names(str, i, i2, true);
    }

    private static List<String> names(String str, int i, int i2, boolean z) {
        ArrayList arrayList = new ArrayList();
        while (i <= i2) {
            arrayList.add(str + (z ? String.format("%02d", Integer.valueOf(i)) : String.valueOf(i)));
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
            register("hunt");
            Iterator<String> it4 = HURT_SLOTS.iterator();
            while (it4.hasNext()) {
                register("hunt." + it4.next());
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

    public static List<String> callSlotsFor(String str) {
        return CALL_SLOTS;
    }

    public static List<String> hurtSlots() {
        return HURT_SLOTS;
    }

    public static int hurtBuiltinCount() {
        return 9;
    }

    public static List<String> shotInFiles() {
        return CATEGORY_FILES.getOrDefault("shots_in", List.of());
    }

    public static List<String> shotOutFiles() {
        return CATEGORY_FILES.getOrDefault("shots_out", List.of());
    }

    public static boolean isSplitCategory(String str) {
        return "shots".equals(str);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x003a, code lost:
    
        if (r3.equals("cat") != false) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String builtinRelPathFor(String str, String str2) {
        char c = 0;
        int indexOf = str == null ? -1 : str.indexOf(58);
        String substring = indexOf < 0 ? "" : str.substring(0, indexOf);
        String substring2 = indexOf < 0 ? "" : str.substring(indexOf + 1);
        String str3 = str2 + ".ogg";
        switch (substring.hashCode()) {
            case 98262:
                break;
            case 3045982:
                if (substring.equals("call")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 3214227:
                if (substring.equals("hunt")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return "actionsounds/" + substring2 + "/" + str3;
            case 1:
                return CALL_GROUP_DEFAULT.equals(substring2) ? "calls/" + str3 : "calls/" + substring2 + "/" + str3;
            case 2:
                return "vanilla/hurt/" + str3;
            default:
                return null;
        }
    }

    public static String eventKeyFor(String str, String str2) {
        String slotKindOf = slotKindOf(str);
        String slotNameOf = slotNameOf(str);
        char c = 65535;
        switch (slotKindOf.hashCode()) {
            case 98262:
                if (slotKindOf.equals("cat")) {
                    c = 0;
                    break;
                }
                break;
            case 3045982:
                if (slotKindOf.equals("call")) {
                    c = 1;
                    break;
                }
                break;
            case 3214227:
                if (slotKindOf.equals("hunt")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return slotNameOf + "." + str2;
            case 1:
                return callEventKey(slotNameOf, str2);
            case 2:
                return "hunt." + str2;
            default:
                return str2;
        }
    }

    public static String labelKeyFor(String str) {
        String slotKindOf = slotKindOf(str);
        String slotNameOf = slotNameOf(str);
        char c = 65535;
        switch (slotKindOf.hashCode()) {
            case 98262:
                if (slotKindOf.equals("cat")) {
                    c = 0;
                    break;
                }
                break;
            case 3045982:
                if (slotKindOf.equals("call")) {
                    c = 1;
                    break;
                }
                break;
            case 3214227:
                if (slotKindOf.equals("hunt")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return "roar_of_love.cat." + slotNameOf;
            case 1:
                return "roar_of_love.call.group." + slotNameOf;
            case 2:
                return "roar_of_love.ui.hurt_preview";
            default:
                return slotNameOf;
        }
    }

    private static String slotKindOf(String str) {
        int indexOf = str == null ? -1 : str.indexOf(58);
        return indexOf < 0 ? "" : str.substring(0, indexOf);
    }

    private static String slotNameOf(String str) {
        int indexOf = str == null ? -1 : str.indexOf(58);
        return indexOf < 0 ? "" : str.substring(indexOf + 1);
    }

    public static boolean isSlotReplaceable(String str) {
        String slotKindOf = slotKindOf(str);
        return "cat".equals(slotKindOf) || "call".equals(slotKindOf) || "hunt".equals(slotKindOf);
    }
}

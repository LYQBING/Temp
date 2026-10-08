//
// Decompiled by Jadx - 878ms
//
package com.roaroflove.client;

import com.nonid.GenderHolder;
import com.nonid.api.animation.NonAnimationApi;
import com.nonid.api.animation.NonAnimationDefinition;
import com.nonid.effect.NonStatusEffects;
import com.roaroflove.RoarOfLove;
import com.roaroflove.config.RoarOfLoveConfig;
import com.roaroflove.sound.RoLSounds;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1293;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_746;
import net.minecraft.class_7923;

public final class RoarSubtitles {
    public static final String ANIM = "anim";
    public static final String ANY = "any";
    private static final int DANMAKU_COLOR = -18206;
    private static final int FLOAT_COLOR = -25896;
    private static final int LANES = 4;
    private static final int LINES = 20;
    public static final String LUST = "lust";
    private static final int MAX_DANMAKU = 4;
    private static final int MAX_FLOAT = 10;
    public static final String ORAL = "oral";
    public static final String PEAK = "peak";
    private static final int RECENT_KEEP = 4;
    public static final String SELF = "self";
    private static final List<Sub> SUBS = new ArrayList();
    private static final Map<String, List<String>> CUSTOM = new HashMap();
    private static long lastMs = 0;
    private static UUID selfInstance = null;
    private static String activeCat = null;
    private static boolean inPeak = false;
    private static final ArrayDeque<String> RECENT = new ArrayDeque<>();

    private RoarSubtitles() {
    }

    public static boolean isEnabled(String str) {
        if (LUST.equals(str)) {
            return RoarOfLoveConfig.isSubLust();
        }
        if (ANIM.equals(str)) {
            return RoarOfLoveConfig.isSubAnim();
        }
        if (PEAK.equals(str)) {
            return RoarOfLoveConfig.isSubPeak();
        }
        if (SELF.equals(str)) {
            return RoarOfLoveConfig.isSubSelf();
        }
        if (ORAL.equals(str)) {
            return RoarOfLoveConfig.isSubOral();
        }
        return false;
    }

    public static void setEnabled(String str, boolean z) {
        if (LUST.equals(str)) {
            RoarOfLoveConfig.setSubLust(z);
            return;
        }
        if (ANIM.equals(str)) {
            RoarOfLoveConfig.setSubAnim(z);
            return;
        }
        if (PEAK.equals(str)) {
            RoarOfLoveConfig.setSubPeak(z);
        } else if (SELF.equals(str)) {
            RoarOfLoveConfig.setSubSelf(z);
        } else if (ORAL.equals(str)) {
            RoarOfLoveConfig.setSubOral(z);
        }
    }

    private static String subtitleCategory(String str) {
        if (str == null) {
            return null;
        }
        if ("default".equals(str)) {
            return ANIM;
        }
        if (SELF.equals(str)) {
            return SELF;
        }
        if (ORAL.equals(str)) {
            return ORAL;
        }
        return null;
    }

    private static String groupOf(class_2960 class_2960Var) {
        try {
            NonAnimationDefinition definition = NonAnimationApi.getDefinition(class_2960Var);
            if (definition == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            if (definition.contentTags() != null) {
                arrayList.addAll(definition.contentTags());
            }
            if (definition.animationTags() != null) {
                arrayList.addAll(definition.animationTags());
            }
            return RoLSounds.callGroupForTags(arrayList);
        } catch (Throwable th) {
            return null;
        }
    }

    public static boolean isMalePlayer() {
        boolean z;
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 == null || method_1551.field_1724 == null) {
            return false;
        }
        try {
            GenderHolder genderHolder = method_1551.field_1724;
            if (genderHolder.hasGender(1)) {
                if (!genderHolder.hasGender(2)) {
                    z = true;
                    return z;
                }
            }
            z = false;
            return z;
        } catch (Throwable th) {
            return false;
        }
    }

    public static boolean isAnimating() {
        return activeCat != null;
    }

    public static void onAnimationStart(UUID uuid, class_2960 class_2960Var) {
        selfInstance = uuid;
        inPeak = false;
        activeCat = subtitleCategory(groupOf(class_2960Var));
    }

    public static void onAnimationEnd(UUID uuid) {
        if (selfInstance != null && selfInstance.equals(uuid)) {
            RoarStyle.clear();
            selfInstance = null;
            activeCat = null;
            inPeak = false;
        }
    }

    public static void onPeak(boolean z) {
        inPeak = z;
    }

    private static int lustLevel(class_746 class_746Var) {
        try {
            class_1293 method_6112 = class_746Var.method_6112(class_7923.field_41174.method_47983(NonStatusEffects.ENERGIZED));
            if (method_6112 == null) {
                return -1;
            }
            return Math.max(0, method_6112.method_5578());
        } catch (Throwable th) {
            return -1;
        }
    }

    public static void update() {
        int lustLevel;
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null && method_1551.field_1724 != null) {
            long currentTimeMillis = System.currentTimeMillis();
            SUBS.removeIf(sub -> {
                return currentTimeMillis - sub.bornMs >= sub.lifeMs;
            });
            if (lastMs == 0) {
                lastMs = currentTimeMillis;
                return;
            }
            float min = Math.min(0.25f, ((float) (currentTimeMillis - lastMs)) / 1000.0f);
            lastMs = currentTimeMillis;
            if (activeCat == null && isEnabled(LUST) && (lustLevel = lustLevel(method_1551.field_1724)) >= 0) {
                if (ThreadLocalRandom.current().nextDouble() < (0.15d + (lustLevel * 0.55d)) * ((RoarBeat.pulse() * 1.8d) + 0.4d) * RoarStyle.subtitleRate() * min) {
                    spawn(LUST, 1);
                }
            }
            if (activeCat != null) {
                String str = inPeak ? PEAK : activeCat;
                if (isEnabled(str)) {
                    if (ThreadLocalRandom.current().nextDouble() < (PEAK.equals(str) ? 1.1d : 0.5d) * ((RoarBeat.pulse() * 1.8d) + 0.4d) * RoarStyle.subtitleRate() * min) {
                        spawn(str, 1);
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        SUBS.clear();
    }

    private static String pickText(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        String str = list.get(ThreadLocalRandom.current().nextInt(list.size()));
        for (int i = 0; i < 8 && RECENT.contains(str); i++) {
            str = list.get(ThreadLocalRandom.current().nextInt(list.size()));
        }
        RECENT.addLast(str);
        while (RECENT.size() > 4) {
            RECENT.removeFirst();
        }
        return str;
    }

    private static void spawn(String str, int i) {
        boolean nextBoolean;
        int i2;
        List<String> pool = pool(str);
        if (!pool.isEmpty() && i > 0) {
            int subForm = RoarOfLoveConfig.subForm();
            for (int i3 = 0; i3 < i; i3++) {
                String fillPlaceholders = fillPlaceholders(pickText(pool));
                if (subForm == 0) {
                    nextBoolean = false;
                } else {
                    nextBoolean = subForm == 1 ? true : ThreadLocalRandom.current().nextBoolean();
                }
                Iterator<Sub> it = SUBS.iterator();
                int i4 = 0;
                int i5 = 0;
                while (it.hasNext()) {
                    if (it.next().danmaku) {
                        i2 = i4 + 1;
                    } else {
                        i5++;
                        i2 = i4;
                    }
                    i4 = i2;
                }
                if ((!nextBoolean || i4 < 4) && (nextBoolean || i5 < MAX_FLOAT)) {
                    Sub sub = new Sub();
                    sub.text = fillPlaceholders;
                    sub.danmaku = nextBoolean;
                    sub.bornMs = System.currentTimeMillis();
                    if (nextBoolean) {
                        sub.lane = pickLane();
                        if (sub.lane >= 0) {
                            sub.lifeMs = 4200 + ThreadLocalRandom.current().nextLong(1400L);
                        }
                    } else {
                        sub.lifeMs = 3200 + ThreadLocalRandom.current().nextLong(2000L);
                        sub.x = 0.06f + (ThreadLocalRandom.current().nextFloat() * 0.5f);
                        sub.y = 0.1f + (ThreadLocalRandom.current().nextFloat() * 0.72f);
                        sub.vx = (ThreadLocalRandom.current().nextBoolean() ? 1.0f : -1.0f) * (0.015f + (ThreadLocalRandom.current().nextFloat() * 0.045f));
                        sub.vy = (ThreadLocalRandom.current().nextBoolean() ? 1.0f : -1.0f) * (0.006f + (ThreadLocalRandom.current().nextFloat() * 0.018f));
                    }
                    SUBS.add(sub);
                }
            }
        }
    }

    private static String fillPlaceholders(String str) {
        if (str != null && str.indexOf(123) >= 0) {
            class_310 method_1551 = class_310.method_1551();
            String str2 = "";
            String str3 = "-";
            if (method_1551 != null && method_1551.field_1724 != null) {
                try {
                    str2 = method_1551.field_1724.method_5477().getString();
                } catch (Throwable th) {
                    str2 = "";
                }
                int lustLevel = lustLevel(method_1551.field_1724);
                if (lustLevel >= 0) {
                    str3 = String.valueOf(lustLevel + 1);
                }
            }
            return str.replace("{player}", str2).replace("{lust}", str3);
        }
        return str;
    }

    private static String reveal(String str, long j) {
        int max;
        return (str == null || !RoarOfLoveConfig.isTypewriter() || (max = Math.max(1, (int) (Math.min(1.0f, ((float) j) / 420.0f) * ((float) str.length())))) >= str.length()) ? str : str.substring(0, max);
    }

    private static List<String> pool(String str) {
        List<String> list;
        ArrayList arrayList = new ArrayList();
        int i = 1;
        while (true) {
            int i2 = i;
            if (i2 > LINES) {
                break;
            }
            String str2 = "roar_of_love.sub." + str + "." + i2;
            String lookup = RoarSubtitleLang.lookup(str2);
            if (lookup == null || lookup.isEmpty()) {
                lookup = class_2561.method_43469(str2, new Object[0]).getString();
            }
            if (lookup != null && !lookup.isEmpty() && !lookup.equals(str2)) {
                arrayList.add(lookup);
            }
            i = i2 + 1;
        }
        List<String> list2 = CUSTOM.get(str);
        if (list2 != null) {
            arrayList.addAll(list2);
        }
        if (!LUST.equals(str) && (list = CUSTOM.get(ANY)) != null) {
            arrayList.addAll(list);
        }
        return arrayList;
    }

    private static float laneProgress(int i, long j) {
        float f = -1.0f;
        Iterator<Sub> it = SUBS.iterator();
        while (true) {
            float f2 = f;
            if (it.hasNext()) {
                Sub next = it.next();
                if (next.danmaku && next.lane == i) {
                    f = ((float) (j - next.bornMs)) / ((float) next.lifeMs);
                    if (f > f2) {
                    }
                }
                f = f2;
            } else {
                return f2;
            }
        }
    }

    private static int pickLane() {
        long currentTimeMillis = System.currentTimeMillis();
        float f = -1.0f;
        int i = 0;
        int i2 = -1;
        while (i < 4) {
            float laneProgress = laneProgress(i, currentTimeMillis);
            if (laneProgress >= 0.0f) {
                if (laneProgress > f) {
                    i2 = i;
                } else {
                    laneProgress = f;
                }
                i++;
                f = laneProgress;
            } else {
                return i;
            }
        }
        if (f < 0.62f) {
            i2 = -1;
        }
        return i2;
    }

    private static String truncate(class_327 class_327Var, String str, int i) {
        if (class_327Var.method_27525(class_2561.method_43470(str)) > i) {
            while (str.length() > 1 && class_327Var.method_27525(class_2561.method_43470(str + "…")) > i) {
                str = str.substring(0, str.length() - 1);
            }
            return str + "…";
        }
        return str;
    }

    public static int boxWidth(int i) {
        return Math.min(210, Math.max(120, i / 2));
    }

    public static int boxHeight() {
        return 89;
    }

    public static void render(class_332 class_332Var, int i, int i2) {
        boolean z;
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 != null && method_1551.field_1772 != null && !SUBS.isEmpty()) {
            class_327 class_327Var = method_1551.field_1772;
            long currentTimeMillis = System.currentTimeMillis();
            int boxWidth = boxWidth(i);
            int boxHeight = boxHeight();
            int x = RoarLayout.x(1, i, i2);
            int y = RoarLayout.y(1, i, i2);
            Iterator<Sub> it = SUBS.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                } else if (it.next().danmaku) {
                    z = true;
                    break;
                }
            }
            if (z) {
                class_332Var.method_25294(x, y, x + boxWidth, boxHeight + y, 0x40000000);
            }
            for (Sub sub : SUBS) {
                float max = Math.max(0.0f, Math.min(1.0f, ((float) (currentTimeMillis - sub.bornMs)) / ((float) sub.lifeMs)));
                float max2 = Math.max(0.0f, Math.min(1.0f, Math.min(max / 0.12f, (1.0f - max) / 0.18f)));
                if (sub.danmaku) {
                    class_332Var.method_51439(class_327Var, class_2561.method_43470(reveal(truncate(class_327Var, sub.text, boxWidth - 10), currentTimeMillis - sub.bornMs)), x + 5 + ((int) ((1.0f - max) * ((boxWidth - 10) - class_327Var.method_27525(class_2561.method_43470(r7))))), ((int) (Math.sin(((currentTimeMillis - sub.bornMs) / 420.0d) + sub.phase) * 1.7999999523162842d)) + y + 7 + (sub.lane * 19), (((int) (235.0f * max2)) << 24) | RoarPalette.danmaku(), true);
                } else {
                    int method_27525 = class_327Var.method_27525(class_2561.method_43470(sub.text));
                    float cos = ((float) Math.cos(((currentTimeMillis - sub.bornMs) / 460.0d) + sub.phase)) * 2.4f;
                    float sin = ((float) Math.sin(((currentTimeMillis - sub.bornMs) / 380.0d) + sub.phase)) * 3.4f;
                    class_332Var.method_51439(class_327Var, class_2561.method_43470(reveal(sub.text, currentTimeMillis - sub.bornMs)), Math.max(4, Math.min((i - method_27525) - 4, (int) (cos + ((sub.x + (sub.vx * max)) * i)))), Math.max(4, Math.min(i2 - 12, (int) ((((max * sub.vy) + sub.y) * i2) + sin))), (((int) (205.0f * max2)) << 24) | RoarPalette.soft(), true);
                }
            }
        }
    }

    public static void clear() {
        SUBS.clear();
    }

    public static int activeCount() {
        return SUBS.size();
    }

    private static void putCustom(String str, String str2) {
        List<String> list = CUSTOM.get(str);
        if (list == null) {
            list = new ArrayList<>();
            CUSTOM.put(str, list);
        }
        list.add(str2);
    }

    private static int separatorIndex(String str) {
        int i = 0;
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if (charAt != 65306 && charAt != ':') {
                i++;
            } else {
                return i;
            }
        }
        return -1;
    }

    private static String mapCategory(String str) {
        String trim = str.toLowerCase(Locale.ROOT).trim();
        if (trim.contains("性欲") || trim.contains(LUST) || trim.contains("horny")) {
            return LUST;
        }
        if (trim.contains("内射") || trim.contains(PEAK) || trim.contains("creampie") || trim.contains("cum")) {
            return PEAK;
        }
        if (trim.contains("自慰") || trim.contains(SELF) || trim.contains("masturb")) {
            return SELF;
        }
        if (trim.contains("口交") || trim.contains(ORAL) || trim.contains("blow")) {
            return ORAL;
        }
        if (trim.contains("动画") || trim.contains(ANIM) || trim.contains("sex")) {
            return ANIM;
        }
        return null;
    }

    public static void clearCustom() {
        CUSTOM.clear();
        RoarOfLoveConfig.setSubCustomFile("");
    }

    public static int loadCustom(Path path) {
        int i;
        String mapCategory;
        CUSTOM.clear();
        try {
            Iterator<String> it = Files.readAllLines(path, StandardCharsets.UTF_8).iterator();
            int i2 = 0;
            while (it.hasNext()) {
                String next = it.next();
                String trim = next == null ? "" : next.trim();
                if (trim.isEmpty() || trim.startsWith("#") || trim.startsWith("//")) {
                    i = i2;
                } else {
                    String str = ANY;
                    int separatorIndex = separatorIndex(trim);
                    if (separatorIndex > 0 && (mapCategory = mapCategory(trim.substring(0, separatorIndex))) != null) {
                        trim = trim.substring(separatorIndex + 1);
                        str = mapCategory;
                    }
                    String[] split = trim.replace((char) 65295, '/').split("/");
                    i = i2;
                    for (String str2 : split) {
                        String trim2 = str2.trim();
                        if (!trim2.isEmpty()) {
                            putCustom(str, trim2);
                            i++;
                        }
                    }
                }
                i2 = i;
            }
            if (i2 > 0) {
                RoarOfLoveConfig.setSubCustomFile(path.getFileName().toString());
                return i2;
            }
            return i2;
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 字幕文件读取失败: " + String.valueOf(path), th);
            return -1;
        }
    }

    public static Path audioFolder() {
        try {
            return FabricLoader.getInstance().getGameDir().resolve("RoarOfLove_Audio");
        } catch (Throwable th) {
            return null;
        }
    }

    public static Path findCustomFile(String str) {
        Path audioFolder = audioFolder();
        if (audioFolder == null || str == null || str.isEmpty() || !Files.isDirectory(audioFolder, new LinkOption[0])) {
            return null;
        }
        try {
            Stream<Path> walk = Files.walk(audioFolder, 4, new FileVisitOption[0]);
            try {
                Path orElse = walk.filter(path -> {
                    return Files.isRegularFile(path, new LinkOption[0]) && path.getFileName().toString().equalsIgnoreCase(str);
                }).findFirst().orElse(null);
                if (walk != null) {
                    walk.close();
                    return orElse;
                }
                return orElse;
            } finally {
            }
        } catch (Throwable th) {
            return null;
        }
    }

    public static boolean reloadSavedCustom() {
        String subCustomFile = RoarOfLoveConfig.subCustomFile();
        if (subCustomFile == null || subCustomFile.isEmpty()) {
            return false;
        }
        Path findCustomFile = findCustomFile(subCustomFile);
        if (findCustomFile == null) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 未找到已保存的字幕文件: " + subCustomFile);
            return false;
        }
        int loadCustom = loadCustom(findCustomFile);
        RoarOfLove.LOGGER.info("[roar_of_love] 已自动加载字幕文件 {} ({} 句)", subCustomFile, Integer.valueOf(loadCustom));
        return loadCustom > 0;
    }

    public static int customCount() {
        int i = 0;
        Iterator<List<String>> it = CUSTOM.values().iterator();
        while (true) {
            int i2 = i;
            if (it.hasNext()) {
                i = it.next().size() + i2;
            } else {
                return i2;
            }
        }
    }
}

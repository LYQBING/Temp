//
// Decompiled by Jadx - 1076ms
//
package com.roaroflove.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nonid.api.animation.NonAnimationApi;
import com.nonid.api.animation.NonAnimationStage;
import com.roaroflove.RoarOfLove;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.class_1132;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3218;
import net.minecraft.class_3298;
import net.minecraft.class_3300;

public final class RoarPlayerControl {
    public static final boolean ENABLED = false;
    private static final double[] SPEEDS = {0.25d, 0.5d, 1.0d, 1.5d, 2.0d};
    private static UUID sessionId = null;
    private static UUID animationId = null;
    private static boolean frozen = false;
    private static int speedIndex = 2;
    private static int lastStageNumber = -1;
    private static final Map<String, Cycle> CYCLE_CACHE = new LinkedHashMap();
    private static final List<String> CYCLE_TRIED = new ArrayList();

    private RoarPlayerControl() {
    }

    public static boolean isFrozen() {
        return frozen;
    }

    public static double currentSpeed() {
        return SPEEDS[Math.max(0, Math.min(SPEEDS.length - 1, speedIndex))];
    }

    public static int speedPercent() {
        return (int) Math.round(currentSpeed() * 100.0d);
    }

    public static void setInstance(UUID uuid) {
        sessionId = uuid;
        frozen = false;
        lastStageNumber = -1;
        RoarOfLove.LOGGER.info("[roar_of_love] 玩家操纵：接管会话 {}", uuid);
    }

    public static void clearInstance() {
        sessionId = null;
        animationId = null;
        frozen = false;
        lastStageNumber = -1;
    }

    public static boolean isActive() {
        return false;
    }

    private static class_3218 serverWorld() {
        class_310 method_1551;
        try {
            method_1551 = class_310.method_1551();
        } catch (Throwable th) {
        }
        if (method_1551 == null || method_1551.method_1576() == null) {
            return null;
        }
        class_1132 method_1576 = method_1551.method_1576();
        for (Method method : method_1576.getClass().getMethods()) {
            if (method.getParameterCount() == 0 && method.getReturnType() == class_3218.class) {
                Object invoke = method.invoke(method_1576, new Object[0]);
                if (invoke instanceof class_3218) {
                    return (class_3218) invoke;
                }
            }
        }
        return null;
    }

    private static void applySpeed(double d) {
        try {
            class_3218 serverWorld = serverWorld();
            if (serverWorld != null && sessionId != null) {
                NonAnimationApi.multiplySpeed(serverWorld, sessionId, d);
            }
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 设置播放倍速失败：{}", th.toString());
        }
    }

    public static void toggleHold() {
    }

    public static void cycleSpeed() {
    }

    public static void nextStage() {
    }

    private static class_2960 currentAnimationResource() {
        NonAnimationStage currentStage;
        class_2960 class_2960Var = null;
        try {
            class_3218 serverWorld = serverWorld();
            if (serverWorld != null && sessionId != null && (currentStage = NonAnimationApi.getCurrentStage(serverWorld, sessionId)) != null) {
                class_2960Var = currentStage.playbackAnimationId() != null ? currentStage.playbackAnimationId() : currentStage.animationId();
            }
        } catch (Throwable th) {
        }
        return class_2960Var;
    }

    public static Cycle currentCycle() {
        return null;
    }

    private static Cycle parseCycle(class_2960 class_2960Var) {
        String str;
        boolean z;
        double d;
        double d2;
        double d3;
        String str2;
        try {
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 == null || method_1551.method_1478() == null) {
                return null;
            }
            class_3300 method_1478 = method_1551.method_1478();
            String method_12832 = class_2960Var.method_12832();
            if (method_12832.startsWith("animations/")) {
                method_12832 = method_12832.substring("animations/".length());
            }
            String[] strArr = {"geckolib/animations/afw/" + method_12832 + ".animation.json", "geckolib/animations/" + method_12832 + ".animation.json", "animations/" + method_12832 + ".animation.json"};
            JsonObject jsonObject = null;
            String str3 = null;
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                String str4 = strArr[i];
                try {
                    class_2960 method_60655 = class_2960.method_60655(class_2960Var.method_12836(), str4);
                    if (method_1478.method_14486(method_60655).isEmpty()) {
                        str2 = str3;
                    } else {
                        InputStream method_14482 = ((class_3298) method_1478.method_14486(method_60655).get()).method_14482();
                        try {
                            InputStreamReader inputStreamReader = new InputStreamReader(method_14482, StandardCharsets.UTF_8);
                            try {
                                JsonElement parseReader = JsonParser.parseReader(inputStreamReader);
                                if (parseReader != null && parseReader.isJsonObject()) {
                                    jsonObject = parseReader.getAsJsonObject();
                                    try {
                                        inputStreamReader.close();
                                        if (method_14482 != null) {
                                            try {
                                                method_14482.close();
                                            } catch (Throwable th) {
                                                str2 = str4;
                                            }
                                        }
                                        str = str4;
                                        break;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        str3 = str4;
                                        if (method_14482 != null) {
                                            try {
                                                method_14482.close();
                                            } catch (Throwable th3) {
                                                th.addSuppressed(th3);
                                            }
                                        }
                                        throw th;
                                        break;
                                    }
                                }
                                inputStreamReader.close();
                                if (method_14482 != null) {
                                    method_14482.close();
                                }
                                str2 = str3;
                            } finally {
                            }
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    }
                } catch (Throwable th5) {
                    str2 = str3;
                }
                i++;
                str3 = str2;
            }
            str = str3;
            if (jsonObject == null || !jsonObject.has("animations")) {
                return null;
            }
            JsonObject asJsonObject = jsonObject.getAsJsonObject("animations");
            ArrayList arrayList = new ArrayList();
            double d4 = -1.0d;
            double d5 = 0.0d;
            double d6 = 0.0d;
            boolean z2 = false;
            Iterator it = asJsonObject.keySet().iterator();
            double d7 = 0.0d;
            while (it.hasNext()) {
                JsonObject asJsonObject2 = asJsonObject.getAsJsonObject((String) it.next());
                double max = asJsonObject2.has("animation_length") ? Math.max(d7, asJsonObject2.get("animation_length").getAsDouble()) : d7;
                if (asJsonObject2.has("bones")) {
                    JsonObject asJsonObject3 = asJsonObject2.getAsJsonObject("bones");
                    Iterator it2 = asJsonObject3.keySet().iterator();
                    while (it2.hasNext()) {
                        JsonObject asJsonObject4 = asJsonObject3.getAsJsonObject((String) it2.next());
                        String[] strArr2 = {"position", "rotation"};
                        int length2 = strArr2.length;
                        int i2 = 0;
                        while (i2 < length2) {
                            String str5 = strArr2[i2];
                            if (asJsonObject4.has(str5)) {
                                JsonObject asJsonObject5 = asJsonObject4.getAsJsonObject(str5);
                                if (asJsonObject5.has("vector")) {
                                    z = z2;
                                    d = d6;
                                    d2 = d5;
                                } else {
                                    ArrayList arrayList2 = new ArrayList();
                                    LinkedHashMap linkedHashMap = new LinkedHashMap();
                                    for (String str6 : asJsonObject5.keySet()) {
                                        try {
                                            double parseDouble = Double.parseDouble(str6);
                                            arrayList2.add(Double.valueOf(parseDouble));
                                            linkedHashMap.put(Double.valueOf(parseDouble), Double.valueOf(vectorMagnitude(asJsonObject5.get(str6))));
                                        } catch (Throwable th6) {
                                        }
                                    }
                                    if (arrayList2.size() < 3) {
                                        z = z2;
                                        d = d6;
                                        d2 = d5;
                                    } else {
                                        arrayList2.sort((v0, v1) -> {
                                            return Double.compare(v0, v1);
                                        });
                                        ArrayList arrayList3 = new ArrayList();
                                        for (int i3 = 1; i3 < arrayList2.size() - 1; i3++) {
                                            arrayList3.add((Double) arrayList2.get(i3));
                                        }
                                        if (arrayList3.isEmpty()) {
                                            z = z2;
                                            d = d6;
                                            d2 = d5;
                                        } else {
                                            if (!arrayList.isEmpty()) {
                                                arrayList3 = arrayList;
                                            }
                                            if ("position".equals(str5)) {
                                                double d8 = Double.MAX_VALUE;
                                                double d9 = -1.7976931348623157E308d;
                                                double d10 = 0.0d;
                                                d2 = 0.0d;
                                                for (Map.Entry entry : linkedHashMap.entrySet()) {
                                                    if (((Double) entry.getValue()).doubleValue() < d8) {
                                                        d8 = ((Double) entry.getValue()).doubleValue();
                                                        d10 = ((Double) entry.getKey()).doubleValue();
                                                    }
                                                    if (((Double) entry.getValue()).doubleValue() > d9) {
                                                        double doubleValue = ((Double) entry.getValue()).doubleValue();
                                                        d3 = ((Double) entry.getKey()).doubleValue();
                                                        d9 = doubleValue;
                                                    } else {
                                                        d3 = d2;
                                                    }
                                                    d2 = d3;
                                                }
                                                double d11 = d9 - d8;
                                                if (d11 > d4 && d9 > 1.0E-6d) {
                                                    z = true;
                                                    d = d10;
                                                    d4 = d11;
                                                    arrayList = arrayList3;
                                                }
                                            }
                                            z = z2;
                                            d = d6;
                                            d2 = d5;
                                            arrayList = arrayList3;
                                        }
                                    }
                                }
                            } else {
                                z = z2;
                                d = d6;
                                d2 = d5;
                            }
                            i2++;
                            z2 = z;
                            d6 = d;
                            d5 = d2;
                        }
                    }
                    d7 = max;
                } else {
                    d7 = max;
                }
            }
            if (d7 <= 0.0d || arrayList.isEmpty()) {
                return null;
            }
            double d12 = Double.MAX_VALUE;
            Iterator it3 = arrayList.iterator();
            while (it3.hasNext()) {
                double doubleValue2 = ((Double) it3.next()).doubleValue();
                if (doubleValue2 > 1.0E-6d) {
                    double d13 = doubleValue2 - 0.0d;
                    if (d13 > 1.0E-6d && d13 < d12) {
                        d12 = d13;
                    }
                }
            }
            if (d12 == Double.MAX_VALUE || d12 <= 1.0E-6d) {
                return null;
            }
            return new Cycle(d12, z2 ? (d5 / d12) % 1.0d : 0.0d, z2 ? (d6 / d12) % 1.0d : 0.5d, str);
        } catch (Throwable th7) {
            RoarOfLove.LOGGER.debug("[roar_of_love] 动画节奏解析异常", th7);
            return null;
        }
    }

    private static double vectorMagnitude(JsonElement jsonElement) {
        if (jsonElement != null) {
            try {
                if (jsonElement.isJsonObject()) {
                    JsonObject asJsonObject = jsonElement.getAsJsonObject();
                    JsonElement jsonElement2 = null;
                    if (asJsonObject.has("vector")) {
                        jsonElement2 = asJsonObject.get("vector");
                    } else {
                        if (asJsonObject.has("post")) {
                            return vectorMagnitude(asJsonObject.get("post"));
                        }
                        if (asJsonObject.has("pre")) {
                            return vectorMagnitude(asJsonObject.get("pre"));
                        }
                    }
                    if (jsonElement2 == null || !jsonElement2.isJsonArray()) {
                        return 0.0d;
                    }
                    Iterator it = jsonElement2.getAsJsonArray().iterator();
                    double d = 0.0d;
                    while (it.hasNext()) {
                        d += Math.abs(((JsonElement) it.next()).getAsDouble());
                    }
                    return d;
                }
            } catch (Throwable th) {
                return 0.0d;
            }
        }
        return 0.0d;
    }
}

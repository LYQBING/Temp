//
// Decompiled by Jadx - 519ms
//
package com.roaroflove.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.roaroflove.RoarOfLove;
import com.roaroflove.config.RoarOfLoveConfig;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class RoarSubtitleLang {
    public static final int EN = 2;
    public static final int FOLLOW = 0;
    public static final int JA = 3;
    public static final int ZH = 1;
    private static final String[] CODES = {"", "zh_cn", "en_us", "ja_jp"};
    private static final Map<Integer, Map<String, String>> CACHE = new HashMap();

    private RoarSubtitleLang() {
    }

    public static String code(int i) {
        return (i < 0 || i >= CODES.length) ? "" : CODES[i];
    }

    public static String labelKey() {
        return "roar_of_love.ui.sublang_" + Math.max(0, Math.min(3, RoarOfLoveConfig.subLang()));
    }

    public static String lookup(String str) {
        Map<String, String> table;
        int subLang = RoarOfLoveConfig.subLang();
        if (subLang == 0 || (table = table(subLang)) == null) {
            return null;
        }
        return table.get(str);
    }

    private static synchronized Map<String, String> table(int i) {
        Map<String, String> map;
        HashMap hashMap;
        synchronized (RoarSubtitleLang.class) {
            if (CACHE.containsKey(Integer.valueOf(i))) {
                map = CACHE.get(Integer.valueOf(i));
            } else {
                String code = code(i);
                if (code.isEmpty()) {
                    map = null;
                } else {
                    try {
                        InputStream resourceAsStream = RoarOfLove.class.getResourceAsStream("/assets/roar_of_love/lang/" + code + ".json");
                        if (resourceAsStream == null) {
                            hashMap = null;
                        } else {
                            try {
                                JsonObject asJsonObject = JsonParser.parseReader(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8)).getAsJsonObject();
                                HashMap hashMap2 = new HashMap();
                                for (Map.Entry entry : asJsonObject.entrySet()) {
                                    if (((String) entry.getKey()).startsWith("roar_of_love.sub.")) {
                                        hashMap2.put((String) entry.getKey(), ((JsonElement) entry.getValue()).getAsString());
                                    }
                                }
                                hashMap = hashMap2;
                            } finally {
                            }
                        }
                        if (resourceAsStream != null) {
                            try {
                                resourceAsStream.close();
                            } catch (Throwable th) {
                                th = th;
                                RoarOfLove.LOGGER.warn("[roar_of_love] 字幕语言 {} 读取失败：{}", code, th.toString());
                                map = hashMap;
                                CACHE.put(Integer.valueOf(i), map);
                                return map;
                            }
                        }
                        map = hashMap;
                    } catch (Throwable th2) {
                        th = th2;
                        hashMap = null;
                    }
                }
                CACHE.put(Integer.valueOf(i), map);
            }
        }
        return map;
    }
}

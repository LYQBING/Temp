//
// Decompiled by Jadx - 571ms
//
package cn.blockforge.generated.semensyringe;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_5250;
import org.jetbrains.annotations.Nullable;

public final class SyringeLang {
    private static final Map<String, Map<String, String>> TABLES = new ConcurrentHashMap();

    private SyringeLang() {
    }

    public static class_5250 text(String key, Object... args) {
        String lang = SyringeConfig.languageCode();
        if ("auto".equals(lang)) {
            return class_2561.method_43469(key, args);
        }
        String template = lookup("semen_syringe", lang, key);
        if (template == null) {
            return class_2561.method_43469(key, args);
        }
        return class_2561.method_43470(format(template, args));
    }

    public static class_5250 donorText(@Nullable class_2960 donor) {
        if (donor == null) {
            return text("semen_syringe.tooltip.mixed", new Object[0]);
        }
        String key = "entity." + donor.method_12836() + "." + donor.method_12832();
        String lang = SyringeConfig.languageCode();
        if (!"auto".equals(lang)) {
            String name = lookup(donor.method_12836(), lang, key);
            if (name != null) {
                return class_2561.method_43470(name);
            }
            String ours = lookup("semen_syringe", lang, key);
            if (ours != null) {
                return class_2561.method_43470(ours);
            }
        }
        return class_2561.method_43471(key);
    }

    private static String format(String template, Object... args) {
        Object obj;
        StringBuilder out = new StringBuilder(template.length() + 16);
        int next = 0;
        int i = 0;
        while (i < template.length()) {
            char c = template.charAt(i);
            if (c != '%' || i + 1 >= template.length()) {
                out.append(c);
                i++;
            } else {
                char n = template.charAt(i + 1);
                if (n == '%') {
                    out.append('%');
                    i += 2;
                } else if (n == 's') {
                    if (next < args.length) {
                        obj = args[next];
                        next++;
                    } else {
                        obj = null;
                    }
                    out.append(stringify(obj));
                    i += 2;
                } else if (n >= '1' && n <= '9' && i + 3 < template.length() && template.charAt(i + 2) == '$' && template.charAt(i + 3) == 's') {
                    int idx = n - '1';
                    out.append(stringify(idx < args.length ? args[idx] : null));
                    i += 4;
                } else {
                    out.append(c);
                    i++;
                }
            }
        }
        return out.toString();
    }

    private static String stringify(@Nullable Object arg) {
        if (arg == null) {
            return "";
        }
        if (arg instanceof class_2561) {
            class_2561 t = (class_2561) arg;
            return t.getString();
        }
        return String.valueOf(arg);
    }

    private static String lookup(String namespace, String lang, String key) {
        return table(namespace, lang).get(key);
    }

    private static Map<String, String> table(String namespace, String lang) {
        return TABLES.computeIfAbsent(namespace + "|" + lang, ignored -> {
            return readTable(namespace, lang);
        });
    }

    private static Map<String, String> readTable(String namespace, String lang) {
        String path = "/assets/" + namespace + "/lang/" + lang + ".json";
        try {
            InputStream in = SyringeLang.class.getResourceAsStream(path);
            try {
                if (in == null) {
                    Map<String, String> of = Map.of();
                    if (in != null) {
                        in.close();
                        return of;
                    }
                    return of;
                }
                JsonElement root = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                if (!root.isJsonObject()) {
                    Map<String, String> of2 = Map.of();
                    if (in != null) {
                        in.close();
                        return of2;
                    }
                    return of2;
                }
                Map<String, String> map = new ConcurrentHashMap<>();
                for (Map.Entry<String, JsonElement> e : root.getAsJsonObject().entrySet()) {
                    if (e.getValue().isJsonPrimitive()) {
                        map.put(e.getKey(), e.getValue().getAsString());
                    }
                }
                if (in != null) {
                    in.close();
                    return map;
                }
                return map;
            } finally {
            }
        } catch (Exception e2) {
            SemenSyringeMod.LOGGER.warn("读取语言文件 {} 失败", path, e2);
            return Map.of();
        }
    }
}

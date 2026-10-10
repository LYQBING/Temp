//
// Decompiled by Jadx - 714ms
//
package cn.blockforge.generated.semensyringe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Locale;
import net.fabricmc.loader.api.FabricLoader;

public final class SyringeConfig {
    public static final int DEFAULT_INJECTION_AMOUNT = 100;
    public static final String LANG_AUTO = "auto";
    public static final String LANG_EN = "en_us";
    public static final String LANG_ZH = "zh_cn";
    public static final int MAX_INJECTION_AMOUNT = 1000;
    public static final int MIN_INJECTION_AMOUNT = 1;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static SyringeConfig instance = new SyringeConfig();
    public boolean injectionSubtitles = true;
    public String language = LANG_EN;
    public int injectionAmount = 100;

    public static void load() {
        Path file = configFile();
        try {
            if (Files.isRegularFile(file, new LinkOption[0])) {
                JsonObject json = (JsonObject) GSON.fromJson(Files.readString(file), JsonObject.class);
                SyringeConfig cfg = new SyringeConfig();
                if (json != null) {
                    if (json.has("injectionSubtitles")) {
                        cfg.injectionSubtitles = json.get("injectionSubtitles").getAsBoolean();
                    }
                    if (json.has("language")) {
                        cfg.language = normalizeLanguage(json.get("language").getAsString());
                    }
                    if (json.has("injectionAmount")) {
                        cfg.injectionAmount = clampAmount(json.get("injectionAmount").getAsInt());
                    }
                }
                instance = cfg;
                return;
            }
            instance.save();
        } catch (Exception e) {
            SemenSyringeMod.LOGGER.warn("读取 config/semen_syringe.json 失败，使用默认配置", e);
            instance = new SyringeConfig();
        }
    }

    public static SyringeConfig get() {
        return instance;
    }

    public static String languageCode() {
        return instance.language;
    }

    public static boolean subtitlesEnabled() {
        return instance.injectionSubtitles;
    }

    public static int doseFor(int availableMl) {
        return Math.max(0, Math.min(clampAmount(instance.injectionAmount), availableMl));
    }

    public static int clampAmount(int ml) {
        return Math.max(1, Math.min(MAX_INJECTION_AMOUNT, ml));
    }

    public String nextLanguage() {
        String str;
        String str2 = this.language;
        char c = 65535;
        switch (str2.hashCode()) {
            case 96647668:
                if (str2.equals(LANG_EN)) {
                    c = 0;
                    break;
                }
                break;
            case 115862300:
                if (str2.equals(LANG_ZH)) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                str = LANG_ZH;
                break;
            case MIN_INJECTION_AMOUNT:
                str = LANG_AUTO;
                break;
            default:
                str = LANG_EN;
                break;
        }
        this.language = str;
        save();
        return this.language;
    }

    public void save() {
        try {
            Path file = configFile();
            Files.createDirectories(file.getParent(), new FileAttribute[0]);
            JsonObject json = new JsonObject();
            json.addProperty("injectionSubtitles", Boolean.valueOf(this.injectionSubtitles));
            json.addProperty("language", this.language);
            json.addProperty("injectionAmount", Integer.valueOf(clampAmount(this.injectionAmount)));
            Files.writeString(file, GSON.toJson(json), new OpenOption[0]);
        } catch (Exception e) {
            SemenSyringeMod.LOGGER.warn("写入 config/semen_syringe.json 失败", e);
        }
    }

    private static String normalizeLanguage(String raw) {
        String v = raw == null ? "" : raw.trim().toLowerCase(Locale.ROOT);
        char c = 65535;
        switch (v.hashCode()) {
            case -1603757456:
                if (v.equals("english")) {
                    c = 2;
                    break;
                }
                break;
            case 3241:
                if (v.equals("en")) {
                    c = 1;
                    break;
                }
                break;
            case 3886:
                if (v.equals("zh")) {
                    c = 4;
                    break;
                }
                break;
            case 96647668:
                if (v.equals(LANG_EN)) {
                    c = 0;
                    break;
                }
                break;
            case 115862300:
                if (v.equals(LANG_ZH)) {
                    c = 3;
                    break;
                }
                break;
            case 746330349:
                if (v.equals("chinese")) {
                    c = 6;
                    break;
                }
                break;
            case 867355701:
                if (v.equals("zh_cn_simple")) {
                    c = 5;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case MIN_INJECTION_AMOUNT:
            case 2:
                return LANG_EN;
            case 3:
            case 4:
            case 5:
            case 6:
                return LANG_ZH;
            default:
                return LANG_AUTO;
        }
    }

    private static Path configFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("semen_syringe.json");
    }
}

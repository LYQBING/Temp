//
// Decompiled by Jadx - 643ms
//
package com.roaroflove.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roaroflove.RoarOfLove;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;

public class RoarOfLoveConfig {
    private static Path file;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static RoarOfLoveConfig$Data data = new RoarOfLoveConfig$Data();
    private static long lastLoadedAt = -1;
    private static long cachedMtime = -1;

    private RoarOfLoveConfig() {
    }

    public static synchronized void load() {
        synchronized (RoarOfLoveConfig.class) {
            try {
                file = FabricLoader.getInstance().getConfigDir().resolve("roar_of_love.json");
                if (Files.exists(file, new LinkOption[0])) {
                    BufferedReader newBufferedReader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
                    try {
                        RoarOfLoveConfig$Data data2 = (RoarOfLoveConfig$Data) GSON.fromJson(newBufferedReader, RoarOfLoveConfig$Data.class);
                        if (data2 != null) {
                            data = data2;
                        }
                        if (newBufferedReader != null) {
                            newBufferedReader.close();
                        }
                    } catch (Throwable th) {
                        if (newBufferedReader != null) {
                            try {
                                newBufferedReader.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }

    public static boolean isTooltips() {
        return data.tooltips;
    }

    public static void setTooltips(boolean enabled) {
        data.tooltips = enabled;
        save();
    }

    public static boolean isPushEffect() {
        return data.pushEffect;
    }

    public static void setPushEffect(boolean enabled) {
        data.pushEffect = enabled;
        save();
    }

    public static boolean isHeatBar() {
        return data.heatBar;
    }

    public static void setHeatBar(boolean enabled) {
        data.heatBar = enabled;
        save();
    }

    public static boolean isOverload() {
        return data.overload;
    }

    public static void setOverload(boolean enabled) {
        data.overload = enabled;
        save();
    }

    public static boolean isDirSub() {
        return data.dirSub;
    }

    public static void setDirSub(boolean enabled) {
        data.dirSub = enabled;
        save();
    }
                        throw th;
                    }
                } else {
                    save();
                }
            } catch (Exception e) {
                RoarOfLove.LOGGER.error("[roar_of_love] 配置文件读取失败，使用默认值", e);
            }
        }
    }

    public static synchronized void save() {
        synchronized (RoarOfLoveConfig.class) {
            try {
                if (file == null) {
                    file = FabricLoader.getInstance().getConfigDir().resolve("roar_of_love.json");
                }
                Files.createDirectories(file.getParent(), new FileAttribute[0]);
                BufferedWriter newBufferedWriter = Files.newBufferedWriter(file, StandardCharsets.UTF_8, new OpenOption[0]);
                try {
                    GSON.toJson(data, newBufferedWriter);
                    if (newBufferedWriter != null) {
                        newBufferedWriter.close();
                    }
                } catch (Throwable th) {
                    if (newBufferedWriter != null) {
                        try {
                            newBufferedWriter.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (IOException e) {
                RoarOfLove.LOGGER.error("[roar_of_love] 配置文件写入失败", e);
            }
        }
    }

    public static synchronized void checkReload(long j) {
        synchronized (RoarOfLoveConfig.class) {
            if (j < lastLoadedAt || j - lastLoadedAt > 200) {
                lastLoadedAt = j;
            }
            try {
                if (Files.exists(file, new LinkOption[0])) {
                    long millis = Files.getLastModifiedTime(file, new LinkOption[0]).toMillis();
                    if (millis != cachedMtime) {
                        cachedMtime = millis;
                        load();
                        RoarOfLove.LOGGER.info("[roar_of_love] 检测到配置文件变化，已重载");
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    public static RoarOfLoveConfig$Data get() {
        return data;
    }

    public static boolean isPinkFilter() {
        return data.pinkFilter;
    }

    public static void setPinkFilter(boolean z) {
        data.pinkFilter = z;
        save();
    }

    public static boolean isBlackFilter() {
        return data.blackFilter;
    }

    public static void setBlackFilter(boolean z) {
        data.blackFilter = z;
        save();
    }

    public static boolean isFlashEffect() {
        return data.flashEffect;
    }

    public static void setFlashEffect(boolean z) {
        data.flashEffect = z;
        save();
    }

    public static boolean isShakeEffect() {
        return data.shakeEffect;
    }

    public static void setShakeEffect(boolean z) {
        data.shakeEffect = z;
        save();
    }

    public static boolean isMuteOtherHurt() {
        return data.muteOtherHurt;
    }

    public static void setMuteOtherHurt(boolean z) {
        data.muteOtherHurt = z;
        save();
    }

    public static boolean isSubLust() {
        return data.subLust;
    }

    public static void setSubLust(boolean z) {
        data.subLust = z;
        save();
    }

    public static boolean isSubAnim() {
        return data.subAnim;
    }

    public static void setSubAnim(boolean z) {
        data.subAnim = z;
        save();
    }

    public static boolean isSubPeak() {
        return data.subPeak;
    }

    public static void setSubPeak(boolean z) {
        data.subPeak = z;
        save();
    }

    public static boolean isSubSelf() {
        return data.subSelf;
    }

    public static void setSubSelf(boolean z) {
        data.subSelf = z;
        save();
    }

    public static boolean isSubOral() {
        return data.subOral;
    }

    public static void setSubOral(boolean z) {
        data.subOral = z;
        save();
    }

    public static int subForm() {
        return Math.max(0, Math.min(2, data.subForm));
    }

    public static void setSubForm(int i) {
        data.subForm = Math.max(0, Math.min(2, i));
        save();
    }

    public static int subtitleGender() {
        return Math.max(0, Math.min(2, data.subtitleGender));
    }

    public static void setSubtitleGender(int i) {
        data.subtitleGender = Math.max(0, Math.min(2, i));
        save();
    }

    public static String subCustomFile() {
        return data.subCustomFile == null ? "" : data.subCustomFile;
    }

    public static void setSubCustomFile(String str) {
        RoarOfLoveConfig$Data data2 = data;
        if (str == null) {
            str = "";
        }
        data2.subCustomFile = str;
        save();
    }

    public static boolean isSoundEnabled(String str) {
        return (str == null || data.disabledSounds == null || data.disabledSounds.contains(str)) ? false : true;
    }

    public static void setSoundEnabled(String str, boolean z) {
        if (str != null) {
            if (data.disabledSounds == null) {
                data.disabledSounds = new ArrayList();
            }
            if (z) {
                data.disabledSounds.remove(str);
            } else if (!data.disabledSounds.contains(str)) {
                data.disabledSounds.add(str);
            }
            save();
        }
    }

    public static int soundVolumeSteps(String str) {
        if (str == null || data.soundVolume == null) {
            return 10;
        }
        Integer volume = data.soundVolume.get(str);
        return volume == null ? 10 : Math.max(0, Math.min(20, volume.intValue()));
    }

    public static void setSoundVolumeSteps(String str, int steps) {
        if (str != null) {
            if (data.soundVolume == null) {
                data.soundVolume = new HashMap();
            }
            data.soundVolume.put(str, Integer.valueOf(Math.max(0, Math.min(20, steps))));
            save();
        }
    }

    public static boolean isBreathEffect() {
        return data.breathEffect;
    }

    public static void setBreathEffect(boolean z) {
        data.breathEffect = z;
        save();
    }

    public static boolean isPulseEffect() {
        return data.pulseEffect;
    }

    public static void setPulseEffect(boolean z) {
        data.pulseEffect = z;
        save();
    }

    public static boolean isRefluxEffect() {
        return data.refluxEffect;
    }

    public static void setRefluxEffect(boolean z) {
        data.refluxEffect = z;
        save();
    }

    public static boolean isLustBar() {
        return data.lustBar;
    }

    public static void setLustBar(boolean z) {
        data.lustBar = z;
        save();
    }

    public static int pulseStyle() {
        return Math.max(0, Math.min(3, data.pulseStyle));
    }

    public static void setPulseStyle(int i) {
        data.pulseStyle = Math.max(0, Math.min(3, i));
        save();
    }

    private static int clampPercent(int i) {
        return Math.max(0, Math.min(100, i));
    }

    public static int lustBarX() {
        return clampPercent(data.lustBarX);
    }

    public static int lustBarY() {
        return clampPercent(data.lustBarY);
    }

    public static void setLustBarPos(int i, int i2) {
        data.lustBarX = clampPercent(i);
        data.lustBarY = clampPercent(i2);
        save();
    }

    public static int subtitleX() {
        return clampPercent(data.subtitleX);
    }

    public static int subtitleY() {
        return clampPercent(data.subtitleY);
    }

    public static void setSubtitlePos(int i, int i2) {
        data.subtitleX = clampPercent(i);
        data.subtitleY = clampPercent(i2);
        save();
    }

    public static float immersionFactor(int i) {
        return (clampStrength(i) - 1) / 9.0f;
    }

    public static int breathStrength() {
        return clampStrength(data.breathStrength);
    }

    public static void setBreathStrength(int i) {
        data.breathStrength = clampStrength(i);
        save();
    }

    public static int pulseStrength() {
        return clampStrength(data.pulseStrength);
    }

    public static void setPulseStrength(int i) {
        data.pulseStrength = clampStrength(i);
        save();
    }

    public static int refluxStrength() {
        return clampStrength(data.refluxStrength);
    }

    public static void setRefluxStrength(int i) {
        data.refluxStrength = clampStrength(i);
        save();
    }

    public static int lustBarStrength() {
        return clampStrength(data.lustBarStrength);
    }

    public static void setLustBarStrength(int i) {
        data.lustBarStrength = clampStrength(i);
        save();
    }

    public static boolean isEcgEffect() {
        return data.ecgEffect;
    }

    public static void setEcgEffect(boolean z) {
        data.ecgEffect = z;
        save();
    }

    public static int ecgStrength() {
        return clampStrength(data.ecgStrength);
    }

    public static void setEcgStrength(int i) {
        data.ecgStrength = clampStrength(i);
        save();
    }

    public static int ecgX() {
        return clampPercent(data.ecgX);
    }

    public static int ecgY() {
        return clampPercent(data.ecgY);
    }

    public static void setEcgPos(int i, int i2) {
        data.ecgX = clampPercent(i);
        data.ecgY = clampPercent(i2);
        save();
    }

    public static boolean isPregnancyHint() {
        return data.pregnancyHint;
    }

    public static void setPregnancyHint(boolean z) {
        data.pregnancyHint = z;
        save();
    }

    public static int pregX() {
        return clampPercent(data.pregX);
    }

    public static int pregY() {
        return clampPercent(data.pregY);
    }

    public static void setPregPos(int i, int i2) {
        data.pregX = clampPercent(i);
        data.pregY = clampPercent(i2);
        save();
    }

    public static boolean isDistantFade() {
        return data.distantFade;
    }

    public static void setDistantFade(boolean z) {
        data.distantFade = z;
        save();
    }

    public static boolean isOcclusion() {
        return data.occlusion;
    }

    public static void setOcclusion(boolean z) {
        data.occlusion = z;
        save();
    }

    public static boolean isAlignMoans() {
        return data.alignMoans;
    }

    public static void setAlignMoans(boolean z) {
        data.alignMoans = z;
        save();
    }

    public static boolean isPostPeakDip() {
        return data.postPeakDip;
    }

    public static void setPostPeakDip(boolean z) {
        data.postPeakDip = z;
        save();
    }

    public static boolean isAmbienceSound() {
        return data.ambienceSound;
    }

    public static void setAmbienceSound(boolean z) {
        data.ambienceSound = z;
        save();
    }

    public static int ambienceStrength() {
        return clampStrength(data.ambienceStrength);
    }

    public static void setAmbienceStrength(int i) {
        data.ambienceStrength = clampStrength(i);
        save();
    }

    public static boolean isAdaptHud() {
        return data.adaptHud;
    }

    public static void setAdaptHud(boolean z) {
        data.adaptHud = z;
        save();
    }

    public static boolean isHrv() {
        return data.hrv;
    }

    public static void setHrv(boolean z) {
        data.hrv = z;
        save();
    }

    public static boolean isBreathHold() {
        return data.breathHold;
    }

    public static void setBreathHold(boolean z) {
        data.breathHold = z;
        save();
    }

    public static boolean isDirector() {
        return data.director;
    }

    public static void setDirector(boolean z) {
        data.director = z;
        save();
    }

    public static boolean isFovBreath() {
        return data.fovBreath;
    }

    public static void setFovBreath(boolean z) {
        data.fovBreath = z;
        save();
    }

    public static boolean isEcgTail() {
        return data.ecgTail;
    }

    public static void setEcgTail(boolean z) {
        data.ecgTail = z;
        save();
    }

    public static boolean isTypewriter() {
        return data.typewriter;
    }

    public static void setTypewriter(boolean z) {
        data.typewriter = z;
        save();
    }

    public static int subLang() {
        return Math.max(0, Math.min(3, data.subLang));
    }

    public static void setSubLang(int i) {
        data.subLang = Math.max(0, Math.min(3, i));
        save();
    }

    public static int palette() {
        return Math.max(0, Math.min(3, data.palette));
    }

    public static void setPalette(int i) {
        data.palette = Math.max(0, Math.min(3, i));
        save();
    }

    private static int clampStrength(int i) {
        return Math.max(1, Math.min(10, i));
    }

    public static float strengthScale(int i) {
        return 0.3f + ((clampStrength(i) - 1) * 0.175f);
    }

    public static int pinkStrength() {
        return clampStrength(data.pinkStrength);
    }

    public static void setPinkStrength(int i) {
        data.pinkStrength = clampStrength(i);
        save();
    }

    public static int blackStrength() {
        return clampStrength(data.blackStrength);
    }

    public static void setBlackStrength(int i) {
        data.blackStrength = clampStrength(i);
        save();
    }

    public static int flashStrength() {
        return clampStrength(data.flashStrength);
    }

    public static void setFlashStrength(int i) {
        data.flashStrength = clampStrength(i);
        save();
    }

    public static boolean isOtherMobCalls() {
        return data.otherMobCalls;
    }

    public static void setOtherMobCalls(boolean z) {
        data.otherMobCalls = z;
        save();
    }

    public static boolean isGuideShown() {
        return data.guideShown;
    }

    public static void setGuideShown(boolean z) {
        data.guideShown = z;
        save();
    }

    public static boolean isFilterViewed() {
        return data.filterViewed;
    }

    public static void setFilterViewed(boolean z) {
        data.filterViewed = z;
        save();
    }

    public static int blackSeconds() {
        return Math.max(5, data.blackSeconds);
    }

    public static void setBlackSeconds(int i) {
        data.blackSeconds = Math.max(5, i);
        save();
    }

    public static boolean isEnabled() {
        return data.enabled;
    }

    public static boolean isRequirePlayerActor() {
        return data.requirePlayerActor;
    }

    public static int intervalTicks() {
        return Math.max(1, (int) Math.round(data.intervalSeconds * 20.0d));
    }

    public static float volume() {
        return data.volume;
    }

    public static float pitch() {
        return data.pitch;
    }

    public static boolean isRoarEnabled() {
        return data.roarEnabled;
    }

    public static boolean isZipEnabled() {
        return data.zipEnabled;
    }

    public static List<String> blockedAnimations() {
        return data.blockedAnimations;
    }

    public static boolean isBlocked(String str) {
        if (str == null || data.blockedAnimations.isEmpty()) {
            return false;
        }
        Iterator it = data.blockedAnimations.iterator();
        while (it.hasNext()) {
            if (((String) it.next()).equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isZipDisabled(String str) {
        return !isZipEnabled(str);
    }

    public static boolean isZipEnabled(String str) {
        if (data.enabledZips == null) {
            return data.disabledZips == null || !data.disabledZips.contains(str);
        }
        return data.enabledZips.contains(str);
    }

    public static boolean needsZipMigration() {
        return data.enabledZips == null;
    }

    public static synchronized void migrateZipAllowlist(List<String> list) {
        synchronized (RoarOfLoveConfig.class) {
            if (data.enabledZips == null) {
                ArrayList arrayList = new ArrayList();
                if (list != null) {
                    for (String str : list) {
                        if (data.disabledZips == null || !data.disabledZips.contains(str)) {
                            arrayList.add(str);
                        }
                    }
                }
                data.enabledZips = arrayList;
                data.disabledZips = new ArrayList();
                save();
            }
        }
    }

    public static void setZipEnabled(String str, boolean z) {
        if (data.enabledZips == null) {
            if (data.disabledZips == null) {
                data.disabledZips = new ArrayList();
            }
            if (z) {
                data.disabledZips.remove(str);
            } else if (!data.disabledZips.contains(str)) {
                data.disabledZips.add(str);
            }
            save();
            return;
        }
        if (z) {
            if (!data.enabledZips.contains(str)) {
                data.enabledZips.add(str);
            }
        } else {
            data.enabledZips.remove(str);
        }
        save();
    }
}

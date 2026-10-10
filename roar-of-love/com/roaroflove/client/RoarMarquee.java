//
// Decompiled by Jadx - 628ms
//
package com.roaroflove.client;

import com.roaroflove.RoarOfLove;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import net.minecraft.class_332;

public final class RoarMarquee {
    private static final String EXPECTED_SHA256 = "efccc2e76af8c27f0934a12f2739c8ecfa386638e479c7b08a04c08d4e126965";
    private static final String KEY = "RoL-FREE-NEVER-SELL";
    private static final String LOCK_TEXT = "【爱之咆哮】公告文件已被删除或修改，模组已停止运行，请重新下载完整的模组文件。";
    private static final long RECHECK_MS = 30000;
    private static final String RESOURCE = "/assets/roar_of_love/marquee.txt";
    public static final boolean SHOW_IN_SETTINGS = true;
    private static volatile boolean locked = false;
    private static volatile String text = "";
    private static long lastCheckMs = 0;

    private RoarMarquee() {
    }

    public static boolean isLocked() {
        return locked;
    }

    public static boolean verify() {
        try {
            InputStream resourceAsStream = RoarOfLove.class.getResourceAsStream(RESOURCE);
            try {
                if (resourceAsStream == null) {
                    locked = true;
                    if (resourceAsStream != null) {
                        resourceAsStream.close();
                    }
                    return false;
                }
                byte[] readAllBytes = resourceAsStream.readAllBytes();
                if (!EXPECTED_SHA256.equalsIgnoreCase(hex(MessageDigest.getInstance("SHA-256").digest(readAllBytes)))) {
                    locked = true;
                    if (resourceAsStream != null) {
                        resourceAsStream.close();
                    }
                    return false;
                }
                String str = new String(readAllBytes, StandardCharsets.UTF_8);
                if (!str.contains(KEY)) {
                    locked = true;
                    if (resourceAsStream != null) {
                        resourceAsStream.close();
                    }
                    return false;
                }
                String str2 = "";
                String[] split = str.split("\r?\n");
                for (String str3 : split) {
                    int indexOf = str3.indexOf(65306);
                    if (indexOf > 0 && str3.startsWith("文本")) {
                        str2 = str3.substring(indexOf + 1).trim();
                    }
                }
                if (str2.isEmpty()) {
                    locked = true;
                    if (resourceAsStream != null) {
                        resourceAsStream.close();
                    }
                    return false;
                }
                text = str2;
                locked = false;
                if (resourceAsStream != null) {
                    resourceAsStream.close();
                }
                return true;
            } finally {
            }
        } catch (Throwable th) {
            locked = true;
            return false;
        }
    }

    public static void tick() {
        long currentTimeMillis = System.currentTimeMillis();
        if (lastCheckMs == 0) {
            lastCheckMs = currentTimeMillis;
            return;
        }
        if (currentTimeMillis - lastCheckMs >= RECHECK_MS) {
            lastCheckMs = currentTimeMillis;
            if (!locked && !verify()) {
                RoarOfLove.LOGGER.error("[roar_of_love] 公告文件校验失败，模组已锁定");
            }
        }
    }

    private static void fail(String str) {
        RoarOfLove.LOGGER.error("[roar_of_love] {}，判定绘制代码已被移除或关闭，模组停止运行", str);
        throw new IllegalStateException("Roar of Love: the top marquee renderer was removed or disabled (" + str + ") - the mod refuses to run.");
    }

    private static String hex(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            sb.append(Character.forDigit((b >> 4) & 15, 16));
            sb.append(Character.forDigit(b & 15, 16));
        }
        return sb.toString();
    }

    public static void render(class_332 class_332Var, int i, int i2) {
    }

    public static String watermarkText() {
        if (locked) {
            return LOCK_TEXT;
        }
        return (text == null || text.isEmpty()) ? "" : text;
    }

    public static boolean isLockedNow() {
        return locked;
    }
}

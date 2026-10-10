//
// Decompiled by Jadx - 764ms
//
package com.roaroflove.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class AudioPaths {
    public static final String FOLDER_NAME = "RoarOfLove_Audio";
    public static final String PACK_NAME = "RoarOfLove-Audio";
    public static final String TEMPLATE_ZIP = "RoarOfLove-Audio-Template.zip";

    private AudioPaths() {
    }

    public static Path gameDir() {
        return FabricLoader.getInstance().getGameDir();
    }

    public static Path audioFolder() {
        return gameDir().resolve(FOLDER_NAME);
    }

    public static Path resourcePacksDir() {
        return gameDir().resolve("resourcepacks");
    }

    public static Path packZip() {
        return resourcePacksDir().resolve("RoarOfLove-Audio.zip");
    }

    public static Path legacyPackFolder() {
        return resourcePacksDir().resolve(PACK_NAME);
    }

    public static String packProfileId(String str) {
        return "file/" + str;
    }

    public static Path packCacheDir() {
        return audioFolder().resolve("_pack_cache");
    }

    public static byte[] readOwnResource(String str) throws IOException {
        InputStream resourceAsStream = AudioPaths.class.getClassLoader().getResourceAsStream(str);
        try {
            if (resourceAsStream == null) {
                throw new IOException("missing resource " + str);
            }
            byte[] readAllBytes = resourceAsStream.readAllBytes();
            if (resourceAsStream != null) {
                resourceAsStream.close();
            }
            return readAllBytes;
        } catch (Throwable th) {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }
}

//
// Decompiled by Jadx - 580ms
//
package com.roaroflove.client;

import com.roaroflove.RoarOfLove;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import net.fabricmc.loader.api.FabricLoader;

public final class RoarBridge {
    private static final String BRIDGE_MOD_ID = "mca_nature_bridge";
    private static final String IDENT_FILE = "mca_nature_bridge.ident.json";
    private static final String SUPPRESS_FILE = "mca_nature_bridge.notice.suppress.json";

    private RoarBridge() {
    }

    public static void init() {
        try {
            FabricLoader fabricLoader = FabricLoader.getInstance();
            Path gameDir = fabricLoader.getGameDir();
            Path configDir = fabricLoader.getConfigDir();
            if (fabricLoader.isModLoaded(BRIDGE_MOD_ID) || Files.exists(gameDir.resolve(IDENT_FILE), new LinkOption[0]) || Files.exists(configDir.resolve(IDENT_FILE), new LinkOption[0])) {
                Path resolve = configDir.resolve(SUPPRESS_FILE);
                if (Files.exists(resolve, new LinkOption[0])) {
                    String str = new String(Files.readAllBytes(resolve), StandardCharsets.UTF_8);
                    if (str.contains("roar_of_love") && str.contains("true")) {
                        return;
                    }
                }
                Files.createDirectories(configDir, new FileAttribute[0]);
                Files.write(resolve, "{\n  \"suppress\": true,\n  \"by\": \"roar_of_love\",\n  \"untilMillis\": 0\n}\n".getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
                RoarOfLove.LOGGER.info("[roar_of_love] 检测到自然物语，已写入暂停其滚动字幕的请求：{}", resolve);
            }
        } catch (Throwable th) {
            RoarOfLove.LOGGER.warn("[roar_of_love] 写入自然物语字幕暂停请求失败：{}", th.toString());
        }
    }
}

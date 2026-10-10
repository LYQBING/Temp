//
// Decompiled by Jadx - 524ms
//
package com.roaroflove;

import com.roaroflove.config.RoarOfLoveConfig;
import com.roaroflove.server.RoarEngine;
import com.roaroflove.sound.RoLSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoarOfLove implements ModInitializer {
    public static final String AUTHOR = "哔哩哔哩：佳佳不好";
    public static final String MOD_ID = "roar_of_love";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public void onInitialize() {
        RoarOfLoveConfig.load();
        RoLSounds.init();
        RoarEngine.init();
        LOGGER.info("[roar_of_love] Need of Nature - The Roar of Love initialized (author: {})", AUTHOR);
    }

    public static String version() {
        try {
            return (String) FabricLoader.getInstance().getModContainer(MOD_ID).map(modContainer -> {
                return modContainer.getMetadata().getVersion().getFriendlyString();
            }).orElse("?");
        } catch (Exception e) {
            return "?";
        }
    }
}

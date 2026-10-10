//
// Decompiled by Jadx - 474ms
//
package cn.blockforge.generated.semensyringe;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public final class SyringeModMenu implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SyringeConfigScreen::new;
    }
}

//
// Decompiled by Jadx - 479ms
//
package cn.blockforge.generated.semensyringe;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.class_1269;
import net.minecraft.class_1761;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_5321;
import net.minecraft.class_7923;
import net.minecraft.class_7924;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SemenSyringeMod implements ModInitializer {
    public static final String MOD_ID = "semen_syringe";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final class_5321<class_1792> SYRINGE_KEY = class_5321.method_29179(class_7924.field_41197, class_2960.method_60655(MOD_ID, "syringe"));
    public static final class_1792 SYRINGE = new SyringeItem(new class_1792.class_1793().method_63686(SYRINGE_KEY).method_7889(1));
    private static final class_5321<class_1761> NON_ITEM_GROUP = class_5321.method_29179(class_7924.field_44688, class_2960.method_60655("needsofnature", "main"));

    public static class_2960 id(String path) {
        return class_2960.method_60655(MOD_ID, path);
    }

    public void onInitialize() {
        SyringeConfig.load();
        SyringeContents.type();
        class_2378.method_39197(class_7923.field_41178, SYRINGE_KEY, SYRINGE);
        ItemGroupEvents.modifyEntriesEvent(NON_ITEM_GROUP).register(entries -> {
            entries.method_45421(SYRINGE);
        });
        LOGGER.debug("[semen_syringe] 灌装配方类型已注册：{}", FillSyringeRecipe.SERIALIZER);
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!(player instanceof class_3222)) {
                return class_1269.field_5811;
            }
            class_3222 serverPlayer = (class_3222) player;
            class_1799 held = serverPlayer.method_5998(hand);
            if (!(held.method_7909() instanceof SyringeItem)) {
                return class_1269.field_5811;
            }
            if (entity instanceof class_3222) {
                class_3222 target = (class_3222) entity;
                if (target == serverPlayer) {
                    return class_1269.field_5811;
                }
                if (SyringeContents.read(held) == null) {
                    return class_1269.field_5811;
                }
                if (serverPlayer.method_5715()) {
                    return SyringeLogic.inject(serverPlayer, target, hand) ? class_1269.field_5812 : class_1269.field_5814;
                }
                SyringeLogic.feedback(serverPlayer, SyringeLang.text("semen_syringe.msg.need_sneak", new Object[0]));
                return class_1269.field_5814;
            }
            return class_1269.field_5811;
        });
        LOGGER.info("[semen_syringe] 注射器已注册，Needs of Nature 桥接就绪");
    }
}

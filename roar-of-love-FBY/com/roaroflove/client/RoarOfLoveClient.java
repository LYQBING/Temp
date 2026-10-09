//
// Decompiled by Jadx - 828ms
//
package com.roaroflove.client;

import com.nonid.api.animation.NonAnimationApi;
import com.nonid.api.animation.NonAnimationDefinition;
import com.nonid.api.animation.NonAnimationEvents;
import com.nonid.api.animation.NonAnimationStage;
import com.nonid.api.client.NonAnimationSoundEvents;
import com.nonid.client.NonSoundCues;
import com.nonid.data.NonPeakStages;
import com.roaroflove.RoarOfLove;
import com.roaroflove.client.audio.AudioPackLoader;
import com.roaroflove.config.RoarOfLoveConfig;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.class_1309;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675;
import net.minecraft.class_437;

public class RoarOfLoveClient implements ClientModInitializer {
    private static final int KEY_F10 = 299;
    private static final int KEY_F7 = 296;
    private static final int KEY_F8 = 297;
    private static final int KEY_F9 = 298;
    private static final int KEY_L = 76;
    private static class_304 openSettingsKey;
    private static class_304 openSettingsKeyL;
    private static class_304 testFlashKey;
    private static class_304 testHeartsKey;
    private static class_304 testShakeKey;
    private static int guideDelayTicks = -1;
    private static int stageLogs = 0;

    private static String flatten(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String lowerCase = str.toLowerCase(Locale.ROOT);
        for (int i = 0; i < lowerCase.length(); i++) {
            char charAt = lowerCase.charAt(i);
            if ((charAt >= 'a' && charAt <= 'z') || (charAt >= '0' && charAt <= '9')) {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }

    private static boolean isPeakCue(String str) {
        String flatten = flatten(str);
        return flatten.contains("shotin") || flatten.contains("creampie") || flatten.contains("inseminate") || flatten.contains("semenin") || flatten.contains("cumshot");
    }

    private static int indexInDefinition(class_2960 class_2960Var, NonAnimationStage nonAnimationStage) {
        NonAnimationDefinition definition = null;
        try {
            definition = NonAnimationApi.getDefinition(class_2960Var);
        } catch (Throwable th) {
            RoarOfLove.LOGGER.debug("[roar_of_love] 定义阶段序号解析异常", th);
        }
        if (definition == null || definition.stages() == null) {
            return -1;
        }
        List stages = definition.stages();
        for (int i = 0; i < stages.size(); i++) {
            NonAnimationStage nonAnimationStage2 = (NonAnimationStage) stages.get(i);
            if (nonAnimationStage2 == nonAnimationStage || nonAnimationStage2.equals(nonAnimationStage)) {
                return i + 1;
            }
        }
        for (int i2 = 0; i2 < stages.size(); i2++) {
            NonAnimationStage nonAnimationStage3 = (NonAnimationStage) stages.get(i2);
            if (nonAnimationStage3.animationId() != null && nonAnimationStage3.animationId().equals(nonAnimationStage.animationId())) {
                return i2 + 1;
            }
        }
        return -1;
    }

    private static int stageNumberOf(NonAnimationStage nonAnimationStage) {
        if (nonAnimationStage == null) {
            return -1;
        }
        Integer stageNumberFromId = NonPeakStages.stageNumberFromId(nonAnimationStage.playbackAnimationId());
        if (stageNumberFromId != null) {
            return stageNumberFromId.intValue();
        }
        Integer stageNumberFromId2 = NonPeakStages.stageNumberFromId(nonAnimationStage.animationId());
        if (stageNumberFromId2 != null) {
            return stageNumberFromId2.intValue();
        }
        return -1;
    }

    private static String soundCategoryOf(String str) {
        if (str == null) {
            return null;
        }
        String flatten = flatten(str);
        if (flatten.startsWith("impactdry")) {
            return "dryimpacts";
        }
        if (flatten.startsWith("impactwet")) {
            return "wetimpacts";
        }
        if (flatten.startsWith("motion")) {
            return "motions";
        }
        if (flatten.startsWith("retract")) {
            return "retract";
        }
        if (flatten.startsWith("shot")) {
            return "shots";
        }
        if (flatten.startsWith("wet")) {
            return "wet";
        }
        return null;
    }

    private static boolean involvesLocalPlayer(List<UUID> list) {
        class_310 method_1551 = class_310.method_1551();
        if (method_1551 == null || method_1551.field_1724 == null || list == null || list.isEmpty()) {
            return false;
        }
        return list.contains(method_1551.field_1724.method_5667());
    }

    private static boolean isLocalAnchor(class_1309 class_1309Var) {
        class_310 method_1551 = class_310.method_1551();
        if (class_1309Var == null || method_1551 == null || method_1551.field_1724 == null) {
            return false;
        }
        return class_1309Var.method_5667().equals(method_1551.field_1724.method_5667());
    }

    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(class_310Var -> {
            AudioPackLoader.onClientStarted();
        });
        ClientPlayConnectionEvents.JOIN.register((class_634Var, packetSender, class_310Var2) -> {
            AudioPackLoader.onWorldJoin();
            try {
                RoarSubtitles.reloadSavedCustom();
            } catch (Throwable th) {
                RoarOfLove.LOGGER.debug("[roar_of_love] 字幕文件自动加载失败", th);
            }
            if (!RoarOfLoveConfig.isGuideShown()) {
                guideDelayTicks = 40;
                RoarOfLove.LOGGER.info("[roar_of_love] 首次进入存档，稍后显示引导窗口");
            }
        });
        NonAnimationSoundEvents.RESOLVE.register(soundContext -> {
            String effect = null;
            String soundCategoryOf = null;
            try {
                effect = soundContext.effect();
                soundCategoryOf = soundCategoryOf(effect);
            } catch (Throwable th) {
                RoarOfLove.LOGGER.debug("[roar_of_love] 音效线索处理异常", th);
            }
            if (soundCategoryOf != null && !RoarOfLoveConfig.isSoundEnabled(soundCategoryOf)) {
                return NonAnimationSoundEvents.SoundOverride.skip();
            }
            if (effect != null && !isLocalAnchor(soundContext.anchor())) {
                if (!RoarOfLoveConfig.isOtherMobCalls()) {
                    return NonAnimationSoundEvents.SoundOverride.skip();
                }
                class_2960 resolveSoundId = NonSoundCues.resolveSoundId(effect, soundContext.anchor(), soundContext.animationResource(), soundContext.stageIndex());
                if (resolveSoundId != null) {
                    return NonAnimationSoundEvents.SoundOverride.play(resolveSoundId, soundContext.volume() * 0.5f, soundContext.pitch());
                }
            }
            if (effect != null && isLocalAnchor(soundContext.anchor()) && isPeakCue(effect)) {
                RoarOfLove.LOGGER.info("[roar_of_love] 内射音效线索 {}（闪屏开关={}）", effect, Boolean.valueOf(RoarOfLoveConfig.isFlashEffect()));
                if (RoarOfLoveConfig.isFlashEffect()) {
                    RoarFilterState.triggerFlash();
                }
            }
            return null;
        });
        NonAnimationEvents.STAGE_CHANGED.register(stageChangedContext -> {
            NonAnimationStage currentStage;
            Integer num;
            Integer num2;
            Integer num3;
            int stageNumberOf;
            try {
                if (involvesLocalPlayer(stageChangedContext.actorUuids()) && (currentStage = stageChangedContext.currentStage()) != null) {
                    class_2960[] class_2960VarArr = {currentStage.playbackAnimationId(), currentStage.animationId(), stageChangedContext.animationId()};
                    String str = "none";
                    int length = class_2960VarArr.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            num = null;
                            num2 = null;
                            break;
                        }
                        class_2960 class_2960Var = class_2960VarArr[i];
                        if (class_2960Var != null) {
                            num2 = NonPeakStages.getPeakStage(class_2960Var);
                            num = NonPeakStages.stageNumberFromId(class_2960Var);
                            if (num2 != null && num != null) {
                                str = "id:" + class_2960Var.method_12832();
                                break;
                            }
                        }
                        i++;
                    }
                    if (num2 == null) {
                        int definitionStageNumber = indexInDefinition(stageChangedContext.animationId(), currentStage);
                        if (definitionStageNumber > 0) {
                            num = Integer.valueOf(definitionStageNumber);
                        }
                        for (class_2960 class_2960Var2 : class_2960VarArr) {
                            Integer fallbackPeakStage = class_2960Var2 == null ? null : NonPeakStages.getPeakStage(class_2960Var2);
                            if (fallbackPeakStage != null) {
                                num2 = fallbackPeakStage;
                                str = "def:" + class_2960Var2.method_12832();
                                break;
                            }
                        }
                    }
                    num3 = num;
                    Integer num4 = num2;
                    if (num4 == null && (stageNumberOf = stageNumberOf(currentStage)) > 0) {
                        num3 = Integer.valueOf(stageNumberOf);
                        str = "no-peak-meta";
                    }
                    if (stageLogs < 80) {
                        stageLogs++;
                        RoarOfLove.LOGGER.info("[roar_of_love] 阶段变化 动画={} 阶段号={} 峰值={} 来源={} 闪屏开关={} 播放id={}", new Object[]{stageChangedContext.animationId(), num3, num4, str, Boolean.valueOf(RoarOfLoveConfig.isFlashEffect()), currentStage.playbackAnimationId()});
                    }
                    if (num4 != null && num3 != null && num3 == num4) {
                        RoarFilterState.startSustain();
                        if (RoarOfLoveConfig.isFlashEffect()) {
                            RoarOfLove.LOGGER.info("[roar_of_love] 命中峰值阶段 {} -> 触发白色闪屏（持续至内射阶段结束）", num3);
                            RoarFilterState.triggerFlash();
                        } else {
                            RoarOfLove.LOGGER.warn("[roar_of_love] 命中峰值阶段 {}，但「内射白色闪屏」开关处于关闭状态", num3);
                        }
                        RoarFilterState.triggerShake(2.2f);
                        RoarSubtitles.onPeak(true);
                        RoarFilterState.triggerReflux();
                        RoarFilterState.triggerPostPeak();
                        RoarFilterState.stopBreathHold();
                        RoarEcg.onStage(true);
                        RoarEcg.onReflux();
                        return;
                    }
                    RoarFilterState.stopSustain();
                    RoarSubtitles.onPeak(false);
                    RoarEcg.onStage(false);
                }
            } catch (Throwable th) {
                RoarOfLove.LOGGER.warn("[roar_of_love] 阶段监听异常", th);
            }
        });
        RoarBridge.init();
        if (!RoarMarquee.verify()) {
            RoarOfLove.LOGGER.error("[roar_of_love] 公告文件（assets/roar_of_love/marquee.txt）缺失或被修改，模组拒绝运行");
            throw new IllegalStateException("Roar of Love: assets/roar_of_love/marquee.txt is missing or modified - the mod refuses to run.");
        }
        ClientTickEvents.END_CLIENT_TICK.register(class_310Var4 -> {
            RoarEcg.tick();
            RoarFilterState.tick();
            RoarPregnancy.tick();
            RoarBeat.tick();
            RoarAmbience.tick();
            RoarVignetteHud.watchdog();
        });
        NonAnimationEvents.STARTED.register(startedContext -> {
            try {
                if (startedContext.session() != null && involvesLocalPlayer(startedContext.session().actorUuids())) {
                    RoarSubtitles.onAnimationStart(startedContext.session().instanceId(), startedContext.session().animationId());
                    RoarEcg.onStart(startedContext.session().instanceId());
                }
            } catch (Throwable th) {
                RoarOfLove.LOGGER.debug("[roar_of_love] 字幕启动监听异常", th);
            }
        });
        NonAnimationEvents.STOPPED.register(stoppedContext -> {
            RoarFilterState.stopSustain();
            RoarSubtitles.onAnimationEnd(stoppedContext.instanceId());
            RoarEcg.onEnd(stoppedContext.instanceId());
        });
        HudElementRegistry.addLast(class_2960.method_60655("roar_of_love", "hud"), (class_332Var, class_9779Var) -> {
            RoarVignetteHud.render(class_332Var, class_9779Var);
        });
        class_304.class_11900 class_11900Var = new class_304.class_11900(class_2960.method_60655("roar_of_love", "settings"));
        openSettingsKey = KeyMappingHelper.registerKeyMapping(new class_304("key.roar_of_love.open_settings", class_3675.class_307.KEYBOARD, KEY_F7, class_11900Var));
        openSettingsKeyL = KeyMappingHelper.registerKeyMapping(new class_304("key.roar_of_love.open_settings_l", class_3675.class_307.KEYBOARD, KEY_L, class_11900Var));
        testFlashKey = KeyMappingHelper.registerKeyMapping(new class_304("key.roar_of_love.test_flash", class_3675.class_307.KEYBOARD, KEY_F8, class_11900Var));
        testHeartsKey = KeyMappingHelper.registerKeyMapping(new class_304("key.roar_of_love.test_hearts", class_3675.class_307.KEYBOARD, KEY_F9, class_11900Var));
        testShakeKey = KeyMappingHelper.registerKeyMapping(new class_304("key.roar_of_love.test_shake", class_3675.class_307.KEYBOARD, KEY_F10, class_11900Var));
        RoarBridge.init();
        if (!RoarMarquee.verify()) {
            RoarOfLove.LOGGER.error("[roar_of_love] 公告文件（assets/roar_of_love/marquee.txt）缺失或被修改，模组拒绝运行");
            throw new IllegalStateException("Roar of Love: assets/roar_of_love/marquee.txt is missing or modified - the mod refuses to run.");
        }
        ClientTickEvents.END_CLIENT_TICK.register(class_310Var5 -> {
            if (guideDelayTicks > 0) {
                guideDelayTicks--;
                if (guideDelayTicks == 0) {
                    if (class_310Var5.field_1755 == null && !RoarOfLoveConfig.isGuideShown()) {
                        RoarOfLoveConfig.setGuideShown(true);
                        RoarOfLove.LOGGER.info("[roar_of_love] 显示首次使用引导窗口");
                        class_310Var5.method_1507(new RoarGuideScreen((class_437) null));
                    } else {
                        guideDelayTicks = 20;
                    }
                }
            }
            while (openSettingsKey.method_1436()) {
                RoarOfLove.LOGGER.info("[roar_of_love] F7 打开设置界面");
                class_310Var5.method_1507(new RoarOfLoveSettingsScreen(class_310Var5.field_1755));
            }
            while (openSettingsKeyL.method_1436()) {
                RoarOfLove.LOGGER.info("[roar_of_love] L 打开设置界面");
                class_310Var5.method_1507(new RoarOfLoveSettingsScreen(class_310Var5.field_1755));
            }
            while (testFlashKey.method_1436()) {
                RoarOfLove.LOGGER.info("[roar_of_love] F8 测试白色闪屏（开关={}）", Boolean.valueOf(RoarOfLoveConfig.isFlashEffect()));
                RoarFilterState.triggerFlash();
            }
            while (testHeartsKey.method_1436()) {
                RoarOfLove.LOGGER.info("[roar_of_love] F9 测试爱心");
                RoarFilterState.debugBurstHearts();
            }
            while (testShakeKey.method_1436()) {
                RoarOfLove.LOGGER.info("[roar_of_love] F10 测试屏幕抖动（开关={}）", Boolean.valueOf(RoarOfLoveConfig.isShakeEffect()));
                RoarFilterState.triggerShakeForced(2.5f);
            }
        });
        ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, class_7157Var) -> {
            commandDispatcher.register(ClientCommands.literal("roaroflove").executes(commandContext -> {
                RoarOfLove.LOGGER.info("[roar_of_love] 命令打开设置界面");
                class_310.method_1551().execute(() -> {
                    class_310.method_1551().method_1507(new RoarOfLoveSettingsScreen(class_310.method_1551().field_1755));
                });
                return 1;
            }));
        });
    }
}

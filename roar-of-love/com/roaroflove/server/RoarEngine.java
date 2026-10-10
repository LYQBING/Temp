//
// Decompiled by Jadx - 777ms
//
package com.roaroflove.server;

import com.nonid.api.animation.NonAnimationApi;
import com.nonid.api.animation.NonAnimationDefinition;
import com.nonid.api.animation.NonAnimationEvents;
import com.nonid.api.animation.NonAnimationSession;
import com.roaroflove.RoarOfLove;
import com.roaroflove.config.RoarOfLoveConfig;
import com.roaroflove.sound.RoLSounds;
import com.roaroflove.util.CallsAvailability;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2960;
import net.minecraft.class_3218;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_7923;
import net.minecraft.server.MinecraftServer;

public final class RoarEngine {
    private static final long MAX_SESSION_TICKS = 36000;
    private static final Map<UUID, RoarEngine$ActiveAnimation> ACTIVE = new HashMap();
    private static long currentTick = 0;
    private static boolean initialized = false;

    private RoarEngine() {
    }

    public static void init() {
        if (!initialized) {
            initialized = true;
            NonAnimationEvents.STARTED.register(startedContext -> {
                UUID choosePlaybackActor;
                if (RoarOfLoveConfig.isEnabled() && RoarOfLoveConfig.isRoarEnabled() && startedContext.session() != null) {
                    class_3218 world = startedContext.world();
                    if (world instanceof class_3218) {
                        NonAnimationSession session = startedContext.session();
                        class_2960 animationId = session.animationId();
                        if ((animationId == null || !RoarOfLoveConfig.isBlocked(animationId.toString())) && (choosePlaybackActor = choosePlaybackActor(world, session.actorUuids())) != null) {
                            float f = involvesPlayer(world, session.actorUuids()) ? 1.0f : 0.5f;
                            if (RoarOfLoveConfig.isOtherMobCalls() || f >= 1.0f) {
                                ACTIVE.put(session.instanceId(), new RoarEngine$ActiveAnimation(choosePlaybackActor, currentTick, resolveCallGroup(animationId), f));
                            }
                        }
                    }
                }
            });
            NonAnimationEvents.STOPPED.register(stoppedContext -> {
                RoarEngine$ActiveAnimation remove = ACTIVE.remove(stoppedContext.instanceId());
                if (remove != null && RoarOfLoveConfig.isEnabled() && RoarOfLoveConfig.isRoarEnabled()) {
                    class_3218 world = stoppedContext.world();
                    if ((world instanceof class_3218) && RoarOfLoveConfig.isSoundEnabled(remove.group) && RoarOfLoveConfig.isSoundEnabled(RoLSounds.callEventKey(remove.group, "end"))) {
                        playCall(world, remove.playAtUuid, "end", remove.group, remove.volumeScale);
                    }
                }
            });
            ServerTickEvents.END_SERVER_TICK.register(RoarEngine::tick);
            RoarOfLove.LOGGER.info("[roar_of_love] 动画叫声引擎已启动");
        }
    }

    private static boolean involvesPlayer(class_3218 class_3218Var, List<UUID> list) {
        if (list != null && !list.isEmpty()) {
            Iterator<UUID> it = list.iterator();
            while (it.hasNext()) {
                if (class_3218Var.getEntity(it.next()) instanceof class_1657) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void tick(MinecraftServer minecraftServer) {
        currentTick = minecraftServer.getTickCount();
        RoarOfLoveConfig.checkReload(currentTick);
        if (RoarOfLoveConfig.isEnabled() && RoarOfLoveConfig.isRoarEnabled()) {
            boolean anyCallsAvailable = CallsAvailability.anyCallsAvailable();
            int intervalTicks = RoarOfLoveConfig.intervalTicks();
            ACTIVE.entrySet().removeIf(entry -> {
                return currentTick - ((RoarEngine$ActiveAnimation) entry.getValue()).startTick >= MAX_SESSION_TICKS;
            });
            Iterator<Map.Entry<UUID, RoarEngine$ActiveAnimation>> it = ACTIVE.entrySet().iterator();
            while (it.hasNext()) {
                RoarEngine$ActiveAnimation value = it.next().getValue();
                if (currentTick >= value.nextCallTick) {
                    value.nextCallTick = currentTick + intervalTicks;
                    if (anyCallsAvailable) {
                        playRandomBegin(minecraftServer, value);
                    }
                }
            }
            return;
        }
        if (!ACTIVE.isEmpty()) {
            ACTIVE.clear();
        }
    }

    private static void playRandomBegin(MinecraftServer minecraftServer, RoarEngine$ActiveAnimation activeAnimation) {
        String pickGroupWithCalls = pickGroupWithCalls(activeAnimation.group);
        if (RoarOfLoveConfig.isSoundEnabled(pickGroupWithCalls)) {
            Set<String> availableCallFiles = CallsAvailability.availableCallFiles(pickGroupWithCalls);
            ArrayList arrayList = new ArrayList();
            for (String str : availableCallFiles) {
                if (str.startsWith("begin") && RoarOfLoveConfig.isSoundEnabled(RoLSounds.callEventKey(pickGroupWithCalls, str))) {
                    arrayList.add(str);
                }
            }
            if (!arrayList.isEmpty()) {
                playCallAt(minecraftServer, activeAnimation.playAtUuid, (String) arrayList.get(ThreadLocalRandom.current().nextInt(arrayList.size())), pickGroupWithCalls, activeAnimation.volumeScale);
            }
        }
    }

    private static String pickGroupWithCalls(String str) {
        return (str == null || str.isEmpty() || !CallsAvailability.anyCallsAvailable(str)) ? "default" : str;
    }

    private static String resolveCallGroup(class_2960 class_2960Var) {
        if (class_2960Var == null) {
            return "default";
        }
        try {
            NonAnimationDefinition definition = NonAnimationApi.getDefinition(class_2960Var);
            if (definition == null) {
                return "default";
            }
            ArrayList arrayList = new ArrayList();
            if (definition.contentTags() != null) {
                arrayList.addAll(definition.contentTags());
            }
            if (definition.animationTags() != null) {
                arrayList.addAll(definition.animationTags());
            }
            String callGroupForTags = RoLSounds.callGroupForTags(arrayList);
            RoarOfLove.LOGGER.debug("[roar_of_love] 动画 {} 归类为叫声分组 {} (tags={})", new Object[]{class_2960Var, callGroupForTags, arrayList});
            return callGroupForTags;
        } catch (Throwable th) {
            RoarOfLove.LOGGER.debug("[roar_of_love] 动画分组解析失败，回退 default", th);
            return "default";
        }
    }

    private static void playCall(class_3218 class_3218Var, UUID uuid, String str, String str2, float f) {
        class_1297 entity = uuid == null ? null : class_3218Var.getEntity(uuid);
        if (entity != null && !entity.method_31481()) {
            String eventKey = RoLSounds.callEventKey(str2, str);
            playAt(class_3218Var, entity.method_23317(), entity.method_23318() + (entity.method_17682() * 0.5d), entity.method_23321(), RoLSounds.get(eventKey), RoarOfLoveConfig.volume() * f * (RoarOfLoveConfig.soundVolumeSteps(eventKey) / 10.0f), RoarOfLoveConfig.pitch());
        }
    }

    private static void playCallAt(MinecraftServer minecraftServer, UUID uuid, String str, String str2, float f) {
        if (uuid != null) {
            for (class_3218 class_3218Var : minecraftServer.method_3738()) {
                class_1297 entity = class_3218Var.getEntity(uuid);
                if (entity != null && !entity.method_31481()) {
                    String eventKey = RoLSounds.callEventKey(str2, str);
                    playAt(class_3218Var, entity.method_23317(), entity.method_23318() + (entity.method_17682() * 0.5d), entity.method_23321(), RoLSounds.get(eventKey), RoarOfLoveConfig.volume() * f * (RoarOfLoveConfig.soundVolumeSteps(eventKey) / 10.0f), RoarOfLoveConfig.pitch());
                    return;
                }
            }
        }
    }

    private static void playAt(class_3218 class_3218Var, double d, double d2, double d3, class_3414 class_3414Var, float f, float f2) {
        if (class_3414Var != null) {
            class_3218Var.method_8465((class_1297) null, d, d2, d3, class_7923.field_41172.method_47983(class_3414Var), class_3419.field_15254, f, f2, class_3218Var.method_8409().method_43055());
        }
    }

    private static UUID choosePlaybackActor(class_3218 class_3218Var, List<UUID> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        boolean isRequirePlayerActor = RoarOfLoveConfig.isRequirePlayerActor();
        Iterator<UUID> it = list.iterator();
        while (it.hasNext()) {
            UUID next = it.next();
            class_1297 entity = class_3218Var.getEntity(next);
            if (entity != null && (!isRequirePlayerActor || (entity instanceof class_1657))) {
                return next;
            }
        }
        return null;
    }
}

//
// Decompiled by Jadx - 572ms
//
package com.roaroflove.client;

import com.roaroflove.config.RoarOfLoveConfig;
import java.util.ArrayDeque;
import net.minecraft.class_1113;
import net.minecraft.class_310;

public final class RoarBeat {
    private static final long MAX_ALIGN_MS = 170;
    private static final int QUEUE_LIMIT = 12;
    private static final ArrayDeque<Entry> QUEUE = new ArrayDeque<>();
    private static volatile boolean replaying = false;
    private static int alignedCount = 0;
    private static volatile long epochMs = System.currentTimeMillis();

    private RoarBeat() {
    }

    public static boolean isReplaying() {
        return replaying;
    }

    public static int alignedCount() {
        return alignedCount;
    }

    public static long periodMs() {
        return 1150 - (Math.max(0, Math.min(3, RoarFilterState.cachedLust())) * (200 / Math.max(1, 3)));
    }

    public static void setEpoch(long j) {
        epochMs = j;
    }

    public static long effectivePeriodMs() {
        long periodMs = periodMs();
        if (!RoarEcg.animationActive() && RoarEcg.isExercising()) {
            periodMs = RoarEcg.exercisePeriod();
        }
        return Math.max(160L, (long) (periodMs * RoarStyle.beatScale()));
    }

    public static double phase() {
        return phaseAt(System.currentTimeMillis());
    }

    public static double phaseAt(long j) {
        double effectivePeriodMs = ((j - epochMs) % r0) / effectivePeriodMs();
        return effectivePeriodMs < 0.0d ? effectivePeriodMs + 1.0d : effectivePeriodMs;
    }

    public static float pulse() {
        return pulseAt(System.currentTimeMillis());
    }

    public static float pulseAt(long j) {
        double phaseAt = phaseAt(j);
        if (phaseAt >= 0.35d) {
            return 0.0f;
        }
        return (float) Math.pow(Math.sin((phaseAt / 0.35d) * 3.141592653589793d), 0.7d);
    }

    public static long nextBeatInMs() {
        long periodMs = periodMs();
        return periodMs - (System.currentTimeMillis() % periodMs);
    }

    public static int beatIndex() {
        return (int) (System.currentTimeMillis() / periodMs());
    }

    public static boolean tryAlign(class_1113 class_1113Var) {
        boolean z = false;
        if (RoarOfLoveConfig.isAlignMoans() && !replaying && class_1113Var != null && RoarEcg.isActive()) {
            long nextBeatInMs = nextBeatInMs();
            if (nextBeatInMs <= MAX_ALIGN_MS) {
                synchronized (QUEUE) {
                    if (QUEUE.size() < QUEUE_LIMIT) {
                        Entry entry = new Entry();
                        entry.instance = class_1113Var;
                        entry.atMs = nextBeatInMs + System.currentTimeMillis();
                        QUEUE.add(entry);
                        alignedCount++;
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    public static void tick() {
        synchronized (QUEUE) {
            if (!QUEUE.isEmpty()) {
                long currentTimeMillis = System.currentTimeMillis();
                while (!QUEUE.isEmpty() && QUEUE.peek().atMs <= currentTimeMillis) {
                    play(QUEUE.poll().instance);
                }
            }
        }
    }

    private static void play(class_1113 class_1113Var) {
        try {
            class_310 method_1551 = class_310.method_1551();
            if (method_1551 != null && method_1551.method_1483() != null) {
                replaying = true;
                try {
                    method_1551.method_1483().method_4873(class_1113Var);
                } finally {
                    replaying = false;
                }
            }
        } catch (Throwable th) {
            replaying = false;
        }
    }
}

package com.roaroflove.client;

import com.roaroflove.config.RoarOfLoveConfig;

public final class RoarDirector {
	private static long startMs;
	private static long learnedMs;
	private static boolean peaked;
	private static boolean armed;

	private RoarDirector() {
	}

	public static void onAnimationStart() {
		startMs = System.currentTimeMillis();
		peaked = false;
		armed = false;
	}

	public static void onPeak() {
		if (startMs > 0L && !peaked) {
			learnedMs = System.currentTimeMillis() - startMs;
			peaked = true;
		}
	}

	public static void tick() {
		if (RoarOfLoveConfig.isDirector() && learnedMs > 0L && startMs > 0L && !peaked && !armed
				&& RoarEcg.animationActive() && System.currentTimeMillis() - startMs >= learnedMs - 2000L) {
			armed = true;
			RoarFilterState.triggerBreathHold();
		}
	}
}

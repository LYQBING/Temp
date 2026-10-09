//
// Decompiled by Jadx - 439ms
//
package com.roaroflove.client;

final class RoarOfLoveSettingsScreen$Action {
    private final String label;
    private final String key;
    private final Runnable run;

    RoarOfLoveSettingsScreen$Action(String label, String key, Runnable run) {
        this.label = label;
        this.key = key;
        this.run = run;
    }

    String key() {
        return key;
    }

    String label() {
        return label;
    }

    Runnable run() {
        return run;
    }
}

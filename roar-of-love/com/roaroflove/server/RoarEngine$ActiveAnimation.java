//
// Decompiled by Jadx - 643ms
//
package com.roaroflove.server;

import java.util.UUID;

final class RoarEngine$ActiveAnimation {
    final String group;
    long nextCallTick;
    final UUID playAtUuid;
    final long startTick;
    final float volumeScale;

    RoarEngine$ActiveAnimation(UUID uuid, long j, String str, float f) {
        this.playAtUuid = uuid;
        this.startTick = j;
        this.group = str == null ? "default" : str;
        this.volumeScale = f;
        this.nextCallTick = j;
    }
}

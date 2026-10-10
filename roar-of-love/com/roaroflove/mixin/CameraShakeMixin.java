//
// Decompiled by Jadx - 524ms
//
package com.roaroflove.mixin;

import com.roaroflove.client.RoarFilterState;
import net.minecraft.class_4184;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_4184.class})
public abstract class CameraShakeMixin {
    @Shadow
    public abstract void setRotation(float yaw, float pitch);

    @Shadow
    public abstract float yaw();

    @Shadow
    public abstract float xRot();

    @Inject(at = {@At("TAIL")}, method = {"alignWithEntity"})
    private void roarOfLove$applyShake(float partialTick, CallbackInfo callbackInfo) {
        float shakeYaw = RoarFilterState.shakeYaw();
        float shakePitch = RoarFilterState.shakePitch();
        if (shakeYaw != 0.0f || shakePitch != 0.0f) {
            setRotation(yaw() + shakeYaw, xRot() + shakePitch);
        }
    }
}

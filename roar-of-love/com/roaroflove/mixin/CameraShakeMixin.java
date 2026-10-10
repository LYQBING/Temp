//
// Decompiled by Jadx - 486ms
//
package com.roaroflove.mixin;

import com.roaroflove.client.RoarFilterState;
import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_4184;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_4184.class})
public abstract class CameraShakeMixin {
    @Shadow
    protected abstract void method_19325(float f, float f2);

    @Shadow
    public abstract float method_19329();

    @Shadow
    public abstract float method_19330();

    @Inject(at = {@At("TAIL")}, method = {"method_19321"})
    private void roarOfLove$applyShake(class_1937 class_1937Var, class_1297 class_1297Var, boolean z, boolean z2, float f, CallbackInfo callbackInfo) {
        float shakeYaw = RoarFilterState.shakeYaw() * RoarFilterState.shakeMotionScale();
        float shakePitch = RoarFilterState.shakePitch() * RoarFilterState.shakeMotionScale();
        if (shakeYaw != 0.0f || shakePitch != 0.0f) {
            method_19325(shakeYaw + method_19329(), shakePitch + method_19330());
        }
    }
}

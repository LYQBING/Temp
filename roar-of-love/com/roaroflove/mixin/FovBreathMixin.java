//
// Decompiled by Jadx - 752ms
//
package com.roaroflove.mixin;

import com.roaroflove.client.RoarFilterState;
import com.roaroflove.config.RoarOfLoveConfig;
import net.minecraft.class_4184;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_4184.class})
public abstract class FovBreathMixin {
    @Inject(at = {@At("RETURN")}, cancellable = true, method = {"calculateFov"})
    private void roarOfLove$breathFov(float f, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (RoarOfLoveConfig.isFovBreath()) {
            float fovBreathFactor = RoarFilterState.fovBreathFactor();
            if (fovBreathFactor != 1.0f) {
                callbackInfoReturnable.setReturnValue(Float.valueOf(fovBreathFactor * callbackInfoReturnable.getReturnValueF()));
            }
        }
    }
}

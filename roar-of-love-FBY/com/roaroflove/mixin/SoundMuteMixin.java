//
// Decompiled by Jadx - 499ms
//
package com.roaroflove.mixin;

import com.roaroflove.client.RoarBeat;
import com.roaroflove.client.RoarSoundDistance;
import com.roaroflove.client.RoarSoundFilter;
import net.minecraft.class_1113;
import net.minecraft.class_1140;
import net.minecraft.class_1144;
import net.minecraft.class_2960;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1144.class})
public abstract class SoundMuteMixin {
    @Shadow
    public abstract class_1140.class_11518 play(class_1113 class_1113Var);

    @Inject(at = {@At("HEAD")}, cancellable = true, method = {"play"})
    private void roarOfLove$muteOtherHurt(class_1113 class_1113Var, CallbackInfoReturnable<class_1140.class_11518> callbackInfoReturnable) {
        if (class_1113Var != null) {
            class_2960 method_4775 = class_1113Var.method_4775();
            if (RoarSoundFilter.isBlocked(method_4775)) {
                callbackInfoReturnable.setReturnValue(class_1140.class_11518.field_60955);
                return;
            }
            if (RoarSoundDistance.isModSound(method_4775) && RoarBeat.tryAlign(class_1113Var)) {
                callbackInfoReturnable.setReturnValue(class_1140.class_11518.field_60955);
                return;
            }
            class_1113 adjust = RoarSoundDistance.adjust(class_1113Var);
            if (adjust != class_1113Var) {
                callbackInfoReturnable.setReturnValue(play(adjust));
            }
        }
    }
}

//
// Decompiled by Jadx - 723ms
//
package com.roaroflove.mixin;

import com.roaroflove.client.RoarOfLoveSettingsScreen;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_353;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_443;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_443.class})
public abstract class SoundOptionsScreenMixin {
    @Inject(at = {@At("TAIL")}, method = {"method_60325"})
    private void roarOfLove$addSettingsButton(CallbackInfo ci) {
        if (this instanceof GameOptionsScreenBodyAccessor) {
            GameOptionsScreenBodyAccessor accessor = (GameOptionsScreenBodyAccessor) this;
            class_353 body = accessor.roarOfLove$getBody();
            if (body != null) {
                class_4185 button = class_4185.method_46430(class_2561.method_43470("Roar of Love 设置…"), b -> {
                    class_310.method_1551().method_1507(new RoarOfLoveSettingsScreen((class_437) (Object) this));
                }).method_46432(150).method_46431();
                body.method_58227(List.of(button));
            }
        }
    }
}

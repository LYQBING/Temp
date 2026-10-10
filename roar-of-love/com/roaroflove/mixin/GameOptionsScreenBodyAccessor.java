//
// Decompiled by Jadx - 596ms
//
package com.roaroflove.mixin;

import net.minecraft.class_353;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = {"net.minecraft.client.gui.screens.options.OptionsSubScreen"})
public interface GameOptionsScreenBodyAccessor {
    @Accessor("list")
    class_353 roarOfLove$getBody();
}

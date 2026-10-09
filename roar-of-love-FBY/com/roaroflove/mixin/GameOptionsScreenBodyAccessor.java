//
// Decompiled by Jadx - 596ms
//
package com.roaroflove.mixin;

import net.minecraft.class_353;
import net.minecraft.class_443;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_443.class})
public interface GameOptionsScreenBodyAccessor {
    @Accessor("list")
    class_353 roarOfLove$getBody();
}

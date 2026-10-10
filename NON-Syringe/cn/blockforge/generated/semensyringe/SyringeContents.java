//
// Decompiled by Jadx - 663ms
//
package cn.blockforge.generated.semensyringe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.Optional;
import net.minecraft.class_1799;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_5321;
import net.minecraft.class_7923;
import net.minecraft.class_7924;
import net.minecraft.class_9135;
import net.minecraft.class_9331;

public final class SyringeContents extends Record {
    public static final Codec<SyringeContents> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(class_2960.field_25139.optionalFieldOf("donor").forGetter((v0) -> {
            return v0.donor();
        }), Codec.INT.fieldOf("ml").forGetter((v0) -> {
            return v0.ml();
        }), Codec.INT.fieldOf("tint").forGetter((v0) -> {
            return v0.tint();
        })).apply(instance, (v1, v2, v3) -> {
            return new SyringeContents(v1, v2, v3);
        });
    });
    private static final class_9331<SyringeContents> TYPE = (class_9331) class_2378.method_39197(class_7923.field_49658, class_5321.method_29179(class_7924.field_49659, SemenSyringeMod.id("contents")), class_9331.method_57873().method_57881(CODEC).method_57882(class_9135.method_56368(CODEC)).method_59871().method_57880());
    private final Optional<class_2960> donor;
    private final int ml;
    private final int tint;

    public SyringeContents(Optional<class_2960> donor, int ml, int tint) {
        this.donor = donor;
        this.ml = ml;
        this.tint = tint;
    }

    public Optional<class_2960> donor() {
        return this.donor;
    }

    @Override
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, SyringeContents.class, Object.class), SyringeContents.class, "donor;ml;tint", "FIELD:Lcn/blockforge/generated/semensyringe/SyringeContents;->donor:Ljava/util/Optional;", "FIELD:Lcn/blockforge/generated/semensyringe/SyringeContents;->ml:I", "FIELD:Lcn/blockforge/generated/semensyringe/SyringeContents;->tint:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    @Override
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, SyringeContents.class), SyringeContents.class, "donor;ml;tint", "FIELD:Lcn/blockforge/generated/semensyringe/SyringeContents;->donor:Ljava/util/Optional;", "FIELD:Lcn/blockforge/generated/semensyringe/SyringeContents;->ml:I", "FIELD:Lcn/blockforge/generated/semensyringe/SyringeContents;->tint:I").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    public int ml() {
        return this.ml;
    }

    public int tint() {
        return this.tint;
    }

    @Override
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, SyringeContents.class), SyringeContents.class, "donor;ml;tint", "FIELD:Lcn/blockforge/generated/semensyringe/SyringeContents;->donor:Ljava/util/Optional;", "FIELD:Lcn/blockforge/generated/semensyringe/SyringeContents;->ml:I", "FIELD:Lcn/blockforge/generated/semensyringe/SyringeContents;->tint:I").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    public static class_9331<SyringeContents> type() {
        return TYPE;
    }

    public class_2960 donorId() {
        return this.donor.orElse(null);
    }

    public boolean mixed() {
        return this.donor.isEmpty();
    }

    public static SyringeContents read(class_1799 stack) {
        return (SyringeContents) stack.method_58694(TYPE);
    }

    public static void write(class_1799 stack, SyringeContents contents) {
        stack.method_57379(TYPE, contents);
    }

    public static void clear(class_1799 stack) {
        stack.method_57381(TYPE);
    }
}

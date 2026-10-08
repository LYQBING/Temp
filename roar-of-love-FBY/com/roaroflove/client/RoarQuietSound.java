//
// Decompiled by Jadx - 917ms
//
package com.roaroflove.client;

import net.minecraft.class_1111;
import net.minecraft.class_1113;
import net.minecraft.class_1144;
import net.minecraft.class_1146;
import net.minecraft.class_2960;
import net.minecraft.class_3419;

public class RoarQuietSound implements class_1113 {
    private final class_1113 base;
    private final float pitchScale;
    private final float volumeScale;

    public RoarQuietSound(class_1113 class_1113Var, float f, float f2) {
        this.base = class_1113Var;
        this.volumeScale = f;
        this.pitchScale = f2;
    }

    public class_2960 method_4775() {
        return this.base.method_4775();
    }

    public class_1146 method_4783(class_1144 class_1144Var) {
        return this.base.method_4783(class_1144Var);
    }

    public class_1111 method_4776() {
        return this.base.method_4776();
    }

    public class_3419 method_4774() {
        return this.base.method_4774();
    }

    public boolean method_4786() {
        return this.base.method_4786();
    }

    public boolean method_4787() {
        return this.base.method_4787();
    }

    public int method_4780() {
        return this.base.method_4780();
    }

    public float method_4781() {
        return this.base.method_4781() * this.volumeScale;
    }

    public float method_4782() {
        return this.base.method_4782() * this.pitchScale;
    }

    public double method_4784() {
        return this.base.method_4784();
    }

    public double method_4779() {
        return this.base.method_4779();
    }

    public double method_4778() {
        return this.base.method_4778();
    }

    public class_1113.class_1114 method_4777() {
        return this.base.method_4777();
    }
}

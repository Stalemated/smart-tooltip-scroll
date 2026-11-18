package com.stalemated.sts.scroll.easing;

import com.stalemated.lib.util.math.MathUtils;

import java.util.function.DoubleUnaryOperator;

public enum ScrollEasingStyle {
    CLASSIC(time -> MathUtils.lerp(0.0, 1.0, time)),
    SMOOTH(MathUtils::easeOutCubic),
    SNAPPY(MathUtils::easeOutExpo);

    private final DoubleUnaryOperator curve;

    ScrollEasingStyle(DoubleUnaryOperator curve) {
        this.curve = curve;
    }

    public double progress(double time) {
        return curve.applyAsDouble(time);
    }
}

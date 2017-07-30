package com.lonebytesoft.hamster.raytracing.util.math;

public final class MathCalculator {

    public static final double DOUBLE_DELTA = 2.0 * Double.MIN_VALUE;
    public static final double DOUBLE_DELTA_APPROX = 1e-6;

    public static boolean isEqual(final double first, final double second) {
        return Math.abs(first - second) <= DOUBLE_DELTA;
    }

    public static boolean isEqualApproximately(final double first, final double second) {
        return Math.abs(first - second) <= DOUBLE_DELTA_APPROX;
    }

}

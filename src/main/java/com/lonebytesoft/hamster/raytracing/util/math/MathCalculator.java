package com.lonebytesoft.hamster.raytracing.util.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static java.lang.Math.acos;
import static java.lang.Math.cbrt;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;

public final class MathCalculator {

    public static final double DOUBLE_DELTA = 2.0 * Double.MIN_VALUE;
    public static final double DOUBLE_DELTA_APPROX = 1e-6;

    public static boolean isEqual(final double first, final double second) {
        return Math.abs(first - second) <= DOUBLE_DELTA;
    }

    public static boolean isEqualApproximately(final double first, final double second) {
        return Math.abs(first - second) <= DOUBLE_DELTA_APPROX;
    }

    public static Collection<Double> solveLinear(final double a, final double b) {
        return Collections.singletonList(-b / a);
    }

    public static Collection<Double> solveQuadratic(final double a, final double b, final double c) {
        final double d = b*b - 4*a*c;
        if (d >= 0) {
            return Arrays.asList(
                    (-b - sqrt(d)) / (2d * a),
                    (-b + sqrt(d)) / (2d * a)
            );
        } else {
            return Collections.emptyList();
        }
    }

    public static Collection<Double> solveQuartic(final double a, final double b, final double c, final double d, final double e) {
        final double delta0 = c*c - 3d*b*d + 12d*a*e;
        final double delta1 = 2d*c*c*c - 9d*b*c*d + 27d*b*b*e + 27d*a*d*d - 72d*a*c*e;
        final double delta = delta1 * delta1 - 4d * delta0 * delta0 * delta0;

        final double p = (8*a*c - 3*b*b) / (8*a*a);
        final double s;
        if (delta < 0) {
            final double phi = acos(delta1 / (2d * sqrt(delta0 * delta0 * delta0)));
            s = sqrt(-2d / 3d * p + 2d / 3d / a * sqrt(delta0) * cos(phi / 3d)) / 2d;
        } else {
            final double q = cbrt((delta1 + sqrt(delta)) / 2d);
            s = sqrt(-2d / 3d * p + 1d / 3d / a * (q + delta0 / q)) / 2d;
        }

        final double q = (b*b*b - 4*a*b*c + 8*a*a*d) / (8*a*a*a);
        final Collection<Double> roots = new ArrayList<>();

        final double d1 = -4d * s * s - 2d * p + q / s;
        if (d1 >= 0) {
            roots.add(-b / 4d / a - s + sqrt(d1) / 2d);
            roots.add(-b / 4d / a - s - sqrt(d1) / 2d);
        }

        final double d2 = -4d * s * s - 2d * p - q / s;
        if (d2 >= 0) {
            roots.add(-b / 4d / a + s + sqrt(d2) / 2d);
            roots.add(-b / 4d / a + s - sqrt(d2) / 2d);
        }

        return roots;
    }

}

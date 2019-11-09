package com.lonebytesoft.hamster.raytracing.shape.generic.cylinder;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.shape.generic.Ball;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;

public class RightCylinderBruteforce<T extends Coordinates> extends RightSymmetricCylindricalSurfaceBruteforce<T> {

    public RightCylinderBruteforce(
            final T from,
            final T to,
            final double radius,
            final long intersectionTestSamples,
            final GeometryCalculator<T> geometryCalculator
    ) {
        super(
                from,
                geometryCalculator.subtract(to, from),
                (distance, r) -> r <= radius,
                calculateBoundaries(from, to, radius, geometryCalculator),
                intersectionTestSamples,
                geometryCalculator
        );
    }

    private static <T extends Coordinates> Ball<T> calculateBoundaries(
            final T from,
            final T to,
            final double radius,
            final GeometryCalculator<T> geometryCalculator
    ) {
        final T axis = geometryCalculator.subtract(to, from);
        return new Ball<>(
                geometryCalculator.follow(from, axis, 0.5),
                Math.sqrt(geometryCalculator.product(axis, axis) / 4 + radius * radius),
                geometryCalculator
        );
    }

}

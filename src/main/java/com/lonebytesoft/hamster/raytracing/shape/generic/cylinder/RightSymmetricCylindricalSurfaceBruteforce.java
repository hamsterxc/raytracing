package com.lonebytesoft.hamster.raytracing.shape.generic.cylinder;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.shape.feature.GeometryCalculating;
import com.lonebytesoft.hamster.raytracing.shape.generic.Ball;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class RightSymmetricCylindricalSurfaceBruteforce<T extends Coordinates>
        implements GeometryCalculating<T> {

    private final T base;
    private final T direction;
    private final BiPredicate<Double, Double> directrix;
    private final Ball<T> boundaries;
    private final double intersectionTestStep;
    private final GeometryCalculator<T> geometryCalculator;

    public RightSymmetricCylindricalSurfaceBruteforce(
            final T base,
            final T direction,
            final BiPredicate<Double, Double> directrix,
            final Ball<T> boundaries,
            final long intersectionTestSamples,
            final GeometryCalculator<T> geometryCalculator
    ) {
        this.base = base;
        this.direction = geometryCalculator.normalize(direction);
        this.directrix = directrix;
        this.boundaries = boundaries;
        this.intersectionTestStep = boundaries.getRadius() * 2d / intersectionTestSamples;
        this.geometryCalculator = geometryCalculator;
    }

    /*
        ray: a + x*b
        axis: c + y*d

        plane: (m - (c + y*d), d) = 0
        ((a + x*b) - (c + y*d), d) = 0
        sum(di*(ai + x*bi - ci - y*di)) = 0
        sum(di*ai) + x*sum(di*bi) - sum(di*ci) - y*sum(di*di) = 0
        y = sum(di*(ai + x*bi - ci)) / sum(di*di)

        r = |(a + x*b) - (c + y*d)|
        find min x where r is in shape bounds
     */
    @Override
    public Double calculateDistance(Ray<T> ray) {
        final Double boundaryDistance = boundaries.calculateDistance(ray);
        if (boundaryDistance == null) {
            return null;
        }

        final Ray<T> passThrough = boundaries.calculatePassThrough(ray);
        final Double passThroughBoundaryDistance = boundaries.calculateDistance(passThrough);
        final double boundaryMin;
        final double boundaryMax;
        if (passThroughBoundaryDistance == null) {
            boundaryMin = 0;
            boundaryMax = boundaryDistance;
        } else {
            boundaryMin = boundaryDistance;
            boundaryMax = boundaryDistance + passThroughBoundaryDistance;
        }

        final T rayDirection = geometryCalculator.normalize(ray.getDirection());
        final Predicate<Double> boundaryPredicate = multiplier -> {
            final T rayEnd = geometryCalculator.follow(ray.getStart(), rayDirection, multiplier);
            final T baseToRayEnd = geometryCalculator.subtract(rayEnd, base);
            final double axisDistance = geometryCalculator.product(direction, baseToRayEnd) / geometryCalculator.product(direction, direction);
            final T axisPoint = geometryCalculator.follow(base, direction, axisDistance);
            final T radiusVector = geometryCalculator.subtract(rayEnd, axisPoint);
            final double radius = geometryCalculator.length(radiusVector);
            return directrix.test(axisDistance, radius);
        };

        double distance = boundaryMin;
        do {
            if (boundaryPredicate.test(distance)) {
                return distance;
            }
            distance += intersectionTestStep;
        } while (distance <= boundaryMax);
        return null;
    }

}

package com.lonebytesoft.hamster.raytracing.shape.generic;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.shape.feature.GeometryCalculating;
import com.lonebytesoft.hamster.raytracing.shape.feature.Reflecting;
import com.lonebytesoft.hamster.raytracing.shape.feature.Refracting;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;
import com.lonebytesoft.hamster.raytracing.shape.feature.Transparent;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import com.lonebytesoft.hamster.raytracing.util.math.MathCalculator;

public class Ball<T extends Coordinates>
        implements GeometryCalculating<T>, Reflecting<T>, Refracting<T>, Transparent<T>, Surfaced<T> {

    private final T center;
    private final double radius;
    private final GeometryCalculator<T> geometryCalculator;

    private final double delta;

    public Ball(
            final T center,
            final double radius,
            final GeometryCalculator<T> geometryCalculator
    ) {
        this.center = center;
        this.radius = radius;
        this.geometryCalculator = geometryCalculator;

        this.delta = Math.max(this.radius / 1e6, MathCalculator.DOUBLE_DELTA_APPROX);
    }

    @Override
    public Double calculateDistance(Ray<T> ray) {
        final Double multiplier = calculateRayDistanceMultiplier(ray);
        if(multiplier == null) {
            return null;
        } else {
            final double length = geometryCalculator.length(ray.getDirection());
            return multiplier * length;
        }
    }

    @Override
    public T calculateNormal(Ray<T> ray) {
        final T intersection = calculateIntersection(ray);
        if(intersection == null) {
            return null;
        } else {
            // normal is the radius in point of intersection
            if(isInside(ray.getStart())) {
                return geometryCalculator.subtract(center, intersection);
            } else {
                return geometryCalculator.subtract(intersection, center);
            }
        }
    }

    @Override
    public Ray<T> calculateReflection(Ray<T> ray) {
        return geometryCalculator.calculateReflection(ray, calculateIntersection(ray), calculateNormal(ray), delta);
    }

    @Override
    public Ray<T> calculateRefraction(Ray<T> ray, double coeffSpace, double coeffSelf) {
        final T intersection = calculateIntersection(ray);
        final T normal = calculateNormal(ray);
        if(isInside(ray.getStart())) {
            return geometryCalculator.calculateRefraction(ray, intersection, normal, coeffSelf, coeffSpace, delta);
        } else {
            return geometryCalculator.calculateRefraction(ray, intersection, normal, coeffSpace, coeffSelf, delta);
        }
    }

    @Override
    public Ray<T> calculatePassThrough(Ray<T> ray) {
        return geometryCalculator.calculatePassThrough(ray, calculateIntersection(ray), delta);
    }

    /**
     * Spherical coordinates
     * (k1, ..., k{n-1}, kn)
     * k1, ..., k{n-1}: [0..1]
     * kn: [0..2]
     */
    // https://en.wikipedia.org/wiki/N-sphere#Spherical_coordinates
    @Override
    public <F extends Coordinates> F mapToSurface(Ray<T> ray, GeometryCalculator<F> geometryCalculator) {
        final T intersection = calculateIntersection(ray);
        if(intersection == null) {
            return null;
        }

        final int dimensions = center.getDimensions();
        final double[] phi = new double[dimensions - 1];
        final double xn = intersection.getCoordinate(dimensions - 1) - center.getCoordinate(dimensions - 1);

        double sum = xn * xn;
        for(int i = 0; i < dimensions - 1; i++) {
            final int index = dimensions - 2 - i;
            final double xk = intersection.getCoordinate(index) - center.getCoordinate(index);

            sum += xk * xk;

            if(MathCalculator.isEqual(sum, 0.0)) {
                phi[index] = 0.0;
            } else {
                final double angle = Math.acos(xk / Math.sqrt(sum));
                phi[index] = angle / Math.PI;
            }
        }
        if(xn < 0.0) {
            phi[dimensions - 2] = 2.0 - phi[dimensions - 2];
        }

        return geometryCalculator.buildVector(index -> phi[index]);
    }

    /**
     * @see #mapToSurface(Ray, GeometryCalculator)
     */
    @Override
    public <F extends Coordinates> T mapFromSurface(F coordinates) {
        final int dimensions = center.getDimensions();
        final double[] coords = new double[dimensions];

        double sinuses = radius;
        for(int i = 0; i < dimensions - 1; i++) {
            final double phi = coordinates.getCoordinate(i) * Math.PI;
            coords[i] = sinuses * Math.cos(phi) + center.getCoordinate(i);
            sinuses *= Math.sin(phi);
        }
        coords[dimensions - 1] = sinuses + center.getCoordinate(dimensions - 1);

        return geometryCalculator.buildVector(index -> coords[index]);
    }

    private T calculateIntersection(final Ray<T> ray) {
        final Double multiplier = calculateRayDistanceMultiplier(ray);
        if(multiplier == null) {
            return null;
        } else {
            return geometryCalculator.follow(ray.getStart(), ray.getDirection(), multiplier);
        }
    }

    /*
        |a + k*b - c| = r
        (a1 + k*b1 - c1)^2 + ... + (an + k*bn - cn)^2 = r^2
        k^2*(b1^2 + ... + bn^2) + k*2*(b1*(a1 - c1) + ... + bn*(an - cn)) + ((a1^2 + c1^2 - 2*a1*c1) + ...) - r^2 = 0
     */
    private Double calculateRayDistanceMultiplier(final Ray<T> ray) {
        final int dimensions = ray.getStart().getDimensions();

        double a = 0.0;
        double b = 0.0;
        double c = 0.0;
        for(int i = 0; i < dimensions; i++) {
            final double rayStartCoordinate = ray.getStart().getCoordinate(i);
            final double rayDirectionCoordinate = ray.getDirection().getCoordinate(i);
            final double centerCoordinate = center.getCoordinate(i);

            a += rayDirectionCoordinate * rayDirectionCoordinate;
            b += 2.0 * rayDirectionCoordinate * (rayStartCoordinate - centerCoordinate);
            c += rayStartCoordinate * rayStartCoordinate + centerCoordinate * centerCoordinate
                    - 2.0 * rayStartCoordinate * centerCoordinate;
        }
        c -= radius * radius;

        final double discriminant = b * b - 4.0 * a * c;
        if(discriminant < 0.0) {
            return null;
        }

        final double k1 = (-b - Math.sqrt(discriminant)) / (2.0 * a);
        final double k2 = (-b + Math.sqrt(discriminant)) / (2.0 * a);

        // ball
//        if(k1 < 0.0) {
//            if(k2 < 0.0) {
//                return null;
//            } else {
//                return 0.0;
//            }
//        } else {
//            return k1;
//        }

        // sphere
        if(k1 >= 0.0) {
            return k1;
        } else if(k2 >= 0.0) {
            return k2;
        } else {
            return null;
        }
    }

    private boolean isInside(final T point) {
        return geometryCalculator.length(geometryCalculator.subtract(point, center)) <= radius;
    }

    @Override
    public String toString() {
        return "Ball{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }

}

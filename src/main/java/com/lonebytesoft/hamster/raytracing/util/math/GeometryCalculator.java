package com.lonebytesoft.hamster.raytracing.util.math;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.CoordinatesCalculator;
import com.lonebytesoft.hamster.raytracing.ray.Ray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class GeometryCalculator {

    public static <T extends Coordinates<T>> T push(final T point, final T direction, final double minDelta) {
        final int dimensions = point.getDimensions();
        double coordinateMin = Double.MAX_VALUE;
        for(int i = 0; i < dimensions; i++) {
            coordinateMin = Math.min(coordinateMin, Math.abs(direction.getCoordinate(i)));
        }

        final double deltaMultiplier = coordinateMin;
        return CoordinatesCalculator.transform(point,
                index -> point.getCoordinate(index) + minDelta * direction.getCoordinate(index) / deltaMultiplier);
    }

    public static <T extends Coordinates<T>> T follow(final T point, final T direction, final double multiplier) {
        return CoordinatesCalculator.transform(point,
                index -> point.getCoordinate(index) + multiplier * direction.getCoordinate(index));
    }

    public static <T extends Coordinates<T>> double product(final T first, final T second) {
        final int dimensions = first.getDimensions();
        if(second.getDimensions() != dimensions) {
            throw new IllegalArgumentException("Vectors dimensionality not equal");
        }

        double result = 0;
        for(int i = 0; i < dimensions; i++) {
            result += first.getCoordinate(i) * second.getCoordinate(i);
        }
        return result;
    }

    public static <T extends Coordinates<T>> double length(final T vector) {
        return Math.sqrt(product(vector, vector));
    }

    public static <T extends Coordinates<T>> T normalize(final T vector) {
        final double length = length(vector);
        return CoordinatesCalculator.transform(vector, index -> vector.getCoordinate(index) / length);
    }

    public static <T extends Coordinates<T>> T rotate(
            final T vector, final int axisFirst, final int axisSecond, final double angle) {
        return CoordinatesCalculator.transform(vector, index -> {
            if(index == axisFirst) {
                return vector.getCoordinate(axisFirst) * Math.cos(angle) + vector.getCoordinate(axisSecond) * Math.sin(angle);
            } else if(index == axisSecond) {
                return vector.getCoordinate(axisFirst) * (-Math.sin(angle)) + vector.getCoordinate(axisSecond) * Math.cos(angle);
            } else {
                return vector.getCoordinate(index);
            }
        });
    }

    public static <T extends Coordinates<T>> Ray<T> calculatePassThrough(
            final Ray<T> ray, final T intersection, final double pushDelta) {
        if(intersection == null) {
            return null;
        } else {
            final T rayDirection = ray.getDirection();
            // todo: hack: pushing ray start out a bit
            final T start = push(intersection, rayDirection, pushDelta);

            return new Ray<>(start, rayDirection);
        }
    }

    // https://math.stackexchange.com/q/13261
    public static <T extends Coordinates<T>> T reflect(final T vector, final T normal) {
        final double product = GeometryCalculator.product(vector, normal);
        final double lengthSquare = GeometryCalculator.product(normal, normal);
        final double coefficient = 2.0 * product / lengthSquare;
        return CoordinatesCalculator.transform(vector,
                index -> vector.getCoordinate(index) - coefficient * normal.getCoordinate(index));
    }

    public static <T extends Coordinates<T>> Ray<T> calculateReflection(
            final Ray<T> ray, final T intersection, final T normal, final double pushDelta) {
        if((intersection == null) || (normal == null)) {
            return null;
        } else {
            final T reflectionVector = reflect(ray.getDirection(), normal);
            // todo: hack: pushing ray start out a bit
            final T reflectionStart = push(intersection, reflectionVector, pushDelta);

            return new Ray<>(reflectionStart, reflectionVector);
        }
    }

    // https://en.wikipedia.org/wiki/Snell%27s_law#Vector_form
    public static <T extends Coordinates<T>> T refract(
            final T vector, final T normal, final double coeffFrom, final double coeffTo) {
        final double vectorLength = GeometryCalculator.length(vector);
        final double normalLength = GeometryCalculator.length(normal);

        final double r = coeffFrom / coeffTo;
        final double c = -GeometryCalculator.product(vector, normal) / (vectorLength * normalLength);

        final double d = 1.0 - r * r * (1.0 - c * c);
        if(d >= 0) {
            final double nCoeff = r * c - Math.sqrt(d);
            return CoordinatesCalculator.transform(vector,
                    index -> r * vector.getCoordinate(index) + nCoeff * normal.getCoordinate(index));
        } else {
            return reflect(vector, normal);
        }
    }

    public static <T extends Coordinates<T>> Ray<T> calculateRefraction(
            final Ray<T> ray, final T intersection, final T normal,
            final double coeffFrom, final double coeffTo, final double pushDelta) {
        if((intersection == null) || (normal == null)) {
            return null;
        } else {
            final T refractionVector = refract(ray.getDirection(), normal, coeffFrom, coeffTo);
            // todo: hack: pushing ray start out a bit
            final T refractionStart = push(intersection, refractionVector, pushDelta);

            return new Ray<>(refractionStart, refractionVector);
        }
    }

    public static <T extends Coordinates<T>> List<T> generateBasis(final T reference) {
        final int dimensions = reference.getDimensions();
        final double[] coords = new double[dimensions];
        Arrays.fill(coords, 0.0);

        final List<T> vectors = new ArrayList<>();
        CoordinatesCalculator.iterate(reference, index -> {
            coords[index] = 1.0;
            vectors.add(reference.obtain(coords));
            coords[index] = 0.0;
        });

        return vectors;
    }

}

package com.lonebytesoft.hamster.raytracing.util.math;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.CoordinatesCalculator;
import com.lonebytesoft.hamster.raytracing.ray.Ray;

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

}

package com.lonebytesoft.hamster.raytracing.coordinates;

import java.util.function.Consumer;
import java.util.function.Function;

public final class CoordinatesCalculator {

    public static <T extends Coordinates<T>> void iterate(final T coordinates, final Consumer<Integer> consumer) {
        final int dimensions = coordinates.getDimensions();
        for(int i = 0; i < dimensions; i++) {
            consumer.accept(i);
        }
    }

    public static <T extends Coordinates<T>> T transform(final T coordinates, final Function<Integer, Double> transformer) {
        final double[] coords = new double[coordinates.getDimensions()];
        iterate(coordinates, index -> coords[index] = transformer.apply(index));
        return coordinates.obtain(coords);
    }

    public static <T extends Coordinates<T>> T subtract(final T minuend, final T subtrahend) {
        return transform(minuend, index -> minuend.getCoordinate(index) - subtrahend.getCoordinate(index));
    }

    public static <T extends Coordinates<T>> T negate(final T coordinates) {
        return transform(coordinates, index -> -coordinates.getCoordinate(index));
    }

    public static <T extends Coordinates<T>> T constant(final T reference, final double value) {
        return transform(reference, index -> value);
    }

    public static <T extends Coordinates<T>> Iterable<T> getWholePoints(final T min, final T max) {
        return () -> new WholeCoordinatesIterator<>(min, max);
    }

}

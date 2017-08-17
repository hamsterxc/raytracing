package com.lonebytesoft.hamster.raytracing.coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class CoordinatesCalculator {

    public static <T extends Coordinates<T>> T transform(final T coordinates, final Function<Integer, Double> transformer) {
        final int dimensions = coordinates.getDimensions();
        final double[] coords = new double[dimensions];
        for(int i = 0; i < dimensions; i++) {
            coords[i] = transformer.apply(i);
        }
        return coordinates.obtain(coords);
    }

    public static <T extends Coordinates<T>> T floor(final T coordinates) {
        return transform(coordinates, index -> Math.floor(coordinates.getCoordinate(index)));
    }

    public static <T extends Coordinates<T>> void iterate(final T coordinates, final Consumer<Integer> consumer) {
        final int dimensions = coordinates.getDimensions();
        for(int i = 0; i < dimensions; i++) {
            consumer.accept(i);
        }
    }

    public static <T extends Coordinates<T>> List<Double> collectToList(final T coordinates) {
        final List<Double> result = new ArrayList<>();
        CoordinatesCalculator.iterate(coordinates, index -> result.add(coordinates.getCoordinate(index)));
        return result;
    }

    public static <T extends Coordinates<T>> T createFromList(final List<Double> coordinates, final T reference) {
        final int dimensions = coordinates.size();
        final double[] coords = new double[dimensions];
        for(int i = 0; i < dimensions; i++) {
            coords[i] = coordinates.get(i);
        }
        return reference.obtain(coords);
    }

    public static <T extends Coordinates<T>> Iterable<T> getWholePoints(final T min, final T max) {
        return () -> new WholeCoordinatesIterator<>(min, max);
    }

}

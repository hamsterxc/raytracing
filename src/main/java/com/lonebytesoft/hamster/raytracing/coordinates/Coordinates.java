package com.lonebytesoft.hamster.raytracing.coordinates;

import java.util.Iterator;

public interface Coordinates<T extends Coordinates<T>> extends Iterable<Double>, Comparable<T> {

    int getDimensions();
    double getCoordinate(int dimension);

    T obtain(double... coordinates);

    @Override
    default Iterator<Double> iterator() {
        return new Iterator<Double>() {
            private int position = 0;

            @Override
            public boolean hasNext() {
                return position < getDimensions();
            }

            @Override
            public Double next() {
                return getCoordinate(position++);
            }
        };
    }

    @Override
    default int compareTo(T o) {
        final int dimensions = getDimensions();
        for(int index = 0; index < dimensions; index++) {
            final int result = Double.compare(getCoordinate(index), o.getCoordinate(index));
            if(result != 0) {
                return result;
            }
        }
        return 0;
    }

    default String asString() {
        final int dimensions = getDimensions();

        final StringBuilder stringBuilder = new StringBuilder("Coordinates{");
        stringBuilder.append("dimensions=").append(dimensions);
        stringBuilder.append(",coordinates=[");
        for(int i = 0; i < dimensions; i++) {
            if(i != 0) {
                stringBuilder.append(',');
            }
            stringBuilder.append(getCoordinate(i));
        }

        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

}

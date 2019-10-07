package com.lonebytesoft.hamster.raytracing.coordinates;

import java.util.Iterator;

public interface Coordinates extends Iterable<Double> {

    int getDimensions();
    double getCoordinate(int index);

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

}

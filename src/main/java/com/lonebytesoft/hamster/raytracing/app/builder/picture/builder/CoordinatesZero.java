package com.lonebytesoft.hamster.raytracing.app.builder.picture.builder;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

import java.util.Iterator;

class CoordinatesZero implements Coordinates<CoordinatesZero> {

    private final int dimensions;

    public CoordinatesZero(final int dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public int getDimensions() {
        return dimensions;
    }

    @Override
    public double getCoordinate(int dimension) {
        return 0.0;
    }

    @Override
    public CoordinatesZero obtain(double... coordinates) {
        return new CoordinatesZero(coordinates.length);
    }

    @Override
    public Iterator<Double> iterator() {
        return new Iterator<Double>() {
            private int position = 0;

            @Override
            public boolean hasNext() {
                return position < dimensions;
            }

            @Override
            public Double next() {
                position++;
                return 0.0;
            }
        };
    }

}

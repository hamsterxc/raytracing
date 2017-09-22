package com.lonebytesoft.hamster.raytracing.coordinates;

import java.util.Arrays;
import java.util.Objects;

abstract class CoordinatesImpl<T extends Coordinates<T>> implements Coordinates<T> {

    private final int dimensions;
    private final double[] coordinates;

    public CoordinatesImpl(final double... coordinates) {
        if((coordinates == null) || (coordinates.length == 0)) {
            throw new IllegalArgumentException("Empty coordinates");
        }

        this.dimensions = coordinates.length;
        this.coordinates = Arrays.copyOf(coordinates, coordinates.length);
    }

    public int getDimensions() {
        return dimensions;
    }

    public double getCoordinate(int dimension) {
        if((dimension >= 0) && (dimension < dimensions)) {
            return coordinates[dimension];
        } else {
            throw new IllegalArgumentException("Dimension index out of bounds: " + dimension);
        }
    }

    protected final void assertDimensionality(final double... coordinates) {
        if(coordinates.length != dimensions) {
            throw new IllegalArgumentException("Cannot create coordinates of another dimensionality:" +
                    " own = " + dimensions + ", asked = " + coordinates.length);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinatesImpl that = (CoordinatesImpl) o;
        return dimensions == that.dimensions &&
                Arrays.equals(coordinates, that.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimensions, coordinates);
    }

    @Override
    public String toString() {
        return "CoordinatesImpl{" +
                "dimensions=" + dimensions +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }

}

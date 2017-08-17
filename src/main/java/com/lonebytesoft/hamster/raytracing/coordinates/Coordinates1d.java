package com.lonebytesoft.hamster.raytracing.coordinates;

public class Coordinates1d extends CoordinatesImpl<Coordinates1d> {

    public Coordinates1d(final double x) {
        super(x);
    }

    @Override
    public Coordinates1d obtain(double... coordinates) {
        assertDimensionality(coordinates);
        return new Coordinates1d(coordinates[0]);
    }

    public double getX() {
        return getCoordinate(0);
    }

}

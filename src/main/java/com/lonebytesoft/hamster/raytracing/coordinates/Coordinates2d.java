package com.lonebytesoft.hamster.raytracing.coordinates;

public class Coordinates2d extends CoordinatesImpl<Coordinates2d> {

    public Coordinates2d(final double x, final double y) {
        super(x, y);
    }

    @Override
    public Coordinates2d obtain(double... coordinates) {
        assertDimensionality(coordinates);
        return new Coordinates2d(coordinates[0], coordinates[1]);
    }

    public double getX() {
        return getCoordinate(0);
    }

    public double getY() {
        return getCoordinate(1);
    }

}

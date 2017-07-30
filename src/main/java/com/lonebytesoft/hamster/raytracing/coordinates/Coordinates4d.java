package com.lonebytesoft.hamster.raytracing.coordinates;

public class Coordinates4d extends CoordinatesImpl<Coordinates4d> {

    public Coordinates4d(final double x, final double y, final double z, final double t) {
        super(x, y, z, t);
    }

    @Override
    public Coordinates4d obtain(double... coordinates) {
        assertDimensionality(coordinates);
        return new Coordinates4d(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
    }

    public double getX() {
        return getCoordinate(0);
    }

    public double getY() {
        return getCoordinate(1);
    }

    public double getZ() {
        return getCoordinate(2);
    }

    public double getT() {
        return getCoordinate(3);
    }

}

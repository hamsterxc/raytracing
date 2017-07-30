package com.lonebytesoft.hamster.raytracing.coordinates;

public class Coordinates3d extends CoordinatesImpl<Coordinates3d> {

    public Coordinates3d(final double x, final double y, final double z) {
        super(x, y, z);
    }

    @Override
    public Coordinates3d obtain(double... coordinates) {
        assertDimensionality(coordinates);
        return new Coordinates3d(coordinates[0], coordinates[1], coordinates[2]);
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

}

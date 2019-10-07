package com.lonebytesoft.hamster.raytracing.coordinates.factory;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates3d;

public class Coordinates3dFactory extends AbstractCoordinatesFactory<Coordinates3d> {

    @Override
    public int getDimensions() {
        return 3;
    }

    @Override
    public Coordinates3d build(double... coordinates) {
        checkDimensions(coordinates);
        return build(coordinates[0], coordinates[1], coordinates[2]);
    }

    public Coordinates3d build(final double x, final double y, final double z) {
        return new Coordinates3DImpl(x, y, z);
    }

    private static class Coordinates3DImpl implements Coordinates3d {

        private final double x;
        private final double y;
        private final double z;

        public Coordinates3DImpl(final double x, final double y, final double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public double getX() {
            return x;
        }

        @Override
        public double getY() {
            return y;
        }

        @Override
        public double getZ() {
            return z;
        }

    }

}

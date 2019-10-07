package com.lonebytesoft.hamster.raytracing.coordinates.factory;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates2d;

public class Coordinates2dFactory extends AbstractCoordinatesFactory<Coordinates2d> {

    @Override
    public int getDimensions() {
        return 2;
    }

    @Override
    public Coordinates2d build(double... coordinates) {
        checkDimensions(coordinates);
        return build(coordinates[0], coordinates[1]);
    }

    public Coordinates2d build(double x, double y) {
        return new Coordinates2DImpl(x, y);
    }

    private static class Coordinates2DImpl implements Coordinates2d {

        private final double x;
        private final double y;

        public Coordinates2DImpl(final double x, final double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double getX() {
            return x;
        }

        @Override
        public double getY() {
            return y;
        }

    }

}

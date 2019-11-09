package com.lonebytesoft.hamster.raytracing.coordinates.factory;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates1d;

public class Coordinates1dFactory extends AbstractCoordinatesFactory<Coordinates1d> {

    @Override
    public int getDimensions() {
        return 1;
    }

    @Override
    public Coordinates1d build(double... coordinates) {
        checkDimensions(coordinates);
        return build(coordinates[0]);
    }

    public Coordinates1d build(double x) {
        return new Coordinates1DImpl(x);
    }

    private static class Coordinates1DImpl implements Coordinates1d {

        private final double x;

        public Coordinates1DImpl(final double x) {
            this.x = x;
        }

        @Override
        public double getX() {
            return x;
        }

    }

}

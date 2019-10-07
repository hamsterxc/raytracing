package com.lonebytesoft.hamster.raytracing.coordinates.factory;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

abstract class AbstractCoordinatesFactory<T extends Coordinates> implements CoordinatesFactory<T> {

    protected void checkDimensions(final double... coordinates) {
        final int expected = getDimensions();
        final int actual = coordinates.length;
        if (actual != expected) {
            throw new IllegalArgumentException("Illegal number of coordinates: expected " + expected + ", actual " + actual);
        }
    }

}

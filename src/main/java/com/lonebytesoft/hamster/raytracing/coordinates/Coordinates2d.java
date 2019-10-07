package com.lonebytesoft.hamster.raytracing.coordinates;

public interface Coordinates2d extends Coordinates {

    double getX();
    double getY();

    @Override
    default int getDimensions() {
        return 2;
    }

    @Override
    default double getCoordinate(int index) {
        switch (index) {
            case 0: return getX();
            case 1: return getY();
            default: throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

}

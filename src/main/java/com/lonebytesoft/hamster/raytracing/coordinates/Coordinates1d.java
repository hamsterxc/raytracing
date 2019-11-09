package com.lonebytesoft.hamster.raytracing.coordinates;

public interface Coordinates1d extends Coordinates {

    double getX();

    @Override
    default int getDimensions() {
        return 1;
    }

    @Override
    default double getCoordinate(int index) {
        switch (index) {
            case 0: return getX();
            default: throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

}

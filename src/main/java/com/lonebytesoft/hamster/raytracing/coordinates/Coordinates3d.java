package com.lonebytesoft.hamster.raytracing.coordinates;

public interface Coordinates3d extends Coordinates {

    double getX();
    double getY();
    double getZ();

    @Override
    default int getDimensions() {
        return 3;
    }

    @Override
    default double getCoordinate(int index) {
        switch (index) {
            case 0: return getX();
            case 1: return getY();
            case 2: return getZ();
            default: throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

}

package com.lonebytesoft.hamster.raytracing.coordinates.factory;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public interface CoordinatesFactory<T extends Coordinates> {

    int getDimensions();
    T build(double... coordinates);

}

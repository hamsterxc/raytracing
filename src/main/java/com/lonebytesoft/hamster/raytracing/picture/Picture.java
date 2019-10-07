package com.lonebytesoft.hamster.raytracing.picture;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public interface Picture<T extends Coordinates> {

    Color getPixelColor(T pixelCoordinates);
    Iterable<T> getAllPixelCoordinates();

}

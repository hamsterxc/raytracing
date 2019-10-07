package com.lonebytesoft.hamster.raytracing.picture;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public interface PictureMutable<T extends Coordinates> extends Picture<T> {

    void setPixelColor(T pixelCoordinates, Color color);

}

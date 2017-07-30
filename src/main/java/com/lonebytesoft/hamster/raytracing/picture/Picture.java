package com.lonebytesoft.hamster.raytracing.picture;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

import java.util.Iterator;

public interface Picture<T extends Coordinates<T>> {

    Color getPixelColor(T pixelCoordinates);

    Iterator<T> getAllPixelCoordinates();

}

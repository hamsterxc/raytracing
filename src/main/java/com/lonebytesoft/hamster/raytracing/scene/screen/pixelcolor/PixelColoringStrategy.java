package com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.Orthotope;

import java.util.function.Function;

public interface PixelColoringStrategy<T extends Coordinates<T>> {

    Color getPixelColor(Orthotope<T> pixelBoundaries, Function<T, Color> coloring);

}

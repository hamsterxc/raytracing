package com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;

import java.util.function.Function;

public interface PixelColoringStrategy<T extends Coordinates> {

    Color getPixelColor(Surfaced<T> pixelBoundaries, Function<T, Color> coloring);

}

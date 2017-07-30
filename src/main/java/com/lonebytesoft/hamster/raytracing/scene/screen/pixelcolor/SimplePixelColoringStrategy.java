package com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.CoordinatesCalculator;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.Orthotope;

import java.util.function.Function;

public class SimplePixelColoringStrategy<T extends Coordinates<T>> implements PixelColoringStrategy<T> {

    @Override
    public Color getPixelColor(Orthotope<T> pixelBoundaries, Function<T, Color> coloring) {
        final T proportion = CoordinatesCalculator.transform(pixelBoundaries.getBase(), index -> 0.5);
        final T coordinates = pixelBoundaries.calculatePoint(proportion);
        return coloring.apply(coordinates);
    }

}

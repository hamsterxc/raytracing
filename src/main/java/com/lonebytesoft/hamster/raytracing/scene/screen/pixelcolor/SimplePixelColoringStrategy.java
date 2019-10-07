package com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;

import java.util.function.Function;

public class SimplePixelColoringStrategy<T extends Coordinates> implements PixelColoringStrategy<T> {

    private final T sample;

    public SimplePixelColoringStrategy(final GeometryCalculator<T> geometryCalculator) {
        this.sample = geometryCalculator.buildVector(index -> 0.5);
    }

    @Override
    public Color getPixelColor(Surfaced<T> pixelBoundaries, Function<T, Color> coloring) {
        final T coordinates = pixelBoundaries.mapFromSurface(sample);
        return coloring.apply(coordinates);
    }

}

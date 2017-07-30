package com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.color.ColorCalculator;
import com.lonebytesoft.hamster.raytracing.color.ColorWeighted;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.Orthotope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class SupersamplingPixelColoringStrategy<T extends Coordinates<T>> implements PixelColoringStrategy<T> {

    private final int multiplier;
    private final Color colorDefault;

    public SupersamplingPixelColoringStrategy(final int multiplier, final Color colorDefault) {
        if(multiplier < 1) {
            throw new IllegalArgumentException("Invalid sampling multiplier: " + multiplier);
        }
        this.multiplier = multiplier;
        this.colorDefault = colorDefault;
    }

    @Override
    public Color getPixelColor(Orthotope<T> pixelBoundaries, Function<T, Color> coloring) {
        final Collection<ColorWeighted> colors = new ArrayList<>();
        final T base = pixelBoundaries.getBase();
        final int dimensions = base.getDimensions();
        final long sampleCount = (long) Math.pow(multiplier, dimensions);
        final double[] coords = new double[dimensions];

        for(long sample = 0; sample < sampleCount; sample++) {
            long index = sample;
            for(int i = 0; i < dimensions; i++) {
                final long position = index % dimensions;
                coords[i] = (2.0 * position + 1.0) / (2.0 * multiplier);
                index /= dimensions;
            }

            final T proportion = base.obtain(coords);
            final T coordinates = pixelBoundaries.calculatePoint(proportion);
            final Color color = coloring.apply(coordinates);
            colors.add(new ColorWeighted(color == null ? colorDefault : color, 1.0));
        }

        return ColorCalculator.combine(colors);
    }

}

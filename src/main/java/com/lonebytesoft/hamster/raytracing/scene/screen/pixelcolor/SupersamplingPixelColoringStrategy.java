package com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.color.ColorCalculator;
import com.lonebytesoft.hamster.raytracing.color.ColorWeighted;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;
import com.lonebytesoft.hamster.raytracing.util.variant.Options;
import com.lonebytesoft.hamster.raytracing.util.variant.Variant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class SupersamplingPixelColoringStrategy<T extends Coordinates<T>> implements PixelColoringStrategy<T> {

    private final Color colorDefault;
    private final Collection<T> samples = new ArrayList<>();

    public SupersamplingPixelColoringStrategy(final int multiplier, final Color colorDefault, final T reference) {
        if(multiplier < 1) {
            throw new IllegalArgumentException("Invalid sampling multiplier: " + multiplier);
        }

        this.colorDefault = colorDefault;

        final int dimensions = reference.getDimensions();
        final Variant samplesVariant = new Options(dimensions, multiplier);
        final double[] coords = new double[dimensions];
        Variant.iterate(samplesVariant, (index, sample) -> {
            for(int i = 0; i < dimensions; i++) {
                coords[i] = (2.0 * sample.get(i) + 1.0) / (2.0 * multiplier);
            }
            this.samples.add(reference.obtain(coords));
        });
    }

    @Override
    public Color getPixelColor(Surfaced<T> pixelBoundaries, Function<T, Color> coloring) {
        final Collection<ColorWeighted> colors = new ArrayList<>();
        for(final T sample : samples) {
            final T coordinates = pixelBoundaries.mapFromSurface(sample);
            final Color color = coloring.apply(coordinates);
            colors.add(new ColorWeighted(color == null ? colorDefault : color, 1.0));
        }

        return ColorCalculator.combine(colors);
    }

}

package com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.color.ColorCalculator;
import com.lonebytesoft.hamster.raytracing.color.ColorWeighted;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import com.lonebytesoft.hamster.raytracing.util.variant.Option;
import com.lonebytesoft.hamster.raytracing.util.variant.Variant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SupersamplingPixelColoringStrategy<T extends Coordinates> implements PixelColoringStrategy<T> {

    private final Color colorDefault;
    private final Collection<T> samples;

    public SupersamplingPixelColoringStrategy(
            final int multiplier,
            final Color colorDefault,
            final GeometryCalculator<T> geometryCalculator
    ) {
        if(multiplier < 1) {
            throw new IllegalArgumentException("Invalid sampling multiplier: " + multiplier);
        }

        this.colorDefault = colorDefault;

        final int dimensions = geometryCalculator.buildVector(index -> 0.0).getDimensions();
        final Variant samples = new Option(dimensions, multiplier);
        this.samples = StreamSupport.stream(samples.spliterator(), false)
                .map(sample -> geometryCalculator.buildVector(index -> (2.0 * sample.get(index) + 1.0) / (2.0 * multiplier)))
                .collect(Collectors.toList());
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

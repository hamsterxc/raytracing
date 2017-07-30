package com.lonebytesoft.hamster.raytracing.scene.screen;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.CoordinatesCalculator;
import com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor.PixelColoringStrategy;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.GeneralOrthotope;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.Orthotope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ScreenOrthotope<S extends Coordinates<S>, F extends Coordinates<F>> extends AbstractScreen<S, F> {

    private final GeneralOrthotope<S> surface;
    private final F resolution;

    private final List<F> vectors;

    public ScreenOrthotope(final GeneralOrthotope<S> surface,
                           final F resolution, final PixelColoringStrategy<F> coloringStrategy) {
        super(resolution, coloringStrategy);
        this.surface = surface;
        this.resolution = resolution;

        final List<F> vectors = new ArrayList<>();
        CoordinatesCalculator.iterate(resolution, index -> {
            final F vector = CoordinatesCalculator.transform(resolution, i -> Objects.equals(i, index) ? 1.0 : 0.0);
            vectors.add(vector);
        });
        this.vectors = Collections.unmodifiableList(vectors);
    }

    @Override
    protected Orthotope<F> getPixelBoundaries(F pixelCoordinates) {
        CoordinatesCalculator.iterate(pixelCoordinates, index -> {
            final double coordinate = pixelCoordinates.getCoordinate(index);
            if((coordinate < 0) || (coordinate >= resolution.getCoordinate(index))) {
                throw new IllegalArgumentException("Pixel coordinate #" + index + " out of bounds");
            }
        });

        return new Orthotope<>(CoordinatesCalculator.floor(pixelCoordinates), vectors);
    }

    @Override
    protected S translate(F coordinates) {
        final F proportions = CoordinatesCalculator.transform(coordinates,
                index -> coordinates.getCoordinate(index) / resolution.getCoordinate(index));
        return surface.calculatePoint(proportions);
    }

}

package com.lonebytesoft.hamster.raytracing.scene.screen;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.CoordinatesCalculator;
import com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor.PixelColoringStrategy;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.Orthotope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ScreenShapeSurfaced<S extends Coordinates<S>, F extends Coordinates<F>> extends AbstractScreen<S, F> {

    private final Surfaced<S> surfaced;
    private final F from;
    private final F resolution;
    private final List<F> vectors;

    public ScreenShapeSurfaced(final Surfaced<S> surfaced, final F from, final F to,
                               final F resolution, final PixelColoringStrategy<F> coloringStrategy) {
        super(resolution, coloringStrategy);
        this.surfaced = surfaced;
        this.from = from;
        this.resolution = resolution;

        final List<F> vectors = new ArrayList<>();
        CoordinatesCalculator.iterate(resolution, index -> {
            final F vector = CoordinatesCalculator.transform(resolution,
                    i -> Objects.equals(i, index)
                            ? (to.getCoordinate(i) - from.getCoordinate(i)) / resolution.getCoordinate(i)
                            : 0.0);
            vectors.add(vector);
        });
        this.vectors = Collections.unmodifiableList(vectors);
    }

    @Override
    protected Surfaced<F> getPixelBoundaries(F pixelCoordinates) {
        CoordinatesCalculator.iterate(pixelCoordinates, index -> {
            final double coordinate = pixelCoordinates.getCoordinate(index);
            if((coordinate < 0) || (coordinate >= resolution.getCoordinate(index))) {
                throw new IllegalArgumentException("Pixel coordinate #" + index + " out of bounds: " + pixelCoordinates);
            }
        });

        final int dimensions = pixelCoordinates.getDimensions();
        return new Orthotope<>(CoordinatesCalculator.transform(pixelCoordinates, index -> {
            double coordinate = from.getCoordinate(index);
            for(int i = 0; i < dimensions; i++) {
                coordinate += Math.floor(pixelCoordinates.getCoordinate(index)) * vectors.get(i).getCoordinate(index);
            }
            return coordinate;
        }), vectors);
    }

    @Override
    protected S translate(F coordinates) {
        return surfaced.mapFromSurface(coordinates);
    }

}

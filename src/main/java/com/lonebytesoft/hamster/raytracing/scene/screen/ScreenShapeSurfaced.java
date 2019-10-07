package com.lonebytesoft.hamster.raytracing.scene.screen;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor.PixelColoringStrategy;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.Orthotope;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ScreenShapeSurfaced<S extends Coordinates, F extends Coordinates> extends AbstractScreen<S, F> {

    private final Surfaced<S> surfaced;
    private final F from;
    private final F resolution;
    private final GeometryCalculator<F> geometryCalculator;

    private final List<F> vectors;

    public ScreenShapeSurfaced(
            final Surfaced<S> surfaced,
            final F from,
            final F to,
            final F resolution,
            final PixelColoringStrategy<F> coloringStrategy,
            final GeometryCalculator<F> geometryCalculator
    ) {
        super(resolution, coloringStrategy, geometryCalculator);
        this.surfaced = surfaced;
        this.from = from;
        this.resolution = resolution;
        this.geometryCalculator = geometryCalculator;

        this.vectors = IntStream.range(0, resolution.getDimensions())
                .mapToObj(index -> geometryCalculator.buildVector(
                        i -> i == index ? (to.getCoordinate(i) - from.getCoordinate(i)) / resolution.getCoordinate(i) : 0.0
                ))
                .collect(Collectors.toList());
    }

    @Override
    protected Surfaced<F> getPixelBoundaries(F pixelCoordinates) {
        IntStream.range(0, pixelCoordinates.getDimensions())
                .forEach(index -> {
                    final double coordinate = pixelCoordinates.getCoordinate(index);
                    if((coordinate < 0) || (coordinate >= resolution.getCoordinate(index))) {
                        throw new IllegalArgumentException("Pixel coordinate #" + index + " out of bounds: " + pixelCoordinates);
                    }
                });

        final int dimensions = pixelCoordinates.getDimensions();
        return new Orthotope<>(geometryCalculator.buildVector(index -> {
            double coordinate = from.getCoordinate(index);
            for(int i = 0; i < dimensions; i++) {
                coordinate += Math.floor(pixelCoordinates.getCoordinate(index)) * vectors.get(i).getCoordinate(index);
            }
            return coordinate;
        }), vectors, geometryCalculator);
    }

    @Override
    protected S translate(F coordinates) {
        return surfaced.mapFromSurface(coordinates);
    }

}

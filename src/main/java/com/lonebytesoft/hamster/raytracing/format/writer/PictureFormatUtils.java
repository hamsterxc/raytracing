package com.lonebytesoft.hamster.raytracing.format.writer;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.Orthotope;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class PictureFormatUtils {

    public static <T extends Coordinates> Orthotope<T> calculateBounds(
            final Picture<T> picture,
            final GeometryCalculator<T> geometryCalculator
    ) {
        final List<Double> base = new ArrayList<>();
        final List<Double> delta = new ArrayList<>();

        for (final T coordinates : picture.getAllPixelCoordinates()) {
            final IntStream indices = IntStream.range(0, coordinates.getDimensions());
            if (base.isEmpty()) {
                indices
                        .forEach(index -> {
                            base.add((double) Long.MAX_VALUE);
                            delta.add(0.0);
                        });
            } else {
                indices
                        .forEach(index -> {
                            final long coord = Math.round(coordinates.getCoordinate(index));
                            base.set(index, Math.min(base.get(index), coord));
                            delta.set(index, Math.max(delta.get(index), coord - base.get(index)));
                        });
            }
        }

        final T baseVector = geometryCalculator.buildVector(base::get);
        final List<T> vectors = IntStream.range(0, delta.size())
                .mapToObj(index -> geometryCalculator.buildVector(i -> i == index ? delta.get(index) : 0.0))
                .collect(Collectors.toList());
        return new Orthotope<>(baseVector, vectors, geometryCalculator);
    }

}

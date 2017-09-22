package com.lonebytesoft.hamster.raytracing.format.writer;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.Orthotope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class PictureFormatUtils {

    public static <T extends Coordinates<T>> Orthotope<T> calculateBounds(final Picture<T> picture) {
        int dimensions = 0;
        double[] base = null;
        double[] delta = null;
        T reference = null;

        for(final T coordinates : picture.getAllPixelCoordinates()) {
            if(base == null) {
                dimensions = coordinates.getDimensions();
                base = new double[dimensions];
                Arrays.fill(base, Long.MAX_VALUE);
                delta = new double[dimensions];
                Arrays.fill(delta, 0);
                reference = coordinates;
            }

            for(int i = 0; i < dimensions; i++) {
                final long coord = Math.round(coordinates.getCoordinate(i));
                base[i] = Math.min(base[i], coord);
                delta[i] = Math.max(delta[i], coord - base[i]);
            }
        }
        if(reference == null) {
            throw new IllegalArgumentException("Empty picture");
        }

        final List<T> vectors = new ArrayList<>(dimensions);
        for(int i = 0; i < dimensions; i++) {
            final double[] coords = new double[dimensions];
            for(int j = 0; j < dimensions; j++) {
                coords[j] = j == i ? delta[i] : 0.0;
            }

            vectors.add(reference.obtain(coords));
        }
        return new Orthotope<>(reference.obtain(base), vectors);
    }

}

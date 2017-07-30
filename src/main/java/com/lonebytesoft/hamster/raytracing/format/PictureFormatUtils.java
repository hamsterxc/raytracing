package com.lonebytesoft.hamster.raytracing.format;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import com.lonebytesoft.hamster.raytracing.util.Utils;

final class PictureFormatUtils {

    public static <T extends Coordinates<T>> T calculateSize(final Picture<T> picture) {
        int dimensions = 0;
        double[] coords = null;
        T reference = null;

        for(final T coordinates : Utils.obtainIterable(picture.getAllPixelCoordinates())) {
            if(coords == null) {
                dimensions = coordinates.getDimensions();
                coords = new double[dimensions];
                reference = coordinates;
            }

            for(int i = 0; i < dimensions; i++) {
                final long coord = Math.round(coordinates.getCoordinate(i));
                if(coord < 0) {
                    throw new IllegalArgumentException("Unexpected pixel coordinates: " + coordinates);
                }
                coords[i] = Math.max(coords[i], coord);
            }
        }
        if(reference == null) {
            throw new IllegalArgumentException("Empty picture");
        }

        for(int i = 0; i < dimensions; i++) {
            coords[i]++;
        }

        return reference.obtain(coords);
    }

}

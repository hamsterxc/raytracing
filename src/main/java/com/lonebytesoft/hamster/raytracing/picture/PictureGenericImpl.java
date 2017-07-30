package com.lonebytesoft.hamster.raytracing.picture;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.CoordinatesCalculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class PictureGenericImpl<T extends Coordinates<T>> implements PictureMutable<T> {

    private final T size;
    private final Color colorDefault;
    private final Map<List<Double>, Color> pixelColors = new ConcurrentHashMap<>();

    public PictureGenericImpl(final T size) {
        this.size = CoordinatesCalculator.floor(size);
        this.colorDefault = null;
    }

    public PictureGenericImpl(final T size, final Color colorDefault) {
        this.size = CoordinatesCalculator.floor(size);
        this.colorDefault = colorDefault;
    }

    @Override
    public Color getPixelColor(T pixelCoordinates) {
        assertCoordinatesBoundaries(pixelCoordinates, size);

        return pixelColors.getOrDefault(convert(pixelCoordinates), colorDefault);
    }

    @Override
    public void setPixelColor(final T pixelCoordinates, final Color pixelColor) {
        assertCoordinatesBoundaries(pixelCoordinates, size);

        if(pixelColor != null) {
            pixelColors.put(convert(pixelCoordinates), pixelColor);
        }
    }

    @Override
    public Iterator<T> getAllPixelCoordinates() {
        return new CoordinatesIterator(size);
    }

    private void assertCoordinatesBoundaries(final T coordinates, final T size) {
        CoordinatesCalculator.iterate(coordinates, index -> {
            final double coordinate = coordinates.getCoordinate(index);
            if((coordinate < 0) || (coordinate > size.getCoordinate(index) - 1)) {
                throw new IllegalArgumentException("Coordinate " + index + " out of bounds");
            }
        });
    }

    private List<Double> convert(final T coordinates) {
        final List<Double> result = new ArrayList<>();
        CoordinatesCalculator.iterate(coordinates, index -> result.add(coordinates.getCoordinate(index)));
        return result;
    }

    private class CoordinatesIterator implements Iterator<T> {
        private final T size;
        private T current;

        public CoordinatesIterator(final T size) {
            this.size = size;
            this.current = CoordinatesCalculator.transform(size, index -> 0.0);
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            final T next = current;

            final int dimensions = size.getDimensions();
            final double[] coords = new double[dimensions];
            boolean enough = false;
            for(int i = 0; i < dimensions; i++) {
//            for(int i = dimensions - 1; i >= 0; i--) {
                coords[i] = current.getCoordinate(i);
                if(!enough) {
                    coords[i] += 1;
                    if(coords[i] > size.getCoordinate(i) - 1) {
                        if(i < dimensions - 1) {
//                        if(i > 0) {
                            coords[i] = 0;
                        } else {
                            current = null;
                            break;
                        }
                    } else {
                        enough = true;
                    }
                }
            }
            if(current != null) {
                current = current.obtain(coords);
            }

            return next;
        }
    }

}

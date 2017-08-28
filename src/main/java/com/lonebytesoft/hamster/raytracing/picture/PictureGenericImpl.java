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

    private final T reference;
    private final Map<List<Double>, Color> pixelColors = new ConcurrentHashMap<>();

    public PictureGenericImpl(final T reference) {
        this.reference = reference;
    }

    @Override
    public Color getPixelColor(T pixelCoordinates) {
        return pixelColors.get(convertToList(pixelCoordinates));
    }

    @Override
    public void setPixelColor(final T pixelCoordinates, final Color pixelColor) {
        if(pixelColor != null) {
            pixelColors.put(convertToList(pixelCoordinates), pixelColor);
        }
    }

    private List<Double> convertToList(final T coordinates) {
        final List<Double> result = new ArrayList<>();
        for(final double coordinate : coordinates) {
            result.add(Math.floor(coordinate));
        }
        return result;
    }

    @Override
    public Iterator<T> getAllPixelCoordinates() {
        return new CoordinatesIterator();
    }

    private class CoordinatesIterator implements Iterator<T> {

        private final Iterator<List<Double>> iterator = pixelColors.keySet().iterator();

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            final List<Double> coords = iterator.next();
            return CoordinatesCalculator.transform(reference, coords::get);
        }

    }

}

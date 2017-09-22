package com.lonebytesoft.hamster.raytracing.picture;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

import java.util.Iterator;

class Picture2dAdapter<T extends Coordinates<T>> implements PictureMutable<T> {

    private final T reference;
    private final Picture2dImpl picture;

    public Picture2dAdapter(final T reference) {
        this.reference = reference;
        this.picture = new Picture2dImpl();
    }

    @Override
    public Color getPixelColor(T pixelCoordinates) {
        return picture.getPixelColor(
                getCoordinate(pixelCoordinates, 0),
                getCoordinate(pixelCoordinates, 1));
    }

    @Override
    public void setPixelColor(T pixelCoordinates, Color color) {
        picture.setPixelColor(
                getCoordinate(pixelCoordinates, 0),
                getCoordinate(pixelCoordinates, 1),
                color);
    }

    private int getCoordinate(final T coordinates, final int dimension) {
        return (int) Math.floor(coordinates.getCoordinate(dimension));
    }

    @Override
    public Iterable<T> getAllPixelCoordinates() {
        return () -> new CoordinatesIterator(picture.getAllPixelCoordinates());
    }

    private class CoordinatesIterator implements Iterator<T> {

        private final Iterator<int[]> iterator;

        public CoordinatesIterator(final Iterator<int[]> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            final int[] next = iterator.next();
            return reference.obtain(next[0], next[1]);
        }

    }

}

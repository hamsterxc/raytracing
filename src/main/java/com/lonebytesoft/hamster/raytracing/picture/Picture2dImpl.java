package com.lonebytesoft.hamster.raytracing.picture;

import com.lonebytesoft.hamster.raytracing.color.Color;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Picture2dImpl {

    private final Map<Integer, Map<Integer, Color>> pixels = new ConcurrentHashMap<>();

    public Color getPixelColor(final int x, final int y) {
        final Map<Integer, Color> line = pixels.get(x);
        return line == null ? null : line.get(y);
    }

    public void setPixelColor(final int x, final int y, final Color color) {
        pixels
                .computeIfAbsent(x, index -> new ConcurrentHashMap<>())
                .put(y, color);
    }

    public Iterator<int[]> getAllPixelCoordinates() {
        return new CoordinatesIterator();
    }

    private class CoordinatesIterator implements Iterator<int[]> {

        private final Iterator<Integer> outer;
        private Iterator<Integer> inner;
        private int current;

        public CoordinatesIterator() {
            outer = pixels.keySet().iterator();
            inner = new Iterator<Integer>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public Integer next() {
                    return null;
                }
            };
        }

        @Override
        public boolean hasNext() {
            return inner.hasNext() || outer.hasNext();
        }

        @Override
        public int[] next() {
            if(!inner.hasNext()) {
                current = outer.next();
                inner = pixels.get(current).keySet().iterator();
            }
            return new int[]{current, inner.next()};
        }

    }

}

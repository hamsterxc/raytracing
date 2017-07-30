package com.lonebytesoft.hamster.raytracing.picture;

import com.lonebytesoft.hamster.raytracing.color.Color;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Picture2dImpl {

    private final int width;
    private final int height;
    private final Map<Integer, Map<Integer, Color>> pixels;

    public Picture2dImpl(final int width, final int height) {
        this.width = width;
        this.height = height;

        this.pixels = new ConcurrentHashMap<>(width);
        for(int i = 0; i < width; i++) {
            pixels.put(i, new ConcurrentHashMap<>(height));
        }
    }

    public Color getPixelColor(final int x, final int y) {
        final Map<Integer, Color> line = pixels.get(x);
        return line == null ? null : line.get(y);
    }

    public void setPixelColor(final int x, final int y, final Color color) {
        if((x >= 0) && (x < width) && (y >= 0) && (y < height)) {
            pixels.get(x).put(y, color);
        }
    }

    public Iterator<int[]> getAllPixelCoordinates() {
        return new CoordinatesIterator(width, height);
    }

    private class CoordinatesIterator implements Iterator<int[]> {

        private final int width;
        private final int height;

        private int x = 0;
        private int y = 0;

        public CoordinatesIterator(final int width, final int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public boolean hasNext() {
            return (x < width - 1) || (x == width - 1) && (y < height - 1);
        }

        @Override
        public int[] next() {
            final int[] next = new int[]{x, y};

            x++;
            if(x >= width) {
                x = 0;
                y++;
            }

            return next;
        }

    }

}

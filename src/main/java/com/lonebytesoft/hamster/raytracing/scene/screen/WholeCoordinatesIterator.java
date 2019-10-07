package com.lonebytesoft.hamster.raytracing.scene.screen;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

class WholeCoordinatesIterator<T extends Coordinates> implements Iterator<T> {

    private final T min;
    private final T max;
    private final GeometryCalculator<T> geometryCalculator;

    private double[] current;

    public WholeCoordinatesIterator(final T min, final T max, final GeometryCalculator<T> geometryCalculator) {
        this.min = min;
        this.max = max;
        this.geometryCalculator = geometryCalculator;

        this.current = IntStream.range(0, min.getDimensions())
                .mapToDouble(index -> Math.ceil(min.getCoordinate(index)))
                .toArray();
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public T next() {
        if(current == null) {
            throw new NoSuchElementException();
        }
        final T next = geometryCalculator.buildVector(index -> current[index]);

        for(int i = 0; i < current.length; i++) {
            current[i] += 1;
            if(current[i] >= max.getCoordinate(i)) {
                if(i < current.length - 1) {
                    current[i] = Math.ceil(min.getCoordinate(i));
                } else {
                    current = null;
                    break;
                }
            } else {
                break;
            }
        }

        return next;
    }

}

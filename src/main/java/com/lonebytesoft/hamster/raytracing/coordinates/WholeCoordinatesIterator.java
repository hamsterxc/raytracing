package com.lonebytesoft.hamster.raytracing.coordinates;

import java.util.Iterator;
import java.util.NoSuchElementException;

class WholeCoordinatesIterator<T extends Coordinates<T>> implements Iterator<T> {

    private final T min;
    private final T max;

    private double[] current;

    public WholeCoordinatesIterator(final T min, final T max) {
        this.min = min;
        this.max = max;

        this.current = new double[min.getDimensions()];
        CoordinatesCalculator.iterate(min,
                index -> current[index] = Math.ceil(min.getCoordinate(index)));
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
        final T next = min.obtain(current);

        for(int i = 0; i < current.length; i++) {
            current[i] += 1;
            if(current[i] > max.getCoordinate(i)) {
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

package com.lonebytesoft.hamster.raytracing.ray;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public class Ray<T extends Coordinates> {

    private final T start;
    private final T direction;

    public Ray(final T start, final T direction) {
        this.start = start;
        this.direction = direction;
    }

    public T getStart() {
        return start;
    }

    public T getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "start=" + start +
                ", direction=" + direction +
                '}';
    }

}

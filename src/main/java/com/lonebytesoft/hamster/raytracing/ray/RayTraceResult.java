package com.lonebytesoft.hamster.raytracing.ray;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

import java.util.Collection;
import java.util.Collections;

public class RayTraceResult<T extends Coordinates<T>> {

    private final Collection<RayTraceResultItemWeighted<T>> items;
    private final double distance;

    private T normal;

    public RayTraceResult(final Collection<RayTraceResultItemWeighted<T>> items, final double distance) {
        this.items = Collections.unmodifiableCollection(items);
        this.distance = distance;
    }

    public Collection<RayTraceResultItemWeighted<T>> getItems() {
        return items;
    }

    public double getDistance() {
        return distance;
    }

    public T getNormal() {
        return normal;
    }

    public void setNormal(T normal) {
        this.normal = normal;
    }

    @Override
    public String toString() {
        return "RayTraceResult{" +
                "items=" + items +
                ", distance=" + distance +
                ", normal=" + normal +
                '}';
    }

}

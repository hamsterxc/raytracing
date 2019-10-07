package com.lonebytesoft.hamster.raytracing.ray;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public class RayTraceResultItemWeighted<T extends Coordinates> {

    private final RayTraceResultItem<T> resultItem;
    private final double weight;

    public RayTraceResultItemWeighted(final RayTraceResultItem<T> resultItem, final double weight) {
        this.resultItem = resultItem;
        this.weight = weight;
    }

    public RayTraceResultItem<T> getResultItem() {
        return resultItem;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "RayTraceResultItemWeighted{" +
                "resultItem=" + resultItem +
                ", weight=" + weight +
                '}';
    }

}

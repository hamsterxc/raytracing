package com.lonebytesoft.hamster.raytracing.ray;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public class RayTraceResultItem<T extends Coordinates> {

    private final RayTraceResultItemType type;

    private final Ray<T> ray;
    private final Color color;

    public RayTraceResultItem(final Ray<T> ray) {
        this.type = RayTraceResultItemType.RAY;
        this.ray = ray;
        this.color = null;
    }

    public RayTraceResultItem(final Color color) {
        this.type = RayTraceResultItemType.COLOR;
        this.ray = null;
        this.color = color;
    }

    public RayTraceResultItemType getType() {
        return type;
    }

    public Ray<T> asRay() {
        return ray;
    }

    public Color asColor() {
        return color;
    }

    @Override
    public String toString() {
        return "RayTraceResultItem{" +
                "type=" + type +
                ", ray=" + ray +
                ", color=" + color +
                '}';
    }

}

package com.lonebytesoft.hamster.raytracing.shape.adapter;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResultItem;
import com.lonebytesoft.hamster.raytracing.shape.ShapeLayer;

public class SolidColoredAdapter<T extends Coordinates<T>> implements ShapeLayer<T> {

    private final Color color;

    public SolidColoredAdapter(final Color color) {
        this.color = color;
    }

    @Override
    public RayTraceResultItem<T> getRayTraceResult(Ray<T> ray) {
        return new RayTraceResultItem<>(color);
    }

}

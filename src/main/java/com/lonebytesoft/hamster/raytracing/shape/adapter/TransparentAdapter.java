package com.lonebytesoft.hamster.raytracing.shape.adapter;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResultItem;
import com.lonebytesoft.hamster.raytracing.shape.ShapeLayer;
import com.lonebytesoft.hamster.raytracing.shape.feature.Transparent;

public class TransparentAdapter<T extends Coordinates<T>> implements ShapeLayer<T> {

    private final Transparent<T> subject;

    public TransparentAdapter(final Transparent<T> subject) {
        this.subject = subject;
    }

    @Override
    public RayTraceResultItem<T> getRayTraceResult(Ray<T> ray) {
        return new RayTraceResultItem<>(subject.calculatePassThrough(ray));
    }

}

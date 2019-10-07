package com.lonebytesoft.hamster.raytracing.shape.adapter;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResultItem;
import com.lonebytesoft.hamster.raytracing.shape.ShapeLayer;
import com.lonebytesoft.hamster.raytracing.shape.feature.Reflecting;

public class ReflectingAdapter<T extends Coordinates> implements ShapeLayer<T> {

    private final Reflecting<T> subject;

    public ReflectingAdapter(final Reflecting<T> subject) {
        this.subject = subject;
    }

    @Override
    public RayTraceResultItem<T> getRayTraceResult(Ray<T> ray) {
        return new RayTraceResultItem<>(subject.calculateReflection(ray));
    }

}

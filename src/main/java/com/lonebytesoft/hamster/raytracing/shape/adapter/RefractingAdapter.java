package com.lonebytesoft.hamster.raytracing.shape.adapter;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResultItem;
import com.lonebytesoft.hamster.raytracing.shape.ShapeLayer;
import com.lonebytesoft.hamster.raytracing.shape.feature.Refracting;

public class RefractingAdapter<T extends Coordinates<T>> implements ShapeLayer<T> {

    private final Refracting<T> subject;
    private final double coeff;

    public RefractingAdapter(final Refracting<T> subject, final double coeff) {
        this.subject = subject;
        this.coeff = coeff;
    }

    @Override
    public RayTraceResultItem<T> getRayTraceResult(Ray<T> ray) {
        return new RayTraceResultItem<>(subject.calculateRefraction(ray, 1.0, coeff));
    }

}

package com.lonebytesoft.hamster.raytracing.shape;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResultItem;

public interface ShapeLayer<T extends Coordinates> {

    RayTraceResultItem<T> getRayTraceResult(Ray<T> ray);

}

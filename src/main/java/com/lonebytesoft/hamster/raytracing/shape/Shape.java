package com.lonebytesoft.hamster.raytracing.shape;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResult;

public interface Shape<T extends Coordinates<T>> {

    RayTraceResult<T> getRayTraceResult(Ray<T> ray);

}

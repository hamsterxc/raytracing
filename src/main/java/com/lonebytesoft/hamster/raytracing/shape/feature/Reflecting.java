package com.lonebytesoft.hamster.raytracing.shape.feature;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;

public interface Reflecting<T extends Coordinates> {

    Ray<T> calculateReflection(Ray<T> ray);

}

package com.lonebytesoft.hamster.raytracing.shape.feature;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;

public interface GeometryCalculating<T extends Coordinates<T>> {

    Double calculateDistance(Ray<T> ray);

    default T calculateNormal(Ray<T> ray) {
        return null;
    }

}

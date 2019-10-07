package com.lonebytesoft.hamster.raytracing.shape.feature;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;

public interface Transparent<T extends Coordinates> {

    Ray<T> calculatePassThrough(Ray<T> ray);

}

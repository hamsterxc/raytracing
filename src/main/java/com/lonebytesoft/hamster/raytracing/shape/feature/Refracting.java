package com.lonebytesoft.hamster.raytracing.shape.feature;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;

public interface Refracting<T extends Coordinates> {

    Ray<T> calculateRefraction(Ray<T> ray, double coeffSpace, double coeffSelf);

}

package com.lonebytesoft.hamster.raytracing.shape.feature;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;

public interface Surfaced<T extends Coordinates<T>> {

    <F extends Coordinates<F>> F mapToSurface(Ray<T> ray, F reference);

}

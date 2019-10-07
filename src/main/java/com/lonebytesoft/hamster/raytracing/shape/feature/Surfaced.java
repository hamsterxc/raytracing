package com.lonebytesoft.hamster.raytracing.shape.feature;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;

public interface Surfaced<S extends Coordinates> {

    <F extends Coordinates> F mapToSurface(Ray<S> ray, GeometryCalculator<F> geometryCalculator);

    <F extends Coordinates> S mapFromSurface(F coordinates);

}

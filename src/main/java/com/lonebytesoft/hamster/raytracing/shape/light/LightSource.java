package com.lonebytesoft.hamster.raytracing.shape.light;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;

public interface LightSource<T extends Coordinates> {

    Double calculateLightAmount(T point, T normal);

    Double calculateGlowAmount(Ray<T> ray);

}

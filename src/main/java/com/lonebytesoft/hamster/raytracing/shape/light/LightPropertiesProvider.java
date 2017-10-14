package com.lonebytesoft.hamster.raytracing.shape.light;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;

public interface LightPropertiesProvider<T extends Coordinates<T>> {

    Double calculateRayCollisionDistance(Ray<T> ray);

    double getIlluminanceAmountMax();

    double getSpaceParticlesDensity();

}

package com.lonebytesoft.hamster.raytracing.scene;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;

public interface RayCollisionDistanceCalculating<T extends Coordinates<T>> {

    Double calculateRayCollisionDistance(Ray<T> ray);

}

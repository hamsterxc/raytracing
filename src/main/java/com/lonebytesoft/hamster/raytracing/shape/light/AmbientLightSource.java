package com.lonebytesoft.hamster.raytracing.shape.light;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;

public class AmbientLightSource<T extends Coordinates> implements LightSource<T> {

    private final double brightness;

    public AmbientLightSource(final double brightness) {
        this.brightness = brightness;
    }

    @Override
    public Double calculateLightAmount(T point, T normal) {
        return brightness;
    }

    @Override
    public Double calculateGlowAmount(Ray<T> ray) {
        return null;
    }

}

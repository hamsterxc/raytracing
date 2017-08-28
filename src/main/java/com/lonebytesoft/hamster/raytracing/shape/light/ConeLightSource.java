package com.lonebytesoft.hamster.raytracing.shape.light;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.CoordinatesCalculator;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;

public class ConeLightSource<T extends Coordinates<T>> extends PointLightSource<T> {

    private final T source;
    private final T direction;
    private final double cos;

    public ConeLightSource(final T source, final T direction, final double angle, final double brightness) {
        super(source, brightness);
        this.source = source;
        this.direction = direction;
        this.cos = Math.cos(angle);
    }

    @Override
    public Double calculateGlowAmount(Ray<T> ray) {
        // todo: cone light source glow
        return null;
    }

    @Override
    protected Double calculateCollisionDistance(T point) {
        final T vector = CoordinatesCalculator.subtract(point, source);
        final double cos = GeometryCalculator.product(vector, direction) /
                (GeometryCalculator.length(vector) * GeometryCalculator.length(direction));
        if(cos >= this.cos) {
            return super.calculateCollisionDistance(point);
        } else {
            return 0.0;
        }
    }

}

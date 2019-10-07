package com.lonebytesoft.hamster.raytracing.shape.adapter;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResultItem;
import com.lonebytesoft.hamster.raytracing.shape.ShapeLayer;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;

import java.util.function.Function;

public class TexturedAdapter<S extends Coordinates, F extends Coordinates> implements ShapeLayer<S> {

    private final Surfaced<S> subject;
    private final Function<F, Color> texture;
    private final GeometryCalculator<F> geometryCalculator;

    public TexturedAdapter(
            final Surfaced<S> subject,
            final Function<F, Color> texture,
            final GeometryCalculator<F> geometryCalculator
    ) {
        this.subject = subject;
        this.texture = texture;
        this.geometryCalculator = geometryCalculator;
    }

    @Override
    public RayTraceResultItem<S> getRayTraceResult(Ray<S> ray) {
        final F surfaceCoordinates = subject.mapToSurface(ray, geometryCalculator);
        final Color color = texture.apply(surfaceCoordinates);
        return new RayTraceResultItem<>(color);
    }

}

package com.lonebytesoft.hamster.raytracing.shape.adapter;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResultItem;
import com.lonebytesoft.hamster.raytracing.shape.ShapeLayer;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;

import java.util.function.Function;

public class TexturedAdapter<S extends Coordinates<S>, F extends Coordinates<F>> implements ShapeLayer<S> {

    private final Surfaced<S> subject;
    private final Function<F, Color> texture;
    private final F reference;

    public TexturedAdapter(final Surfaced<S> subject, final Function<F, Color> texture, final F reference) {
        this.subject = subject;
        this.texture = texture;
        this.reference = reference;
    }

    @Override
    public RayTraceResultItem<S> getRayTraceResult(Ray<S> ray) {
        final F surfaceCoordinates = subject.mapToSurface(ray, reference);
        final Color color = texture.apply(surfaceCoordinates);
        return new RayTraceResultItem<>(color);
    }

}

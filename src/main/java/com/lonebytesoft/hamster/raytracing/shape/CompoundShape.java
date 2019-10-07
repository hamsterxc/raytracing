package com.lonebytesoft.hamster.raytracing.shape;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResult;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResultItemWeighted;
import com.lonebytesoft.hamster.raytracing.shape.feature.GeometryCalculating;

import java.util.ArrayList;
import java.util.Collection;

public class CompoundShape<T extends Coordinates> implements Shape<T> {

    private final GeometryCalculating<T> subject;
    private final Collection<ShapeLayerWeighted> layers = new ArrayList<>();

    public CompoundShape(final GeometryCalculating<T> subject) {
        this.subject = subject;
    }

    public void addLayer(final ShapeLayer<T> layer, final double weight) {
        layers.add(new ShapeLayerWeighted(layer, weight));
    }

    @Override
    public RayTraceResult<T> getRayTraceResult(Ray<T> ray) {
        final Double distance = subject.calculateDistance(ray);
        if(distance == null) {
            return null;
        } else {
            final Collection<RayTraceResultItemWeighted<T>> items = new ArrayList<>();
            for(final ShapeLayerWeighted layer : layers) {
                items.add(new RayTraceResultItemWeighted<>(layer.getLayer().getRayTraceResult(ray), layer.getWeight()));
            }

            final RayTraceResult<T> rayTraceResult = new RayTraceResult<>(items, distance);
            rayTraceResult.setNormal(subject.calculateNormal(ray));
            return rayTraceResult;
        }
    }

    private class ShapeLayerWeighted {
        private final ShapeLayer<T> layer;
        private final double weight;

        public ShapeLayerWeighted(final ShapeLayer<T> layer, final double weight) {
            this.layer = layer;
            this.weight = weight;
        }

        public ShapeLayer<T> getLayer() {
            return layer;
        }

        public double getWeight() {
            return weight;
        }
    }

}

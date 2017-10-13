package com.lonebytesoft.hamster.raytracing.shape.light;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.CoordinatesCalculator;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.scene.RayCollisionDistanceCalculating;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import com.lonebytesoft.hamster.raytracing.util.math.MathCalculator;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class PointLightSource<T extends Coordinates<T>> implements LightSource<T> {

    // todo: extract magic constant somewhere
    private static final double PARTICLES_DENSITY = 0.005;

    private final T source;
    private final double brightness;

    private RayCollisionDistanceCalculating<T> rayTracer;

    public PointLightSource(final T source, final double brightness) {
        this.source = source;
        this.brightness = brightness;
    }

    @Override
    public Double calculateLightAmount(T point, T normal) {
        final T vector = CoordinatesCalculator.subtract(source, point);
        final double distance = GeometryCalculator.length(vector);
        final Double rayCollisionDistance = calculateCollisionDistance(point);
        if(isVisible(distance, rayCollisionDistance)) {
            final double cos;
            if(normal == null) {
                cos = 1.0;
            } else {
                cos = Math.abs(GeometryCalculator.product(normal, vector) / (GeometryCalculator.length(normal) * distance));
            }

            return brightness * cos / (distance * distance);
        } else {
            return null;
        }
    }

    protected Double calculateCollisionDistance(final T point) {
        if(rayTracer == null) {
            return null;
        } else {
            final T direction = CoordinatesCalculator.subtract(point, source);
            final Ray<T> ray = new Ray<>(source, direction);
            return rayTracer.calculateRayCollisionDistance(ray);
        }
    }

    private boolean isVisible(final double distance, final Double collisionDistance) {
        return (collisionDistance == null)
                || (collisionDistance > distance)
                || MathCalculator.isEqualApproximately(collisionDistance, distance);
    }

    // todo: calculating as if no parts of ray are obscured by objects, i.e. no light falls on those ray parts because of those objects
    /*
        Calculating total amount of light that falls on all tiny particles along given ray
        Assuming cos = 1 for all particles

        total_light_amount = \int_{lit_areas}(brightness * 1 / distance_to_source^2 * dx)
        where distance_to_source is a distance from source to a point on ray with direction multiplier x

        \int(dx / distance_to_source^2)
        = \int(dx / \sum_{i=1}^{dimensions}(ray_start_i + x * ray_direction_i - source_i)^2)
        = {
            a = \sum_{i=1}^{dimensions}(ray_direction_i)^2
            b = \sum_{i=1}^{dimensions}(ray_direction * (ray_start_i - source_i))
            c = \sum_{i=1}^{dimensions}(ray_start_i - source_i)^2
            d = \sqrt{a*c - b^2}
        }
        = {
            -1/ray_direction_0 * 1/(ray_start_0 + x * ray_direction_0 - source_0) + C, if dimensions = 1
            atan((a * x + b / d)) / d + C, if dimensions > 1
        }
     */
    @Override
    public Double calculateGlowAmount(Ray<T> ray) {
        return calculateLitArea(ray)
                .stream()
                .map(litArea -> calculateGlowAmount(ray, litArea))
                .filter(Objects::nonNull)
                .mapToDouble(amount -> amount)
                .sum();
    }

    private Double calculateGlowAmount(final Ray<T> ray, final LitAreaDefinition litArea) {
        final int dimensions = ray.getStart().getDimensions();
        if(dimensions == 1) {
            final double rayStart = ray.getStart().getCoordinate(0);
            final double rayDirection = ray.getDirection().getCoordinate(0);
            final double source = this.source.getCoordinate(0);
            return (1.0 / (litArea.getStart() * rayDirection + rayStart - source)
                    - 1.0 / (litArea.getEnd() * rayDirection + rayStart - source))
                    / rayDirection;
        } else {
            double a = 0;
            double b = 0;
            double c = 0;
            for(int i = 0; i < dimensions; i++) {
                final double directionCoordinate = ray.getDirection().getCoordinate(i);
                final double differenceCoordinate = ray.getStart().getCoordinate(i) - source.getCoordinate(i);
                a += directionCoordinate * directionCoordinate;
                b += directionCoordinate * differenceCoordinate;
                c += differenceCoordinate * differenceCoordinate;
            }

            final double dSquare = a * c - b * b;
            if(dSquare <= 0) {
                return null;
            }
            final double d = Math.sqrt(dSquare);

            final double lowerValue = Math.atan((a * litArea.getStart() + b) / d);
            final double upperValue = Math.atan((a * litArea.getEnd() + b) / d);
            final double integral = (upperValue - lowerValue) / d;

            return integral * brightness * PARTICLES_DENSITY;
        }
    }

    protected Collection<LitAreaDefinition> calculateLitArea(final Ray<T> ray) {
        final Double collisionDistance = rayTracer == null ? null : rayTracer.calculateRayCollisionDistance(ray);
        final double end = collisionDistance == null ? Double.MAX_VALUE : collisionDistance;

        return Collections.singletonList(new LitAreaDefinition(0, end));
    }

    public void setRayTracer(RayCollisionDistanceCalculating<T> rayTracer) {
        this.rayTracer = rayTracer;
    }

}

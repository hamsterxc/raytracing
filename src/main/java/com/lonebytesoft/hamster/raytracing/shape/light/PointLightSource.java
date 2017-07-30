package com.lonebytesoft.hamster.raytracing.shape.light;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.CoordinatesCalculator;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.scene.RayCollisionDistanceCalculating;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import com.lonebytesoft.hamster.raytracing.util.math.MathCalculator;

public class PointLightSource<T extends Coordinates<T>> implements LightSource<T> {

    private final T point;
    private final double brightness;

    private RayCollisionDistanceCalculating<T> rayTracer;

    public PointLightSource(final T point, final double brightness) {
        this.point = point;
        this.brightness = brightness;
    }

    @Override
    public Double calculateLightAmount(T point, T normal) {
        final T vector = CoordinatesCalculator.transform(this.point,
                index -> this.point.getCoordinate(index) - point.getCoordinate(index));
        final double distance = GeometryCalculator.length(vector);
        final Double rayCollisionDistance = calculateDistance(this.point, point);
        if((rayCollisionDistance == null) || (rayCollisionDistance > distance)
                || (MathCalculator.isEqualApproximately(rayCollisionDistance, distance))) {
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

    /*
        |a + k*b - c| -> min
        (a1 + k*b1 - c1)^2 + ... + (an + k*bn - cn)^2 -> min
        k^2*(b1^2 + ... + bn^2) + k*2*(b1*(a1 - c1) + ... + bn*(an - cn)) + ((a1 - c1)^2 + ... + (an - cn)^2) -> min
        k = (b1*(c1 - a1) + ... + bn*(cn - an)) / (b1^2 + ... + bn^2)
     */
    @Override
    public Double calculateGlowAmount(Ray<T> ray) {
        final int dimensions = ray.getStart().getDimensions();

        double numerator = 0.0;
        double denominator = 0.0;
        for(int i = 0; i < dimensions; i++) {
            final double rayDirectionCoordinate = ray.getDirection().getCoordinate(i);
            numerator += rayDirectionCoordinate * (point.getCoordinate(i) - ray.getStart().getCoordinate(i));
            denominator += rayDirectionCoordinate * rayDirectionCoordinate;
        }

        final double multiplier = numerator / denominator;
        final T nearest = GeometryCalculator.follow(ray.getStart(), ray.getDirection(),
                multiplier < 0 ? 0 : multiplier);

        // todo: smarter glow calculation
        // e.g., if glow is visible not in the nearest to source point but in some other on this ray
        final T vector = CoordinatesCalculator.transform(point,
                index -> nearest.getCoordinate(index) - point.getCoordinate(index));
        final double distance = GeometryCalculator.length(vector);
        final Double rayCollisionDistance = calculateDistance(point, nearest);
        if((rayCollisionDistance == null) || (rayCollisionDistance > distance)) {
            // todo: is glow amount calculation reasonable?
            final double amount = brightness / (distance * distance);
            return Math.pow(amount, 1.0/4.0);
        } else {
            return null;
        }
    }

    public void setRayTracer(RayCollisionDistanceCalculating<T> rayTracer) {
        this.rayTracer = rayTracer;
    }

    private Double calculateDistance(final T from, final T to) {
        if(rayTracer == null) {
            return null;
        } else {
            final T direction = CoordinatesCalculator.transform(from,
                    index -> to.getCoordinate(index) - from.getCoordinate(index));
            final Ray<T> ray = new Ray<>(from, direction);
            return rayTracer.calculateRayCollisionDistance(ray);
        }
    }

}

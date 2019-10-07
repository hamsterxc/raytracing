package com.lonebytesoft.hamster.raytracing.scene.beholder;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.color.ColorCalculator;
import com.lonebytesoft.hamster.raytracing.color.ColorWeighted;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResult;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResultItem;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResultItemWeighted;
import com.lonebytesoft.hamster.raytracing.scene.screen.Screen;
import com.lonebytesoft.hamster.raytracing.shape.Shape;
import com.lonebytesoft.hamster.raytracing.shape.light.LightPropertiesProvider;
import com.lonebytesoft.hamster.raytracing.shape.light.LightSource;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class BeholderImpl<S extends Coordinates, F extends Coordinates>
        implements Beholder<F>, LightPropertiesProvider<S> {

    private static final Logger logger = LoggerFactory.getLogger(BeholderImpl.class);

    private final S eye;
    private final Screen<S, F> screen;
    private final Color colorDefault;
    private final GeometryCalculator<S> geometryCalculator;

    private final Collection<Shape<S>> shapes = new ArrayList<>();
    private final Collection<LightSource<S>> lightSources = new ArrayList<>();

    // reasonable default values
    private double illuminanceAmountMax = 10.0;
    private double spaceParticlesDensity = 0.005;

    public BeholderImpl(
            final S eye,
            final Screen<S, F> screen,
            final Color colorDefault,
            final GeometryCalculator<S> geometryCalculator
    ) {
        this.eye = eye;
        this.screen = screen;
        this.colorDefault = colorDefault;
        this.geometryCalculator = geometryCalculator;
    }

    @Override
    public Picture<F> getPicture() {
        return screen.getPicture(coordinates -> {
            final S direction = geometryCalculator.subtract(coordinates, eye);
            final Ray<S> ray = new Ray<>(coordinates, direction);
            return traceRay(ray);
        });
    }

    private Color traceRay(final Ray<S> ray) {
        logger.trace("Tracing ray {}", ray);
        if(shapes.size() == 0) {
            return null;
        }

        final List<RayTraceResult<S>> results = new ArrayList<>();
        for(final Shape<S> shape : shapes) {
            final RayTraceResult<S> result = shape.getRayTraceResult(ray);
            if(result != null) {
                results.add(result);
            }
        }

        if(results.size() == 0) {
            final double lightAmount = lightSources.stream()
                    .map(lightSource -> lightSource.calculateGlowAmount(ray))
                    .filter(Objects::nonNull)
                    .mapToDouble(Double::doubleValue)
                    .sum();
            logger.trace("Ray {}: empty result, glow light amount = {}", ray, lightAmount);
            final double illuminance = calculateIlluminance(lightAmount);
            return ColorCalculator.illuminate(colorDefault, illuminance);
        }
        results.sort(Comparator.comparingDouble(RayTraceResult::getDistance));

        final RayTraceResult<S> result = results.get(0);
        logger.trace("Ray {}: result {}", ray, result);
        final Collection<ColorWeighted> colors = new ArrayList<>();
        for(final RayTraceResultItemWeighted<S> resultItemWeighted : result.getItems()) {
            final RayTraceResultItem<S> resultItem = resultItemWeighted.getResultItem();
            switch(resultItem.getType()) {
                case RAY:
                    Color next = traceRay(resultItem.asRay());
                    if(next == null) {
                        next = colorDefault;
                    }
                    colors.add(new ColorWeighted(next, resultItemWeighted.getWeight()));
                    break;

                case COLOR:
                    colors.add(new ColorWeighted(resultItem.asColor(), resultItemWeighted.getWeight()));
                    break;
            }
        }

        final double lightAmount = lightSources
                .stream()
                .map(lightSource -> lightSource.calculateLightAmount(
                        geometryCalculator.follow(ray.getStart(), ray.getDirection(),
                                result.getDistance() / geometryCalculator.length(ray.getDirection())),
                        result.getNormal()))
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
        final double illuminance = calculateIlluminance(lightAmount);

        final Color color = ColorCalculator.combine(colors);
        final Color colorIlluminated = ColorCalculator.illuminate(color, illuminance);
        logger.trace("Ray {}, color = {}, light amount = {}, resulting color = {}",
                ray, color, lightAmount, colorIlluminated);
        return colorIlluminated;
    }

    private double calculateIlluminance(final double lightAmount) {
        return lightAmount / getIlluminanceAmountMax();
    }

    @Override
    public Double calculateRayCollisionDistance(Ray<S> ray) {
        return shapes
                .stream()
                .map(s -> s.getRayTraceResult(ray))
                .filter(Objects::nonNull)
                .map(RayTraceResult::getDistance)
                .min(Double::compare)
                .orElse(null);
    }

    @Override
    public double getIlluminanceAmountMax() {
        return illuminanceAmountMax;
    }

    public void setIlluminanceAmountMax(double illuminanceAmountMax) {
        this.illuminanceAmountMax = illuminanceAmountMax;
    }

    @Override
    public double getSpaceParticlesDensity() {
        return spaceParticlesDensity;
    }

    public void setSpaceParticlesDensity(double spaceParticlesDensity) {
        this.spaceParticlesDensity = spaceParticlesDensity;
    }

    public void addShape(Shape<S> shape) {
        shapes.add(shape);
    }

    public void addLightSource(LightSource<S> lightSource) {
        lightSources.add(lightSource);
    }

}

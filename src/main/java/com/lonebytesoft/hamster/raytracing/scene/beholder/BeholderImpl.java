package com.lonebytesoft.hamster.raytracing.scene.beholder;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.color.ColorCalculator;
import com.lonebytesoft.hamster.raytracing.color.ColorWeighted;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.CoordinatesCalculator;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResult;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResultItem;
import com.lonebytesoft.hamster.raytracing.ray.RayTraceResultItemWeighted;
import com.lonebytesoft.hamster.raytracing.scene.RayCollisionDistanceCalculating;
import com.lonebytesoft.hamster.raytracing.scene.screen.Screen;
import com.lonebytesoft.hamster.raytracing.shape.Shape;
import com.lonebytesoft.hamster.raytracing.shape.light.LightSource;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class BeholderImpl<S extends Coordinates<S>, F extends Coordinates<F>>
        implements Beholder<F>, RayCollisionDistanceCalculating<S> {

    private static final Logger logger = LoggerFactory.getLogger(BeholderImpl.class);

    private static final double ILLUMINANCE_AMOUNT_MAX = 10.0;

    private final S eye;
    private final Screen<S, F> screen;
    private final Color colorDefault;

    private final Collection<Shape<S>> shapes = new ArrayList<>();
    private final Collection<LightSource<S>> lightSources = new ArrayList<>();

    public BeholderImpl(final S eye, final Screen<S, F> screen, final Color colorDefault) {
        this.eye = eye;
        this.screen = screen;
        this.colorDefault = colorDefault;
    }

    @Override
    public Picture<F> getPicture() {
        return screen.getPicture(coordinates -> {
            final S direction = CoordinatesCalculator.transform(coordinates,
                    index -> coordinates.getCoordinate(index) - eye.getCoordinate(index));
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
                        GeometryCalculator.follow(ray.getStart(), ray.getDirection(),
                                result.getDistance() / GeometryCalculator.length(ray.getDirection())),
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
        if(lightAmount < 0.0) {
            return 0.0;
        } else if(lightAmount > ILLUMINANCE_AMOUNT_MAX) {
            return 1.0;
        } else {
            return lightAmount / ILLUMINANCE_AMOUNT_MAX;
        }
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

    public void addShape(Shape<S> shape) {
        shapes.add(shape);
    }

    public void addLightSource(LightSource<S> lightSource) {
        lightSources.add(lightSource);
    }

}

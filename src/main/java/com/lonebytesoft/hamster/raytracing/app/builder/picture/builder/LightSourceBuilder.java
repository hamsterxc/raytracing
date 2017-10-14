package com.lonebytesoft.hamster.raytracing.app.builder.picture.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.AmbientLightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.ConeLightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.PointLightDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.shape.light.AmbientLightSource;
import com.lonebytesoft.hamster.raytracing.shape.light.ConeLightSource;
import com.lonebytesoft.hamster.raytracing.shape.light.LightPropertiesProvider;
import com.lonebytesoft.hamster.raytracing.shape.light.LightSource;
import com.lonebytesoft.hamster.raytracing.shape.light.PointLightSource;

public class LightSourceBuilder<T extends Coordinates<T>> {

    private final CoordinatesBuilder coordinatesBuilder;

    public LightSourceBuilder(final CoordinatesBuilder coordinatesBuilder) {
        this.coordinatesBuilder = coordinatesBuilder;
    }

    public LightSource<T> buildLightSource(final LightDefinition lightDefinition,
                                           final LightPropertiesProvider<T> rayTracer) {
        switch(lightDefinition.getType()) {
            case AMBIENT:
                final AmbientLightDefinition ambientLightDefinition = (AmbientLightDefinition) lightDefinition;
                return new AmbientLightSource<>(ambientLightDefinition.getBrightness());

            case CONE:
                final ConeLightDefinition coneLightDefinition = (ConeLightDefinition) lightDefinition;
                final ConeLightSource<T> coneLightSource = new ConeLightSource<T>(
                        coordinatesBuilder.buildCoordinates(coneLightDefinition.getSource()),
                        coordinatesBuilder.buildCoordinates(coneLightDefinition.getDirection()),
                        coneLightDefinition.getAngle(),
                        coneLightDefinition.getBrightness()
                );
                coneLightSource.setLightPropertiesProvider(rayTracer);
                return coneLightSource;

            case POINT:
                final PointLightDefinition pointLightDefinition = (PointLightDefinition) lightDefinition;
                final PointLightSource<T> pointLightSource = new PointLightSource<T>(
                        coordinatesBuilder.buildCoordinates(pointLightDefinition.getSource()),
                        pointLightDefinition.getBrightness()
                );
                pointLightSource.setLightPropertiesProvider(rayTracer);
                return pointLightSource;

            default:
                throw new RuntimeException("Unknown light source type: " + lightDefinition.getType());
        }
    }

}

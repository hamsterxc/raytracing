package com.lonebytesoft.hamster.raytracing.app.builder.picture.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.ScreenDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import com.lonebytesoft.hamster.raytracing.scene.beholder.BeholderImpl;
import com.lonebytesoft.hamster.raytracing.scene.screen.Screen;
import com.lonebytesoft.hamster.raytracing.scene.screen.ScreenShapeSurfaced;

public class PictureBuilder<S extends Coordinates<S>, F extends Coordinates<F>> {

    private final CoordinatesBuilder coordinatesBuilder;
    private final PixelColoringStrategyBuilder<F> pixelColoringStrategyBuilder;
    private final ShapeBuilder<S> shapeBuilder;
    private final LightSourceBuilder<S> lightSourceBuilder;

    public PictureBuilder(final CoordinatesBuilder coordinatesBuilder,
                          final PixelColoringStrategyBuilder<F> pixelColoringStrategyBuilder,
                          final ShapeBuilder<S> shapeBuilder,
                          final LightSourceBuilder<S> lightSourceBuilder) {
        this.coordinatesBuilder = coordinatesBuilder;
        this.pixelColoringStrategyBuilder = pixelColoringStrategyBuilder;
        this.shapeBuilder = shapeBuilder;
        this.lightSourceBuilder = lightSourceBuilder;
    }

    public Picture<F> buildPicture(final SceneDefinition sceneDefinition) {
        final ScreenDefinition screenDefinition = sceneDefinition.getScreen();
        final Screen<S, F> screen = new ScreenShapeSurfaced<>(
                shapeBuilder.buildSurfaced(screenDefinition.getShape()),
                coordinatesBuilder.buildCoordinates(screenDefinition.getFrom()),
                coordinatesBuilder.buildCoordinates(screenDefinition.getTo()),
                coordinatesBuilder.buildCoordinates(screenDefinition.getResolution()),
                pixelColoringStrategyBuilder.build(screenDefinition.getPixelColoring())
        );

        final BeholderImpl<S, F> beholder = new BeholderImpl<>(
                coordinatesBuilder.buildCoordinates(sceneDefinition.getEye()),
                screen,
                sceneDefinition.getColorDefault()
        );

        final Double illuminanceAmountMax = sceneDefinition.getLightProperties().getIlluminanceAmountMax();
        if(illuminanceAmountMax != null) {
            beholder.setIlluminanceAmountMax(illuminanceAmountMax);
        }

        final Double spaceParticlesDensity = sceneDefinition.getLightProperties().getSpaceParticlesDensity();
        if(spaceParticlesDensity != null) {
            beholder.setSpaceParticlesDensity(spaceParticlesDensity);
        }

        for(final ShapeDefinition shapeDefinition : sceneDefinition.getShapes()) {
            beholder.addShape(shapeBuilder.buildShape(shapeDefinition));
        }

        for(final LightDefinition lightDefinition : sceneDefinition.getLights()) {
            beholder.addLightSource(lightSourceBuilder.buildLightSource(lightDefinition, beholder));
        }

        return beholder.getPicture();
    }
    
}

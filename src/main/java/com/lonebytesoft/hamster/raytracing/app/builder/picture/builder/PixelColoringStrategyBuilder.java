package com.lonebytesoft.hamster.raytracing.app.builder.picture.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor.PixelColoringDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor.SupersamplingPixelColoringDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor.PixelColoringStrategy;
import com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor.SimplePixelColoringStrategy;
import com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor.SupersamplingPixelColoringStrategy;

public class PixelColoringStrategyBuilder<T extends Coordinates<T>> {

    private final CoordinatesBuilder coordinatesBuilder;
    private final int pictureDimensions;

    public PixelColoringStrategyBuilder(final CoordinatesBuilder coordinatesBuilder, final int pictureDimensions) {
        this.coordinatesBuilder = coordinatesBuilder;
        this.pictureDimensions = pictureDimensions;
    }

    public PixelColoringStrategy<T> build(final PixelColoringDefinition pixelColoringDefinition) {
        final T reference = coordinatesBuilder.buildCoordinates(pictureDimensions);
        switch(pixelColoringDefinition.getType()) {
            case SIMPLE:
                return new SimplePixelColoringStrategy<>(reference);

            case SUPERSAMPLING:
                final SupersamplingPixelColoringDefinition supersampling =
                        (SupersamplingPixelColoringDefinition) pixelColoringDefinition;
                return new SupersamplingPixelColoringStrategy<>(
                        supersampling.getMultiplier(), supersampling.getColorDefault(), reference);

            default:
                throw new RuntimeException("Unknown pixel coloring type: " + pixelColoringDefinition.getType());
        }
    }

}

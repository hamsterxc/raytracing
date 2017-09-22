package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.PixelColoringExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor.PixelColoringDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor.SupersamplingPixelColoringDefinition;
import com.lonebytesoft.hamster.raytracing.color.Color;

public class PixelColoringBuilder implements ExpressionBuilder<PixelColoringExtendedDefinition> {

    private final ExpressionBuilder<Color> colorBuilder;
    private final CoordinatesBuilder coordinatesBuilder;

    public PixelColoringBuilder(final ExpressionBuilder<Color> colorBuilder,
                                final CoordinatesBuilder coordinatesBuilder) {
        this.colorBuilder = colorBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
    }

    @Override
    public String build(PixelColoringExtendedDefinition extendedDefinition) {
        final PixelColoringDefinition definition = extendedDefinition.getPixelColoringDefinition();
        switch(definition.getType()) {
            case SIMPLE:
                return "new SimplePixelColoringStrategy<>("
                        + coordinatesBuilder.buildReference(extendedDefinition.getPictureDimensions())
                        + ")";

            case SUPERSAMPLING:
                final SupersamplingPixelColoringDefinition supersamplingPixelColoringDefinition =
                        (SupersamplingPixelColoringDefinition) definition;
                return "new SupersamplingPixelColoringStrategy<>("
                        + supersamplingPixelColoringDefinition.getMultiplier()
                        + ","
                        + colorBuilder.build(supersamplingPixelColoringDefinition.getColorDefault())
                        + ","
                        + coordinatesBuilder.buildReference(extendedDefinition.getPictureDimensions())
                        + ")";

            default:
                throw new RuntimeException("Unknown pixel coloring type: " + definition.getType());
        }
    }

}

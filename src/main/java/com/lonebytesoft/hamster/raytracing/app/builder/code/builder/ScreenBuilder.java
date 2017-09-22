package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.PixelColoringExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ScreenExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.ScreenDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;

public class ScreenBuilder implements StatementBuilder<ScreenExtendedDefinition> {

    private final ExpressionBuilder<String> variableNameBuilder;
    private final ExpressionBuilder<PixelColoringExtendedDefinition> pixelColoringBuilder;
    private final CoordinatesBuilder coordinatesBuilder;
    private final StatementBuilder<ShapeDefinition> bareShapeBuilder;

    public ScreenBuilder(final ExpressionBuilder<String> variableNameBuilder,
                         final ExpressionBuilder<PixelColoringExtendedDefinition> pixelColoringBuilder,
                         final CoordinatesBuilder coordinatesBuilder,
                         final StatementBuilder<ShapeDefinition> bareShapeBuilder) {
        this.variableNameBuilder = variableNameBuilder;
        this.pixelColoringBuilder = pixelColoringBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
        this.bareShapeBuilder = bareShapeBuilder;
    }

    @Override
    public String build(ScreenExtendedDefinition extendedDefinition, String name) {
        final ScreenDefinition definition = extendedDefinition.getScreenDefinition();

        final StringBuilder code = new StringBuilder();

        final String shapeName = variableNameBuilder.build("shape");
        code.append(bareShapeBuilder.build(definition.getShape(), shapeName));

        code
                .append("final Screen<")
                .append(coordinatesBuilder.buildClassName(extendedDefinition.getSpaceDimensions()))
                .append(",")
                .append(coordinatesBuilder.buildClassName(extendedDefinition.getPictureDimensions()))
                .append("> ")
                .append(name)
                .append(" = new ScreenShapeSurfaced<>(")
                .append(shapeName)
                .append(",")
                .append(coordinatesBuilder.build(definition.getFrom()))
                .append(",")
                .append(coordinatesBuilder.build(definition.getTo()))
                .append(",")
                .append(coordinatesBuilder.build(definition.getResolution()))
                .append(",")
                .append(pixelColoringBuilder.build(new PixelColoringExtendedDefinition(
                        definition.getPixelColoring(), extendedDefinition.getPictureDimensions())))
                .append(");\n");

        return code.toString();
    }

}

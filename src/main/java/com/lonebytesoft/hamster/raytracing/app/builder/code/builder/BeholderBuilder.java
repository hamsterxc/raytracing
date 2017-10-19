package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.LightExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ScreenExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ShapeExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightPropertiesDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.color.Color;

public class BeholderBuilder implements StatementBuilder<SceneDefinition> {

    private final ExpressionBuilder<String> variableNameBuilder;
    private final ExpressionBuilder<Color> colorBuilder;
    private final CoordinatesBuilder coordinatesBuilder;
    private final StatementBuilder<ScreenExtendedDefinition> screenBuilder;
    private final StatementBuilder<ShapeExtendedDefinition> shapeBuilder;
    private final StatementBuilder<LightExtendedDefinition> lightBuilder;

    public BeholderBuilder(final ExpressionBuilder<String> variableNameBuilder,
                           final ExpressionBuilder<Color> colorBuilder,
                           final CoordinatesBuilder coordinatesBuilder,
                           final StatementBuilder<ScreenExtendedDefinition> screenBuilder,
                           final StatementBuilder<ShapeExtendedDefinition> shapeBuilder,
                           final StatementBuilder<LightExtendedDefinition> lightBuilder) {
        this.variableNameBuilder = variableNameBuilder;
        this.colorBuilder = colorBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
        this.screenBuilder = screenBuilder;
        this.shapeBuilder = shapeBuilder;
        this.lightBuilder = lightBuilder;
    }

    @Override
    public String build(SceneDefinition definition, String name) {
        final StringBuilder code = new StringBuilder();

        final int spaceDimensions = definition.getSpaceDimensions();
        final int pictureDimensions = definition.getPictureDimensions();

        code.append(coordinatesBuilder.build(definition.getEye(), "eye"));

        code.append(screenBuilder.build(new ScreenExtendedDefinition(
                definition.getScreen(), spaceDimensions, pictureDimensions),"screen"));

        code
                .append("final BeholderImpl<")
                .append(coordinatesBuilder.buildClassName(spaceDimensions))
                .append(",")
                .append(coordinatesBuilder.buildClassName(pictureDimensions))
                .append("> beholder = new BeholderImpl<>(eye, screen, ")
                .append(colorBuilder.build(definition.getColorDefault()))
                .append(");\n");

        final LightPropertiesDefinition lightProperties = definition.getLightProperties();
        if(lightProperties != null) {
            final Double illuminanceAmountMax = lightProperties.getIlluminanceAmountMax();
            if(illuminanceAmountMax != null) {
                code.append("beholder.setIlluminanceAmountMax(").append(illuminanceAmountMax).append(");\n");
            }

            final Double spaceParticlesDensity = lightProperties.getSpaceParticlesDensity();
            if(spaceParticlesDensity != null) {
                code.append("beholder.setSpaceParticlesDensity(").append(spaceParticlesDensity).append(");\n");
            }
        }

        for(final ShapeDefinition shapeDefinition : definition.getShapes()) {
            final String shapeName = variableNameBuilder.build("shape");
            code.append(shapeBuilder.build(new ShapeExtendedDefinition(shapeDefinition, spaceDimensions), shapeName));
            code
                    .append("beholder.addShape(")
                    .append(shapeName)
                    .append(");\n");
        }

        for(final LightDefinition lightDefinition : definition.getLights()) {
            final String lightName = variableNameBuilder.build("light");
            final LightExtendedDefinition lightExtendedDefinition = new LightExtendedDefinition(
                    lightDefinition, spaceDimensions, "beholder");
            code.append(lightBuilder.build(lightExtendedDefinition, lightName));
            code
                    .append("beholder.addLightSource(")
                    .append(lightName)
                    .append(");\n");
        }

        return code.toString();
    }
    
}

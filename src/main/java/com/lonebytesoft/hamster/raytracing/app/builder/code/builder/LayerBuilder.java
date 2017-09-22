package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.LayerExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.LayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.RefractingLayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.SolidColoredLayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.texture.TexturedCheckersLayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.texture.TexturedLayerDefinition;
import com.lonebytesoft.hamster.raytracing.color.Color;

import java.util.stream.Collectors;

public class LayerBuilder implements ExpressionBuilder<LayerExtendedDefinition> {

    private final ExpressionBuilder<Color> colorBuilder;
    private final CoordinatesBuilder coordinatesBuilder;

    public LayerBuilder(final ExpressionBuilder<Color> colorBuilder,
                        final CoordinatesBuilder coordinatesBuilder) {
        this.colorBuilder = colorBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
    }

    @Override
    public String build(LayerExtendedDefinition extendedDefinition) {
        final LayerDefinition definition = extendedDefinition.getLayerDefinition();
        switch(definition.getType()) {
            case REFLECTING:
                return "new ReflectingAdapter<>(" + extendedDefinition.getSubjectName() + ")";

            case REFRACTING:
                final RefractingLayerDefinition refractingLayerDefinition = (RefractingLayerDefinition) definition;
                return "new RefractingAdapter<>(" + extendedDefinition.getSubjectName() + "," + refractingLayerDefinition.getCoeff() + ")";

            case SOLID_COLORED:
                final SolidColoredLayerDefinition solidColoredLayerDefinition = (SolidColoredLayerDefinition) definition;
                return "new SolidColoredAdapter<>(" + colorBuilder.build(solidColoredLayerDefinition.getColor()) + ")";

            case TEXTURED:
                return buildTexturedLayer((TexturedLayerDefinition) definition, extendedDefinition.getSubjectName());

            case TRANSPARENT:
                return "new TransparentAdapter<>(" + extendedDefinition.getSubjectName() + ")";

            default:
                throw new RuntimeException("Unknown layer type: " + definition.getType());
        }
    }

    private String buildTexturedLayer(final TexturedLayerDefinition texturedLayerDefinition, final String subjectName) {
        final String texture;
        final int surfaceDimensions;
        switch(texturedLayerDefinition.getTextureType()) {
            case CHECKERS:
                final TexturedCheckersLayerDefinition texturedCheckersLayerDefinition =
                        (TexturedCheckersLayerDefinition) texturedLayerDefinition;
                texture = new StringBuilder()
                        .append("new CheckersTexture<>(Arrays.asList(")
                        .append(texturedCheckersLayerDefinition.getMultipliers().stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(",")))
                        .append("),Arrays.asList(")
                        .append(texturedCheckersLayerDefinition.getColors().stream()
                                .map(colorBuilder::build)
                                .collect(Collectors.joining(",")))
                        .append("))")
                        .toString();
                surfaceDimensions = texturedCheckersLayerDefinition.getMultipliers().size();
                break;

            default:
                throw new RuntimeException("Unknown texture type: " + texturedLayerDefinition.getTextureType());
        }

        return "new TexturedAdapter<>(" + subjectName + "," + texture + ","
                + coordinatesBuilder.buildReference(surfaceDimensions) + ")";
    }

}

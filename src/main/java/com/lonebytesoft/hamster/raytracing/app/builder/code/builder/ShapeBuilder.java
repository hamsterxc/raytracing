package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.LayerExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ShapeExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.LayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;

public class ShapeBuilder implements StatementBuilder<ShapeExtendedDefinition> {

    private final ExpressionBuilder<String> variableNameBuilder;
    private final ExpressionBuilder<LayerExtendedDefinition> layerBuilder;
    private final CoordinatesBuilder coordinatesBuilder;
    private final StatementBuilder<ShapeDefinition> bareShapeBuilder;

    public ShapeBuilder(final ExpressionBuilder<String> variableNameBuilder,
                        final ExpressionBuilder<LayerExtendedDefinition> layerBuilder,
                        final CoordinatesBuilder coordinatesBuilder,
                        final StatementBuilder<ShapeDefinition> bareShapeBuilder) {
        this.variableNameBuilder = variableNameBuilder;
        this.layerBuilder = layerBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
        this.bareShapeBuilder = bareShapeBuilder;
    }

    @Override
    public String build(ShapeExtendedDefinition extendedDefinition, String name) {
        final ShapeDefinition shapeDefinition = extendedDefinition.getShapeDefinition();

        final String bareShapeName = variableNameBuilder.build("shape");
        final StringBuilder code = new StringBuilder(
                bareShapeBuilder.build(shapeDefinition, bareShapeName));

        code
                .append("final CompoundShape<")
                .append(coordinatesBuilder.buildClassName(extendedDefinition.getSpaceDimensions()))
                .append("> ")
                .append(name)
                .append(" = new CompoundShape<>(")
                .append(bareShapeName)
                .append(");\n");

        for(final LayerDefinition layerDefinition : shapeDefinition.getLayers()) {
            code
                    .append(name)
                    .append(".addLayer(")
                    .append(layerBuilder.build(new LayerExtendedDefinition(layerDefinition, bareShapeName)))
                    .append(", ")
                    .append(layerDefinition.getWeight())
                    .append(");\n");
        }

        return code.toString();
    }

}

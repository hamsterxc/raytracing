package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorRotationDefinition;

public class VectorBuilder implements StatementBuilder<VectorDefinition> {

    private final ExpressionBuilder<String> variableNameBuilder;
    private final CoordinatesBuilder coordinatesBuilder;

    public VectorBuilder(final ExpressionBuilder<String> variableNameBuilder,
                         final CoordinatesBuilder coordinatesBuilder) {
        this.variableNameBuilder = variableNameBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
    }

    @Override
    public String build(VectorDefinition definition, String name) {
        String variableName = variableNameBuilder.build("vector");

        final StringBuilder code = new StringBuilder(
                coordinatesBuilder.build(definition.getCoordinates(), variableName));

        for(final VectorRotationDefinition rotation : definition.getRotations()) {
            final String newVariableName = variableNameBuilder.build("vector");
            code
                    .append("final ")
                    .append(coordinatesBuilder.buildClassName(definition.getCoordinates()))
                    .append(" ")
                    .append(newVariableName)
                    .append(" = GeometryCalculator.rotate(")
                    .append(variableName)
                    .append(',')
                    .append(rotation.getAxisFirst())
                    .append(',')
                    .append(rotation.getAxisSecond())
                    .append(',')
                    .append(rotation.getAngle())
                    .append(");\n");
            variableName = newVariableName;
        }

        code
                .append("final ")
                .append(coordinatesBuilder.buildClassName(definition.getCoordinates()))
                .append(" ")
                .append(name)
                .append(" = ")
                .append(variableName)
                .append(";\n");

        return code.toString();
    }

}

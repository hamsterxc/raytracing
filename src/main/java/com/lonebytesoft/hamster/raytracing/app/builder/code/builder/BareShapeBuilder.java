package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.BallDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.OrthotopeDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BareShapeBuilder implements StatementBuilder<ShapeDefinition> {

    private final ExpressionBuilder<String> variableNameBuilder;
    private final CoordinatesBuilder coordinatesBuilder;
    private final StatementBuilder<VectorDefinition> vectorBuilder;

    public BareShapeBuilder(final ExpressionBuilder<String> variableNameBuilder,
                            final CoordinatesBuilder coordinatesBuilder,
                            final StatementBuilder<VectorDefinition> vectorBuilder) {
        this.variableNameBuilder = variableNameBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
        this.vectorBuilder = vectorBuilder;
    }

    @Override
    public String build(ShapeDefinition definition, String name) {
        switch(definition.getType()) {
            case BALL:
                return buildBareBall((BallDefinition) definition, name);

            case ORTHOTOPE:
                return buildBareOrthotope((OrthotopeDefinition) definition, name);

            default:
                throw new RuntimeException("Unknown shape type: " + definition.getType());
        }
    }

    private String buildBareBall(final BallDefinition ballDefinition, final String name) {
        return new StringBuilder()
                .append("final Ball<")
                .append(coordinatesBuilder.buildClassName(ballDefinition.getCenter()))
                .append("> ")
                .append(name)
                .append(" = new Ball<>(")
                .append(coordinatesBuilder.build(ballDefinition.getCenter()))
                .append(",")
                .append(ballDefinition.getRadius())
                .append(");\n")
                .toString();
    }

    private String buildBareOrthotope(final OrthotopeDefinition orthotopeDefinition, final String name) {
        final StringBuilder code = new StringBuilder();

        final List<String> vectors = new ArrayList<>();
        orthotopeDefinition.getVectors().stream()
                .map(vectorDefinition -> {
                    final String variableName = variableNameBuilder.build("vector");
                    vectors.add(variableName);
                    return vectorBuilder.build(vectorDefinition, variableName);
                })
                .forEach(code::append);

        code
                .append("final GeneralOrthotope<")
                .append(coordinatesBuilder.buildClassName(orthotopeDefinition.getBase()))
                .append("> ")
                .append(name)
                .append(" = new GeneralOrthotope<>(")
                .append(coordinatesBuilder.build(orthotopeDefinition.getBase()))
                .append(",Arrays.asList(")
                .append(vectors.stream().collect(Collectors.joining(",")))
                .append("),")
                .append(orthotopeDefinition.isInfinite())
                .append(");\n");

        return code.toString();
    }

}

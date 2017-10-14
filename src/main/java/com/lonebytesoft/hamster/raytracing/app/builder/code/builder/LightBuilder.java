package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.LightExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.AmbientLightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.ConeLightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.PointLightDefinition;

public class LightBuilder implements StatementBuilder<LightExtendedDefinition> {

    private final ExpressionBuilder<String> variableNameBuilder;
    private final CoordinatesBuilder coordinatesBuilder;
    private final StatementBuilder<VectorDefinition> vectorBuilder;

    public LightBuilder(final ExpressionBuilder<String> variableNameBuilder,
                        final CoordinatesBuilder coordinatesBuilder,
                        final StatementBuilder<VectorDefinition> vectorBuilder) {
        this.variableNameBuilder = variableNameBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
        this.vectorBuilder = vectorBuilder;
    }

    @Override
    public String build(LightExtendedDefinition extendedDefinition, String name) {
        final LightDefinition lightDefinition = extendedDefinition.getLightDefinition();
        switch(lightDefinition.getType()) {
            case AMBIENT:
                final AmbientLightDefinition ambientLightDefinition = (AmbientLightDefinition) lightDefinition;
                return new StringBuilder()
                        .append("final AmbientLightSource<")
                        .append(coordinatesBuilder.buildClassName(extendedDefinition.getSpaceDimensions()))
                        .append("> ")
                        .append(name)
                        .append(" = new AmbientLightSource<>(")
                        .append(ambientLightDefinition.getBrightness())
                        .append(");\n")
                        .toString();

            case CONE:
                final ConeLightDefinition coneLightDefinition = (ConeLightDefinition) lightDefinition;
                final String coneLightDirectionName = variableNameBuilder.build("vector");
                final StringBuilder coneLightCode = new StringBuilder();

                coneLightCode.append(vectorBuilder.build(coneLightDefinition.getDirection(), coneLightDirectionName));

                coneLightCode
                        .append("final ConeLightSource<")
                        .append(coordinatesBuilder.buildClassName(extendedDefinition.getSpaceDimensions()))
                        .append("> ")
                        .append(name)
                        .append(" = new ConeLightSource<>(")
                        .append(coordinatesBuilder.build(coneLightDefinition.getSource()))
                        .append(",")
                        .append(coneLightDirectionName)
                        .append(",")
                        .append(coneLightDefinition.getAngle())
                        .append(",")
                        .append(coneLightDefinition.getBrightness())
                        .append(");\n");

                if(extendedDefinition.getLightPropertiesProviderName() != null) {
                    coneLightCode.append(buildLightPropertiesProviderSetStatement(
                            name, extendedDefinition.getLightPropertiesProviderName()));
                }

                return coneLightCode.toString();

            case POINT:
                final PointLightDefinition pointLightDefinition = (PointLightDefinition) lightDefinition;
                final StringBuilder pointLightCode = new StringBuilder();

                pointLightCode
                        .append("final PointLightSource<")
                        .append(coordinatesBuilder.buildClassName(extendedDefinition.getSpaceDimensions()))
                        .append("> ")
                        .append(name)
                        .append(" = new PointLightSource<>(")
                        .append(coordinatesBuilder.build(pointLightDefinition.getSource()))
                        .append(",")
                        .append(pointLightDefinition.getBrightness())
                        .append(");\n");

                if(extendedDefinition.getLightPropertiesProviderName() != null) {
                    pointLightCode.append(buildLightPropertiesProviderSetStatement(
                            name, extendedDefinition.getLightPropertiesProviderName()));
                }

                return pointLightCode.toString();

            default:
                throw new RuntimeException("Unknown light source type: " + lightDefinition.getType());
        }
    }

    private String buildLightPropertiesProviderSetStatement(final String lightSourceName,
                                                            final String lightPropertiesProviderName) {
        return lightSourceName + ".setLightPropertiesProvider(" + lightPropertiesProviderName + ");\n";
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.FeatureNotImplementedException;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.LightBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.StatementBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.LightExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightType;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.Commit;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.CommitManager;

public class LightBuilderFactory implements BuilderFactory<StatementBuilder<LightExtendedDefinition>> {

    private final CommitManager commitManager;
    private final ExpressionBuilder<String> variableNameBuilder;
    private final CoordinatesBuilder coordinatesBuilder;
    private final StatementBuilder<VectorDefinition> vectorBuilder;

    public LightBuilderFactory(final CommitManager commitManager,
                               final ExpressionBuilder<String> variableNameBuilder,
                               final CoordinatesBuilder coordinatesBuilder,
                               final StatementBuilder<VectorDefinition> vectorBuilder) {
        this.commitManager = commitManager;
        this.variableNameBuilder = variableNameBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
        this.vectorBuilder = vectorBuilder;
    }

    @Override
    public StatementBuilder<LightExtendedDefinition> build(String commitHash) {
        if(commitManager.isOlder(commitHash, Commit.ADDED_CONE_LIGHT.getHash())) {
            return new PreConeLightBuilder(variableNameBuilder, coordinatesBuilder, vectorBuilder);
        } else if(commitManager.isOlder(commitHash, Commit.REFACTORED_LIGHT_PROPERTIES.getHash())) {
            return new PreLightPropertiesLightBuilder(variableNameBuilder, coordinatesBuilder, vectorBuilder);
        } else {
            return new LightBuilder(variableNameBuilder, coordinatesBuilder, vectorBuilder);
        }
    }

    private static class PreLightPropertiesLightBuilder extends LightBuilder {
        public PreLightPropertiesLightBuilder(final ExpressionBuilder<String> variableNameBuilder,
                                              final CoordinatesBuilder coordinatesBuilder,
                                              final StatementBuilder<VectorDefinition> vectorBuilder) {
            super(variableNameBuilder, coordinatesBuilder, vectorBuilder);
        }

        @Override
        public String build(LightExtendedDefinition extendedDefinition, String name) {
            return super.build(extendedDefinition, name)
                    .replace("setLightPropertiesProvider", "setRayTracer");
        }
    }

    private static class PreConeLightBuilder extends PreLightPropertiesLightBuilder {
        public PreConeLightBuilder(final ExpressionBuilder<String> variableNameBuilder,
                                   final CoordinatesBuilder coordinatesBuilder,
                                   final StatementBuilder<VectorDefinition> vectorBuilder) {
            super(variableNameBuilder, coordinatesBuilder, vectorBuilder);
        }

        @Override
        public String build(LightExtendedDefinition extendedDefinition, String name) {
            final LightType lightType = extendedDefinition.getLightDefinition().getType();
            if(lightType == LightType.CONE) {
                throw new FeatureNotImplementedException("cone light source");
            }
            return super.build(extendedDefinition, name);
        }
    }

}

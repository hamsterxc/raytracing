package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.FeatureNotImplementedException;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.LightBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.StatementBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.LightExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightType;

public class LightBuilderFactory implements BuilderFactory<StatementBuilder<LightExtendedDefinition>> {

    private final ExpressionBuilder<String> variableNameBuilder;
    private final CoordinatesBuilder coordinatesBuilder;
    private final StatementBuilder<VectorDefinition> vectorBuilder;

    public LightBuilderFactory(final ExpressionBuilder<String> variableNameBuilder,
                               final CoordinatesBuilder coordinatesBuilder,
                               final StatementBuilder<VectorDefinition> vectorBuilder) {
        this.variableNameBuilder = variableNameBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
        this.vectorBuilder = vectorBuilder;
    }

    @Override
    public StatementBuilder<LightExtendedDefinition> build(Commit commit) {
        if(commit.isOlder(Commit.ADDED_CONE_LIGHT)) {
            return new PreConeLightBuilder(variableNameBuilder, coordinatesBuilder, vectorBuilder);
        } else {
            return new LightBuilder(variableNameBuilder, coordinatesBuilder, vectorBuilder);
        }
    }

    private static class PreConeLightBuilder extends LightBuilder {
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

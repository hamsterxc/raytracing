package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.StatementBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.VectorBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;

public class VectorBuilderFactory implements BuilderFactory<StatementBuilder<VectorDefinition>> {

    private final ExpressionBuilder<String> variableNameBuilder;
    private final CoordinatesBuilder coordinatesBuilder;

    public VectorBuilderFactory(final ExpressionBuilder<String> variableNameBuilder,
                                final CoordinatesBuilder coordinatesBuilder) {
        this.variableNameBuilder = variableNameBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
    }

    @Override
    public StatementBuilder<VectorDefinition> build(Commit commit) {
        return new VectorBuilder(variableNameBuilder, coordinatesBuilder);
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.StatementBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.VectorBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.CommitManager;

public class VectorBuilderFactory implements BuilderFactory<StatementBuilder<VectorDefinition>> {

    private final CommitManager commitManager;
    private final ExpressionBuilder<String> variableNameBuilder;
    private final CoordinatesBuilder coordinatesBuilder;

    public VectorBuilderFactory(final CommitManager commitManager,
                                final ExpressionBuilder<String> variableNameBuilder,
                                final CoordinatesBuilder coordinatesBuilder) {
        this.commitManager = commitManager;
        this.variableNameBuilder = variableNameBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
    }

    @Override
    public StatementBuilder<VectorDefinition> build(String commitHash) {
        return new VectorBuilder(variableNameBuilder, coordinatesBuilder);
    }

}

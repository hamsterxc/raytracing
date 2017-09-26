package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.BareShapeBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.StatementBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.CommitManager;

public class BareShapeBuilderFactory implements BuilderFactory<StatementBuilder<ShapeDefinition>> {

    private final CommitManager commitManager;
    private final ExpressionBuilder<String> variableNameBuilder;
    private final CoordinatesBuilder coordinatesBuilder;
    private final StatementBuilder<VectorDefinition> vectorBuilder;

    public BareShapeBuilderFactory(final CommitManager commitManager,
                                   final ExpressionBuilder<String> variableNameBuilder,
                                   final CoordinatesBuilder coordinatesBuilder,
                                   final StatementBuilder<VectorDefinition> vectorBuilder) {
        this.commitManager = commitManager;
        this.variableNameBuilder = variableNameBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
        this.vectorBuilder = vectorBuilder;
    }

    @Override
    public StatementBuilder<ShapeDefinition> build(String commitHash) {
        return new BareShapeBuilder(variableNameBuilder, coordinatesBuilder, vectorBuilder);
    }

}

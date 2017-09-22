package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.BareShapeBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.StatementBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;

public class BareShapeBuilderFactory implements BuilderFactory<StatementBuilder<ShapeDefinition>> {

    private final ExpressionBuilder<String> variableNameBuilder;
    private final CoordinatesBuilder coordinatesBuilder;
    private final StatementBuilder<VectorDefinition> vectorBuilder;

    public BareShapeBuilderFactory(final ExpressionBuilder<String> variableNameBuilder,
                                   final CoordinatesBuilder coordinatesBuilder,
                                   final StatementBuilder<VectorDefinition> vectorBuilder) {
        this.variableNameBuilder = variableNameBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
        this.vectorBuilder = vectorBuilder;
    }

    @Override
    public StatementBuilder<ShapeDefinition> build(Commit commit) {
        return new BareShapeBuilder(variableNameBuilder, coordinatesBuilder, vectorBuilder);
    }

}

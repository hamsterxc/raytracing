package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ShapeBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.StatementBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.LayerExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ShapeExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;

public class ShapeBuilderFactory implements BuilderFactory<StatementBuilder<ShapeExtendedDefinition>> {

    private final ExpressionBuilder<String> variableNameBuilder;
    private final ExpressionBuilder<LayerExtendedDefinition> layerBuilder;
    private final CoordinatesBuilder coordinatesBuilder;
    private final StatementBuilder<ShapeDefinition> bareShapeBuilder;

    public ShapeBuilderFactory(final ExpressionBuilder<String> variableNameBuilder,
                               final ExpressionBuilder<LayerExtendedDefinition> layerBuilder,
                               final CoordinatesBuilder coordinatesBuilder,
                               final StatementBuilder<ShapeDefinition> bareShapeBuilder) {
        this.variableNameBuilder = variableNameBuilder;
        this.layerBuilder = layerBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
        this.bareShapeBuilder = bareShapeBuilder;
    }

    @Override
    public StatementBuilder<ShapeExtendedDefinition> build(Commit commit) {
        return new ShapeBuilder(variableNameBuilder, layerBuilder, coordinatesBuilder, bareShapeBuilder);
    }

}

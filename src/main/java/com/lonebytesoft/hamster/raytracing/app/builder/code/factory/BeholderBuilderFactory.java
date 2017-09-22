package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.BeholderBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.StatementBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.LightExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ScreenExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ShapeExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import com.lonebytesoft.hamster.raytracing.color.Color;

public class BeholderBuilderFactory implements BuilderFactory<StatementBuilder<SceneDefinition>> {

    private final ExpressionBuilder<String> variableNameBuilder;
    private final ExpressionBuilder<Color> colorBuilder;
    private final CoordinatesBuilder coordinatesBuilder;
    private final StatementBuilder<ScreenExtendedDefinition> screenBuilder;
    private final StatementBuilder<ShapeExtendedDefinition> shapeBuilder;
    private final StatementBuilder<LightExtendedDefinition> lightBuilder;

    public BeholderBuilderFactory(final ExpressionBuilder<String> variableNameBuilder,
                                  final ExpressionBuilder<Color> colorBuilder,
                                  final CoordinatesBuilder coordinatesBuilder,
                                  final StatementBuilder<ScreenExtendedDefinition> screenBuilder,
                                  final StatementBuilder<ShapeExtendedDefinition> shapeBuilder,
                                  final StatementBuilder<LightExtendedDefinition> lightBuilder) {
        this.variableNameBuilder = variableNameBuilder;
        this.colorBuilder = colorBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
        this.screenBuilder = screenBuilder;
        this.shapeBuilder = shapeBuilder;
        this.lightBuilder = lightBuilder;
    }

    @Override
    public StatementBuilder<SceneDefinition> build(Commit commit) {
        return new BeholderBuilder(
                variableNameBuilder, colorBuilder, coordinatesBuilder, screenBuilder, shapeBuilder, lightBuilder);
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.StatementBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ClassFileDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.LayerExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.LightExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.PixelColoringExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ScreenExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ShapeExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.color.Color;

public class CodeBuilderFactory implements BuilderFactory<ExpressionBuilder<ClassFileDefinition>> {

    @Override
    public ExpressionBuilder<ClassFileDefinition> build(Commit commit) {
        final ExpressionBuilder<String> variableNameBuilder = new VariableNameBuilderFactory().build(commit);
        final ExpressionBuilder<Color> colorBuilder = new ColorBuilderFactory().build(commit);
        final CoordinatesBuilder coordinatesBuilder = new CoordinatesBuilderFactory().build(commit);
        final StatementBuilder<VectorDefinition> vectorBuilder = new VectorBuilderFactory(
                variableNameBuilder, coordinatesBuilder
        ).build(commit);

        final StatementBuilder<ShapeDefinition> bareShapeBuilder = new BareShapeBuilderFactory(
                variableNameBuilder, coordinatesBuilder, vectorBuilder
        ).build(commit);
        final ExpressionBuilder<LayerExtendedDefinition> layerBuilder = new LayerBuilderFactory(
                colorBuilder, coordinatesBuilder
        ).build(commit);
        final StatementBuilder<ShapeExtendedDefinition> shapeBuilder = new ShapeBuilderFactory(
                variableNameBuilder, layerBuilder, coordinatesBuilder, bareShapeBuilder
        ).build(commit);

        final StatementBuilder<LightExtendedDefinition> lightBuilder = new LightBuilderFactory(
                variableNameBuilder, coordinatesBuilder, vectorBuilder
        ).build(commit);

        final ExpressionBuilder<PixelColoringExtendedDefinition> pixelColoringBuilder = new PixelColoringBuilderFactory(
                colorBuilder, coordinatesBuilder
        ).build(commit);
        final StatementBuilder<ScreenExtendedDefinition> screenBuilder = new ScreenBuilderFactory(
                variableNameBuilder, pixelColoringBuilder, coordinatesBuilder, bareShapeBuilder
        ).build(commit);

        final StatementBuilder<SceneDefinition> beholderBuilder = new BeholderBuilderFactory(
                variableNameBuilder, colorBuilder, coordinatesBuilder,
                screenBuilder, shapeBuilder, lightBuilder
        ).build(commit);
        final ExpressionBuilder<SceneDefinition> importsBuilder = new ImportsBuilderFactory().build(commit);
        final ExpressionBuilder<ClassFileDefinition> classFileBuilder = new ClassFileBuilderFactory(
                importsBuilder, beholderBuilder, coordinatesBuilder
        ).build(commit);

        return classFileBuilder;
    }

}

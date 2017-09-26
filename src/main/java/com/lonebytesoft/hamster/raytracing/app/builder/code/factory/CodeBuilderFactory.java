package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

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
import com.lonebytesoft.hamster.raytracing.app.helper.commit.CommitManager;
import com.lonebytesoft.hamster.raytracing.color.Color;

public class CodeBuilderFactory implements BuilderFactory<ExpressionBuilder<ClassFileDefinition>> {

    private final CommitManager commitManager;

    public CodeBuilderFactory(final CommitManager commitManager) {
        this.commitManager = commitManager;
    }

    @Override
    public ExpressionBuilder<ClassFileDefinition> build(String commitHash) {
        final ExpressionBuilder<String> variableNameBuilder = new VariableNameBuilderFactory(commitManager).build(commitHash);
        final ExpressionBuilder<Color> colorBuilder = new ColorBuilderFactory(commitManager).build(commitHash);
        final CoordinatesBuilder coordinatesBuilder = new CoordinatesBuilderFactory(commitManager).build(commitHash);
        final StatementBuilder<VectorDefinition> vectorBuilder = new VectorBuilderFactory(
                commitManager, variableNameBuilder, coordinatesBuilder
        ).build(commitHash);

        final StatementBuilder<ShapeDefinition> bareShapeBuilder = new BareShapeBuilderFactory(
                commitManager, variableNameBuilder, coordinatesBuilder, vectorBuilder
        ).build(commitHash);
        final ExpressionBuilder<LayerExtendedDefinition> layerBuilder = new LayerBuilderFactory(
                commitManager, colorBuilder, coordinatesBuilder
        ).build(commitHash);
        final StatementBuilder<ShapeExtendedDefinition> shapeBuilder = new ShapeBuilderFactory(
                commitManager, variableNameBuilder, layerBuilder, coordinatesBuilder, bareShapeBuilder
        ).build(commitHash);

        final StatementBuilder<LightExtendedDefinition> lightBuilder = new LightBuilderFactory(
                commitManager, variableNameBuilder, coordinatesBuilder, vectorBuilder
        ).build(commitHash);

        final ExpressionBuilder<PixelColoringExtendedDefinition> pixelColoringBuilder = new PixelColoringBuilderFactory(
                commitManager, colorBuilder, coordinatesBuilder
        ).build(commitHash);
        final StatementBuilder<ScreenExtendedDefinition> screenBuilder = new ScreenBuilderFactory(
                commitManager, variableNameBuilder, pixelColoringBuilder, coordinatesBuilder, bareShapeBuilder
        ).build(commitHash);

        final StatementBuilder<SceneDefinition> beholderBuilder = new BeholderBuilderFactory(
                commitManager, variableNameBuilder, colorBuilder, coordinatesBuilder,
                screenBuilder, shapeBuilder, lightBuilder
        ).build(commitHash);
        final ExpressionBuilder<SceneDefinition> importsBuilder = new ImportsBuilderFactory(commitManager).build(commitHash);
        final ExpressionBuilder<ClassFileDefinition> classFileBuilder = new ClassFileBuilderFactory(
                commitManager, importsBuilder, beholderBuilder, coordinatesBuilder
        ).build(commitHash);

        return classFileBuilder;
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.FeatureNotImplementedException;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ScreenBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.StatementBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.PixelColoringExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ScreenExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.ScreenDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeType;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.Commit;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.CommitManager;

public class ScreenBuilderFactory implements BuilderFactory<StatementBuilder<ScreenExtendedDefinition>> {

    private final CommitManager commitManager;
    private final ExpressionBuilder<String> variableNameBuilder;
    private final ExpressionBuilder<PixelColoringExtendedDefinition> pixelColoringBuilder;
    private final CoordinatesBuilder coordinatesBuilder;
    private final StatementBuilder<ShapeDefinition> bareShapeBuilder;

    public ScreenBuilderFactory(final CommitManager commitManager,
                                final ExpressionBuilder<String> variableNameBuilder,
                                final ExpressionBuilder<PixelColoringExtendedDefinition> pixelColoringBuilder,
                                final CoordinatesBuilder coordinatesBuilder,
                                final StatementBuilder<ShapeDefinition> bareShapeBuilder) {
        this.commitManager = commitManager;
        this.variableNameBuilder = variableNameBuilder;
        this.pixelColoringBuilder = pixelColoringBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
        this.bareShapeBuilder = bareShapeBuilder;
    }

    @Override
    public StatementBuilder<ScreenExtendedDefinition> build(String commitHash) {
        if(commitManager.isOlder(commitHash, Commit.ADDED_SURFACED_SCREEN.getHash())) {
            return new PreSurfacedScreenBuilder(variableNameBuilder, pixelColoringBuilder, coordinatesBuilder, bareShapeBuilder);
        } else {
            return new ScreenBuilder(variableNameBuilder, pixelColoringBuilder, coordinatesBuilder, bareShapeBuilder);
        }
    }

    private static class PreSurfacedScreenBuilder extends ScreenBuilder {
        private final ExpressionBuilder<String> variableNameBuilder;
        private final ExpressionBuilder<PixelColoringExtendedDefinition> pixelColoringBuilder;
        private final CoordinatesBuilder coordinatesBuilder;
        private final StatementBuilder<ShapeDefinition> bareShapeBuilder;

        public PreSurfacedScreenBuilder(final ExpressionBuilder<String> variableNameBuilder,
                                        final ExpressionBuilder<PixelColoringExtendedDefinition> pixelColoringBuilder,
                                        final CoordinatesBuilder coordinatesBuilder,
                                        final StatementBuilder<ShapeDefinition> bareShapeBuilder) {
            super(variableNameBuilder, pixelColoringBuilder, coordinatesBuilder, bareShapeBuilder);
            this.variableNameBuilder = variableNameBuilder;
            this.pixelColoringBuilder = pixelColoringBuilder;
            this.coordinatesBuilder = coordinatesBuilder;
            this.bareShapeBuilder = bareShapeBuilder;
        }

        @Override
        public String build(ScreenExtendedDefinition extendedDefinition, String name) {
            final ScreenDefinition definition = extendedDefinition.getScreenDefinition();
            if(definition.getShape().getType() != ShapeType.ORTHOTOPE) {
                throw new FeatureNotImplementedException("screen shape non-orthotope");
            }

            final StringBuilder code = new StringBuilder();

            final String shapeName = variableNameBuilder.build("shape");
            code.append(bareShapeBuilder.build(definition.getShape(), shapeName));

            code
                    .append("final Screen<")
                    .append(coordinatesBuilder.buildClassName(extendedDefinition.getSpaceDimensions()))
                    .append(",")
                    .append(coordinatesBuilder.buildClassName(extendedDefinition.getPictureDimensions()))
                    .append("> ")
                    .append(name)
                    .append(" = new ScreenOrthotope<>(")
                    .append(shapeName)
                    .append(",")
                    .append(coordinatesBuilder.build(definition.getResolution()))
                    .append(",")
                    .append(pixelColoringBuilder.build(new PixelColoringExtendedDefinition(
                            definition.getPixelColoring(), extendedDefinition.getPictureDimensions())))
                    .append(");\n");

            return code.toString();
        }
    }

}

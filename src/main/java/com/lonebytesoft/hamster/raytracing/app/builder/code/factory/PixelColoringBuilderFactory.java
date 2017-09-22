package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.PixelColoringBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.PixelColoringExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor.PixelColoringDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor.SupersamplingPixelColoringDefinition;
import com.lonebytesoft.hamster.raytracing.color.Color;

public class PixelColoringBuilderFactory implements BuilderFactory<ExpressionBuilder<PixelColoringExtendedDefinition>> {

    private final ExpressionBuilder<Color> colorBuilder;
    private final CoordinatesBuilder coordinatesBuilder;

    public PixelColoringBuilderFactory(final ExpressionBuilder<Color> colorBuilder,
                                       final CoordinatesBuilder coordinatesBuilder) {
        this.colorBuilder = colorBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
    }

    @Override
    public ExpressionBuilder<PixelColoringExtendedDefinition> build(Commit commit) {
        if(commit.isOlder(Commit.REFACTORED_PIXEL_COLORING)) {
            return new PreRefactoringPixelColoringBuilder(colorBuilder, coordinatesBuilder);
        } else {
            return new PixelColoringBuilder(colorBuilder, coordinatesBuilder);
        }
    }

    private static class PreRefactoringPixelColoringBuilder extends PixelColoringBuilder {
        private final ExpressionBuilder<Color> colorBuilder;

        public PreRefactoringPixelColoringBuilder(final ExpressionBuilder<Color> colorBuilder,
                                                  final CoordinatesBuilder coordinatesBuilder) {
            super(colorBuilder, coordinatesBuilder);
            this.colorBuilder = colorBuilder;
        }

        @Override
        public String build(PixelColoringExtendedDefinition extendedDefinition) {
            final PixelColoringDefinition definition = extendedDefinition.getPixelColoringDefinition();
            switch(definition.getType()) {
                case SIMPLE:
                    return "new SimplePixelColoringStrategy<>()";

                case SUPERSAMPLING:
                    final SupersamplingPixelColoringDefinition supersamplingPixelColoringDefinition =
                            (SupersamplingPixelColoringDefinition) definition;
                    return "new SupersamplingPixelColoringStrategy<>("
                            + supersamplingPixelColoringDefinition.getMultiplier()
                            + ","
                            + colorBuilder.build(supersamplingPixelColoringDefinition.getColorDefault())
                            + ")";

                default:
                    return super.build(extendedDefinition);
            }
        }
    }

}
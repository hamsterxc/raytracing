package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.FeatureNotImplementedException;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.LayerBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.LayerExtendedDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.LayerType;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.texture.TextureType;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.texture.TexturedLayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.Commit;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.CommitManager;
import com.lonebytesoft.hamster.raytracing.color.Color;

public class LayerBuilderFactory implements BuilderFactory<ExpressionBuilder<LayerExtendedDefinition>> {

    private final CommitManager commitManager;
    private final ExpressionBuilder<Color> colorBuilder;
    private final CoordinatesBuilder coordinatesBuilder;

    public LayerBuilderFactory(final CommitManager commitManager,
                               final ExpressionBuilder<Color> colorBuilder,
                               final CoordinatesBuilder coordinatesBuilder) {
        this.commitManager = commitManager;
        this.colorBuilder = colorBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
    }

    @Override
    public ExpressionBuilder<LayerExtendedDefinition> build(String commitHash) {
        if(commitManager.isOlder(commitHash, Commit.ADDED_EXAMPLE.getHash())) {
            return new PreCheckersLayerBuilder(colorBuilder, coordinatesBuilder);
        } else if(commitManager.isOlder(commitHash, Commit.ADDED_REFRACTING.getHash())) {
            return new PreRefractingLayerBuilder(colorBuilder, coordinatesBuilder);
        } else {
            return new LayerBuilder(colorBuilder, coordinatesBuilder);
        }
    }

    private static class PreRefractingLayerBuilder extends LayerBuilder {
        public PreRefractingLayerBuilder(final ExpressionBuilder<Color> colorBuilder,
                                         final CoordinatesBuilder coordinatesBuilder) {
            super(colorBuilder, coordinatesBuilder);
        }

        @Override
        public String build(LayerExtendedDefinition extendedDefinition) {
            final LayerType layerType = extendedDefinition.getLayerDefinition().getType();
            if(layerType == LayerType.REFRACTING) {
                throw new FeatureNotImplementedException("layer refracting");
            }
            return super.build(extendedDefinition);
        }
    }

    private static class PreCheckersLayerBuilder extends PreRefractingLayerBuilder {
        public PreCheckersLayerBuilder(final ExpressionBuilder<Color> colorBuilder,
                                       final CoordinatesBuilder coordinatesBuilder) {
            super(colorBuilder, coordinatesBuilder);
        }

        @Override
        public String build(LayerExtendedDefinition extendedDefinition) {
            final LayerType layerType = extendedDefinition.getLayerDefinition().getType();
            if(layerType == LayerType.TEXTURED) {
                final TextureType textureType = ((TexturedLayerDefinition) extendedDefinition.getLayerDefinition()).getTextureType();
                if(textureType == TextureType.CHECKERS) {
                    throw new FeatureNotImplementedException("layer textured checkers");
                }
            }
            return super.build(extendedDefinition);
        }
    }

}

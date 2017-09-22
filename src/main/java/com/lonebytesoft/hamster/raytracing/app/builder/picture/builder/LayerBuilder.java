package com.lonebytesoft.hamster.raytracing.app.builder.picture.builder;

import com.lonebytesoft.hamster.raytracing.app.CheckersTexture;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.LayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.RefractingLayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.SolidColoredLayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.texture.TexturedCheckersLayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.texture.TexturedLayerDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.shape.ShapeLayer;
import com.lonebytesoft.hamster.raytracing.shape.adapter.ReflectingAdapter;
import com.lonebytesoft.hamster.raytracing.shape.adapter.RefractingAdapter;
import com.lonebytesoft.hamster.raytracing.shape.adapter.SolidColoredAdapter;
import com.lonebytesoft.hamster.raytracing.shape.adapter.TexturedAdapter;
import com.lonebytesoft.hamster.raytracing.shape.adapter.TransparentAdapter;
import com.lonebytesoft.hamster.raytracing.shape.feature.Reflecting;
import com.lonebytesoft.hamster.raytracing.shape.feature.Refracting;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;
import com.lonebytesoft.hamster.raytracing.shape.feature.Transparent;

@SuppressWarnings("unchecked")
public class LayerBuilder<T extends Coordinates<T>> {

    private final CoordinatesBuilder coordinatesBuilder;

    public LayerBuilder(final CoordinatesBuilder coordinatesBuilder) {
        this.coordinatesBuilder = coordinatesBuilder;
    }

    public <F extends Coordinates<F>> ShapeLayer<T> buildLayer(final LayerDefinition layerDefinition, final Object entity) {
        switch(layerDefinition.getType()) {
            case REFLECTING:
                return new ReflectingAdapter<>(PictureBuilderUtils.cast(entity, Reflecting.class));

            case REFRACTING:
                final RefractingLayerDefinition refractingLayerDefinition = (RefractingLayerDefinition) layerDefinition;
                return new RefractingAdapter<>(PictureBuilderUtils.cast(entity, Refracting.class), refractingLayerDefinition.getCoeff());

            case SOLID_COLORED:
                final SolidColoredLayerDefinition solidColoredLayerDefinition = (SolidColoredLayerDefinition) layerDefinition;
                return new SolidColoredAdapter<>(solidColoredLayerDefinition.getColor());

            case TEXTURED:
                final TexturedLayerDefinition texturedLayerDefinition = (TexturedLayerDefinition) layerDefinition;
                final Surfaced<T> surfaced = PictureBuilderUtils.cast(entity, Surfaced.class);
                switch(texturedLayerDefinition.getTextureType()) {
                    case CHECKERS:
                        final TexturedCheckersLayerDefinition texturedCheckersLayerDefinition =
                                (TexturedCheckersLayerDefinition) texturedLayerDefinition;
                        return new TexturedAdapter<T, F>(
                                surfaced,
                                new CheckersTexture<>(
                                        texturedCheckersLayerDefinition.getMultipliers(),
                                        texturedCheckersLayerDefinition.getColors()),
                                coordinatesBuilder.buildCoordinates(texturedCheckersLayerDefinition.getMultipliers().size()));

                    default:
                        throw new RuntimeException("Unknown texture type: " + texturedLayerDefinition.getTextureType());
                }

            case TRANSPARENT:
                return new TransparentAdapter<>(PictureBuilderUtils.cast(entity, Transparent.class));

            default:
                throw new RuntimeException("Unknown layer type: " + layerDefinition.getType());
        }
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.texture;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.LayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.LayerType;

public abstract class TexturedLayerDefinition extends LayerDefinition {

    public TexturedLayerDefinition(final double weight) {
        super(weight);
    }

    @Override
    public LayerType getType() {
        return LayerType.TEXTURED;
    }

    public abstract TextureType getTextureType();

}

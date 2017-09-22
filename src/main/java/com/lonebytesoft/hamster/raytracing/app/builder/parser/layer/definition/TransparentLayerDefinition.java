package com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition;

public class TransparentLayerDefinition extends LayerDefinition {

    public TransparentLayerDefinition(final double weight) {
        super(weight);
    }

    @Override
    public LayerType getType() {
        return LayerType.TRANSPARENT;
    }

}

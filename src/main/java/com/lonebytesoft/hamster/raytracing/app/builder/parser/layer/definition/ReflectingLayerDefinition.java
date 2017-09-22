package com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition;

public class ReflectingLayerDefinition extends LayerDefinition {

    public ReflectingLayerDefinition(final double weight) {
        super(weight);
    }

    @Override
    public LayerType getType() {
        return LayerType.REFLECTING;
    }

}

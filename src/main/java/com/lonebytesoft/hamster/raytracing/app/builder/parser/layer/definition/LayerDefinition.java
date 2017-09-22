package com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition;

public abstract class LayerDefinition {

    private final double weight;

    public LayerDefinition(final double weight) {
        this.weight = weight;
    }

    public abstract LayerType getType();

    public double getWeight() {
        return weight;
    }

}

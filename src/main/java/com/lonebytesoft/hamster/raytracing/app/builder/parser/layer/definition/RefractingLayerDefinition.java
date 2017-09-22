package com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition;

public class RefractingLayerDefinition extends LayerDefinition {

    private final double coeff;

    public RefractingLayerDefinition(final double weight, final double coeff) {
        super(weight);
        this.coeff = coeff;
    }

    @Override
    public LayerType getType() {
        return LayerType.REFRACTING;
    }

    public double getCoeff() {
        return coeff;
    }

}

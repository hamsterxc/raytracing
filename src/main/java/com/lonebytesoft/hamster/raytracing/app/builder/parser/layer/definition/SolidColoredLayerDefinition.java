package com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition;

import com.lonebytesoft.hamster.raytracing.color.Color;

public class SolidColoredLayerDefinition extends LayerDefinition {

    private final Color color;

    public SolidColoredLayerDefinition(final double weight, final Color color) {
        super(weight);
        this.color = color;
    }

    @Override
    public LayerType getType() {
        return LayerType.SOLID_COLORED;
    }

    public Color getColor() {
        return color;
    }

}

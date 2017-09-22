package com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor;

import com.lonebytesoft.hamster.raytracing.color.Color;

public class SupersamplingPixelColoringDefinition extends PixelColoringDefinition {

    private final int multiplier;
    private final Color colorDefault;

    public SupersamplingPixelColoringDefinition(final int multiplier, final Color colorDefault) {
        this.multiplier = multiplier;
        this.colorDefault = colorDefault;
    }

    @Override
    public PixelColoringType getType() {
        return PixelColoringType.SUPERSAMPLING;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public Color getColorDefault() {
        return colorDefault;
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.code.definition;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor.PixelColoringDefinition;

public class PixelColoringExtendedDefinition {

    private final PixelColoringDefinition pixelColoringDefinition;
    private final int pictureDimensions;

    public PixelColoringExtendedDefinition(final PixelColoringDefinition pixelColoringDefinition,
                                           final int pictureDimensions) {
        this.pixelColoringDefinition = pixelColoringDefinition;
        this.pictureDimensions = pictureDimensions;
    }

    public PixelColoringDefinition getPixelColoringDefinition() {
        return pixelColoringDefinition;
    }

    public int getPictureDimensions() {
        return pictureDimensions;
    }

}

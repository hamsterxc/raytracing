package com.lonebytesoft.hamster.raytracing.app.builder.code.definition;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.ScreenDefinition;

public class ScreenExtendedDefinition {

    private final ScreenDefinition screenDefinition;
    private final int spaceDimensions;
    private final int pictureDimensions;

    public ScreenExtendedDefinition(final ScreenDefinition screenDefinition,
                                    final int spaceDimensions, final int pictureDimensions) {
        this.screenDefinition = screenDefinition;
        this.spaceDimensions = spaceDimensions;
        this.pictureDimensions = pictureDimensions;
    }

    public ScreenDefinition getScreenDefinition() {
        return screenDefinition;
    }

    public int getSpaceDimensions() {
        return spaceDimensions;
    }

    public int getPictureDimensions() {
        return pictureDimensions;
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.code.definition;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightDefinition;

public class LightExtendedDefinition {

    private final LightDefinition lightDefinition;
    private final int spaceDimensions;
    private final String rayTracerName;

    public LightExtendedDefinition(final LightDefinition lightDefinition,
                                   final int spaceDimensions, final String rayTracerName) {
        this.lightDefinition = lightDefinition;
        this.spaceDimensions = spaceDimensions;
        this.rayTracerName = rayTracerName;
    }

    public LightDefinition getLightDefinition() {
        return lightDefinition;
    }

    public int getSpaceDimensions() {
        return spaceDimensions;
    }

    public String getRayTracerName() {
        return rayTracerName;
    }

}

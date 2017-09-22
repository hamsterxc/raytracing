package com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition;

public class AmbientLightDefinition extends LightDefinition {

    private final double brightness;

    public AmbientLightDefinition(final double brightness) {
        this.brightness = brightness;
    }

    @Override
    public LightType getType() {
        return LightType.AMBIENT;
    }

    public double getBrightness() {
        return brightness;
    }

}

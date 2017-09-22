package com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public class PointLightDefinition extends LightDefinition {

    private final Coordinates<?> source;
    private final double brightness;

    public PointLightDefinition(final Coordinates<?> source, final double brightness) {
        this.source = source;
        this.brightness = brightness;
    }

    @Override
    public LightType getType() {
        return LightType.POINT;
    }

    public Coordinates<?> getSource() {
        return source;
    }

    public double getBrightness() {
        return brightness;
    }

}

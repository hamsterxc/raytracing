package com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public class ConeLightDefinition extends LightDefinition {

    private final Coordinates<?> source;
    private final VectorDefinition direction;
    private final double angle;
    private final double brightness;

    public ConeLightDefinition(final Coordinates<?> source, final VectorDefinition direction,
                               final double angle, final double brightness) {
        this.source = source;
        this.direction = direction;
        this.angle = angle;
        this.brightness = brightness;
    }

    @Override
    public LightType getType() {
        return LightType.CONE;
    }

    public Coordinates<?> getSource() {
        return source;
    }

    public VectorDefinition getDirection() {
        return direction;
    }

    public double getAngle() {
        return angle;
    }

    public double getBrightness() {
        return brightness;
    }

}

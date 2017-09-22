package com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition;

public class VectorRotationDefinition {

    private final int axisFirst;
    private final int axisSecond;
    private final double angle;

    public VectorRotationDefinition(final int axisFirst, final int axisSecond, final double angle) {
        this.axisFirst = axisFirst;
        this.axisSecond = axisSecond;
        this.angle = angle;
    }

    public int getAxisFirst() {
        return axisFirst;
    }

    public int getAxisSecond() {
        return axisSecond;
    }

    public double getAngle() {
        return angle;
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public class BallDefinition extends ShapeDefinition {

    private final Coordinates<?> center;
    private final double radius;

    public BallDefinition(final Coordinates<?> center, final double radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public ShapeType getType() {
        return ShapeType.BALL;
    }

    public Coordinates<?> getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

}

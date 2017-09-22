package com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor.PixelColoringDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public class ScreenDefinition {

    private final ShapeDefinition shape;
    private final Coordinates<?> from;
    private final Coordinates<?> to;
    private final Coordinates<?> resolution;
    private final PixelColoringDefinition pixelColoring;

    public ScreenDefinition(final ShapeDefinition shape, final Coordinates<?> from, final Coordinates<?> to,
                            final Coordinates<?> resolution, final PixelColoringDefinition pixelColoring) {
        this.shape = shape;
        this.from = from;
        this.to = to;
        this.resolution = resolution;
        this.pixelColoring = pixelColoring;
    }

    public ShapeDefinition getShape() {
        return shape;
    }

    public Coordinates<?> getFrom() {
        return from;
    }

    public Coordinates<?> getTo() {
        return to;
    }

    public Coordinates<?> getResolution() {
        return resolution;
    }

    public PixelColoringDefinition getPixelColoring() {
        return pixelColoring;
    }

}

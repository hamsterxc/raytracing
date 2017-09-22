package com.lonebytesoft.hamster.raytracing.app.builder.code.definition;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;

public class ShapeExtendedDefinition {

    private final ShapeDefinition shapeDefinition;
    private final int spaceDimensions;

    public ShapeExtendedDefinition(final ShapeDefinition shapeDefinition,
                                   final int spaceDimensions) {
        this.shapeDefinition = shapeDefinition;
        this.spaceDimensions = spaceDimensions;
    }

    public ShapeDefinition getShapeDefinition() {
        return shapeDefinition;
    }

    public int getSpaceDimensions() {
        return spaceDimensions;
    }

}

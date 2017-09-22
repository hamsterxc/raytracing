package com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VectorDefinition {

    private final Coordinates<?> coordinates;
    private final List<VectorRotationDefinition> rotations = new ArrayList<>();

    public VectorDefinition(final Coordinates<?> coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates<?> getCoordinates() {
        return coordinates;
    }

    public List<VectorRotationDefinition> getRotations() {
        return Collections.unmodifiableList(rotations);
    }

    public void addRotation(final VectorRotationDefinition rotation) {
        this.rotations.add(rotation);
    }

}

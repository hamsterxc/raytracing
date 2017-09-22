package com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrthotopeDefinition extends ShapeDefinition {

    private final Coordinates<?> base;
    private final List<VectorDefinition> vectors;
    private final boolean isInfinite;

    public OrthotopeDefinition(final Coordinates<?> base, final List<VectorDefinition> vectors, final boolean isInfinite) {
        this.base = base;
        this.vectors = Collections.unmodifiableList(new ArrayList<>(vectors));
        this.isInfinite = isInfinite;
    }

    @Override
    public ShapeType getType() {
        return ShapeType.ORTHOTOPE;
    }

    public Coordinates<?> getBase() {
        return base;
    }

    public List<VectorDefinition> getVectors() {
        return vectors;
    }

    public boolean isInfinite() {
        return isInfinite;
    }

}

package com.lonebytesoft.hamster.raytracing.shape.generic.orthotope;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

import java.util.List;

public class Orthotope<T extends Coordinates<T>> extends GeneralOrthotope<T> {

    public Orthotope(final T base, final List<T> vectors) {
        super(base, vectors, false);
        if(vectors.size() != base.getDimensions()) {
            throw new IllegalArgumentException("Vectors count not equal to dimensionality");
        }
    }

}

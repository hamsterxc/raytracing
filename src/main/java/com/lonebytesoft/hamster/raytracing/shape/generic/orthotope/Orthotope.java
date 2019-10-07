package com.lonebytesoft.hamster.raytracing.shape.generic.orthotope;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;

import java.util.List;

public class Orthotope<T extends Coordinates> extends GeneralOrthotope<T> {

    public Orthotope(
            final T base,
            final List<T> vectors,
            final GeometryCalculator<T> geometryCalculator
    ) {
        super(base, vectors, false, geometryCalculator);
        if(vectors.size() != base.getDimensions()) {
            throw new IllegalArgumentException("Vectors count not equal to dimensionality");
        }
    }

}

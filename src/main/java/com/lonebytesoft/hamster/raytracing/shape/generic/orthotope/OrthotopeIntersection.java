package com.lonebytesoft.hamster.raytracing.shape.generic.orthotope;

import java.util.Arrays;
import java.util.List;

class OrthotopeIntersection {

    private final double distanceMultiplier;

    private final long fixVectorsIndex;
    private final long fixVectorsValueIndex;
    private final double[] vectorMultipliers;

    public OrthotopeIntersection(final List<Double> solution, final long fixVectorsIndex, final long fixVectorsValueIndex) {
        this.fixVectorsIndex = fixVectorsIndex;
        this.fixVectorsValueIndex = fixVectorsValueIndex;

        distanceMultiplier = solution.get(0);

        final int vectorsCount = solution.size() - 1;
        vectorMultipliers = new double[vectorsCount];
        for(int i = 0; i < vectorsCount; i++) {
            vectorMultipliers[i] = solution.get(i + 1);
        }
    }

    public double getDistanceMultiplier() {
        return distanceMultiplier;
    }

    public long getFixVectorsIndex() {
        return fixVectorsIndex;
    }

    public long getFixVectorsValueIndex() {
        return fixVectorsValueIndex;
    }

    public double[] getVectorMultipliers() {
        return Arrays.copyOf(vectorMultipliers, vectorMultipliers.length);
    }

}

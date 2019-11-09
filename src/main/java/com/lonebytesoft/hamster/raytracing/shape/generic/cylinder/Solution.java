package com.lonebytesoft.hamster.raytracing.shape.generic.cylinder;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public class Solution<T extends Coordinates> {

    private final double[] sXCoeffs;
    private final double[] yXCoeffs;
    private final T rayDirection;
    private final Double rayDistance;
    private final T intersection;
    private final Double axisDistance;
    private final T axisPoint;

    public Solution(
            final double[] sXCoeffs,
            final double[] yXCoeffs,
            final T rayDirection,
            final Double rayDistance,
            final T intersection,
            final Double axisDistance,
            final T axisPoint
    ) {
        this.sXCoeffs = sXCoeffs;
        this.yXCoeffs = yXCoeffs;
        this.rayDirection = rayDirection;
        this.rayDistance = rayDistance;
        this.intersection = intersection;
        this.axisDistance = axisDistance;
        this.axisPoint = axisPoint;
    }

    public double[] getSXCoeffs() {
        return sXCoeffs;
    }

    public double[] getYXCoeffs() {
        return yXCoeffs;
    }

    public T getRayDirection() {
        return rayDirection;
    }

    public Double getRayDistance() {
        return rayDistance;
    }

    public T getIntersection() {
        return intersection;
    }

    public Double getAxisDistance() {
        return axisDistance;
    }

    public T getAxisPoint() {
        return axisPoint;
    }

//    public static <T extends Coordinates> Builder<T> builder() {
//        return new Builder<>();
//    }
//
//    private static class Builder<T extends Coordinates> {
//
//        private T rayDirection;
//        private Double rayDistance;
//        private Double axisDistance;
//        private double[] sXCoeffs = {};
//        private double[] yXCoeffs = {};
//        private T intersection;
//        private T axisPoint;
//
//        private Builder() {
//        }
//
//        public Builder rayDirection(final T rayDirection) {
//            this.rayDirection = rayDirection;
//            return this;
//        }
//
//        public Builder rayDistance(final Double rayDistance) {
//            this.rayDistance = rayDistance;
//            return this;
//        }
//
//        public Builder axisDistance(final Double axisDistance) {
//            this.axisDistance = axisDistance;
//            return this;
//        }
//
//        public Builder sXCoeffs(final double[] sXCoeffs) {
//            this.sXCoeffs = sXCoeffs;
//            return this;
//        }
//
//        public Builder yXCoeffs(final double[] yXCoeffs) {
//            this.yXCoeffs = yXCoeffs;
//            return this;
//        }
//
//        public Builder intersection(final T intersection) {
//            this.intersection = intersection;
//            return this;
//        }
//
//        public Builder axisPoint(final T axisPoint) {
//            this.axisPoint = axisPoint;
//            return this;
//        }
//
//        public Solution<T> build() {
//            return new Solution<>(rayDirection, rayDistance, axisDistance, sXCoeffs, yXCoeffs, intersection, axisPoint);
//        }
//
//    }

}

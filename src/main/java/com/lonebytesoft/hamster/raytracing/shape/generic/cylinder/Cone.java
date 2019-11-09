package com.lonebytesoft.hamster.raytracing.shape.generic.cylinder;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import com.lonebytesoft.hamster.raytracing.util.math.MathCalculator;

import static java.lang.Math.cos;
import static java.lang.Math.tan;

public class Cone<T extends Coordinates> extends RightSymmetricCylindricalSurface<T> {

    private final T apex;
    private final T direction;
    private final double angle;
    private final double length;
    private final GeometryCalculator<T> geometryCalculator;

    public Cone(
            final T apex,
            final T direction,
            final double angle,
            final double length,
            final GeometryCalculator<T> geometryCalculator
    ) {
        super(apex, direction, geometryCalculator);
        this.apex = apex;
        this.direction = geometryCalculator.normalize(direction);
        this.angle = angle;
        this.length = length;
        this.geometryCalculator = geometryCalculator;
    }

    /*
        |r| <= y * tg(angle)

        tg(angle) = t
        y = x*p + q
        s^2 = x^2*a + x*b + c
        x^2*a + x*b + c = t^2*(x^2*p^2 + x*2*p*q + q*q)
        x^2*(a - t^2*p^2) + x*(b - t^2*2*p*q) + (c - t^2*q*q) = 0
     */
    @Override
    protected Double solve(double[] sXCoeffs, double[] yXCoeffs) {
        final double tan = tan(angle);
        final double tanSquare = tan * tan;
        return chooseRoot(
                MathCalculator.solveQuadratic(
                        sXCoeffs[2] - tanSquare * yXCoeffs[1] * yXCoeffs[1],
                        sXCoeffs[1] - 2 * tanSquare * yXCoeffs[1] * yXCoeffs[0],
                        sXCoeffs[0] - tanSquare * yXCoeffs[0] * yXCoeffs[0]
                ),
                x -> {
                    final double y = calculateValue(x, yXCoeffs);
                    return (y >= 0) && (y <= length);
                }
        );
    }

    @Override
    protected boolean isInside(double axisDistance, double distance) {
        return (axisDistance >= 0) && (axisDistance <= length) && (distance <= (axisDistance * tan(angle)));
    }

    @Override
    protected T calculateNormalBase(Solution<T> solution) {
        final double cos = cos(angle);
        final double distance = solution.getAxisDistance() / (cos * cos);
        return geometryCalculator.follow(apex, direction, distance);
    }

}

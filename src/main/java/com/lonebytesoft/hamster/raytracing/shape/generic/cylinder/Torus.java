package com.lonebytesoft.hamster.raytracing.shape.generic.cylinder;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import com.lonebytesoft.hamster.raytracing.util.math.MathCalculator;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Torus<T extends Coordinates> extends RightSymmetricCylindricalSurface<T> {

    private final T center;
    private final double radiusMajor;
    private final double radiusMinor;
    private final GeometryCalculator<T> geometryCalculator;

    public Torus(
            final T center,
            final T axis,
            final double radiusMajor,
            final double radiusMinor,
            final GeometryCalculator<T> geometryCalculator
    ) {
        super(center, axis, geometryCalculator);
        this.center = center;
        this.radiusMajor = radiusMajor;
        this.radiusMinor = radiusMinor;
        this.geometryCalculator = geometryCalculator;
    }

    /*
        abs(s - R) <= sqrt(r^2 - y^2)

        s = R +- sqrt(r^2 - y^2)
        s^2 = R^2 +- 2*R*sqrt(r^2 - y^2) + r^2 - y^2

        y = x*p + q
        s^2 = x^2*a + x*b + c
        x^2*a + x*b + c = R^2 +- 2*R*sqrt(r^2 - y^2) + r^2 - y^2
        4*R^2*(r^2 - y^2) = (x^2*a + x*b + c - R^2 - r^2 + y^2)^2
        4*R^2*(r*2 - x^2*p^2 - x*2*p*q - q^2) = (x^2*a + x*b + c - R^2 - r^2 + x^2*p^2 + x*2*p*q + q^2)^2
        4*R^2*(x^2*(-p^2) + x*(-2*p*q) + (r^2 - q^2)) = (x^2*(a + p^2) + x*(b + 2*p*q) + (c + q^2 - R^2 - r^2))^2
        x^2*A + x*B + C = (x^2*D + x*E + F)^2
        x^4*(D*D) + x^3*(2*D*E) + x^2*(E*E + 2*D*F - A) + x*(2*E*F - B) + (F*F - C) = 0

        A = -4ppRR
        B = -8pqRR
        C = 4RR(rr-qq)
        D = a + pp
        E = b + 2pq
        F = c + qq - RR - rr
     */
    @Override
    protected Double solve(double[] sXCoeffs, double[] yXCoeffs) {
        final double a = -4d * yXCoeffs[1] * yXCoeffs[1] * radiusMajor * radiusMajor;
        final double b = -8d * yXCoeffs[1] * yXCoeffs[0] * radiusMajor * radiusMajor;
        final double c = 4d * radiusMajor * radiusMajor * (radiusMinor * radiusMinor - yXCoeffs[0] * yXCoeffs[0]);
        final double d = sXCoeffs[2] + yXCoeffs[1] * yXCoeffs[1];
        final double e = sXCoeffs[1] + 2d * yXCoeffs[1] * yXCoeffs[0];
        final double f = sXCoeffs[0] + yXCoeffs[0] * yXCoeffs[0] - radiusMajor * radiusMajor - radiusMinor * radiusMinor;

        return chooseRoot(
                MathCalculator.solveQuartic(
                        d * d,
                        2d * d * e,
                        e * e + 2d * d * f - a,
                        2d * e * f - b,
                        f * f - c
                ),
                x -> {
                    final double y = calculateValue(x, yXCoeffs);
                    return abs(y) <= radiusMinor;
                }
        );
    }

    /*
        abs(s - R) <= sqrt(r^2 - y^2)
     */
    @Override
    protected boolean isInside(double axisDistance, double distance) {
        return (abs(axisDistance) <= radiusMinor)
                && (abs(distance - radiusMajor) <= sqrt(radiusMinor * radiusMinor - axisDistance * axisDistance));
    }

    /*
        c + ((a + x*b) - (c + y*d))*(R/s)
     */
    @Override
    protected T calculateNormalBase(Solution<T> solution) {
        final T vector = geometryCalculator.subtract(solution.getIntersection(), solution.getAxisPoint());
        final double multiplier = radiusMajor / sqrt(calculateValue(solution.getRayDistance(), solution.getSXCoeffs()));
        return geometryCalculator.follow(center, vector, multiplier);
    }

}

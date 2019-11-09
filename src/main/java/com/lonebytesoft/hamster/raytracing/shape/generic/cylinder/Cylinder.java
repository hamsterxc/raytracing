package com.lonebytesoft.hamster.raytracing.shape.generic.cylinder;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import com.lonebytesoft.hamster.raytracing.util.math.MathCalculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static java.lang.Math.sqrt;

public class Cylinder<T extends Coordinates> extends RightSymmetricCylindricalSurface<T> {

    private final T base;
    private final T direction;
    private final double radius;
    private final double length;
    private final GeometryCalculator<T> geometryCalculator;

    public Cylinder(
            final T from,
            final T to,
            final double radius,
            final GeometryCalculator<T> geometryCalculator
    ) {
        super(from, geometryCalculator.subtract(to, from), geometryCalculator);

        final T axis = geometryCalculator.subtract(to, from);
        this.base = from;
        this.direction = geometryCalculator.normalize(axis);
        this.radius = radius;
        this.length = geometryCalculator.length(axis);
        this.geometryCalculator = geometryCalculator;
    }

    @Override
    protected Double solve(double[] sXCoeffs, double[] yXCoeffs) {
        final Collection<Double> roots = new ArrayList<>(MathCalculator.solveQuadratic(
                sXCoeffs[2],
                sXCoeffs[1],
                sXCoeffs[0] - radius * radius
        ));

        filterOnLid(MathCalculator.solveLinear(yXCoeffs[1], yXCoeffs[0]), sXCoeffs).ifPresent(roots::add);
        filterOnLid(MathCalculator.solveLinear(yXCoeffs[1], yXCoeffs[0] - length), sXCoeffs).ifPresent(roots::add);

        return chooseRoot(
                roots,
                x -> {
                    final double y = calculateValue(x, yXCoeffs);
                    return (y >= 0) && (y <= length);
                }
        );
    }

    private Optional<Double> filterOnLid(final Collection<Double> roots, final double[] xCoeffs) {
        return roots
                .stream()
                .findFirst()
                .filter(x -> sqrt(calculateValue(x, xCoeffs)) <= radius);
    }

    @Override
    protected boolean isInside(double axisDistance, double distance) {
        return (axisDistance >= 0) && (axisDistance <= length) && (distance <= radius);
    }

    @Override
    protected T calculateNormalBase(Solution<T> solution) {
        return geometryCalculator.follow(base, direction, solution.getAxisDistance());
    }

}

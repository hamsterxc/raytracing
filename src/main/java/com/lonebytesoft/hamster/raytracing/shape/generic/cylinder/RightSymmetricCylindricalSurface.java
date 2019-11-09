package com.lonebytesoft.hamster.raytracing.shape.generic.cylinder;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.shape.feature.GeometryCalculating;
import com.lonebytesoft.hamster.raytracing.shape.feature.Reflecting;
import com.lonebytesoft.hamster.raytracing.shape.feature.Refracting;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;
import com.lonebytesoft.hamster.raytracing.shape.feature.Transparent;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import com.lonebytesoft.hamster.raytracing.util.math.MathCalculator;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class RightSymmetricCylindricalSurface<T extends Coordinates>
        implements GeometryCalculating<T>, Transparent<T>, Reflecting<T>, Refracting<T>, Surfaced<T> {

    private final T base;
    private final T direction;
    private final GeometryCalculator<T> geometryCalculator;
    private final Collection<T> orthogonalComplement;

    public RightSymmetricCylindricalSurface(
            final T base,
            final T direction,
            final GeometryCalculator<T> geometryCalculator
    ) {
        this.base = base;
        this.direction = geometryCalculator.normalize(direction);
        this.geometryCalculator = geometryCalculator;
        this.orthogonalComplement = geometryCalculator.calculateOrthogonalComplement(Collections.singletonList(direction));
    }

    @Override
    public Double calculateDistance(Ray<T> ray) {
        return solve(ray).getRayDistance();
    }

    /*
        ray: a + x*b
        axis: c + y*d
        r = (a + x*b) - (c + y*d)

        (r, d) = 0
        sum((ai + x*bi - ci - y*di)*di) = 0
        sum(ai*di) + x*sum(bi*di) - sum(ci*di) - y*sum(di*di) = 0
        y = x*A + B, A = sum(bi*di) / sum(di*di), B = (sum(ai*di) - sum(ci*di)) / sum(di*di)

        (r, r) = s^2
        sum(((ai + x*bi) - (ci + y*di))^2) = s^2
        sum(ai*ai + 2*x*ai*bi + x*x*bi*bi - 2*ai*ci - 2*y*ai*di - 2*x*bi*ci - 2*x*y*bi*di + ci*ci + 2*y*ci*di + y*y*di*di) = s^2
        x^2*sum(bi*bi) - 2*x*y*sum(bi*di) + y^2*sum(di*di) + x*2*sum(ai*bi - bi*ci) + y*2*sum(ci*di - ai*di) + sum(ai*ai - 2*ai*ci + ci*ci) = s^2
        x^2*C + x*y*D + y^2*E + x*F + y*G + H = s^2
        x^2*C + x*(x*A + B)*D + (x^2*A*A + x*2*A*B + B*B)*E + x*F + (x*A + B)*G + H = s^2
        x^2*(C + A*D + A*A*E) + x*(B*D + 2*A*B*E + F + A*G) + (B*B*E + B*G + H) = s^2

        A = (b, d) / (d, d) = bd/dd
        B = (a - c, d) / (d, d) = ed/dd
        C = (b, b) = bb
        D = -2 * (b, d) = -2*bd
        E = (d, d) = dd
        F = 2 * (a - c, b) = 2*eb
        G = -2 * (a - c, d) = -2*ed
        H = (a - c, a - c) = ee

        x^2*(bb - 2*bd*bd/dd + bd*bd/dd) + x*(-2*bd*ed/dd + 2*bd*ed/dd + 2*eb - 2*bd*ed/dd) + (ed*ed/dd - 2*ed*ed/dd + ee) = s^2
        x^2*(bb - bd*bd/dd) + x*2*(eb - bd*ed/dd) + (ee - ed*ed/dd) = s^2
        y = x*bd/dd + ed/dd
     */
    protected Solution<T> solve(final Ray<T> ray) {
        final T rayDirection = geometryCalculator.normalize(ray.getDirection());
        final T vector = geometryCalculator.subtract(ray.getStart(), base);

        final double bb = geometryCalculator.product(rayDirection, rayDirection);
        final double bd = geometryCalculator.product(rayDirection, direction);
        final double be = geometryCalculator.product(rayDirection, vector);
        final double dd = geometryCalculator.product(direction, direction);
        final double de = geometryCalculator.product(direction, vector);
        final double ee = geometryCalculator.product(vector, vector);

        final double[] sXCoeffs = new double[]{
                ee - de * de / dd,
                2 * (be - bd * de / dd),
                bb - bd * bd / dd
        };
        final double[] yXCoeffs = new double[]{
                de / dd,
                bd / dd
        };

        final Double x = solve(sXCoeffs, yXCoeffs);
        final Double y = Optional.ofNullable(x).map(xValue -> calculateValue(xValue, yXCoeffs)).orElse(null);
        final T intersection = Optional.ofNullable(x)
                .map(distance -> geometryCalculator.follow(ray.getStart(), rayDirection, x))
                .orElse(null);
        final T axisPoint = Optional.ofNullable(y)
                .map(distance -> geometryCalculator.follow(base, direction, distance))
                .orElse(null);

        return new Solution<>(
                sXCoeffs,
                yXCoeffs,
                rayDirection,
                x,
                intersection,
                y,
                axisPoint
        );
    }

    protected abstract Double solve(final double[] sXCoeffs, final double[] yXCoeffs);

    protected double calculateValue(final double base, final double[] coeffs) {
        double multiplier = 1d;
        double result = 0d;
        for (final double coeff : coeffs) {
            result += coeff * multiplier;
            multiplier *= base;
        }
        return result;
    }

    protected Double chooseRoot(final Collection<Double> roots, final Predicate<Double> filter) {
        return roots
                .stream()
                .filter(root -> root >= 0d)
                .filter(filter)
                .sorted()
                .findFirst()
                .orElse(null);
    }

    protected boolean isStartInside(final Ray<T> ray) {
        final Solution<T> solution = solve(ray);
        final T vector = geometryCalculator.subtract(ray.getStart(), solution.getAxisPoint());
        final double distance = geometryCalculator.length(vector);
        return isInside(solution.getAxisDistance(), distance);
    }

    protected abstract boolean isInside(final double axisDistance, final double distance);

    @Override
    public T calculateNormal(Ray<T> ray) {
        final Solution<T> solution = solve(ray);
        if (solution.getIntersection() == null) {
            return null;
        } else {
            final T base = calculateNormalBase(solution);
            if (base == null) {
                return null;
            } else {
                if (isStartInside(ray)) {
                    return geometryCalculator.subtract(base, solution.getIntersection());
                } else {
                    return geometryCalculator.subtract(solution.getIntersection(), base);
                }
            }
        }
    }

    protected abstract T calculateNormalBase(Solution<T> solution);

    @Override
    public Ray<T> calculatePassThrough(Ray<T> ray) {
        final Solution<T> solution = solve(ray);
        return geometryCalculator.calculatePassThrough(ray, solution.getIntersection(), MathCalculator.DOUBLE_DELTA_APPROX);
    }

    @Override
    public Ray<T> calculateReflection(Ray<T> ray) {
        final Solution<T> solution = solve(ray);
        final T normal = calculateNormal(ray);
        return geometryCalculator.calculateReflection(ray, solution.getIntersection(), normal, MathCalculator.DOUBLE_DELTA_APPROX);
    }

    @Override
    public Ray<T> calculateRefraction(Ray<T> ray, double coeffSpace, double coeffSelf) {
        final Solution<T> solution = solve(ray);
        final T normal = calculateNormal(ray);

        final double coeffFrom;
        final double coeffTo;
        if (isStartInside(ray)) {
            coeffFrom = coeffSelf;
            coeffTo = coeffSpace;
        } else {
            coeffFrom = coeffSpace;
            coeffTo = coeffSelf;
        }
        return geometryCalculator.calculateRefraction(ray, solution.getIntersection(), normal, coeffFrom, coeffTo, MathCalculator.DOUBLE_DELTA_APPROX);
    }

    @Override
    public <F extends Coordinates> F mapToSurface(Ray<T> ray, GeometryCalculator<F> geometryCalculator) {
        final Solution<T> solution = solve(ray);
        if (solution.getIntersection() == null) {
            return null;
        } else {
            return geometryCalculator.buildVector(index -> solution.getAxisDistance());
        }
    }

    @Override
    public <F extends Coordinates> T mapFromSurface(F coordinates) {
        throw new UnsupportedOperationException();
    }

}

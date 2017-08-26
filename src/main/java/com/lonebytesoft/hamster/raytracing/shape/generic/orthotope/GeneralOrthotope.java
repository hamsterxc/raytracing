package com.lonebytesoft.hamster.raytracing.shape.generic.orthotope;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.CoordinatesCalculator;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.shape.feature.GeometryCalculating;
import com.lonebytesoft.hamster.raytracing.shape.feature.Reflecting;
import com.lonebytesoft.hamster.raytracing.shape.feature.Refracting;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;
import com.lonebytesoft.hamster.raytracing.shape.feature.Transparent;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import com.lonebytesoft.hamster.raytracing.util.math.MathCalculator;
import com.lonebytesoft.hamster.raytracing.util.math.equation.LinearEquation;
import com.lonebytesoft.hamster.raytracing.util.math.equation.LinearSystemSolver;
import com.lonebytesoft.hamster.raytracing.util.math.equation.Solution;
import com.lonebytesoft.hamster.raytracing.util.math.equation.SolutionType;
import com.lonebytesoft.hamster.raytracing.util.variant.Combination;
import com.lonebytesoft.hamster.raytracing.util.variant.Options;
import com.lonebytesoft.hamster.raytracing.util.variant.Variant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GeneralOrthotope<T extends Coordinates<T>>
        implements GeometryCalculating<T>, Surfaced<T>, Transparent<T>, Reflecting<T>, Refracting<T> {

    private final T base;
    private final List<T> vectors;
    private final boolean isInfinite;

    private final double delta;

    public GeneralOrthotope(final T base, final List<T> vectors, final boolean isInfinite) {
        final int size = vectors.size();
        for(int i = 0; i < size - 1; i++) {
            for(int j = i + 1; j < size; j++) {
                final double product = GeometryCalculator.product(vectors.get(i), vectors.get(j));
                if(!MathCalculator.isEqualApproximately(product, 0.0)) {
                    throw new IllegalArgumentException("Vectors #" + (i + 1) + " and #" + (j + 1) + " not orthogonal");
                }
            }
        }

        this.base = base;
        this.vectors = Collections.unmodifiableList(vectors);
        this.isInfinite = isInfinite;

        double delta = MathCalculator.DOUBLE_DELTA_APPROX;
        for(final T vector : this.vectors) {
            delta = Math.max(delta, GeometryCalculator.length(vector) / 1e6);
        }
        this.delta = delta;
    }

    public T getBase() {
        return base;
    }

    public List<T> getVectors() {
        return vectors;
    }

    @Override
    public Double calculateDistance(Ray<T> ray) {
        final OrthotopeIntersection intersection = calculateClosestIntersection(ray);
        if(intersection == null) {
            return null;
        } else {
            final double length = GeometryCalculator.length(ray.getDirection());
            return intersection.getDistanceMultiplier() * length;
        }
    }

    @Override
    public T calculateNormal(Ray<T> ray) {
        final int spaceDimensions = base.getDimensions();
        final int orthotopeDimensions = vectors.size();

        final T normal;
        if(orthotopeDimensions == spaceDimensions) {
            // calculating normal for the closest facet
            // normal is the vector not constructing that facet
            final OrthotopeIntersection intersection = calculateClosestIntersection(ray, orthotopeDimensions - 1);
            if(intersection == null) {
                return null;
            } else {
                final T vector = vectors.get((int) intersection.getFixVectorsIndex());
                if(intersection.getFixVectorsValueIndex() == 0) {
                    normal = CoordinatesCalculator.transform(vector, index -> -vector.getCoordinate(index));
                } else {
                    normal = vector;
                }
            }
        } else if(orthotopeDimensions == spaceDimensions - 1) {
            // fixing the last normal coordinate to 1.0
            final Collection<LinearEquation> equations = new ArrayList<>();
            for(final T vector : vectors) {
                final List<Double> coeffs = new ArrayList<>();
                CoordinatesCalculator.iterate(vector, index -> {
                    if(index < spaceDimensions - 1) {
                        coeffs.add(vector.getCoordinate(index));
                    } else {
                        final double free = -vector.getCoordinate(index);
                        equations.add(new LinearEquation(coeffs, free));
                    }
                });
            }

            final Solution solution = LinearSystemSolver.solve(equations);
            if(solution.getType() == SolutionType.UNIQUE) {
                normal = CoordinatesCalculator.transform(base, index -> {
                    if(index < spaceDimensions - 1) {
                        return solution.getValues().get(index);
                    } else {
                        return 1.0;
                    }
                });
            } else {
                throw new IllegalStateException("Unexpected solution type: " + solution.getType().name());
            }
        } else {
            throw new IllegalStateException("Cannot calculate normal for an orthotope of dimension < (space dimension)-1");
        }

        if(isInside(ray.getStart())) {
            return CoordinatesCalculator.transform(normal, index -> -normal.getCoordinate(index));
        } else {
            return normal;
        }
    }

    /**
     * (k1, k2, ..., kn)
     * k1 = f + k1'
     *      f: 0..m-1 - facet index
     *      m - facets count
     * k1', k2, ..., kn: [0..1] - respective vector coefficient
     */
    @Override
    public <F extends Coordinates<F>> F mapToSurface(Ray<T> ray, F reference) {
        final int facetDimensions = reference.getDimensions();
        final OrthotopeIntersection intersection = calculateClosestIntersection(ray, facetDimensions);
        if(intersection == null) {
            return null;
        } else {
            final double[] coords = intersection.getVectorMultipliers();

            // adding facet's index to the first coordinate - constructing orthotope net
            final int fixCount = vectors.size() - facetDimensions;
            final long fixVariants = (long) Math.pow(2, fixCount);
            final long facetIndex = intersection.getFixVectorsIndex() * fixVariants + intersection.getFixVectorsValueIndex();
            coords[0] += facetIndex;

            return reference.obtain(coords);
        }
    }

    /**
     * @see #mapToSurface(Ray, Coordinates)
     */
    @Override
    public <F extends Coordinates<F>> T mapFromSurface(F coordinates) {
        final int spaceDimensions = base.getDimensions();
        final int orthotopeDimensions = vectors.size();
        final int facetDimensions = coordinates.getDimensions();

        final int fixCount = vectors.size() - facetDimensions;
        final long fixVariants = (long) Math.pow(2, fixCount);
        final long facetIndex = (long) Math.floor(coordinates.getCoordinate(0));
        final long fixVectorsIndex = facetIndex / fixVariants;
        final long fixVectorsValueIndex = facetIndex % fixVariants;

        final Variant fixIndices = new Combination(fixCount, orthotopeDimensions);
        for(long variant = 0; variant < fixVectorsIndex; variant++) {
            fixIndices.advance();
        }
        final List<Integer> fixVectors = fixIndices.current();

        final Variant fixValues = new Options(fixCount, 2);
        for(long variant = 0; variant < fixVectorsValueIndex; variant++) {
            fixValues.advance();
        }
        final List<Integer> fixVectorsValue = fixValues.current();

        final F facetCoordinates = CoordinatesCalculator.transform(coordinates, index -> {
            if(index == 0) {
                return coordinates.getCoordinate(index) - facetIndex;
            } else {
                return coordinates.getCoordinate(index);
            }
        });
        return CoordinatesCalculator.transform(base, index -> {
            double coordinate = base.getCoordinate(index);
            int facetCoordinateIndex = 0;
            for(int i = 0; i < orthotopeDimensions; i++) {
                final int vectorIndex = fixVectors.indexOf(i);
                if(vectorIndex < 0) {
                    coordinate += vectors.get(i).getCoordinate(index) * facetCoordinates.getCoordinate(facetCoordinateIndex);
                    facetCoordinateIndex++;
                } else {
                    coordinate += vectors.get(i).getCoordinate(index) * fixVectorsValue.get(vectorIndex);
                }
            }
            return coordinate;
        });
    }

    @Override
    public Ray<T> calculatePassThrough(Ray<T> ray) {
        final T intersectionPoint = calculateIntersectionPoint(ray);
        if(intersectionPoint == null) {
            return null;
        } else {
            final T rayDirection = ray.getDirection();
            // todo: hack: pushing ray start out a bit
            final T start = GeometryCalculator.push(intersectionPoint, rayDirection, delta);
            return new Ray<>(start, rayDirection);
        }
    }

    @Override
    public Ray<T> calculateReflection(Ray<T> ray) {
        return GeometryCalculator.calculateReflection(ray, calculateIntersectionPoint(ray), calculateNormal(ray), delta);
    }

    @Override
    public Ray<T> calculateRefraction(Ray<T> ray, double coeffSpace, double coeffSelf) {
        final T intersection = calculateIntersectionPoint(ray);
        final T normal = calculateNormal(ray);
        // todo: hack: switching inside & outside for "good appearance"?
        if(isInside(ray.getStart())) {
            return GeometryCalculator.calculateRefraction(ray, intersection, normal, coeffSpace, coeffSelf, delta);
        } else {
            return GeometryCalculator.calculateRefraction(ray, intersection, normal, coeffSelf, coeffSpace, delta);
        }
    }

    private List<Double> calculateIntersectionSolution(final Ray<T> ray, final T base, final List<T> vectors) {
        final Collection<LinearEquation> equations = new ArrayList<>();
        CoordinatesCalculator.iterate(base, index -> {
            final List<Double> coeffs = new ArrayList<>();
            coeffs.add(ray.getDirection().getCoordinate(index));
            for(final T vector : vectors) {
                coeffs.add(-vector.getCoordinate(index));
            }

            final double free = base.getCoordinate(index) - ray.getStart().getCoordinate(index);
            equations.add(new LinearEquation(coeffs, free));
        });

        final Solution solution = LinearSystemSolver.solve(equations);
        switch(solution.getType()) {
            case UNIQUE:
                return solution.getValues();

            case INFINITE:
                // todo: maybe find the closest intersection point
//                throw new IllegalStateException("Unexpected solution: infinite");
                return null;

            case INCONSISTENT:
                return null;

            default:
                throw new IllegalStateException("Unexpected solution type: " + solution.getType().name());
        }
    }

    private OrthotopeIntersection calculateClosestIntersection(final Ray<T> ray) {
        return calculateClosestIntersection(ray, vectors.size());
    }

    /*
        facetDim > ortDim
            error
        facetDim == ortDim
            infinite
                ortDim == spaceDim
                    immediate intersection
                ortDim < spaceDim
                    solve
            not infinite
                ortDim == spaceDim
                    iterate facets (n-1), solve
                ortDim < spaceDim
                    solve
        facetDim < ortDim
            iterate facets, solve
     */
    private OrthotopeIntersection calculateClosestIntersection(final Ray<T> ray, final int facetDimensions) {
        final int spaceDimensions = base.getDimensions();
        final int orthotopeDimensions = vectors.size();
        final boolean isFullOrthotope = (facetDimensions == orthotopeDimensions) && (orthotopeDimensions == spaceDimensions);

        if(facetDimensions > orthotopeDimensions) {
            throw new IllegalArgumentException("Facet dimensionality " + facetDimensions +
                    " greater than orthotope dimensionality " + orthotopeDimensions);
        }

        // the whole space - immediate intersection
        if(isFullOrthotope && isInfinite) {
            // base + k1*v1 + ... + kn*vn = start
            final Collection<LinearEquation> equations = new ArrayList<>();
            CoordinatesCalculator.iterate(base, index -> {
                final List<Double> coeffs = new ArrayList<>();
                for(final T vector : vectors) {
                    coeffs.add(vector.getCoordinate(index));
                }

                final double free = ray.getStart().getCoordinate(index) - base.getCoordinate(index);
                equations.add(new LinearEquation(coeffs, free));
            });

            final Solution solution = LinearSystemSolver.solve(equations);
            if(solution.getType() == SolutionType.UNIQUE) {
                final List<Double> coeffs = new ArrayList<>(solution.getValues());
                coeffs.add(0, 0.0);

                return new OrthotopeIntersection(coeffs, 0, 0);
            } else {
                throw new IllegalStateException("Unexpected solution type: " + solution.getType().name());
            }
        }

        final int iterateFacetDimensions = isFullOrthotope ? orthotopeDimensions - 1 : facetDimensions;
        final int fixCount = orthotopeDimensions - iterateFacetDimensions;
        final Variant fixIndices = new Combination(fixCount, orthotopeDimensions);
        final Variant fixValues = new Options(fixCount, 2);

        final OrthotopeIntersectionHolder intersectionHolder = new OrthotopeIntersectionHolder();
        Variant.iterate(fixIndices, (fixVectorsIndex, fixVectors) -> {
            final List<T> vectors = new ArrayList<>();
            for(int i = 0; i < orthotopeDimensions; i++) {
                if(!fixVectors.contains(i)) {
                    vectors.add(this.vectors.get(i));
                }
            }

            Variant.iterate(fixValues, (fixVectorsValueIndex, fixVectorsValue) -> {
                final T base = CoordinatesCalculator.transform(this.base, index -> {
                    double coordinate = this.base.getCoordinate(index);
                    for(int j = 0; j < fixCount; j++) {
                        coordinate += fixVectorsValue.get(j) * this.vectors.get(fixVectors.get(j)).getCoordinate(index);
                    }
                    return coordinate;
                });

                final List<Double> solution = calculateIntersectionSolution(ray, base, vectors);
                if(solution != null) {
                    final List<Double> solutionAdjusted;
                    if(isFullOrthotope) {
                        solutionAdjusted = new ArrayList<>(solution);
                        solutionAdjusted.add(fixVectors.get(0) + 1, (double) fixVectorsValue.get(0));
                    } else {
                        solutionAdjusted = solution;
                    }

                    final OrthotopeIntersection candidate = new OrthotopeIntersection(solutionAdjusted, fixVectorsIndex, fixVectorsValueIndex);
                    if((candidate.getDistanceMultiplier() >= 0.0)
                            && (intersectionHolder.isEmpty() || (candidate.getDistanceMultiplier() < intersectionHolder.get().getDistanceMultiplier()))
                            && (isInfinite || isWithinBounds(candidate))) {
                        intersectionHolder.set(candidate);
                    }
                }
            });
        });

        return intersectionHolder.get();
    }

    private boolean isWithinBounds(final OrthotopeIntersection orthotopeIntersection) {
        for(final double multiplier : orthotopeIntersection.getVectorMultipliers()) {
            if((multiplier < 0.0) || (multiplier > 1.0)) {
                return false;
            }
        }
        return true;
    }

    private boolean isInside(final T point) {
        final Collection<LinearEquation> equations = new ArrayList<>();
        CoordinatesCalculator.iterate(base, index -> {
            final List<Double> coeffs = new ArrayList<>();
            for(final T vector : vectors) {
                coeffs.add(vector.getCoordinate(index));
            }

            final double free = point.getCoordinate(index) - base.getCoordinate(index);
            equations.add(new LinearEquation(coeffs, free));
        });

        final Solution solution = LinearSystemSolver.solve(equations);
        return (solution.getType() == SolutionType.UNIQUE) && solution.getValues()
                .stream()
                .allMatch(coeff -> (coeff >= 0.0) && (coeff <= 1.0));
    }

    private T calculateIntersectionPoint(final Ray<T> ray) {
        final OrthotopeIntersection intersection = calculateClosestIntersection(ray);
        if(intersection == null) {
            return null;
        } else {
            return GeometryCalculator.follow(ray.getStart(), ray.getDirection(), intersection.getDistanceMultiplier());
        }
    }

    @Override
    public String toString() {
        return "GeneralOrthotope{" +
                "base=" + base +
                ", vectors=" + vectors +
                ", isInfinite=" + isInfinite +
                '}';
    }

    private class OrthotopeIntersectionHolder {
        private OrthotopeIntersection intersection = null;

        public boolean isEmpty() {
            return intersection == null;
        }

        public OrthotopeIntersection get() {
            return intersection;
        }

        public void set(final OrthotopeIntersection intersection) {
            this.intersection = intersection;
        }
    }

}

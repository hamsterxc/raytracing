package com.lonebytesoft.hamster.raytracing.util.math;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.factory.CoordinatesFactory;
import com.lonebytesoft.hamster.raytracing.ray.Ray;
import com.lonebytesoft.hamster.raytracing.util.math.equation.LinearEquation;
import com.lonebytesoft.hamster.raytracing.util.math.equation.LinearSystemSolver;
import com.lonebytesoft.hamster.raytracing.util.math.equation.Solution;
import com.lonebytesoft.hamster.raytracing.util.math.equation.SolutionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.IntToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class GeometryCalculator<T extends Coordinates> {

    private final CoordinatesFactory<T> coordinatesFactory;

    public GeometryCalculator(final CoordinatesFactory<T> coordinatesFactory) {
        this.coordinatesFactory = coordinatesFactory;
    }

    public T buildVector(final IntToDoubleFunction generator) {
        return coordinatesFactory.build(
                IntStream.range(0, coordinatesFactory.getDimensions())
                        .mapToDouble(generator)
                        .toArray()
        );
    }

    public T push(final T point, final T direction, final double minDelta) {
        double coordinateMin = Double.MAX_VALUE;
        for (double coordinate : direction) {
            coordinateMin = Math.min(coordinateMin, Math.abs(coordinate));
        }

        final double deltaMultiplier = coordinateMin;
        return buildVector(index -> point.getCoordinate(index) + minDelta * direction.getCoordinate(index) / deltaMultiplier);
    }

    public T follow(final T point, final T direction, final double multiplier) {
        return buildVector(index -> point.getCoordinate(index) + multiplier * direction.getCoordinate(index));
    }

    public T subtract(final T minuend, final T subtrahend) {
        return follow(minuend, subtrahend, -1);
    }

    public double product(final T first, final T second) {
        return IntStream.range(0, first.getDimensions())
                .mapToDouble(index -> first.getCoordinate(index) * second.getCoordinate(index))
                .sum();
    }

    public double length(final T vector) {
        return Math.sqrt(product(vector, vector));
    }

    public T normalize(final T vector) {
        final double length = length(vector);
        return buildVector(index -> vector.getCoordinate(index) / length);
    }

    public T rotate(final T vector, final int axisFirst, final int axisSecond, final double angle) {
        return buildVector(
                index -> {
                    if (index == axisFirst) {
                        return vector.getCoordinate(axisFirst) * Math.cos(angle) + vector.getCoordinate(axisSecond) * Math.sin(angle);
                    } else if (index == axisSecond) {
                        return vector.getCoordinate(axisFirst) * (-Math.sin(angle)) + vector.getCoordinate(axisSecond) * Math.cos(angle);
                    } else {
                        return vector.getCoordinate(index);
                    }
                }
        );
    }

    public Ray<T> calculatePassThrough(final Ray<T> ray, final T intersection, final double pushDelta) {
        if(intersection == null) {
            return null;
        } else {
            final T rayDirection = ray.getDirection();
            // todo: hack: pushing ray start out a bit
            final T start = push(intersection, rayDirection, pushDelta);

            return new Ray<>(start, rayDirection);
        }
    }

    // https://math.stackexchange.com/q/13261
    public T reflect(final T vector, final T normal) {
        final double product = product(vector, normal);
        final double lengthSquare = product(normal, normal);
        final double coefficient = 2.0 * product / lengthSquare;
        return buildVector(index -> vector.getCoordinate(index) - coefficient * normal.getCoordinate(index));
    }

    public Ray<T> calculateReflection(final Ray<T> ray, final T intersection, final T normal, final double pushDelta) {
        if((intersection == null) || (normal == null)) {
            return null;
        } else {
            final T reflectionVector = reflect(ray.getDirection(), normal);
            // todo: hack: pushing ray start out a bit
            final T reflectionStart = push(intersection, reflectionVector, pushDelta);

            return new Ray<>(reflectionStart, reflectionVector);
        }
    }

    // https://en.wikipedia.org/wiki/Snell%27s_law#Vector_form
    public T refract(final T vector, final T normal, final double coeffFrom, final double coeffTo) {
        final double vectorLength = length(vector);
        final double normalLength = length(normal);

        final double r = coeffFrom / coeffTo;
        final double c = -product(vector, normal) / (vectorLength * normalLength);

        final double d = 1.0 - r * r * (1.0 - c * c);
        if(d >= 0) {
            final double nCoeff = r * c - Math.sqrt(d);
            return buildVector(index -> r * vector.getCoordinate(index) + nCoeff * normal.getCoordinate(index));
        } else {
            return reflect(vector, normal);
        }
    }

    public Ray<T> calculateRefraction(
            final Ray<T> ray, final T intersection, final T normal,
            final double coeffFrom, final double coeffTo, final double pushDelta
    ) {
        if((intersection == null) || (normal == null)) {
            return null;
        } else {
            final T refractionVector = refract(ray.getDirection(), normal, coeffFrom, coeffTo);
            // todo: hack: pushing ray start out a bit
            final T refractionStart = push(intersection, refractionVector, pushDelta);

            return new Ray<>(refractionStart, refractionVector);
        }
    }

    public Collection<T> generateBasis() {
        return IntStream.range(0, coordinatesFactory.getDimensions())
                .mapToObj(index -> buildVector(i -> i == index ? 1.0 : 0.0))
                .collect(Collectors.toList());
    }

    public Collection<T> calculateOrthogonalComplement(final Collection<T> vectors) {
        final int subspaceDimensions = Optional.ofNullable(vectors).map(Collection::size).orElse(0);
        final int spaceDimensions = coordinatesFactory.getDimensions();

        if (subspaceDimensions == 0) {
            return generateBasis();
        } else if (subspaceDimensions == spaceDimensions) {
            return Collections.emptyList();
        } else if (subspaceDimensions > spaceDimensions) {
            throw new IllegalArgumentException("Given more vectors than space dimensionality");
        }

        final List<T> vectorsList = new ArrayList<>(vectors);
        IntStream.range(0, subspaceDimensions - 1).forEach(i ->
                IntStream.range(i + 1, subspaceDimensions).forEach(j -> {
                    if (!MathCalculator.isEqualApproximately(0d, product(vectorsList.get(i), vectorsList.get(j)))) {
                        throw new IllegalArgumentException("Given vectors are not orthogonal to each other (e.g., #" + i + ", #" + j + ")");
                    }
                }));

        final List<List<Double>> knownVectors = vectors
                .stream()
                .map(vector -> StreamSupport.stream(vector.spliterator(), false).collect(Collectors.toList()))
                .collect(Collectors.toCollection(ArrayList::new));
        final List<List<Double>> complement = new ArrayList<>();
        for (int i = 0; i < spaceDimensions - subspaceDimensions; i++) {
            final List<Double> vector = calculateOrthogonalVector(knownVectors);
            knownVectors.add(vector);
            complement.add(vector);
        }

        return complement
                .stream()
                .map(vector -> buildVector(vector::get))
                .collect(Collectors.toList());
    }

    private List<Double> calculateOrthogonalVector(final List<List<Double>> vectors) {
        final int variablesTotal = vectors.size();
        final int dimensions = vectors.get(0).size();
        final boolean[] isVariable = new boolean[dimensions];
        int variables = 0;
        for (int i = 0; i < dimensions; i++) {
            final int index = i;
            isVariable[index] = (variables < variablesTotal) && vectors.stream().anyMatch(vector -> !MathCalculator.isEqual(0.0, vector.get(index)));
            if (isVariable[index]) {
                variables++;
            }
        }

        final Collection<LinearEquation> equations = vectors
                .stream()
                .map(vector -> new LinearEquation(
                        IntStream.range(0, dimensions).filter(index -> isVariable[index]).mapToObj(vector::get).collect(Collectors.toList()),
                        -IntStream.range(0, dimensions).filter(index -> !isVariable[index]).mapToObj(vector::get).reduce(Double::sum).orElse(0d)
                ))
                .collect(Collectors.toList());
        final Solution solution = LinearSystemSolver.solve(equations);
        if (solution.getType() != SolutionType.UNIQUE) {
            throw new IllegalStateException("Unexpected solution: " + solution);
        }

        final List<Double> vector = new ArrayList<>(dimensions);
        int variableIndex = 0;
        for (int i = 0; i < dimensions; i++) {
            final double value = isVariable[i] ? solution.getValues().get(variableIndex++) : 1d;
            vector.add(value);
        }
        return vector;
    }

}

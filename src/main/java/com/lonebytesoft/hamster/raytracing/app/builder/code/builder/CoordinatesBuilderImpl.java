package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class CoordinatesBuilderImpl implements CoordinatesBuilder {

    @Override
    public String build(Coordinates<?> definition) {
        return "new " + buildClassName(definition) + '(' +
                StreamSupport.stream(definition.spliterator(), false)
                        .map(String::valueOf)
                        .collect(Collectors.joining(","))
                + ')';
    }

    @Override
    public String build(Coordinates<?> definition, String name) {
        return "final " + buildClassName(definition) + " " + name + " = " + build(definition) + ";\n";
    }

    @Override
    public String buildClassName(Coordinates<?> coordinates) {
        return buildClassName(coordinates.getDimensions());
    }

    @Override
    public String buildClassName(int dimensions) {
        return "Coordinates" + dimensions + 'd';
    }

    @Override
    public String buildReference(int dimensions) {
        return "new " + buildClassName(dimensions) + '(' +
                IntStream.range(0, dimensions)
                        .boxed()
                        .map(i -> "0")
                        .collect(Collectors.joining(","))
                + ')';
    }

}

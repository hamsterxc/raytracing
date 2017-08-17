package com.lonebytesoft.hamster.raytracing.app;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.CoordinatesCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CheckersTexture<T extends Coordinates<T>> implements Function<T, Color> {

    private final List<Double> multipliers;
    private final List<Color> colors;

    public CheckersTexture(final List<Double> multipliers, final List<Color> colors) {
        this.multipliers = new ArrayList<>(multipliers);
        this.colors = new ArrayList<>(colors);
    }

    @Override
    public Color apply(T coordinates) {
        if(coordinates.getDimensions() != multipliers.size()) {
            throw new IllegalStateException("Number of multipliers not equal to dimensionality");
        }

        final IndexCalculator indexCalculator = new IndexCalculator();
        CoordinatesCalculator.iterate(coordinates,
                index -> indexCalculator.accept(coordinates.getCoordinate(index), multipliers.get(index)));
        return colors.get(indexCalculator.calculate(colors.size()));
    }

    private class IndexCalculator {
        private int value = 0;

        public void accept(final double coordinate, final double multiplier) {
            value += (int) Math.floor(coordinate * multiplier);
        }

        public int calculate(final int size) {
            final int value = this.value % size;
            return value < 0 ? value + size : value;
        }
    }

}

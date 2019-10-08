package com.lonebytesoft.hamster.raytracing.util.variant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Combination implements Variant {

    private final int size;
    private final int limit;
    private final List<Integer> combination;

    public Combination(final int size, final int limit) {
        if(size > limit) {
            throw new IllegalArgumentException("Requested set size + " + size + " greater than collection size " + limit);
        }
        if(size < 0) {
            throw new IllegalArgumentException("Negative size");
        }

        this.size = size;
        this.limit = limit;
        this.combination = new ArrayList<>();

        reset();
    }

    @Override
    public void reset() {
        combination.clear();
        combination.addAll(IntStream.range(0, size).boxed().collect(Collectors.toList()));
    }

    @Override
    public boolean advance() {
        final int size = combination.size();
        for(int i = size - 1; i >= 0; i--) {
            final int value = combination.get(i);
            if(value + (size - i) < limit) {
                for(int j = 0; j < size - i; j++) {
                    combination.set(i + j, value + j + 1);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Integer> current() {
        return buildView(combination);
    }

}

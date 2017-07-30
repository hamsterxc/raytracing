package com.lonebytesoft.hamster.raytracing.util.variant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Options implements Variant {

    private final int size;
    private final int limit;
    private final List<Integer> options;

    public Options(final int size, final int limit) {
        if(size < 0) {
            throw new IllegalArgumentException("Negative size");
        }

        this.size = size;
        this.limit = limit;
        this.options = new ArrayList<>();

        reset();
    }

    @Override
    public void reset() {
        options.clear();
        options.addAll(IntStream.range(0, size)
                .map(i -> 0)
                .boxed()
                .collect(Collectors.toList()));
    }

    @Override
    public boolean advance() {
        final int size = options.size();
        for(int i = size - 1; i >= 0; i--) {
            final int value = options.get(i);
            if(value < limit - 1) {
                options.set(i, value + 1);
                for(int j = i + 1; j < size; j++) {
                    options.set(j, 0);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Integer> current() {
        return Collections.unmodifiableList(options);
    }

}

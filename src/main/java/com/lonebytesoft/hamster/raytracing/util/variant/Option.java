package com.lonebytesoft.hamster.raytracing.util.variant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Option implements Variant {

    private final List<Integer> limits;
    private final List<Integer> options;

    public Option(final int size, final int limit) {
        this(Collections.nCopies(size, limit));
    }

    public Option(final List<Integer> limits) {
        if(limits.stream().anyMatch(limit -> limit <= 0)) {
            throw new IllegalArgumentException("No limits can be <= 0");
        }

        this.limits = new ArrayList<>(limits);
        this.options = new ArrayList<>();

        reset();
    }

    @Override
    public void reset() {
        options.clear();
        options.addAll(Collections.nCopies(limits.size(), 0));
    }

    @Override
    public boolean advance() {
        final int size = options.size();
        for(int i = size - 1; i >= 0; i--) {
            final int value = options.get(i);
            if(value < limits.get(i) - 1) {
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
        return buildView(options);
    }

}

package com.lonebytesoft.hamster.raytracing.util.variant;

import java.util.List;
import java.util.function.BiConsumer;

public interface Variant {

    void reset();
    boolean advance();
    List<Integer> current();

    static void iterate(final Variant variant, final BiConsumer<Long, List<Integer>> consumer) {
        variant.reset();
        long index = 0;
        do {
            consumer.accept(index, variant.current());
            index++;
        } while (variant.advance());
    }

}

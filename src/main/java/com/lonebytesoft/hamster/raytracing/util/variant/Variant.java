package com.lonebytesoft.hamster.raytracing.util.variant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public interface Variant extends Iterable<List<Integer>> {

    void reset();
    boolean advance();
    List<Integer> current();

    @Override
    default Iterator<List<Integer>> iterator() {
        reset();
        return new Iterator<List<Integer>>() {
            private List<Integer> next = current();

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public List<Integer> next() {
                if(this.next == null) {
                    throw new NoSuchElementException();
                }

                final List<Integer> next = this.next;
                this.next = advance() ? current() : null;
                return next;
            }
        };
    }

    default Iterable<VariantWithIndex> withIndex() {
        reset();
        return () -> new Iterator<VariantWithIndex>() {
            private Iterator<List<Integer>> iterator = Variant.this.iterator();
            private long index = 0;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public VariantWithIndex next() {
                return new VariantWithIndex(index++, iterator.next());
            }
        };
    }

    default List<Integer> buildView(final List<Integer> values) {
        return Collections.unmodifiableList(new ArrayList<>(values));
    }

}

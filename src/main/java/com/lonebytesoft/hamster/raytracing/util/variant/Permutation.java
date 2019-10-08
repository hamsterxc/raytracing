package com.lonebytesoft.hamster.raytracing.util.variant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Permutation implements Variant {

    private final int size;
    private final List<Integer> permutation;

    private final List<Integer> indicesReversed;

    public Permutation(final int size) {
        if(size < 0) {
            throw new IllegalArgumentException("Negative size");
        }

        this.size = size;
        this.permutation = new ArrayList<>();

        indicesReversed = IntStream.range(0, size)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(indicesReversed);

        reset();
    }

    @Override
    public void reset() {
        permutation.clear();
        permutation.addAll(IntStream.range(0, size).boxed().collect(Collectors.toList()));
    }

    // https://en.wikipedia.org/wiki/Permutation#Generation_in_lexicographic_order
    @Override
    public boolean advance() {
        final Optional<Integer> kOptional = indicesReversed.stream()
                .skip(1)
                .filter(index -> permutation.get(index) < permutation.get(index + 1))
                .findFirst();
        final int k;
        if(kOptional.isPresent()) {
            k = kOptional.get();
        } else {
            return false;
        }

        final int value = permutation.get(k);
        final Optional<Integer> lOptional = indicesReversed.stream()
                .filter(index -> (index > k) && (value < permutation.get(index)))
                .findFirst();
        final int l;
        if(lOptional.isPresent()) {
            l = lOptional.get();
        } else {
            return false;
        }

        Collections.swap(permutation, k, l);
        Collections.reverse(permutation.subList(k + 1, size));

        return true;
    }

    @Override
    public List<Integer> current() {
        return buildView(permutation);
    }

}

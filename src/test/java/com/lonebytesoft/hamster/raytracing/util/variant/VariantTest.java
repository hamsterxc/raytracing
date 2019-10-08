package com.lonebytesoft.hamster.raytracing.util.variant;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class VariantTest {

    @Test
    public void testCombination() {
        final Variant combination = new Combination(3, 5);

        Assert.assertEquals(
                Arrays.asList(
                        new VariantWithIndex(0, Arrays.asList(0, 1, 2)),
                        new VariantWithIndex(1, Arrays.asList(0, 1, 3)),
                        new VariantWithIndex(2, Arrays.asList(0, 1, 4)),
                        new VariantWithIndex(3, Arrays.asList(0, 2, 3)),
                        new VariantWithIndex(4, Arrays.asList(0, 2, 4)),
                        new VariantWithIndex(5, Arrays.asList(0, 3, 4)),
                        new VariantWithIndex(6, Arrays.asList(1, 2, 3)),
                        new VariantWithIndex(7, Arrays.asList(1, 2, 4)),
                        new VariantWithIndex(8, Arrays.asList(1, 3, 4)),
                        new VariantWithIndex(9, Arrays.asList(2, 3, 4))
                ),
                collectWithIndex(combination)
        );
    }

    @Test
    public void testOption() {
        final Variant option = new Option(2, 3);

        Assert.assertEquals(
                Arrays.asList(
                        new VariantWithIndex(0, Arrays.asList(0, 0)),
                        new VariantWithIndex(1, Arrays.asList(0, 1)),
                        new VariantWithIndex(2, Arrays.asList(0, 2)),
                        new VariantWithIndex(3, Arrays.asList(1, 0)),
                        new VariantWithIndex(4, Arrays.asList(1, 1)),
                        new VariantWithIndex(5, Arrays.asList(1, 2)),
                        new VariantWithIndex(6, Arrays.asList(2, 0)),
                        new VariantWithIndex(7, Arrays.asList(2, 1)),
                        new VariantWithIndex(8, Arrays.asList(2, 2))
                ),
                collectWithIndex(option)
        );
    }

    @Test
    public void testOption_variableLimits() {
        final Variant option = new Option(Arrays.asList(1, 3, 2));

        Assert.assertEquals(
                Arrays.asList(
                        new VariantWithIndex(0, Arrays.asList(0, 0, 0)),
                        new VariantWithIndex(1, Arrays.asList(0, 0, 1)),
                        new VariantWithIndex(2, Arrays.asList(0, 1, 0)),
                        new VariantWithIndex(3, Arrays.asList(0, 1, 1)),
                        new VariantWithIndex(4, Arrays.asList(0, 2, 0)),
                        new VariantWithIndex(5, Arrays.asList(0, 2, 1))
                ),
                collectWithIndex(option)
        );
    }

    @Test
    public void testPermutation() {
        final Variant permutation = new Permutation(3);

        Assert.assertEquals(
                Arrays.asList(
                        new VariantWithIndex(0, Arrays.asList(0, 1, 2)),
                        new VariantWithIndex(1, Arrays.asList(0, 2, 1)),
                        new VariantWithIndex(2, Arrays.asList(1, 0, 2)),
                        new VariantWithIndex(3, Arrays.asList(1, 2, 0)),
                        new VariantWithIndex(4, Arrays.asList(2, 0, 1)),
                        new VariantWithIndex(5, Arrays.asList(2, 1, 0))
                ),
                collectWithIndex(permutation)
        );
    }

    private List<VariantWithIndex> collectWithIndex(final Variant variant) {
        return StreamSupport.stream(variant.withIndex().spliterator(), false)
                .collect(Collectors.toList());
    }

}

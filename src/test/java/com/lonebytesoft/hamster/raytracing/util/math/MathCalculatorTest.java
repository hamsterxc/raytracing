package com.lonebytesoft.hamster.raytracing.util.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MathCalculatorTest {

    @Test
    public void testSolveQuartic() {
//        assertSolution(Arrays.asList(-1d, 2d), MathCalculator.solveQuartic(2, -3, 4, -5, -14));
//        assertSolution(Arrays.asList(-1d, 1d), MathCalculator.solveQuartic(1, 0, -2, 0, 1));
        assertSolution(Arrays.asList(-1d, 1d), MathCalculator.solveQuartic(1, 0, 0, 0, -1));
        assertSolution(Collections.emptyList(), MathCalculator.solveQuartic(1, 0, 0, 0, 1));
    }

    private void assertSolution(final Collection<Double> expected, final Collection<Double> actual) {
        final List<Double> expectedSorted = expected.stream().distinct().sorted().collect(Collectors.toList());
        final List<Double> actualSorted = actual.stream().distinct().sorted().collect(Collectors.toList());
        final String message = "Expected: " + expectedSorted + ", actual: " + actualSorted;

        Assert.assertEquals(message, expectedSorted.size(), actualSorted.size());

        IntStream.range(0, expectedSorted.size())
                .forEach(index -> Assert.assertTrue(
                        message + ", root #" + index,
                        MathCalculator.isEqualApproximately(expectedSorted.get(index), actualSorted.get(index))
                ));
    }

}

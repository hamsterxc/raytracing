package com.lonebytesoft.hamster.raytracing.util.variant;

import java.util.List;
import java.util.Objects;

public class VariantWithIndex {

    private final long index;
    private final List<Integer> value;

    public VariantWithIndex(long index, List<Integer> value) {
        this.index = index;
        this.value = value;
    }

    public long getIndex() {
        return index;
    }

    public List<Integer> getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariantWithIndex that = (VariantWithIndex) o;
        return index == that.index &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, value);
    }

    @Override
    public String toString() {
        return "#" + index + " = " + value;
    }

}

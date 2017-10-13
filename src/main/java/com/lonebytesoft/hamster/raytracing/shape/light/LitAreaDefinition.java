package com.lonebytesoft.hamster.raytracing.shape.light;

class LitAreaDefinition {

    private final double start;
    private final double end;

    public LitAreaDefinition(final double start, final double end) {
        this.start = start;
        this.end = end;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

}

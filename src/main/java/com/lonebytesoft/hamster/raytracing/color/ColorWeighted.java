package com.lonebytesoft.hamster.raytracing.color;

public class ColorWeighted {

    private final Color color;
    private final double weight;

    public ColorWeighted(final Color color, final double weight) {
        this.color = color;
        this.weight = weight;
    }

    public Color getColor() {
        return color;
    }

    public double getWeight() {
        return weight;
    }

}

package com.lonebytesoft.hamster.raytracing.color;

import java.util.Objects;

public class Color {

    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color RED = new Color(1, 0, 0);
    public static final Color GREEN = new Color(0, 1, 0);
    public static final Color BLUE = new Color(0, 0, 1);
    public static final Color CYAN = new Color(0, 1, 1);
    public static final Color MAGENTA = new Color(1, 0, 1);
    public static final Color YELLOW = new Color(1, 1, 0);
    public static final Color WHITE = new Color(1, 1, 1);

    private final double red;
    private final double green;
    private final double blue;

    public Color(final double red, final double green, final double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    @Override
    public String toString() {
        return "Color{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return
                (ColorCalculator.colorComponentToByte(color.red) == ColorCalculator.colorComponentToByte(red)) &&
                (ColorCalculator.colorComponentToByte(color.green) == ColorCalculator.colorComponentToByte(green)) &&
                (ColorCalculator.colorComponentToByte(color.blue) == ColorCalculator.colorComponentToByte(blue));
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                ColorCalculator.colorComponentToByte(red),
                ColorCalculator.colorComponentToByte(blue),
                ColorCalculator.colorComponentToByte(green)
        );
    }

}

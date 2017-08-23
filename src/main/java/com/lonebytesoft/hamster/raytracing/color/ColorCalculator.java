package com.lonebytesoft.hamster.raytracing.color;

import java.util.Collection;

public final class ColorCalculator {

    public static Color combine(final Collection<ColorWeighted> colors) {
        double red = 0;
        double green = 0;
        double blue = 0;
        double weight = 0;
        for(final ColorWeighted colorWeighted : colors) {
            final Color color = colorWeighted.getColor();
            final double colorWeight = colorWeighted.getWeight();
            red += color.getRed() * colorWeight;
            green += color.getGreen() * colorWeight;
            blue += color.getBlue() * colorWeight;
            weight += colorWeight;
        }

        if(weight == 0) {
            throw new IllegalArgumentException("Zero weight sum");
        }

        return new Color(red / weight, green / weight, blue / weight);
    }

    /**
     * Illuminates given color shifting it towards black or white
     * @param color color to illuminate
     * @param illuminance [-1..1], -1 shifts all the way to black (total darkness), 1 shifts totally to white (absolute light)
     * @return resulting color
     */
    public static Color illuminate(final Color color, final double illuminance) {
        final double amount = illuminance < -1.0 ? -1.0 : (illuminance > 1.0 ? 1.0 : illuminance);
        if(amount >= 0.0) {
            return new Color(
                    color.getRed() + (1.0 - color.getRed()) * amount,
                    color.getGreen() + (1.0 - color.getGreen()) * amount,
                    color.getBlue() + (1.0 - color.getBlue()) * amount
            );
        } else {
            return new Color(
                    color.getRed() * (1.0 + amount),
                    color.getGreen() * (1.0 + amount),
                    color.getBlue() * (1.0 + amount)
            );
        }
    }

}

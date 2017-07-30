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

    public static Color illuminate(final Color color, final double illuminance) {
        return new Color(
                color.getRed() + (1.0 - color.getRed()) * illuminance,
                color.getGreen() + (1.0 - color.getGreen()) * illuminance,
                color.getBlue() + (1.0 - color.getBlue()) * illuminance
        );
    }
    
//    public static Color illuminate(final Color color, final double illuminance) {
//        final ColorHsv colorHsv = new ColorHsv(color);
//        final ColorHsv illuminated = new ColorHsv(colorHsv.getHue(), colorHsv.getSaturation(),
//                colorHsv.getValue() + (1.0 - colorHsv.getValue()) * illuminance);
//        return illuminated.toColor();
//    }

}

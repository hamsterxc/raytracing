package com.lonebytesoft.hamster.raytracing.color;

import com.lonebytesoft.hamster.raytracing.util.math.MathCalculator;

class ColorHsv {

    private final double hue;
    private final double saturation;
    private final double value;

    public ColorHsv(final double hue, final double saturation, final double value) {
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
    }

    public ColorHsv(final Color color) {
        final double r = color.getRed();
        final double g = color.getGreen();
        final double b = color.getBlue();

        final double max = Math.max(r, Math.max(g, b));
        final double min = Math.min(r, Math.min(g, b));

        if(MathCalculator.isEqual(min, max)) {
            hue = 0.0;
        } else {
            final double hueDivisor = 6.0 * (max - min);
            if((r >= g) && (r >= b)) {
                if(g >= b) {
                    hue = (g - b) / hueDivisor;
                } else {
                    hue = (g - b) / hueDivisor + 1.0;
                }
            } else if((g >= r) && (g >= b)) {
                hue = (b - r) / hueDivisor + 1.0/3.0;
            } else {
                hue = (r - g) / hueDivisor + 2.0 / 3.0;
            }
        }

        if(MathCalculator.isEqual(0.0, max)) {
            saturation = 0.0;
        } else {
            saturation = 1.0 - min / max;
        }

        value = max;
    }

    public double getHue() {
        return hue;
    }

    public double getSaturation() {
        return saturation;
    }

    public double getValue() {
        return value;
    }

    public Color toColor() {
        final double vmin = (1.0 - saturation) * value;
        final double a = (value - vmin) * ((hue * 6.0 - Math.floor(hue * 6.0)) / 6.0);
        final double vinc = vmin + a;
        final double vdec = value - a;

        switch((int) Math.floor(hue * 6.0)) {
            case 0:
            case 6:
                return new Color(value, vinc, vmin);

            case 1:
                return new Color(vdec, value, vmin);

            case 2:
                return new Color(vmin, value, vinc);

            case 3:
                return new Color(vmin, vdec, value);

            case 4:
                return new Color(vinc, vmin, value);

            case 5:
                return new Color(value, vmin, vdec);

            default:
                throw new IllegalStateException("Unexpected hue value: " + hue);
        }
    }

}

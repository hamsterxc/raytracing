package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.color.Color;

public class ColorBuilder implements ExpressionBuilder<Color> {

    @Override
    public String build(Color definition) {
        return "new Color(" + definition.getRed() + "," + definition.getGreen() + "," + definition.getBlue() + ")";
    }

}

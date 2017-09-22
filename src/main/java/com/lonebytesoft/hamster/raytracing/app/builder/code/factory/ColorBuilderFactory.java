package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ColorBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.color.Color;

public class ColorBuilderFactory implements BuilderFactory<ExpressionBuilder<Color>> {

    @Override
    public ExpressionBuilder<Color> build(Commit commit) {
        return new ColorBuilder();
    }

}

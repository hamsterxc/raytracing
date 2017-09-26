package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ColorBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.CommitManager;
import com.lonebytesoft.hamster.raytracing.color.Color;

public class ColorBuilderFactory implements BuilderFactory<ExpressionBuilder<Color>> {

    private final CommitManager commitManager;

    public ColorBuilderFactory(final CommitManager commitManager) {
        this.commitManager = commitManager;
    }

    @Override
    public ExpressionBuilder<Color> build(String commitHash) {
        return new ColorBuilder();
    }

}

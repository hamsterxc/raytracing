package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.VariableNameBuilder;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.CommitManager;

import java.util.concurrent.atomic.AtomicInteger;

public class VariableNameBuilderFactory implements BuilderFactory<ExpressionBuilder<String>> {

    private final CommitManager commitManager;

    public VariableNameBuilderFactory(final CommitManager commitManager) {
        this.commitManager = commitManager;
    }

    @Override
    public ExpressionBuilder<String> build(String commitHash) {
        return new VariableNameBuilder(new AtomicInteger(0));
    }

}

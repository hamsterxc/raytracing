package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.VariableNameBuilder;

import java.util.concurrent.atomic.AtomicInteger;

public class VariableNameBuilderFactory implements BuilderFactory<ExpressionBuilder<String>> {

    @Override
    public ExpressionBuilder<String> build(Commit commit) {
        return new VariableNameBuilder(new AtomicInteger(0));
    }

}

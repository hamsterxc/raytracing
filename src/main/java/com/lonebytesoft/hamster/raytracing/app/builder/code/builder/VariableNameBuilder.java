package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import java.util.concurrent.atomic.AtomicInteger;

public class VariableNameBuilder implements ExpressionBuilder<String> {

    private final AtomicInteger counter;

    public VariableNameBuilder(final AtomicInteger counter) {
        this.counter = counter;
    }

    @Override
    public String build(String definition) {
        return definition + counter.getAndIncrement();
    }

}

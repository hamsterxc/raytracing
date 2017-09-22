package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

public interface StatementBuilder<T> {

    String build(T definition, String name);

}

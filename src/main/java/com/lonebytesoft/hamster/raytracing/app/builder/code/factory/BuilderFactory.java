package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

public interface BuilderFactory<T> {

    T build(String commitHash);

}

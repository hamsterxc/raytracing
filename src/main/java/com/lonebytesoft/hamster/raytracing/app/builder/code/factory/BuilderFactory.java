package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;

public interface BuilderFactory<T> {

    T build(Commit commit);

}

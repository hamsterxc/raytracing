package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilderImpl;

public class CoordinatesBuilderFactory implements BuilderFactory<CoordinatesBuilder> {

    @Override
    public CoordinatesBuilder build(Commit commit) {
        return new CoordinatesBuilderImpl();
    }

}

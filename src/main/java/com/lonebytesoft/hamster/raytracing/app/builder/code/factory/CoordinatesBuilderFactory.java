package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilderImpl;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.CommitManager;

public class CoordinatesBuilderFactory implements BuilderFactory<CoordinatesBuilder> {

    private final CommitManager commitManager;

    public CoordinatesBuilderFactory(final CommitManager commitManager) {
        this.commitManager = commitManager;
    }

    @Override
    public CoordinatesBuilder build(String commitHash) {
        return new CoordinatesBuilderImpl();
    }

}

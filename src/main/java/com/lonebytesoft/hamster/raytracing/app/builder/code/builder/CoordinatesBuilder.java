package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public interface CoordinatesBuilder extends ExpressionBuilder<Coordinates<?>>, StatementBuilder<Coordinates<?>> {

    String buildClassName(final Coordinates<?> coordinates);
    String buildClassName(final int dimensions);

    String buildReference(final int dimensions);

}

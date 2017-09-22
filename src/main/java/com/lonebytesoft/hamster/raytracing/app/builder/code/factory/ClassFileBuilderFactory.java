package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ClassFileBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.StatementBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ClassFileDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;

public class ClassFileBuilderFactory implements BuilderFactory<ExpressionBuilder<ClassFileDefinition>> {

    private final ExpressionBuilder<SceneDefinition> importsBuilder;
    private final StatementBuilder<SceneDefinition> beholderBuilder;
    private final CoordinatesBuilder coordinatesBuilder;

    public ClassFileBuilderFactory(final ExpressionBuilder<SceneDefinition> importsBuilder,
                                   final StatementBuilder<SceneDefinition> beholderBuilder,
                                   final CoordinatesBuilder coordinatesBuilder) {
        this.importsBuilder = importsBuilder;
        this.beholderBuilder = beholderBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
    }

    @Override
    public ExpressionBuilder<ClassFileDefinition> build(Commit commit) {
        return new ClassFileBuilder(importsBuilder, beholderBuilder, coordinatesBuilder);
    }

}

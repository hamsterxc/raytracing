package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.FeatureNotImplementedException;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ClassFileBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.StatementBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ClassFileDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.Commit;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.CommitManager;

public class ClassFileBuilderFactory implements BuilderFactory<ExpressionBuilder<ClassFileDefinition>> {

    private final CommitManager commitManager;
    private final ExpressionBuilder<SceneDefinition> importsBuilder;
    private final StatementBuilder<SceneDefinition> beholderBuilder;
    private final CoordinatesBuilder coordinatesBuilder;

    public ClassFileBuilderFactory(final CommitManager commitManager,
                                   final ExpressionBuilder<SceneDefinition> importsBuilder,
                                   final StatementBuilder<SceneDefinition> beholderBuilder,
                                   final CoordinatesBuilder coordinatesBuilder) {
        this.commitManager = commitManager;
        this.importsBuilder = importsBuilder;
        this.beholderBuilder = beholderBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
    }

    @Override
    public ExpressionBuilder<ClassFileDefinition> build(String commitHash) {
        if(commitManager.isOlder(commitHash, Commit.INITIALIZED_FRAMEWORK.getHash())) {
            throw new FeatureNotImplementedException("raytracing");
        } else {
            return new ClassFileBuilder(importsBuilder, beholderBuilder, coordinatesBuilder);
        }
    }

}

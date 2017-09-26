package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ImportsBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.Commit;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.CommitManager;

public class ImportsBuilderFactory implements BuilderFactory<ExpressionBuilder<SceneDefinition>> {

    private final CommitManager commitManager;

    public ImportsBuilderFactory(final CommitManager commitManager) {
        this.commitManager = commitManager;
    }

    @Override
    public ExpressionBuilder<SceneDefinition> build(String commitHash) {
        if(commitManager.isOlder(commitHash, Commit.REGRESSION_PERFORMANCE_TESTS.getHash())) {
            return new PreTestsImportsBuilder();
        } else {
            return new ImportsBuilder();
        }
    }

    private static class PreTestsImportsBuilder extends ImportsBuilder {
        @Override
        public String build(SceneDefinition definition) {
            return super.build(definition)
                    .replace(
                            "com.lonebytesoft.hamster.raytracing.format.writer.",
                            "com.lonebytesoft.hamster.raytracing.format."
                    );
        }
    }

}

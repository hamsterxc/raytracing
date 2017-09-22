package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ImportsBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;

public class ImportsBuilderFactory implements BuilderFactory<ExpressionBuilder<SceneDefinition>> {

    @Override
    public ExpressionBuilder<SceneDefinition> build(Commit commit) {
        if(commit.isOlder(Commit.REGRESSION_PERFORMANCE_TESTS)) {
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

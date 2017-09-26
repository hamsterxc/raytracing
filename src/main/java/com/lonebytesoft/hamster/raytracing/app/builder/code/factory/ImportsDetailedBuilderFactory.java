package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ImportsDetailedBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.Commit;
import com.lonebytesoft.hamster.raytracing.app.helper.commit.CommitManager;

public class ImportsDetailedBuilderFactory implements BuilderFactory<ExpressionBuilder<SceneDefinition>> {

    private final CommitManager commitManager;

    public ImportsDetailedBuilderFactory(final CommitManager commitManager) {
        this.commitManager = commitManager;
    }

    @Override
    public ExpressionBuilder<SceneDefinition> build(String commitHash) {
        if(commitManager.isOlder(commitHash, Commit.REWORKED_PICTURE_BUILDING.getHash())) {
            return new PreReworkPictureBuildingImportsDetailedBuilder();
        } else if(commitManager.isOlder(commitHash, Commit.ADDED_EXAMPLE.getHash())) {
            return new PreCheckersTextureImportsDetailedBuilder();
        } else if(commitManager.isOlder(commitHash, Commit.ADDED_REFRACTING.getHash())) {
            return new PreRefractingImportsDetailedBuilder();
        } else if(commitManager.isOlder(commitHash, Commit.ADDED_CONE_LIGHT.getHash())) {
            return new PreConeLightImportsDetailedBuilder();
        } else if(commitManager.isOlder(commitHash, Commit.ADDED_SURFACED_SCREEN.getHash())) {
            return new PreScreenSurfacedImportsDetailedBuilder();
        } else if(commitManager.isOlder(commitHash, Commit.REGRESSION_PERFORMANCE_TESTS.getHash())) {
            return new PreTestsImportsDetailedBuilder();
        } else {
            return new ImportsDetailedBuilder();
        }
    }
    
    private static class PreTestsImportsDetailedBuilder extends ImportsDetailedBuilder {
        @Override
        public String build(SceneDefinition definition) {
            return super.build(definition)
                    .replace(
                            "com.lonebytesoft.hamster.raytracing.format.writer.",
                            "com.lonebytesoft.hamster.raytracing.format."
                    );
        }
    }

    private static class PreScreenSurfacedImportsDetailedBuilder extends PreTestsImportsDetailedBuilder {
        @Override
        public String build(SceneDefinition definition) {
            return super.build(definition)
                    .replace(
                            "com.lonebytesoft.hamster.raytracing.scene.screen.ScreenShapeSurfaced",
                            "com.lonebytesoft.hamster.raytracing.scene.screen.ScreenOrthotope"
                    );
        }
    }

    private static class PreConeLightImportsDetailedBuilder extends PreScreenSurfacedImportsDetailedBuilder {
        @Override
        public String build(SceneDefinition definition) {
            return super.build(definition)
                    .replace(
                            "import com.lonebytesoft.hamster.raytracing.shape.light.ConeLightSource;\n",
                            ""
                    );
        }
    }

    private static class PreRefractingImportsDetailedBuilder extends PreConeLightImportsDetailedBuilder {
        @Override
        public String build(SceneDefinition definition) {
            return super.build(definition)
                    .replace(
                            "import com.lonebytesoft.hamster.raytracing.shape.adapter.ReflectingAdapter;\n",
                            ""
                    );
        }
    }

    private static class PreCheckersTextureImportsDetailedBuilder extends PreRefractingImportsDetailedBuilder {
        @Override
        public String build(SceneDefinition definition) {
            return super.build(definition)
                    .replace(
                            "import com.lonebytesoft.hamster.raytracing.app.CheckersTexture;\n",
                            ""
                    );
        }
    }

    private static class PreReworkPictureBuildingImportsDetailedBuilder extends PreCheckersTextureImportsDetailedBuilder {
        @Override
        public String build(SceneDefinition definition) {
            return super.build(definition)
                    .replace(
                            "import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates1d;\n",
                            ""
                    );
        }
    }

}

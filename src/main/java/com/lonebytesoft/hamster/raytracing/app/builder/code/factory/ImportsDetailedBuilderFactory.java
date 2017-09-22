package com.lonebytesoft.hamster.raytracing.app.builder.code.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.code.Commit;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ImportsDetailedBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;

public class ImportsDetailedBuilderFactory implements BuilderFactory<ExpressionBuilder<SceneDefinition>> {

    @Override
    public ExpressionBuilder<SceneDefinition> build(Commit commit) {
        if(commit.isOlder(Commit.REWORKED_PICTURE_BUILDING)) {
            return new PreReworkPictureBuildingImportsDetailedBuilder();
        } else if(commit.isOlder(Commit.ADDED_EXAMPLE)) {
            return new PreCheckersTextureImportsDetailedBuilder();
        } else if(commit.isOlder(Commit.ADDED_REFRACTING)) {
            return new PreRefractingImportsDetailedBuilder();
        } else if(commit.isOlder(Commit.ADDED_CONE_LIGHT)) {
            return new PreConeLightImportsDetailedBuilder();
        } else if(commit.isOlder(Commit.ADDED_SURFACED_SCREEN)) {
            return new PreScreenSurfacedImportsDetailedBuilder();
        } else if(commit.isOlder(Commit.REGRESSION_PERFORMANCE_TESTS)) {
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

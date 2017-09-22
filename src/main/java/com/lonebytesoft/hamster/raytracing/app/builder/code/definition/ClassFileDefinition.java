package com.lonebytesoft.hamster.raytracing.app.builder.code.definition;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;

public class ClassFileDefinition {

    private final SceneDefinition sceneDefinition;
    private final String packageName;
    private final String className;
    private final String outputFileName;

    public ClassFileDefinition(final SceneDefinition sceneDefinition,
                               final String packageName, final String className, final String outputFileName) {
        this.sceneDefinition = sceneDefinition;
        this.packageName = packageName;
        this.className = className;
        this.outputFileName = outputFileName;
    }

    public SceneDefinition getSceneDefinition() {
        return sceneDefinition;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

}

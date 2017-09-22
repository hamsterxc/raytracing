package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ClassFileDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;

public class ClassFileBuilder implements ExpressionBuilder<ClassFileDefinition> {

    private final ExpressionBuilder<SceneDefinition> importsBuilder;
    private final StatementBuilder<SceneDefinition> beholderBuilder;
    private final CoordinatesBuilder coordinatesBuilder;

    public ClassFileBuilder(final ExpressionBuilder<SceneDefinition> importsBuilder,
                            final StatementBuilder<SceneDefinition> beholderBuilder,
                            final CoordinatesBuilder coordinatesBuilder) {
        this.importsBuilder = importsBuilder;
        this.beholderBuilder = beholderBuilder;
        this.coordinatesBuilder = coordinatesBuilder;
    }

    @Override
    public String build(ClassFileDefinition definition) {
        final StringBuilder code = new StringBuilder();

        if(definition.getPackageName() != null) {
            code.append("package ").append(definition.getPackageName()).append(";\n\n");
        }

        code.append(importsBuilder.build(definition.getSceneDefinition()));

        code
                .append("\npublic class ").append(definition.getClassName()).append(" {\n")
                .append(buildClassContent(
                        definition.getSceneDefinition(),
                        definition.getClassName(),
                        definition.getOutputFileName()))
                .append("\n}\n");

        return code.toString();
    }

    private String buildClassContent(final SceneDefinition sceneDefinition, final String className, final String outputFileName) {
        final StringBuilder code = new StringBuilder();

        code.append("\nprivate static final Logger logger = LoggerFactory.getLogger(").append(className).append(".class);\n");

        code
                .append("\npublic static void main(String[] args) throws Exception {\n")
                .append(buildMainContent(sceneDefinition, outputFileName))
                .append("}\n");

        return code.toString();
    }

    private String buildMainContent(final SceneDefinition sceneDefinition, final String outputFileName) {
        final StringBuilder code = new StringBuilder();
        code.append("logger.info(\"Start\");\n");

        code.append(beholderBuilder.build(sceneDefinition, "beholder"));

        code
                .append("final Picture<")
                .append(coordinatesBuilder.buildClassName(sceneDefinition.getPictureDimensions()))
                .append("> picture = beholder.getPicture();\n");
        code
                .append("final PictureWriter<")
                .append(coordinatesBuilder.buildClassName(sceneDefinition.getPictureDimensions()))
                .append("> pictureWriter = new BmpWriter();\n");
        code
                .append("pictureWriter.write(picture, new BufferedOutputStream(new FileOutputStream(\"")
                .append(outputFileName)
                .append("\")));\n");

        code.append("logger.info(\"End\");\n");
        return code.toString();
    }

}

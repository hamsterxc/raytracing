package com.lonebytesoft.hamster.raytracing.app.builder.code;

import com.lonebytesoft.hamster.raytracing.app.builder.code.builder.ExpressionBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.code.definition.ClassFileDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.code.factory.CodeBuilderFactory;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.factory.XmlParserFactory;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;

public class XmlCodeBuilder {

    public static void main(String[] args) throws Exception {
        final String inputFileName = System.getProperty("raytracing.xmlcodebuilder.input");
        final String packageName = System.getProperty("raytracing.xmlcodebuilder.package", null);
        final String className = System.getProperty("raytracing.xmlcodebuilder.class", "raytracing");
        final String pictureName = System.getProperty("raytracing.xmlcodebuilder.picture", "picture.bmp");
        final String commitHash = System.getProperty("raytracing.xmlcodebuilder.commithash", null);

        final Commit commit = Commit.find(commitHash);

        if(inputFileName == null) {
            throw new RuntimeException("Input file not specified");
        }

        final Document document;
        try(final InputStream inputStream = new FileInputStream(inputFileName)) {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
        }

        final XmlParser xmlParser = new XmlParserFactory().obtainXmlParser();
        final SceneDefinition sceneDefinition = xmlParser.parse(document);

        final ExpressionBuilder<ClassFileDefinition> classFileBuilder = new CodeBuilderFactory().build(commit);
        final ClassFileDefinition classFileDefinition = new ClassFileDefinition(
                sceneDefinition, packageName, className, pictureName);
        final String code = classFileBuilder.build(classFileDefinition);

        System.out.println(code);
    }

}

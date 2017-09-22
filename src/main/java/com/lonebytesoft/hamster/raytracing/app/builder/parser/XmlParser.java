package com.lonebytesoft.hamster.raytracing.app.builder.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.validator.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.Map;

public class XmlParser {

    private static final String TAG_SCENE = "scene";

    private final TagParser<SceneDefinition> sceneParser;
    private final Validator validator;

    public XmlParser(final TagParser<SceneDefinition> sceneParser, final Validator validator) {
        this.sceneParser = sceneParser;
        this.validator = validator;
    }

    public SceneDefinition parse(final Document document) {
        final Map<String, Node> nodes = XmlParseUtils.convertToMap(document.getChildNodes());
        final SceneDefinition sceneDefinition = XmlParseUtils.processNodeMandatory(nodes, TAG_SCENE, sceneParser::parse);
        validator.validate(sceneDefinition);
        return sceneDefinition;
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParseUtils;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.ScreenDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import org.w3c.dom.Node;

import java.util.Map;

public class SceneTagParser implements TagParser<SceneDefinition> {

    private static final String TAG_COLOR_DEFAULT = "colordefault";
    private static final String TAG_EYE = "eye";
    private static final String TAG_SCREEN = "screen";
    private static final String TAG_SHAPES = "shapes";
    private static final String TAG_LIGHTS = "lights";

    private final TagParser<Color> colorParser;
    private final TagParser<VectorDefinition> vectorParser;
    private final TagParser<ScreenDefinition> screenParser;
    private final TagParser<ShapeDefinition> shapeParser;
    private final TagParser<LightDefinition> lightParser;

    public SceneTagParser(final TagParser<Color> colorParser, final TagParser<VectorDefinition> vectorParser,
                          final TagParser<ScreenDefinition> screenParser) {
        this(colorParser, vectorParser, screenParser, null, null);
    }

    public SceneTagParser(final TagParser<Color> colorParser, final TagParser<VectorDefinition> vectorParser,
                          final TagParser<ScreenDefinition> screenParser,
                          final TagParser<ShapeDefinition> shapeParser, final TagParser<LightDefinition> lightParser) {
        this.colorParser = colorParser;
        this.vectorParser = vectorParser;
        this.screenParser = screenParser;
        this.shapeParser = shapeParser;
        this.lightParser = lightParser;
    }

    @Override
    public SceneDefinition parse(Node node) {
        final Map<String, Node> nodes = XmlParseUtils.convertToMap(node.getChildNodes());

        final Color colorDefault = XmlParseUtils.processNodeMandatory(nodes, TAG_COLOR_DEFAULT, colorParser::parse);
        final Coordinates<?> eye = XmlParseUtils.processNodeMandatory(nodes, TAG_EYE,
                eyeTag -> vectorParser.parse(eyeTag).getCoordinates());
        final ScreenDefinition screen = XmlParseUtils.processNodeMandatory(nodes, TAG_SCREEN, screenParser::parse);

        final SceneDefinition scene = new SceneDefinition(colorDefault, eye, screen);

        if(shapeParser != null) {
            XmlParseUtils.consumeNode(nodes, TAG_SHAPES, false, shapesNode ->
                    XmlParseUtils.convertToStream(shapesNode.getChildNodes())
                            .map(shapeParser::parse)
                            .forEach(scene::addShape));
        }

        if(lightParser != null) {
            XmlParseUtils.consumeNode(nodes, TAG_LIGHTS, false, lightsNode ->
                    XmlParseUtils.convertToStream(lightsNode.getChildNodes())
                            .map(lightParser::parse)
                            .forEach(scene::addLight));
        }

        return scene;
    }

}

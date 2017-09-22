package com.lonebytesoft.hamster.raytracing.app.builder.parser.light.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParseUtils;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParserException;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.AmbientLightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.ConeLightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.PointLightDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import org.w3c.dom.Node;

import java.util.Map;

public class LightTagParser implements TagParser<LightDefinition> {

    private static final String TAG_AMBIENT = "ambient";
    private static final String TAG_CONE = "cone";
    private static final String TAG_POINT = "point";

    private static final String TAG_BRIGHTNESS = "brightness";
    private static final String TAG_SOURCE = "source";

    private static final String TAG_CONE_DIRECTION = "direction";
    private static final String TAG_CONE_ANGLE = "angle";

    private final TagParser<Double> doubleParser;
    private final TagParser<VectorDefinition> vectorParser;

    public LightTagParser(final TagParser<Double> doubleParser, final TagParser<VectorDefinition> vectorParser) {
        this.doubleParser = doubleParser;
        this.vectorParser = vectorParser;
    }

    @Override
    public LightDefinition parse(Node node) {
        final Map<String, Node> nodes = XmlParseUtils.convertToMap(node.getChildNodes());
        switch(node.getNodeName().toLowerCase()) {
            case TAG_AMBIENT:
                return new AmbientLightDefinition(parseBrightness(nodes));

            case TAG_CONE:
                final VectorDefinition direction = XmlParseUtils.processNodeMandatory(nodes, TAG_CONE_DIRECTION, vectorParser::parse);
                final double angle = XmlParseUtils.processNodeMandatory(nodes, TAG_CONE_ANGLE, doubleParser::parse);
                return new ConeLightDefinition(parseSource(nodes), direction, angle, parseBrightness(nodes));

            case TAG_POINT:
                return new PointLightDefinition(parseSource(nodes), parseBrightness(nodes));

            default:
                throw new XmlParserException("light type", node.getNodeName(), "Unknown type");
        }
    }

    private double parseBrightness(final Map<String, Node> nodes) {
        return XmlParseUtils.processNodeMandatory(nodes, TAG_BRIGHTNESS, doubleParser::parse);
    }

    private Coordinates<?> parseSource(final Map<String, Node> nodes) {
        return XmlParseUtils.processNodeMandatory(nodes, TAG_SOURCE, node -> vectorParser.parse(node).getCoordinates());
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParseUtils;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParserException;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.LayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.ReflectingLayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.RefractingLayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.SolidColoredLayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.TransparentLayerDefinition;
import com.lonebytesoft.hamster.raytracing.color.Color;
import org.w3c.dom.Node;

import java.util.Map;

public class LayerTagParser implements TagParser<LayerDefinition> {

    private static final String TAG_REFLECTING = "reflecting";
    private static final String TAG_REFRACTING = "refracting";
    private static final String TAG_SOLID_COLORED = "solidcolored";
    private static final String TAG_TEXTURED = "textured";
    private static final String TAG_TRANSPARENT = "transparent";

    private static final String TAG_REFRACTING_COEFF = "coeff";
    private static final String TAG_SOLID_COLORED_COLOR = "color";

    private final TagParser<Double> doubleParser;
    private final TagParser<Color> colorParser;
    private final TagParser<LayerDefinition> texturedParser;

    public LayerTagParser(final TagParser<Double> doubleParser, final TagParser<Color> colorParser,
                          final TagParser<LayerDefinition> texturedParser) {
        this.doubleParser = doubleParser;
        this.colorParser = colorParser;
        this.texturedParser = texturedParser;
    }

    @Override
    public LayerDefinition parse(Node node) {
        final double weight = LayerTagParserUtils.parseWeight(node);

        final Map<String, Node> nodes = XmlParseUtils.convertToMap(node.getChildNodes());
        switch(node.getNodeName().toLowerCase()) {
            case TAG_REFLECTING:
                return new ReflectingLayerDefinition(weight);

            case TAG_REFRACTING:
                return new RefractingLayerDefinition(weight, parseRefractingCoeff(nodes));

            case TAG_SOLID_COLORED:
                return new SolidColoredLayerDefinition(weight, parseSolidColoredColor(nodes));

            case TAG_TEXTURED:
                return texturedParser.parse(node);

            case TAG_TRANSPARENT:
                return new TransparentLayerDefinition(weight);

            default:
                throw new XmlParserException("layer type", node.getNodeName(), "Unknown type");
        }
    }

    private double parseRefractingCoeff(final Map<String, Node> nodes) {
        return XmlParseUtils.processNodeMandatory(nodes, TAG_REFRACTING_COEFF, doubleParser::parse);
    }

    private Color parseSolidColoredColor(final Map<String, Node> nodes) {
        return XmlParseUtils.processNodeMandatory(nodes, TAG_SOLID_COLORED_COLOR, colorParser::parse);
    }

}

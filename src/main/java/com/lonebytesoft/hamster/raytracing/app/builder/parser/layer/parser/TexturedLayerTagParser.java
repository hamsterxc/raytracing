package com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParseUtils;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParserException;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.LayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.texture.TexturedCheckersLayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.texture.TexturedLayerDefinition;
import com.lonebytesoft.hamster.raytracing.color.Color;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TexturedLayerTagParser implements TagParser<LayerDefinition> {

    private static final String TAG_TEXTURE = "texture";

    private static final String TAG_CHECKERS = "checkers";

    private static final String TAG_CHECKERS_MULTIPLIERS = "multipliers";
    private static final String TAG_CHECKERS_MULTIPLIER = "multiplier";
    private static final String TAG_CHECKERS_COLORS = "colors";
    private static final String TAG_CHECKERS_COLOR = "color";

    private final TagParser<Double> doubleParser;
    private final TagParser<Color> colorParser;

    public TexturedLayerTagParser(final TagParser<Double> doubleParser, final TagParser<Color> colorParser) {
        this.doubleParser = doubleParser;
        this.colorParser = colorParser;
    }

    @Override
    public LayerDefinition parse(Node node) {
        final double weight = LayerTagParserUtils.parseWeight(node);

        return XmlParseUtils.processNodeMandatory(XmlParseUtils.convertToMap(node.getChildNodes()), TAG_TEXTURE,
                textureNode -> XmlParseUtils.convertToStream(textureNode.getChildNodes())
                        .map(textureTypeNode -> {
                            switch(textureTypeNode.getNodeName().toLowerCase()) {
                                case TAG_CHECKERS:
                                    return parseCheckers(weight, textureTypeNode);

                                default:
                                    throw new XmlParserException("textured layer texture type",
                                            '<' + textureTypeNode.getNodeName() + '>', "Unknown type");
                            }
                        })
                        .findFirst()
                        .orElseThrow(() -> new XmlParserException("textured layer texture",
                                '<' + textureNode.getNodeName() + '>', "Texture node not present")));
    }

    private TexturedLayerDefinition parseCheckers(final double weight, final Node node) {
        final Map<String, Node> nodes = XmlParseUtils.convertToMap(node.getChildNodes());

        final List<Double> multipliers = XmlParseUtils.processNodeMandatory(nodes, TAG_CHECKERS_MULTIPLIERS,
                multipliersNode -> XmlParseUtils.convertToStream(multipliersNode.getChildNodes())
                        .filter(multiplierNode -> multiplierNode.getNodeName().toLowerCase().equals(TAG_CHECKERS_MULTIPLIER))
                        .map(doubleParser::parse)
                        .collect(Collectors.toList()));

        final List<Color> colors = XmlParseUtils.processNodeMandatory(nodes, TAG_CHECKERS_COLORS,
                colorsNode -> XmlParseUtils.convertToStream(colorsNode.getChildNodes())
                        .filter(colorNode -> colorNode.getNodeName().toLowerCase().equals(TAG_CHECKERS_COLOR))
                        .map(colorParser::parse)
                        .collect(Collectors.toList()));

        return new TexturedCheckersLayerDefinition(weight, multipliers, colors);
    }

}

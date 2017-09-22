package com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParseUtils;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParserException;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor.PixelColoringDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor.SimplePixelColoringDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor.SupersamplingPixelColoringDefinition;
import com.lonebytesoft.hamster.raytracing.color.Color;
import org.w3c.dom.Node;

import java.util.Map;

public class PixelColoringTagParser implements TagParser<PixelColoringDefinition> {

    private static final String TAG_SIMPLE = "simple";
    private static final String TAG_SUPERSAMPLING = "supersampling";

    private static final String TAG_SUPERSAMPLING_MULTIPLIER = "multiplier";
    private static final String TAG_SUPERSAMPLING_COLOR_DEFAULT = "colordefault";

    private final TagParser<Double> doubleParser;
    private final TagParser<Color> colorParser;

    public PixelColoringTagParser(final TagParser<Double> doubleParser, final TagParser<Color> colorParser) {
        this.doubleParser = doubleParser;
        this.colorParser = colorParser;
    }

    @Override
    public PixelColoringDefinition parse(Node node) {
        final Map<String, Node> nodes = XmlParseUtils.convertToMap(node.getChildNodes());
        switch(node.getNodeName().toLowerCase()) {
            case TAG_SIMPLE:
                return new SimplePixelColoringDefinition();

            case TAG_SUPERSAMPLING:
                final int multiplier = XmlParseUtils.processNodeMandatory(nodes, TAG_SUPERSAMPLING_MULTIPLIER,
                        multiplierNode -> doubleParser.parse(multiplierNode).intValue());
                final Color colorDefault = XmlParseUtils.processNodeMandatory(nodes, TAG_SUPERSAMPLING_COLOR_DEFAULT,
                        colorParser::parse);
                return new SupersamplingPixelColoringDefinition(multiplier, colorDefault);

            default:
                throw new XmlParserException("pixel coloring type", node.getNodeName(), "Unknown type");
        }
    }

}

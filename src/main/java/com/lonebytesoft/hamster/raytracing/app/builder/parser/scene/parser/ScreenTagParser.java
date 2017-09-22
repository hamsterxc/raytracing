package com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParseUtils;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParserException;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.ScreenDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor.PixelColoringDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import org.w3c.dom.Node;

import java.util.Map;

public class ScreenTagParser implements TagParser<ScreenDefinition> {

    private static final String TAG_SHAPE = "shape";
    private static final String TAG_FROM = "from";
    private static final String TAG_TO = "to";
    private static final String TAG_RESOLUTION = "resolution";
    private static final String TAG_PIXEL_COLORING = "pixelcoloring";

    private final TagParser<VectorDefinition> vectorParser;
    private final TagParser<ShapeDefinition> shapeParser;
    private final TagParser<PixelColoringDefinition> pixelColoringParser;

    public ScreenTagParser(final TagParser<VectorDefinition> vectorParser,
                           final TagParser<ShapeDefinition> shapeParser, final TagParser<PixelColoringDefinition> pixelColoringParser) {
        this.vectorParser = vectorParser;
        this.shapeParser = shapeParser;
        this.pixelColoringParser = pixelColoringParser;
    }

    @Override
    public ScreenDefinition parse(Node node) {
        final Map<String, Node> nodes = XmlParseUtils.convertToMap(node.getChildNodes());

        final ShapeDefinition shape = XmlParseUtils.processNodeMandatory(nodes, TAG_SHAPE,
                shapeNode -> XmlParseUtils.convertToStream(shapeNode.getChildNodes())
                        .map(shapeParser::parse)
                        .findFirst()
                        .orElseThrow(() -> new XmlParserException("screen shape", '<' + shapeNode.getNodeName() + '>',
                                "Screen shape node not present"))
        );
        final Coordinates<?> from = XmlParseUtils.processNodeMandatory(nodes, TAG_FROM,
                fromNode -> vectorParser.parse(fromNode).getCoordinates());
        final Coordinates<?> to = XmlParseUtils.processNodeMandatory(nodes, TAG_TO,
                toNode -> vectorParser.parse(toNode).getCoordinates());
        final Coordinates<?> resolution = XmlParseUtils.processNodeMandatory(nodes, TAG_RESOLUTION,
                resolutionNode -> vectorParser.parse(resolutionNode).getCoordinates());
        final PixelColoringDefinition pixelColoring = XmlParseUtils.processNodeMandatory(nodes, TAG_PIXEL_COLORING,
                pixelColoringNode -> XmlParseUtils.convertToStream(pixelColoringNode.getChildNodes())
                        .map(pixelColoringParser::parse)
                        .findFirst()
                        .orElseThrow(() -> new XmlParserException("screen pixel coloring",
                                '<' + pixelColoringNode.getNodeName() + '>', "Screen pixel coloring node not present"))
        );

        return new ScreenDefinition(shape, from, to, resolution, pixelColoring);
    }

}

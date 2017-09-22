package com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParseUtils;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParserException;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.LayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import org.w3c.dom.Node;

public class ShapeTagParser implements TagParser<ShapeDefinition> {

    private static final String TAG_BALL = "ball";
    private static final String TAG_ORTHOTOPE = "orthotope";

    private static final String TAG_LAYERS = "layers";

    private final TagParser<ShapeDefinition> ballParser;
    private final TagParser<ShapeDefinition> orthotopeParser;
    private final TagParser<LayerDefinition> layerParser;

    public ShapeTagParser(final TagParser<ShapeDefinition> ballParser, final TagParser<ShapeDefinition> orthotopeParser,
                          final TagParser<LayerDefinition> layerParser) {
        this.ballParser = ballParser;
        this.orthotopeParser = orthotopeParser;
        this.layerParser = layerParser;
    }

    @Override
    public ShapeDefinition parse(final Node node) {
        final ShapeDefinition shape = parseBare(node);

        XmlParseUtils.consumeNode(XmlParseUtils.convertToMap(node.getChildNodes()), TAG_LAYERS, false, layersNode -> {
            XmlParseUtils.convertToStream(layersNode.getChildNodes())
                    .map(layerParser::parse)
                    .forEach(shape::addLayer);
        });

        return shape;
    }

    private ShapeDefinition parseBare(final Node node) {
        switch(node.getNodeName().toLowerCase()) {
            case TAG_BALL:
                return ballParser.parse(node);

            case TAG_ORTHOTOPE:
                return orthotopeParser.parse(node);

            default:
                throw new XmlParserException("shape", node.getNodeName(), "Unknown type");
        }
    }

}

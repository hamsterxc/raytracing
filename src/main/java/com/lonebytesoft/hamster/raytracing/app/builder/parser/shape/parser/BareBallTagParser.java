package com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParseUtils;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.BallDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import org.w3c.dom.Node;

import java.util.Map;

public class BareBallTagParser implements TagParser<ShapeDefinition> {

    private static final String TAG_CENTER = "center";
    private static final String TAG_RADIUS = "radius";

    private final TagParser<Double> doubleParser;
    private final TagParser<VectorDefinition> vectorParser;

    public BareBallTagParser(final TagParser<Double> doubleParser, final TagParser<VectorDefinition> vectorParser) {
        this.doubleParser = doubleParser;
        this.vectorParser = vectorParser;
    }

    @Override
    public ShapeDefinition parse(Node node) {
        final Map<String, Node> nodes = XmlParseUtils.convertToMap(node.getChildNodes());

        final Coordinates<?> center = XmlParseUtils.processNodeMandatory(nodes, TAG_CENTER,
                centerNode -> vectorParser.parse(centerNode).getCoordinates());
        final double radius = XmlParseUtils.processNodeMandatory(nodes, TAG_RADIUS, doubleParser::parse);

        return new BallDefinition(center, radius);
    }

}

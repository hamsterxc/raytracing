package com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParseUtils;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.OrthotopeDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BareOrthotopeTagParser implements TagParser<ShapeDefinition> {

    private static final String TAG_BASE = "base";
    private static final String TAG_VECTORS = "vectors";
    private static final String TAG_VECTOR = "vector";
    private static final String TAG_INFINITE = "infinite";

    private final TagParser<Boolean> booleanParser;
    private final TagParser<VectorDefinition> vectorParser;

    public BareOrthotopeTagParser(final TagParser<Boolean> booleanParser, final TagParser<VectorDefinition> vectorParser) {
        this.booleanParser = booleanParser;
        this.vectorParser = vectorParser;
    }

    @Override
    public ShapeDefinition parse(Node node) {
        final Map<String, Node> nodes = XmlParseUtils.convertToMap(node.getChildNodes());

        final Coordinates<?> base = XmlParseUtils.processNodeMandatory(nodes, TAG_BASE,
                baseNode -> vectorParser.parse(baseNode).getCoordinates());

        final List<VectorDefinition> vectors = XmlParseUtils.processNodeMandatory(nodes, TAG_VECTORS,
                vectorsNode -> XmlParseUtils.convertToStream(vectorsNode.getChildNodes())
                        .filter(vectorNode -> vectorNode.getNodeName().equalsIgnoreCase(TAG_VECTOR))
                        .map(vectorParser::parse)
                        .collect(Collectors.toList()));

        final boolean isInfinite = XmlParseUtils.processNodeMandatory(nodes, TAG_INFINITE, booleanParser::parse);

        return new OrthotopeDefinition(base, vectors, isInfinite);
    }

}

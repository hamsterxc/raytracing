package com.lonebytesoft.hamster.raytracing.app.builder.parser.common.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParseUtils;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParserException;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorRotationDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates1d;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates2d;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates3d;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates4d;
import org.w3c.dom.Node;

import java.util.Map;

public class VectorTagParser implements TagParser<VectorDefinition> {

    private static final String ATTRIBUTE_COORDINATES = "coordinates";
    private static final String TAG_ROTATE = "rotate";
    
    private static final String TAG_ROTATE_AXIS_FIRST = "axisfirst";
    private static final String TAG_ROTATE_AXIS_SECOND = "axissecond";
    private static final String TAG_ROTATE_ANGLE = "angle";
    
    private final TagParser<Double> doubleParser;

    public VectorTagParser(final TagParser<Double> doubleParser) {
        this.doubleParser = doubleParser;
    }

    @Override
    public VectorDefinition parse(Node node) {
        final String value = XmlParseUtils.getAttribute(node, ATTRIBUTE_COORDINATES)
                .orElseThrow(() -> new XmlParserException("coordinates", '<' + node.getNodeName() + '>',
                        "Coordinates attribute not present"));
        final VectorDefinition vector = new VectorDefinition(parseCoordinates(value));
        
        XmlParseUtils.convertToStream(node.getChildNodes())
                .filter(childNode -> childNode.getNodeName().equalsIgnoreCase(TAG_ROTATE))
                .map(childNode -> {
                    final Map<String, Node> nodes = XmlParseUtils.convertToMap(childNode.getChildNodes());
                    
                    final int axisFirst = XmlParseUtils.processNodeMandatory(nodes, TAG_ROTATE_AXIS_FIRST,
                            axisFirstNode -> doubleParser.parse(axisFirstNode).intValue());
                    final int axisSecond = XmlParseUtils.processNodeMandatory(nodes, TAG_ROTATE_AXIS_SECOND,
                            axisSecondNode -> doubleParser.parse(axisSecondNode).intValue());
                    final double angle = XmlParseUtils.processNodeMandatory(nodes, TAG_ROTATE_ANGLE, doubleParser::parse);

                    return new VectorRotationDefinition(axisFirst, axisSecond, angle);
                })
                .forEach(vector::addRotation);

        return vector;
    }

    private Coordinates<?> parseCoordinates(final String value) {
        final String[] parts = XmlParseUtils.splitValue(value);
        try {
            switch(parts.length) {
                case 1:
                    return new Coordinates1d(
                            Double.parseDouble(parts[0])
                    );

                case 2:
                    return new Coordinates2d(
                            Double.parseDouble(parts[0]),
                            Double.parseDouble(parts[1])
                    );

                case 3:
                    return new Coordinates3d(
                            Double.parseDouble(parts[0]),
                            Double.parseDouble(parts[1]),
                            Double.parseDouble(parts[2])
                    );

                case 4:
                    return new Coordinates4d(
                            Double.parseDouble(parts[0]),
                            Double.parseDouble(parts[1]),
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3])
                    );

                default:
                    throw new XmlParserException("coordinates", value, parts.length + "-dimensions coordinates not supported");
            }
        } catch (NumberFormatException e) {
            throw new XmlParserException("coordinates", value, e);
        }
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.parser.common.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParseUtils;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParserException;
import com.lonebytesoft.hamster.raytracing.color.Color;
import org.w3c.dom.Node;

public class ColorTagParser implements TagParser<Color> {

    @Override
    public Color parse(Node node) {
        return parse(node.getTextContent());
    }

    private Color parse(final String value) {
        final String[] parts = XmlParseUtils.splitValue(value);
        if(parts.length == 3) {
            try {
                return new Color(
                        Double.parseDouble(parts[0]),
                        Double.parseDouble(parts[1]),
                        Double.parseDouble(parts[2])
                );
            } catch (NumberFormatException e) {
                throw new XmlParserException("color", value, e);
            }
        } else {
            throw new XmlParserException("color", value, "Incorrect number of parts, expected 3");
        }
    }

}

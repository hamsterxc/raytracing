package com.lonebytesoft.hamster.raytracing.app.builder.parser.common.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParserException;
import org.w3c.dom.Node;

public class DoubleTagParser implements TagParser<Double> {

    @Override
    public Double parse(Node node) {
        return parse(node.getTextContent());
    }

    private double parse(final String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new XmlParserException("number", value, e);
        }
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.parser.common.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.Collection;

public class BooleanTagParser implements TagParser<Boolean> {

    private static final Collection<String> TRUE_VALUES = Arrays.asList("true", "1");

    @Override
    public Boolean parse(Node node) {
        return parse(node.getTextContent());
    }

    private boolean parse(final String value) {
        return TRUE_VALUES.contains(value.toLowerCase());
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParseUtils;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParserException;
import org.w3c.dom.Node;

final class LayerTagParserUtils {

    private static final String ATTRIBUTE_WEIGHT = "weight";

    public static double parseWeight(final Node node) {
        return Double.parseDouble(XmlParseUtils.getAttribute(node, ATTRIBUTE_WEIGHT)
                .orElseThrow(() -> new XmlParserException("layer weight", '<' + node.getNodeName() + '>',
                        "Weight attribute not present")));
    }

}

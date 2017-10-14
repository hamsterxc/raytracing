package com.lonebytesoft.hamster.raytracing.app.builder.parser.light.parser;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParseUtils;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightPropertiesDefinition;
import org.w3c.dom.Node;

import java.util.Map;

public class LightPropertiesTagParser implements TagParser<LightPropertiesDefinition> {

    private static final String TAG_ILLUMINANCE_AMOUNT_MAX = "illuminanceamountmax";
    private static final String TAG_SPACE_PARTICLES_DENSITY = "spaceparticlesdensity";

    private final TagParser<Double> doubleParser;

    public LightPropertiesTagParser(final TagParser<Double> doubleParser) {
        this.doubleParser = doubleParser;
    }

    @Override
    public LightPropertiesDefinition parse(Node node) {
        final Map<String, Node> nodes = XmlParseUtils.convertToMap(node.getChildNodes());

        final Double illuminanceAmountMax =
                XmlParseUtils.processNodeOptional(nodes, TAG_ILLUMINANCE_AMOUNT_MAX, doubleParser::parse).orElse(null);
        final Double spaceParticlesDensity =
                XmlParseUtils.processNodeOptional(nodes, TAG_SPACE_PARTICLES_DENSITY, doubleParser::parse).orElse(null);

        return new LightPropertiesDefinition(illuminanceAmountMax, spaceParticlesDensity);
    }

}

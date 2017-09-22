package com.lonebytesoft.hamster.raytracing.app.builder.parser.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.TagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.parser.BooleanTagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.parser.ColorTagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.parser.DoubleTagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.parser.VectorTagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.LayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.parser.LayerTagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.parser.TexturedLayerTagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.parser.LightTagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.ScreenDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.pixelcolor.PixelColoringDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.parser.PixelColoringTagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.parser.SceneTagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.parser.ScreenTagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.parser.BareBallTagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.parser.BareOrthotopeTagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.parser.ShapeTagParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.validator.Validator;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.validator.ValidatorImpl;
import com.lonebytesoft.hamster.raytracing.color.Color;

public class XmlParserFactory {

    public XmlParser obtainXmlParser() {
        final TagParser<Boolean> booleanParser = new BooleanTagParser();
        final TagParser<Color> colorParser = new ColorTagParser();
        final TagParser<Double> doubleParser = new DoubleTagParser();
        final TagParser<VectorDefinition> vectorParser = new VectorTagParser(doubleParser);

        final TagParser<LayerDefinition> texturedParser = new TexturedLayerTagParser(doubleParser, colorParser);
        final TagParser<LayerDefinition> layerParser = new LayerTagParser(doubleParser, colorParser, texturedParser);

        final TagParser<ShapeDefinition> ballParser = new BareBallTagParser(doubleParser, vectorParser);
        final TagParser<ShapeDefinition> orthotopeParser = new BareOrthotopeTagParser(booleanParser, vectorParser);
        final TagParser<ShapeDefinition> shapeParser = new ShapeTagParser(ballParser, orthotopeParser, layerParser);

        final TagParser<LightDefinition> lightParser = new LightTagParser(doubleParser, vectorParser);

        final TagParser<PixelColoringDefinition> pixelColoringParser = new PixelColoringTagParser(doubleParser, colorParser);

        final TagParser<ScreenDefinition> screenParser = new ScreenTagParser(vectorParser, shapeParser, pixelColoringParser);

        final TagParser<SceneDefinition> sceneParser = new SceneTagParser(
                colorParser, vectorParser, screenParser, shapeParser, lightParser);

        final Validator validator = new ValidatorImpl();

        return new XmlParser(sceneParser, validator);
    }

}

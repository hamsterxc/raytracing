package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;

public class ImportsDetailedBuilder implements ExpressionBuilder<SceneDefinition> {

    @Override
    public String build(SceneDefinition definition) {
        return
                "import com.lonebytesoft.hamster.raytracing.app.CheckersTexture;\n" +
                "import com.lonebytesoft.hamster.raytracing.color.Color;\n" +
                "import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates1d;\n" +
                "import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates2d;\n" +
                "import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates3d;\n" +
                "import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates4d;\n" +
                "import com.lonebytesoft.hamster.raytracing.format.writer.BmpWriter;\n" +
                "import com.lonebytesoft.hamster.raytracing.format.writer.PictureWriter;\n" +
                "import com.lonebytesoft.hamster.raytracing.picture.Picture;\n" +
                "import com.lonebytesoft.hamster.raytracing.scene.beholder.BeholderImpl;\n" +
                "import com.lonebytesoft.hamster.raytracing.scene.screen.Screen;\n" +
                "import com.lonebytesoft.hamster.raytracing.scene.screen.ScreenShapeSurfaced;\n" +
                "import com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor.SimplePixelColoringStrategy;\n" +
                "import com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor.SupersamplingPixelColoringStrategy;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.CompoundShape;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.Shape;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.adapter.ReflectingAdapter;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.adapter.RefractingAdapter;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.adapter.SolidColoredAdapter;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.adapter.TexturedAdapter;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.adapter.TransparentAdapter;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.generic.Ball;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.GeneralOrthotope;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.Orthotope;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.light.AmbientLightSource;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.light.ConeLightSource;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.light.PointLightSource;\n" +
                "import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;\n" +
                "import org.slf4j.Logger;\n" +
                "import org.slf4j.LoggerFactory;\n" +
                "import java.io.BufferedOutputStream;\n" +
                "import java.io.FileOutputStream;\n" +
                "import java.util.Arrays;\n";
    }

}

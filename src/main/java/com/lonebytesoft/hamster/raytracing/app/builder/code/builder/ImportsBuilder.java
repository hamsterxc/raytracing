package com.lonebytesoft.hamster.raytracing.app.builder.code.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;

public class ImportsBuilder implements ExpressionBuilder<SceneDefinition> {

    @Override
    public String build(SceneDefinition definition) {
        return
                "import com.lonebytesoft.hamster.raytracing.app.*;\n" +
                "import com.lonebytesoft.hamster.raytracing.color.*;\n" +
                "import com.lonebytesoft.hamster.raytracing.coordinates.*;\n" +
                "import com.lonebytesoft.hamster.raytracing.format.writer.*;\n" +
                "import com.lonebytesoft.hamster.raytracing.picture.*;\n" +
                "import com.lonebytesoft.hamster.raytracing.scene.beholder.*;\n" +
                "import com.lonebytesoft.hamster.raytracing.scene.screen.*;\n" +
                "import com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor.*;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.*;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.adapter.*;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.generic.*;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.*;\n" +
                "import com.lonebytesoft.hamster.raytracing.shape.light.*;\n" +
                "import com.lonebytesoft.hamster.raytracing.util.math.*;\n" +
                "import org.slf4j.Logger;\n" +
                "import org.slf4j.LoggerFactory;\n" +
                "import java.io.BufferedOutputStream;\n" +
                "import java.io.FileOutputStream;\n" +
                "import java.util.Arrays;\n";
    }

}

package com.lonebytesoft.hamster.raytracing.app;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates2d;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates3d;
import com.lonebytesoft.hamster.raytracing.format.BmpWriter;
import com.lonebytesoft.hamster.raytracing.format.PictureWriter;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import com.lonebytesoft.hamster.raytracing.scene.beholder.BeholderImpl;
import com.lonebytesoft.hamster.raytracing.scene.screen.Screen;
import com.lonebytesoft.hamster.raytracing.scene.screen.ScreenShapeSurfaced;
import com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor.SupersamplingPixelColoringStrategy;
import com.lonebytesoft.hamster.raytracing.shape.CompoundShape;
import com.lonebytesoft.hamster.raytracing.shape.Shape;
import com.lonebytesoft.hamster.raytracing.shape.adapter.ReflectingAdapter;
import com.lonebytesoft.hamster.raytracing.shape.adapter.SolidColoredAdapter;
import com.lonebytesoft.hamster.raytracing.shape.adapter.TexturedAdapter;
import com.lonebytesoft.hamster.raytracing.shape.generic.Ball;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.GeneralOrthotope;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.Orthotope;
import com.lonebytesoft.hamster.raytracing.shape.light.PointLightSource;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class Example3d {

    private static final Logger logger = LoggerFactory.getLogger(Example3d.class);

    private static final String OUTPUT_FILE_NAME = "3d.bmp";

    public static void main(String[] args) throws Exception {
        logger.info("Start");

        final Coordinates3d eye = new Coordinates3d(0, 0, 0);
        final Screen<Coordinates3d, Coordinates2d> screen = new ScreenShapeSurfaced<>(
                new GeneralOrthotope<>(new Coordinates3d(100, -100, -100),
                        Arrays.asList(new Coordinates3d(0, 200, 0), new Coordinates3d(0, 0, 200)),
                        false),
                new Coordinates2d(1.0, 1.0),
                new Coordinates2d(0.0, 0.0),
                new Coordinates2d(1024, 1024),
                new SupersamplingPixelColoringStrategy<>(3, Color.BLACK)
        );
        final BeholderImpl<Coordinates3d, Coordinates2d> beholder = new BeholderImpl<>(eye, screen, Color.BLACK);

        beholder.addShape(generatePlane());
        beholder.addShape(generateCube());
        beholder.addShape(generateBallLeft());
        beholder.addShape(generateBallRight());

        final PointLightSource<Coordinates3d> lightSource = new PointLightSource<>(new Coordinates3d(600, 0, 400), 500_000);
        lightSource.setRayTracer(beholder);
        beholder.addLightSource(lightSource);

        final Picture<Coordinates2d> picture = beholder.getPicture();
        final PictureWriter<Coordinates2d> pictureWriter = new BmpWriter();
        pictureWriter.write(picture, new BufferedOutputStream(new FileOutputStream(OUTPUT_FILE_NAME)));

        logger.info("End");
    }

    private static Shape<Coordinates3d> generatePlane() {
        final GeneralOrthotope<Coordinates3d> orthotope = new GeneralOrthotope<>(
                new Coordinates3d(0, 0, -300),
                Arrays.asList(new Coordinates3d(100, 0, 0), new Coordinates3d(0, 100, 0)),
                true
        );
        final CompoundShape<Coordinates3d> shape = new CompoundShape<>(orthotope);
        shape.addLayer(new TexturedAdapter<>(orthotope,
                new CheckersTexture<>(Arrays.asList(2.0, 2.0), Arrays.asList(Color.BLACK, Color.WHITE)),
                new Coordinates2d(0, 0)), 1);
        shape.addLayer(new ReflectingAdapter<>(orthotope), 1);
        return shape;
    }

    private static Shape<Coordinates3d> generateBallLeft() {
        final Ball<Coordinates3d> ball = new Ball<>(new Coordinates3d(500, 500, -100), 200);
        final CompoundShape<Coordinates3d> shape = new CompoundShape<>(ball);
        shape.addLayer(new SolidColoredAdapter<>(Color.GREEN), 1);
        shape.addLayer(new ReflectingAdapter<>(ball), 1);
        return shape;
    }

    private static Shape<Coordinates3d> generateBallRight() {
        final Ball<Coordinates3d> ball = new Ball<>(new Coordinates3d(500, -500, -100), 200);
        final CompoundShape<Coordinates3d> shape = new CompoundShape<>(ball);
        shape.addLayer(new SolidColoredAdapter<>(Color.BLUE), 1);
        shape.addLayer(new ReflectingAdapter<>(ball), 1);
        return shape;
    }

    private static Shape<Coordinates3d> generateCube() {
        final Orthotope<Coordinates3d> cube = new Orthotope<>(new Coordinates3d(600, -200, 200), Arrays.asList(
                rotateXyz(400.0, 0.0, 0.0, Math.PI / 6.0, Math.PI / 6.0, Math.PI / 4.0),
                rotateXyz(0.0, 400.0, 0.0, Math.PI / 6.0, Math.PI / 6.0, Math.PI / 4.0),
                rotateXyz(0.0, 0.0, 400.0, Math.PI / 6.0, Math.PI / 6.0, Math.PI / 4.0)
        ));
        final CompoundShape<Coordinates3d> shape = new CompoundShape<>(cube);
        shape.addLayer(new TexturedAdapter<>(cube, new CheckersTexture<>(
                Arrays.asList(1.0, 0.0),
                Arrays.asList(Color.CYAN, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA)),
                new Coordinates2d(0, 0)), 1);
        shape.addLayer(new ReflectingAdapter<>(cube), 1);
        return shape;
    }

    private static Coordinates3d rotateXyz(final double x, final double y, final double z,
                                           final double angleX, final double angleY, final double angleZ) {
        return GeometryCalculator.rotate(
                GeometryCalculator.rotate(
                    GeometryCalculator.rotate(
                        new Coordinates3d(x, y, z),
                        1, 2, angleX),
                    0, 2, angleY),
                0, 1, angleZ);
    }

}

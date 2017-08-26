package com.lonebytesoft.hamster.raytracing.scene.screen;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.CoordinatesCalculator;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import com.lonebytesoft.hamster.raytracing.picture.PictureMutable;
import com.lonebytesoft.hamster.raytracing.picture.PictureMutableFactory;
import com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor.PixelColoringStrategy;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public abstract class AbstractScreen<S extends Coordinates<S>, F extends Coordinates<F>> implements Screen<S, F> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractScreen.class);

    private final F resolution;
    private final PixelColoringStrategy<F> coloringStrategy;

    protected AbstractScreen(final F resolution, final PixelColoringStrategy<F> coloringStrategy) {
        this.resolution = resolution;
        this.coloringStrategy = coloringStrategy;
    }

    @Override
    public Picture<F> getPicture(Function<S, Color> rayTracer) {
        final PictureMutable<F> picture = PictureMutableFactory.obtainPictureMutable(resolution);
        final ExecutorService executorService = Executors.newWorkStealingPool();

        for(final F pixelCoordinates : CoordinatesCalculator.getWholePoints(
                CoordinatesCalculator.transform(resolution, index -> 0.0), resolution)) {
            executorService.submit(() -> {
                logger.debug("Getting pixel {}", pixelCoordinates);
                final Surfaced<F> pixelBoundaries = getPixelBoundaries(pixelCoordinates);
                picture.setPixelColor(pixelCoordinates, coloringStrategy.getPixelColor(pixelBoundaries, coordinates -> {
                    final S coordinatesSolid = translate(coordinates);
                    return rayTracer.apply(coordinatesSolid);
                }));
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return picture;
    }

    protected abstract Surfaced<F> getPixelBoundaries(F pixelCoordinates);

    protected abstract S translate(F coordinates);

}

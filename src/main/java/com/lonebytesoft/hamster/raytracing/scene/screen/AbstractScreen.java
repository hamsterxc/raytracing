package com.lonebytesoft.hamster.raytracing.scene.screen;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import com.lonebytesoft.hamster.raytracing.picture.PictureMutable;
import com.lonebytesoft.hamster.raytracing.picture.PictureMutableImpl;
import com.lonebytesoft.hamster.raytracing.scene.screen.pixelcolor.PixelColoringStrategy;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public abstract class AbstractScreen<S extends Coordinates, F extends Coordinates> implements Screen<S, F> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractScreen.class);

    private static final long LOG_MESSAGE_COUNT = 10;

    private final F resolution;
    private final PixelColoringStrategy<F> coloringStrategy;
    private final GeometryCalculator<F> geometryCalculator;

    protected AbstractScreen(
            final F resolution,
            final PixelColoringStrategy<F> coloringStrategy,
            final GeometryCalculator<F> geometryCalculator
    ) {
        this.resolution = resolution;
        this.coloringStrategy = coloringStrategy;
        this.geometryCalculator = geometryCalculator;
    }

    @Override
    public Picture<F> getPicture(Function<S, Color> rayTracer) {
        final PictureMutable<F> picture = new PictureMutableImpl<>(geometryCalculator);
        final ExecutorService executorService = Executors.newWorkStealingPool();

        final long count = StreamSupport.stream(getPixels().spliterator(), false).count();
        final ProgressLogger progressLogger = new ProgressLogger(logger, count, LOG_MESSAGE_COUNT);
        final AtomicLong counter = new AtomicLong(0);
        for(final F pixelCoordinates : getPixels()) {
            executorService.submit(() -> {
                logger.trace("Getting pixel {}", pixelCoordinates);

                try {
                    final Surfaced<F> pixelBoundaries = getPixelBoundaries(pixelCoordinates);
                    picture.setPixelColor(pixelCoordinates, coloringStrategy.getPixelColor(pixelBoundaries, coordinates -> {
                        final S coordinatesSolid = translate(coordinates);
                        return rayTracer.apply(coordinatesSolid);
                    }));
                } catch (Exception e) {
                    logger.warn("Failed to calculate pixel color {}: {}", pixelCoordinates, e.getMessage());
                }

                progressLogger.log(counter.incrementAndGet());
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

    private Iterable<F> getPixels() {
        return () -> new WholeCoordinatesIterator<>(
                geometryCalculator.buildVector(index -> 0.0),
                resolution,
                geometryCalculator
        );
    }

    protected abstract Surfaced<F> getPixelBoundaries(F pixelCoordinates);

    protected abstract S translate(F coordinates);

}

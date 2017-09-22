package com.lonebytesoft.hamster.raytracing.app.builder.picture.factory;

import com.lonebytesoft.hamster.raytracing.app.builder.picture.builder.CoordinatesBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.picture.builder.LayerBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.picture.builder.LightSourceBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.picture.builder.PictureBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.picture.builder.PixelColoringStrategyBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.picture.builder.ShapeBuilder;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public class PictureBuilderFactory {

    public <S extends Coordinates<S>, F extends Coordinates<F>> PictureBuilder<S, F> obtainPictureBuilder(
            final int pictureDimensions) {
        final CoordinatesBuilder coordinatesBuilder = new CoordinatesBuilder();
        final PixelColoringStrategyBuilder<F> pixelColoringStrategyBuilder =
                new PixelColoringStrategyBuilder<>(coordinatesBuilder, pictureDimensions);
        final LayerBuilder<S> layerBuilder = new LayerBuilder<>(coordinatesBuilder);
        final ShapeBuilder<S> shapeBuilder = new ShapeBuilder<>(coordinatesBuilder, layerBuilder);
        final LightSourceBuilder<S> lightSourceBuilder = new LightSourceBuilder<>(coordinatesBuilder);

        return new PictureBuilder<>(coordinatesBuilder, pixelColoringStrategyBuilder, shapeBuilder, lightSourceBuilder);
    }

}

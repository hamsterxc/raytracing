package com.lonebytesoft.hamster.raytracing.scene.screen;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.picture.Picture;

import java.util.function.Function;

public interface Screen<S extends Coordinates<S>, F extends Coordinates<F>> {

    Picture<F> getPicture(Function<S, Color> rayTracer);

}

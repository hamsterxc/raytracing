package com.lonebytesoft.hamster.raytracing.scene.screen;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.picture.Picture;

import java.util.function.Function;

public interface Screen<S extends Coordinates, F extends Coordinates> {

    Picture<F> getPicture(Function<S, Color> rayTracer);

}

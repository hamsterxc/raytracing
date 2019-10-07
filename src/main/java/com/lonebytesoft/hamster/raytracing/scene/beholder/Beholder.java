package com.lonebytesoft.hamster.raytracing.scene.beholder;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.picture.Picture;

public interface Beholder<T extends Coordinates> {

    Picture<T> getPicture();

}

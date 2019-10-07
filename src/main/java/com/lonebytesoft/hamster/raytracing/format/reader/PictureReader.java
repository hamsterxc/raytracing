package com.lonebytesoft.hamster.raytracing.format.reader;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.picture.Picture;

import java.io.IOException;
import java.io.InputStream;

public interface PictureReader<T extends Coordinates> {

    Picture<T> read(InputStream inputStream) throws IOException;

}

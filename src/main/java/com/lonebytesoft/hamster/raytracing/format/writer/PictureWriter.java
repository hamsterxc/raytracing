package com.lonebytesoft.hamster.raytracing.format.writer;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.picture.Picture;

import java.io.IOException;
import java.io.OutputStream;

public interface PictureWriter<T extends Coordinates> {

    void write(Picture<T> picture, OutputStream outputStream) throws IOException;

}

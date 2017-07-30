package com.lonebytesoft.hamster.raytracing.picture;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public final class PictureMutableFactory {

    public static <T extends Coordinates<T>> PictureMutable<T> obtainPictureMutable(final T size) {
        switch(size.getDimensions()) {
            case 2:
                return new Picture2dAdapter<>(size);

            default:
                return new PictureGenericImpl<>(size);
        }
    }

}

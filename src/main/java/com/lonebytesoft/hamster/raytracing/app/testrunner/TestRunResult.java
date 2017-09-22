package com.lonebytesoft.hamster.raytracing.app.testrunner;

import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.picture.Picture;

class TestRunResult<T extends Coordinates<T>> {

    private final long time;
    private final TestPictureStatus pictureStatus;
    private final Picture<T> picture;

    public TestRunResult(final long time, final TestPictureStatus pictureStatus, final Picture<T> picture) {
        this.time = time;
        this.pictureStatus = pictureStatus;
        this.picture = picture;
    }

    public long getTime() {
        return time;
    }

    public TestPictureStatus getPictureStatus() {
        return pictureStatus;
    }

    public Picture<T> getPicture() {
        return picture;
    }

}

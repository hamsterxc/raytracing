package com.lonebytesoft.hamster.raytracing.app.testrunner;

class DefinitionRunResult {

    private final String sceneName;
    private final long timeBase;
    private final long timeTarget;
    private final TestPictureStatus pictureStatus;

    public DefinitionRunResult(final String sceneName, final long timeBase, final long timeTarget,
                               final TestPictureStatus pictureStatus) {
        this.sceneName = sceneName;
        this.timeBase = timeBase;
        this.timeTarget = timeTarget;
        this.pictureStatus = pictureStatus;
    }

    public String getSceneName() {
        return sceneName;
    }

    public long getTimeBase() {
        return timeBase;
    }

    public long getTimeTarget() {
        return timeTarget;
    }

    public TestPictureStatus getPictureStatus() {
        return pictureStatus;
    }

}

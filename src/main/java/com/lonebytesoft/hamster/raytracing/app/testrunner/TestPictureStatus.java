package com.lonebytesoft.hamster.raytracing.app.testrunner;

enum TestPictureStatus {

    INVALID("Invalid picture built"),
    DIFFER("Differ"),
    IDENTICAL("Identical"),
    ;

    private final String message;

    TestPictureStatus(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

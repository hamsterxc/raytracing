package com.lonebytesoft.hamster.raytracing.app.helper.commit;

/**
 * Commits that introduced backwards-incompatible changes to code
 * Example usage: code builder factories
 */
public enum Commit {

    INITIALIZED_FRAMEWORK(
            "5ee682daad055912803245eee040e1969a7e9769",
            "Initialized raytracing framework\n" +
            "Orthotope, ball\n" +
            "Surfaced, transparent, reflecting\n" +
            "Ambient, point light source\n" +
            "Supersampling\n" +
            "BMP format"
    ),
    REWORKED_PICTURE_BUILDING(
            "8fa6759f1c028a8b86917851f0933494a4b827e6",
            "Reworked picture building"
    ),
    ADDED_EXAMPLE(
            "66adf906d3b27abaf5240e30cfd4b4a824ac1b96",
            "Added example application"
    ),
    ADDED_REFRACTING(
            "a1769fe0f2f7f1209729b0fdd4b5806eeadeae53",
            "Added Refracting feature for ball and orthotope"
    ),
    ADDED_CONE_LIGHT(
            "857da54e25db77b4f7c6bcf5b31fae4fba074ea5",
            "Added cone light source"
    ),
    ADDED_SURFACED_SCREEN(
            "0ec23bf451c7580fa1d86c7c16128f94ab3d6d09",
            "Refactored ScreenOrthotope - generalized class to accept any Surfaced"
    ),
    REFACTORED_PIXEL_COLORING(
            "1ac41846203b7e5b531d6489fcab41eed7897250",
            "Refactored PixelColoringStrategy implementors"
    ),
    REGRESSION_PERFORMANCE_TESTS(
            "78fcec053a131c3208361d6821d68c4a419a2e65",
            "Added regression and performance tests\n" +
            "Shell script test wrapper\n" +
            "Scene XML definition parser\n" +
            "Picture builder for XML definitions\n" +
            "Code builder for XML definitions\n" +
            "Sample definitions"
    ),

    WORKING_COPY(
            null,
            "<working copy>"
    ),
    ;

    private final String hash;
    private final String message;

    Commit(final String hash) {
        this(hash, null);
    }

    Commit(final String hash, final String message) {
        this.hash = hash;
        this.message = message;
    }

    public String getHash() {
        return hash;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "hash='" + hash + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}

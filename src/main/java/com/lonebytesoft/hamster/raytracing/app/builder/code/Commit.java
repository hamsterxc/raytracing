package com.lonebytesoft.hamster.raytracing.app.builder.code;

import java.util.Objects;

public enum Commit {

    INITIAL(
            "a79c22be2b09411bb86f222c92be89f7eecabc33",
            "Initial commit"),
    ADDED_GITIGNORE(
            "9dde51ce31af29d20235f14b71e1c580eb23b396",
            "Added gitignore"),
    INITIALIZED_FRAMEWORK(
            "5ee682daad055912803245eee040e1969a7e9769",
            "Initialized raytracing framework Orthotope, ball Surfaced, transparent, reflecting Ambient, point light source Supersampling BMP format",
            ADDED_GITIGNORE),
    MERGE_MASTER(
            "cf3695dfe862d9eea13b09857676ba61c6131a22",
            "Merge branch 'master' of https://github.com/hamsterxc/raytracing",
            INITIAL, INITIALIZED_FRAMEWORK),
    REWORKED_PICTURE_BUILDING(
            "8fa6759f1c028a8b86917851f0933494a4b827e6",
            "Reworked picture building",
            MERGE_MASTER),
    EXTENDED_SURFACED(
            "f3a51e59c93659890154d4e572e039df265cbc89",
            "Extended Surfaced interface: added surface to solid coordinates method",
            REWORKED_PICTURE_BUILDING),
    ADDED_EXAMPLE(
            "66adf906d3b27abaf5240e30cfd4b4a824ac1b96",
            "Added example application",
            EXTENDED_SURFACED),
    FIXED_EXAMPLE(
            "0b4bb9c88aad28481cb5b3e76262005a946b41fd",
            "Fixed example application picture resolution, added maven profile to run app",
            ADDED_EXAMPLE),
    ADDED_REFRACTING(
            "a1769fe0f2f7f1209729b0fdd4b5806eeadeae53",
            "Added Refracting feature for ball and orthotope",
            FIXED_EXAMPLE),
    ADDED_CONE_LIGHT(
            "857da54e25db77b4f7c6bcf5b31fae4fba074ea5",
            "Added cone light source",
            ADDED_REFRACTING),
    ADDED_SURFACED_SCREEN(
            "0ec23bf451c7580fa1d86c7c16128f94ab3d6d09",
            "Refactored ScreenOrthotope - generalized class to accept any Surfaced",
            ADDED_CONE_LIGHT),
    REFACTORED_PIXEL_COLORING(
            "1ac41846203b7e5b531d6489fcab41eed7897250",
            "Refactored PixelColoringStrategy implementors",
            ADDED_SURFACED_SCREEN),
    REFACTORED_COORDINATES_CALCULATOR(
            "b952d7233aff0371d6e58d5047b912caccfd5f04",
            "Refactored CoordinatesCalculator methods",
            REFACTORED_PIXEL_COLORING),
    CLEANED_UP_LOG4J_POM(
            "0ef153c68abff788b21eff752106f085006d6a24",
            "Cleaned up log4j config, removed redundant Spring dependencies",
            REFACTORED_COORDINATES_CALCULATOR),

    WORKING_COPY(
            null,
            "<working copy>",
            CLEANED_UP_LOG4J_POM),
    ;

    private final String hash;
    private final String message;
    private final Commit[] parents;

    Commit(final String hash, final String message, final Commit... parents) {
        this.hash = hash;
        this.message = message;
        this.parents = parents;
    }

    public boolean isEqual(final Commit commit) {
        return Objects.equals(hash, commit.hash);
    }

    public boolean isIncluded(final Commit ancestor) {
        if(isEqual(ancestor)) {
            return true;
        }

        for(final Commit parent : parents) {
            if(parent.isEqual(ancestor) || parent.isIncluded(ancestor)) {
                return true;
            }
        }

        return false;
    }

    public boolean isOlder(final Commit descendant) {
        return !isIncluded(descendant);
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

    public static Commit find(final String hash) {
        for(final Commit commit : values()) {
            if(Objects.equals(hash, commit.hash)) {
                return commit;
            }
        }
        throw new RuntimeException("Commit '" + hash + "' not found");
    }

}

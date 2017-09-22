package com.lonebytesoft.hamster.raytracing.app.testrunner;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

class RunResultsLogger {

    private static final String HEADER_SCENE_NAME = "Scene";
    private static final String HEADER_TIME_BASE = "Base, s";
    private static final String HEADER_TIME_TARGET = "Target, s";
    private static final String HEADER_TIME = "Time, s";
    private static final String HEADER_PICTURE_STATUS = "Picture regression";
    private static final int SPACING = 2;

    private static final long NANOS_IN_SECOND = TimeUnit.SECONDS.toNanos(1);

    private final Consumer<String> logger;

    private boolean isVerbose;
    private boolean isComparing;
    private boolean isPictureRegression;

    public RunResultsLogger(final Consumer<String> logger) {
        this.logger = logger;
    }

    public void log(final List<DefinitionRunResult> results) {
        final String lineFormat;
        final Function<DefinitionRunResult, String> lineFunction;
        if(isVerbose) {
            final int sceneNameWidth = calculateMaxWidth(
                    results,
                    result -> result.getSceneName().length(),
                    HEADER_SCENE_NAME) + SPACING;

            final ToIntFunction<Long> timeStringLengthCalculator =
                    time -> (int) Math.floor(Math.log10(time / NANOS_IN_SECOND) + 1) + 4;

            final String pictureRegressionPart = isPictureRegression ? "%s" : "";
            final String headerFormat;
            if(isComparing) {
                final int baseTimeWidth = calculateMaxWidth(
                        results,
                        result -> timeStringLengthCalculator.applyAsInt(result.getTimeBase()),
                        HEADER_TIME_BASE) + SPACING;
                final int targetTimeWidth = calculateMaxWidth(
                        results,
                        result -> timeStringLengthCalculator.applyAsInt(result.getTimeTarget()),
                        HEADER_TIME_TARGET) + SPACING;

                headerFormat =
                        buildFormatPart(sceneNameWidth, "s")
                        + buildFormatPart(baseTimeWidth, "s")
                        + buildFormatPart(targetTimeWidth, "s")
                        + pictureRegressionPart;
                lineFormat =
                        buildFormatPart(sceneNameWidth, "s")
                        + buildFormatPart(baseTimeWidth, ".3f")
                        + buildFormatPart(targetTimeWidth, ".3f")
                        + pictureRegressionPart;
            } else {
                final int timeWidth = calculateMaxWidth(
                        results,
                        result -> timeStringLengthCalculator.applyAsInt(result.getTimeTarget()),
                        HEADER_TIME) + SPACING;
                headerFormat =
                        buildFormatPart(sceneNameWidth, "s")
                        + buildFormatPart(timeWidth, "s")
                        + pictureRegressionPart;
                lineFormat =
                        buildFormatPart(sceneNameWidth, "s")
                        + buildFormatPart(timeWidth, ".3f")
                        + pictureRegressionPart;
            }

            final String header = obtainHeaderString(headerFormat, isComparing, isPictureRegression);
            logger.accept(header);
        } else {
            lineFormat =
                    isComparing
                    ? (isPictureRegression ? "%s\t%.3f\t%.3f\t%s" : "%s\t%.3f\t%.3f")
                    : (isPictureRegression ? "%s\t%.3f\t%s" : "%s\t%.3f");
        }
        lineFunction = obtainLineFunction(lineFormat, isComparing, isPictureRegression);

        results.stream()
                .map(lineFunction)
                .forEach(logger);
    }

    private int calculateMaxWidth(final Collection<DefinitionRunResult> results,
                                  final ToIntFunction<DefinitionRunResult> mapper,
                                  final String header) {
        return Math.max(results.stream()
                .mapToInt(mapper)
                .max()
                .orElse(0), header.length());
    }

    private String buildFormatPart(final int width, final String conversion) {
        return "%-" + width + conversion;
    }

    private String obtainHeaderString(
            final String format, final boolean isComparing, final boolean isPictureRegression) {
        if(isComparing) {
            if(isPictureRegression) {
                return String.format(format,
                        HEADER_SCENE_NAME, HEADER_TIME_BASE, HEADER_TIME_TARGET, HEADER_PICTURE_STATUS);
            } else {
                return String.format(format,
                        HEADER_SCENE_NAME, HEADER_TIME_BASE, HEADER_TIME_TARGET);
            }
        } else {
            if(isPictureRegression) {
                return String.format(format,
                        HEADER_SCENE_NAME, HEADER_TIME, HEADER_PICTURE_STATUS);
            } else {
                return String.format(format,
                        HEADER_SCENE_NAME, HEADER_TIME);
            }
        }
    }

    private Function<DefinitionRunResult, String> obtainLineFunction(
            final String format, final boolean isComparing, final boolean isPictureRegression) {
        if(isComparing) {
            if(isPictureRegression) {
                return result -> String.format(format,
                        result.getSceneName(), (double) result.getTimeBase() / NANOS_IN_SECOND,
                        (double) result.getTimeTarget() / NANOS_IN_SECOND, result.getPictureStatus().getMessage());
            } else {
                return result -> String.format(format,
                        result.getSceneName(), (double) result.getTimeBase() / NANOS_IN_SECOND,
                        (double) result.getTimeTarget() / NANOS_IN_SECOND);
            }
        } else {
            if(isPictureRegression) {
                return result -> String.format(format,
                        result.getSceneName(),
                        (double) result.getTimeTarget() / NANOS_IN_SECOND, result.getPictureStatus().getMessage());
            } else {
                return result -> String.format(format,
                        result.getSceneName(),
                        (double) result.getTimeTarget() / NANOS_IN_SECOND);
            }
        }
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }

    public void setComparing(boolean comparing) {
        isComparing = comparing;
    }

    public void setPictureRegression(boolean pictureRegression) {
        isPictureRegression = pictureRegression;
    }

}

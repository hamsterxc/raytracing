package com.lonebytesoft.hamster.raytracing.app.builder.code;

public class FeatureNotImplementedException extends RuntimeException {

    private final String feature;

    public FeatureNotImplementedException(final String feature) {
        super("Feature '" + feature + "' not implemented");
        this.feature = feature;
    }

    public String getFeature() {
        return feature;
    }

}

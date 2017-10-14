package com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition;

public class LightPropertiesDefinition {

    private final Double illuminanceAmountMax;
    private final Double spaceParticlesDensity;

    public LightPropertiesDefinition(final Double illuminanceAmountMax, final Double spaceParticlesDensity) {
        this.illuminanceAmountMax = illuminanceAmountMax;
        this.spaceParticlesDensity = spaceParticlesDensity;
    }

    public Double getIlluminanceAmountMax() {
        return illuminanceAmountMax;
    }

    public Double getSpaceParticlesDensity() {
        return spaceParticlesDensity;
    }

}

package com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.texture;

import com.lonebytesoft.hamster.raytracing.color.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TexturedCheckersLayerDefinition extends TexturedLayerDefinition {

    private final List<Double> multipliers;
    private final List<Color> colors;

    public TexturedCheckersLayerDefinition(final double weight, final List<Double> multipliers, final List<Color> colors) {
        super(weight);
        this.multipliers = Collections.unmodifiableList(new ArrayList<>(multipliers));
        this.colors = Collections.unmodifiableList(new ArrayList<>(colors));
    }

    @Override
    public TextureType getTextureType() {
        return TextureType.CHECKERS;
    }

    public List<Double> getMultipliers() {
        return multipliers;
    }

    public List<Color> getColors() {
        return colors;
    }

}

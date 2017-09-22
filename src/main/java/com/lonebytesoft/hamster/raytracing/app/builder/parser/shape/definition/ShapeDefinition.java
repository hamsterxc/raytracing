package com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.LayerDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class ShapeDefinition {

    private final Collection<LayerDefinition> layers = new ArrayList<>();

    public abstract ShapeType getType();

    public Collection<LayerDefinition> getLayers() {
        return Collections.unmodifiableCollection(layers);
    }

    public void addLayer(final LayerDefinition layer) {
        layers.add(layer);
    }

}

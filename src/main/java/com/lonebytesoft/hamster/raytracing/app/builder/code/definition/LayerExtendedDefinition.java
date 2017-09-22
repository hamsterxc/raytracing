package com.lonebytesoft.hamster.raytracing.app.builder.code.definition;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.LayerDefinition;

public class LayerExtendedDefinition {

    private final LayerDefinition layerDefinition;
    private final String subjectName;

    public LayerExtendedDefinition(final LayerDefinition layerDefinition, final String subjectName) {
        this.layerDefinition = layerDefinition;
        this.subjectName = subjectName;
    }

    public LayerDefinition getLayerDefinition() {
        return layerDefinition;
    }

    public String getSubjectName() {
        return subjectName;
    }

}

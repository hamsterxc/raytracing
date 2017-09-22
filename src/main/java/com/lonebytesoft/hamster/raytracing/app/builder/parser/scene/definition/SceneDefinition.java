package com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SceneDefinition {

    private final Color colorDefault;
    private final Coordinates<?> eye;
    private final ScreenDefinition screen;
    private final Collection<ShapeDefinition> shapes = new ArrayList<>();
    private final Collection<LightDefinition> lights = new ArrayList<>();

    public SceneDefinition(final Color colorDefault, final Coordinates<?> eye, final ScreenDefinition screen) {
        this.colorDefault = colorDefault;
        this.eye = eye;
        this.screen = screen;
    }

    public int getSpaceDimensions() {
        return eye.getDimensions();
    }

    public int getPictureDimensions() {
        return screen.getResolution().getDimensions();
    }

    public Color getColorDefault() {
        return colorDefault;
    }

    public Coordinates<?> getEye() {
        return eye;
    }

    public ScreenDefinition getScreen() {
        return screen;
    }

    public Collection<ShapeDefinition> getShapes() {
        return Collections.unmodifiableCollection(shapes);
    }

    public void addShape(final ShapeDefinition shape) {
        shapes.add(shape);
    }

    public Collection<LightDefinition> getLights() {
        return Collections.unmodifiableCollection(lights);
    }

    public void addLight(final LightDefinition light) {
        lights.add(light);
    }

}

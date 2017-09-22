package com.lonebytesoft.hamster.raytracing.app.builder.parser.validator;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParserException;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.ConeLightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.LightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.light.definition.PointLightDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.ScreenDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.BallDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.OrthotopeDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;

public class ValidatorImpl implements Validator {

    @Override
    public void validate(final SceneDefinition scene) {
        final int dimensions = scene.getSpaceDimensions();

        final ScreenDefinition screen = scene.getScreen();
        validateScreen(dimensions, screen);

        for(final ShapeDefinition shape : scene.getShapes()) {
            validateShape(dimensions, shape, shape.getType().toString());
        }

        for(final LightDefinition light : scene.getLights()) {
            validateLight(dimensions, light, light.getType().toString());
        }
    }

    private void validateDimensions(final int dimensionsExpected, final Coordinates<?> actual, final String subject) {
        if(actual.getDimensions() != dimensionsExpected) {
            throw new XmlParserException(subject + " dimensions incorrect: expected = " + dimensionsExpected
                    + ", actual = " + actual.getDimensions());
        }
    }

    private void validateScreen(final int dimensionsExpected, final ScreenDefinition screen) {
        validateShape(dimensionsExpected, screen.getShape(), "Screen shape");

        final int dimensionsSurface = screen.getResolution().getDimensions();
        validateDimensions(dimensionsSurface, screen.getFrom(), "Screen shape 'from'");
        validateDimensions(dimensionsSurface, screen.getTo(), "Screen shape 'to'");
    }

    private void validateShape(final int dimensionsExpected, final ShapeDefinition shape, final String subject) {
        switch(shape.getType()) {
            case BALL:
                final BallDefinition ball = (BallDefinition) shape;
                validateDimensions(dimensionsExpected, ball.getCenter(), subject + " center");
                break;

            case ORTHOTOPE:
                final OrthotopeDefinition orthotope = (OrthotopeDefinition) shape;

                validateDimensions(dimensionsExpected, orthotope.getBase(), subject + " base");

                final int count = orthotope.getVectors().size();
                for(int i = 0; i < count; i++) {
                    validateDimensions(dimensionsExpected, orthotope.getVectors().get(i).getCoordinates(),
                            subject + " vector #" + i);
                }
                break;

            default:
                throw new XmlParserException("Unknown shape type: " + shape.getType());
        }
    }

    private void validateLight(final int dimensionsExpected, final LightDefinition light, final String subject) {
        switch(light.getType()) {
            case AMBIENT:
                break;

            case CONE:
                final ConeLightDefinition coneLight = (ConeLightDefinition) light;
                validateDimensions(dimensionsExpected, coneLight.getSource(), subject + " source");
                validateDimensions(dimensionsExpected, coneLight.getDirection().getCoordinates(), subject + " direction");
                break;

            case POINT:
                final PointLightDefinition pointLight = (PointLightDefinition) light;
                validateDimensions(dimensionsExpected, pointLight.getSource(), subject + " source");
                break;

            default:
                throw new XmlParserException("Unknown light type: " + light.getType());
        }
    }

}

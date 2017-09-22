package com.lonebytesoft.hamster.raytracing.app.builder.picture.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.common.definition.VectorRotationDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates1d;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates2d;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates3d;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates4d;
import com.lonebytesoft.hamster.raytracing.util.math.GeometryCalculator;

@SuppressWarnings("unchecked")
public class CoordinatesBuilder {

    public <T extends Coordinates<T>> T buildCoordinates(final int dimensions) {
        return (T) buildCoordinatesGeneric(new CoordinatesZero(dimensions));
    }

    public <T extends Coordinates<T>> T buildCoordinates(final Coordinates<?> coordinates) {
        return (T) buildCoordinatesGeneric(coordinates);
    }

    public <T extends Coordinates<T>> T buildCoordinates(final VectorDefinition vectorDefinition) {
        T vector = (T) buildCoordinatesGeneric(vectorDefinition.getCoordinates());

        for(final VectorRotationDefinition vectorRotationDefinition : vectorDefinition.getRotations()) {
            vector = GeometryCalculator.rotate(
                    vector,
                    vectorRotationDefinition.getAxisFirst(),
                    vectorRotationDefinition.getAxisSecond(),
                    vectorRotationDefinition.getAngle()
            );
        }

        return vector;
    }

    private Coordinates<?> buildCoordinatesGeneric(final Coordinates<?> coordinates) {
        switch(coordinates.getDimensions()) {
            case 1:
                return new Coordinates1d(
                        coordinates.getCoordinate(0)
                );

            case 2:
                return new Coordinates2d(
                        coordinates.getCoordinate(0),
                        coordinates.getCoordinate(1)
                );

            case 3:
                return new Coordinates3d(
                        coordinates.getCoordinate(0),
                        coordinates.getCoordinate(1),
                        coordinates.getCoordinate(2)
                );

            case 4:
                return new Coordinates4d(
                        coordinates.getCoordinate(0),
                        coordinates.getCoordinate(1),
                        coordinates.getCoordinate(2),
                        coordinates.getCoordinate(3)
                );

            default:
                throw new RuntimeException("Unsupported number of dimensions: " + coordinates.getDimensions());
        }
    }

}

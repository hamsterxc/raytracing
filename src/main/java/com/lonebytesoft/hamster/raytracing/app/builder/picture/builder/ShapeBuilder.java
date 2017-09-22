package com.lonebytesoft.hamster.raytracing.app.builder.picture.builder;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.layer.definition.LayerDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.BallDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.OrthotopeDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.shape.definition.ShapeDefinition;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.shape.CompoundShape;
import com.lonebytesoft.hamster.raytracing.shape.Shape;
import com.lonebytesoft.hamster.raytracing.shape.feature.GeometryCalculating;
import com.lonebytesoft.hamster.raytracing.shape.feature.Surfaced;
import com.lonebytesoft.hamster.raytracing.shape.generic.Ball;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.GeneralOrthotope;
import com.lonebytesoft.hamster.raytracing.util.Utils;

import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ShapeBuilder<T extends Coordinates<T>> {

    private final CoordinatesBuilder coordinatesBuilder;
    private final LayerBuilder<T> layerBuilder;

    public ShapeBuilder(final CoordinatesBuilder coordinatesBuilder, final LayerBuilder<T> layerBuilder) {
        this.coordinatesBuilder = coordinatesBuilder;
        this.layerBuilder = layerBuilder;
    }

    public Surfaced<T> buildSurfaced(final ShapeDefinition shapeDefinition) {
        return Utils.cast(buildEntity(shapeDefinition), Surfaced.class);
    }

    public Shape<T> buildShape(final ShapeDefinition shapeDefinition) {
        final Object entity = buildEntity(shapeDefinition);
        final CompoundShape<T> shape = new CompoundShape<>(Utils.cast(entity, GeometryCalculating.class));
        for(final LayerDefinition layerDefinition : shapeDefinition.getLayers()) {
            shape.addLayer(layerBuilder.buildLayer(layerDefinition, entity), layerDefinition.getWeight());
        }
        return shape;
    }

    public Object buildEntity(final ShapeDefinition shapeDefinition) {
        switch(shapeDefinition.getType()) {
            case BALL:
                return buildBall((BallDefinition) shapeDefinition);

            case ORTHOTOPE:
                return buildOrthotope((OrthotopeDefinition) shapeDefinition);

            default:
                throw new RuntimeException("Unknown shape type: " + shapeDefinition.getType());
        }
    }

    private Ball<T> buildBall(final BallDefinition ballDefinition) {
        return new Ball<>(
                (T) coordinatesBuilder.buildCoordinates(ballDefinition.getCenter()),
                ballDefinition.getRadius()
        );
    }

    private GeneralOrthotope<T> buildOrthotope(final OrthotopeDefinition orthotopeDefinition) {
        return new GeneralOrthotope<>(
                coordinatesBuilder.buildCoordinates(orthotopeDefinition.getBase()),
                orthotopeDefinition.getVectors().stream()
                        .map(vector -> (T) coordinatesBuilder.buildCoordinates(vector))
                        .collect(Collectors.toList()),
                orthotopeDefinition.isInfinite()
        );
    }

}

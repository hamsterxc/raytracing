package com.lonebytesoft.hamster.raytracing.coordinates;

public interface Coordinates<T extends Coordinates<T>> extends Iterable<Double> {

    int getDimensions();
    double getCoordinate(int dimension);

    T obtain(double... coordinates);

    default String asString() {
        final int dimensions = getDimensions();

        final StringBuilder stringBuilder = new StringBuilder("Coordinates{");
        stringBuilder.append("dimensions=").append(dimensions);
        stringBuilder.append(",coordinates=[");
        for(int i = 0; i < dimensions; i++) {
            if(i != 0) {
                stringBuilder.append(',');
            }
            stringBuilder.append(getCoordinate(i));
        }

        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

}

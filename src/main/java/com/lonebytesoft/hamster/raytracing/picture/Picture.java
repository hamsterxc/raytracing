package com.lonebytesoft.hamster.raytracing.picture;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates;
import com.lonebytesoft.hamster.raytracing.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public interface Picture<T extends Coordinates<T>> {

    Color getPixelColor(T pixelCoordinates);

    Iterator<T> getAllPixelCoordinates();

    default boolean isEqual(Picture<T> other) {
        final List<T> allMine = new ArrayList<>();
        for(final T coordinates : Utils.obtainIterable(getAllPixelCoordinates())) {
            final Color mine = getPixelColor(coordinates);
            final Color theirs = other.getPixelColor(coordinates);
            if(!Objects.equals(mine, theirs)) {
                return false;
            }
            allMine.add(coordinates);
        }

        final List<T> allTheirs = new ArrayList<>();
        StreamSupport.stream(Utils.obtainIterable(other.getAllPixelCoordinates()).spliterator(), false)
                .forEach(allTheirs::add);

        final int size = allMine.size();
        if(size != allTheirs.size()) {
            return false;
        }

        Collections.sort(allMine);
        Collections.sort(allTheirs);
        for(int i = 0; i < size; i++) {
            if(!Objects.equals(allMine.get(i), allTheirs.get(i))) {
                return false;
            }
        }

        return true;
    }

}

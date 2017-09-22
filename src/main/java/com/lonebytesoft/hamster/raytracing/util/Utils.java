package com.lonebytesoft.hamster.raytracing.util;

import java.util.Iterator;

public final class Utils {

    public static <T> Iterable<T> obtainIterable(final Iterator<T> iterator) {
        return () -> iterator;
    }

    public static <T> T cast(final Object entity, final Class<T> clazz) {
        if(clazz.isAssignableFrom(entity.getClass())) {
            return clazz.cast(entity);
        } else {
            throw new RuntimeException(entity.getClass().getSimpleName() + " is not a " + clazz.getSimpleName());
        }
    }

}

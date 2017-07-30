package com.lonebytesoft.hamster.raytracing.util;

import java.util.Iterator;

public final class Utils {

    public static <T> Iterable<T> obtainIterable(final Iterator<T> iterator) {
        return () -> iterator;
    }

}

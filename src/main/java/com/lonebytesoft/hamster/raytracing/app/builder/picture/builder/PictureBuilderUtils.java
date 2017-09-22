package com.lonebytesoft.hamster.raytracing.app.builder.picture.builder;

final class PictureBuilderUtils {

    public static <T> T cast(final Object entity, final Class<T> clazz) {
        if(clazz.isAssignableFrom(entity.getClass())) {
            return clazz.cast(entity);
        } else {
            throw new RuntimeException(entity.getClass().getSimpleName() + " is not a " + clazz.getSimpleName());
        }
    }

}

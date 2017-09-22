package com.lonebytesoft.hamster.raytracing.format;

public final class BmpFormatCommons {

    public static final int HEADER_SIZE_BMP = 14;
    public static final int HEADER_SIZE_DIB = 40;

    // should be not less than actual number of bytes per pixel read/written
    public static final int BYTES_PER_PIXEL = 3;

    private static final int BYTES_ROW_GRANULARITY = 4;

    public static int calculateFileSize(final int width, final int height) {
        return HEADER_SIZE_BMP + HEADER_SIZE_DIB + calculateBitmapDataSize(width, height);
    }

    public static int calculateBitmapDataSize(final int width, final int height) {
        return height * (width * BYTES_PER_PIXEL + calculatePadding(width));
    }

    public static int calculatePadding(final int width) {
        final int paddingExtra = (width * BYTES_PER_PIXEL) % BYTES_ROW_GRANULARITY;
        return paddingExtra == 0 ? 0 : BYTES_ROW_GRANULARITY - paddingExtra;
    }

}

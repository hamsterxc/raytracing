package com.lonebytesoft.hamster.raytracing.format.reader;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.color.ColorCalculator;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates2d;
import com.lonebytesoft.hamster.raytracing.format.BmpFormatCommons;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import com.lonebytesoft.hamster.raytracing.picture.PictureMutable;
import com.lonebytesoft.hamster.raytracing.picture.PictureMutableFactory;

import java.io.IOException;
import java.io.InputStream;

public class BmpReader implements PictureReader<Coordinates2d> {

    // https://en.wikipedia.org/wiki/BMP_file_format
    @Override
    public Picture<Coordinates2d> read(InputStream inputStream) throws IOException {
        // BMP header
        verify(0x42, readOne(inputStream), 1);
        verify(0x4D, readOne(inputStream), 1);
        final int sizeTotal = readFour(inputStream);
        skip(inputStream, 4);
        verify(BmpFormatCommons.HEADER_SIZE_BMP + BmpFormatCommons.HEADER_SIZE_DIB, readFour(inputStream), 4);

        // DIB header
        verify(BmpFormatCommons.HEADER_SIZE_DIB, readFour(inputStream), 4);
        final int width = readFour(inputStream);
        final int height = readFour(inputStream);
        verify(1, readTwo(inputStream), 2);
        verify(8 * BmpFormatCommons.BYTES_PER_PIXEL, readTwo(inputStream), 2);
        verify(0, readFour(inputStream), 4); // other compression methods not supported
        verify(BmpFormatCommons.calculateBitmapDataSize(width, height), readFour(inputStream), 4);
        skip(inputStream, 8);
        verify(0, readFour(inputStream), 4); // color palette not supported
        skip(inputStream, 4);

        verify(BmpFormatCommons.calculateFileSize(width, height), sizeTotal, 4);

        // Bitmap data
        final PictureMutable<Coordinates2d> picture = PictureMutableFactory.obtainPictureMutable(new Coordinates2d(0, 0));
        final int padding = BmpFormatCommons.calculatePadding(width);
        for(int y = height - 1; y >= 0; y--) {
            for(int x = 0; x < width; x++) {
                final double blue = ColorCalculator.colorByteToComponent(readOne(inputStream));
                final double green = ColorCalculator.colorByteToComponent(readOne(inputStream));
                final double red = ColorCalculator.colorByteToComponent(readOne(inputStream));
                skip(inputStream, BmpFormatCommons.BYTES_PER_PIXEL - 3);

                picture.setPixelColor(new Coordinates2d(x, y), new Color(red, green, blue));
            }
            skip(inputStream, padding);
        }

        return picture;
    }

    private void verify(final int expected, final int actual, final int bytes) throws IOException {
        if(expected != actual) {
            throw new IOException("Unexpected value: expected = " + buildNumberString(expected, bytes)
                    + ", actual = " + buildNumberString(actual, bytes));
        }
    }

    private String buildNumberString(final int number, final int bytes) {
        return String.format("%d (0x%0" + bytes * 2 + "x)", number, number);
    }

    private int readFour(final InputStream inputStream) throws IOException {
        return readOne(inputStream)
                | (readOne(inputStream) << 8)
                | (readOne(inputStream) << 16)
                | (readOne(inputStream) << 24);
    }

    private int readTwo(final InputStream inputStream) throws IOException {
        return readOne(inputStream)
                | (readOne(inputStream) << 8);
    }

    private int readOne(final InputStream inputStream) throws IOException {
        final int read = inputStream.read();
        if(read == -1) {
            throw new IOException("Could not read value from stream");
        } else {
            return read & 0xFF;
        }
    }

    private void skip(final InputStream inputStream, final long bytes) throws IOException {
        long remaining = bytes;
        while(remaining > 0) {
            final long skipped = inputStream.skip(remaining);
            if(skipped > 0) {
                remaining -= skipped;
            } else if(skipped == 0) {
                if(inputStream.read() == -1) {
                    throw new IOException("Unexpected EOF");
                } else {
                    remaining--;
                }
            } else {
                throw new IOException("skip() returned a negative value");
            }
        }
    }

}

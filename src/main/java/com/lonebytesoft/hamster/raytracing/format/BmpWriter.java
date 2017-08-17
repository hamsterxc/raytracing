package com.lonebytesoft.hamster.raytracing.format;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates2d;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.Orthotope;

import java.io.IOException;
import java.io.OutputStream;

public class BmpWriter implements PictureWriter<Coordinates2d> {

    private static final int BYTES_PER_PIXEL = 3;
    private static final int BYTES_ROW_GRANULARITY = 4;

    private static final int HEADER_SIZE_BMP = 14;
    private static final int HEADER_SIZE_DIB = 40;

    // https://en.wikipedia.org/wiki/BMP_file_format
    @Override
    public void write(Picture<Coordinates2d> picture, OutputStream outputStream) throws IOException {
        final Orthotope<Coordinates2d> bounds = PictureFormatUtils.calculateBounds(picture);
        final int width = (int) Math.round(bounds.getVectors().get(0).getX() + 1);
        final int height = (int) Math.round(bounds.getVectors().get(1).getY() + 1);

        final int paddingExtra = (width * BYTES_PER_PIXEL) % BYTES_ROW_GRANULARITY;
        final int padding = paddingExtra == 0 ? 0 : BYTES_ROW_GRANULARITY - paddingExtra;

        // BMP header
        writeOne(0x42, outputStream);
        writeOne(0x4D, outputStream);
        writeFour(HEADER_SIZE_BMP + HEADER_SIZE_DIB + height * (width * BYTES_PER_PIXEL + padding), outputStream);
        writeTwo(0, outputStream);
        writeTwo(0, outputStream);
        writeFour(HEADER_SIZE_BMP + HEADER_SIZE_DIB, outputStream);

        // DIB header
        writeFour(HEADER_SIZE_DIB, outputStream);
        writeFour(width, outputStream);
        writeFour(height, outputStream);
        writeTwo(1, outputStream);
        writeTwo(8 * BYTES_PER_PIXEL, outputStream);
        writeFour(0, outputStream);
        writeFour(height * (width * BYTES_PER_PIXEL + padding), outputStream);
        writeFour(2835, outputStream);
        writeFour(2835, outputStream);
        writeFour(0, outputStream);
        writeFour(0, outputStream);

        // Bitmap data
        for(int y = height - 1; y >= 0; y--) {
            for(int x = 0; x < width; x++) {
                final Color color = picture.getPixelColor(
                        new Coordinates2d(x + bounds.getBase().getX(), y + bounds.getBase().getY()));
                if(color == null) {
                    for(int i = 0; i < BYTES_PER_PIXEL; i++) {
                        writeOne(0, outputStream);
                    }
                } else {
                    writeOne(calculateColorByte(color.getBlue()), outputStream);
                    writeOne(calculateColorByte(color.getGreen()), outputStream);
                    writeOne(calculateColorByte(color.getRed()), outputStream);
                }
            }
            for(int i = 0; i < padding; i++) {
                writeOne(0, outputStream);
            }
        }

        outputStream.flush();
    }

    private void writeFour(final int value, final OutputStream outputStream) throws IOException {
        outputStream.write(value & 0xFF);
        outputStream.write((value >> 8) & 0xFF);
        outputStream.write((value >> 16) & 0xFF);
        outputStream.write((value >> 24) & 0xFF);
    }

    private void writeTwo(final int value, final OutputStream outputStream) throws IOException {
        outputStream.write(value & 0xFF);
        outputStream.write((value >> 8) & 0xFF);
    }

    private void writeOne(final int value, final OutputStream outputStream) throws IOException {
        outputStream.write(value & 0xFF);
    }

    private int calculateColorByte(final double color) {
        final int colorByte = (int)Math.round(color * 256);
        if(colorByte < 0) {
            return 0;
        } else if(colorByte > 0xFF) {
            return 0xFF;
        } else {
            return colorByte;
        }
    }

}

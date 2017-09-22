package com.lonebytesoft.hamster.raytracing.format.writer;

import com.lonebytesoft.hamster.raytracing.color.Color;
import com.lonebytesoft.hamster.raytracing.color.ColorCalculator;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates2d;
import com.lonebytesoft.hamster.raytracing.format.BmpFormatCommons;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import com.lonebytesoft.hamster.raytracing.shape.generic.orthotope.Orthotope;

import java.io.IOException;
import java.io.OutputStream;

public class BmpWriter implements PictureWriter<Coordinates2d> {

    // https://en.wikipedia.org/wiki/BMP_file_format
    @Override
    public void write(Picture<Coordinates2d> picture, OutputStream outputStream) throws IOException {
        final Orthotope<Coordinates2d> bounds = PictureFormatUtils.calculateBounds(picture);
        final int width = (int) Math.round(bounds.getVectors().get(0).getX() + 1);
        final int height = (int) Math.round(bounds.getVectors().get(1).getY() + 1);

        // BMP header
        writeOne(0x42, outputStream);
        writeOne(0x4D, outputStream);
        writeFour(BmpFormatCommons.calculateFileSize(width, height), outputStream);
        writeTwo(0, outputStream);
        writeTwo(0, outputStream);
        writeFour(BmpFormatCommons.HEADER_SIZE_BMP + BmpFormatCommons.HEADER_SIZE_DIB, outputStream);

        // DIB header
        writeFour(BmpFormatCommons.HEADER_SIZE_DIB, outputStream);
        writeFour(width, outputStream);
        writeFour(height, outputStream);
        writeTwo(1, outputStream);
        writeTwo(8 * BmpFormatCommons.BYTES_PER_PIXEL, outputStream);
        writeFour(0, outputStream);
        writeFour(BmpFormatCommons.calculateBitmapDataSize(width, height), outputStream);
        writeFour(2835, outputStream);
        writeFour(2835, outputStream);
        writeFour(0, outputStream);
        writeFour(0, outputStream);

        // Bitmap data
        final int padding = BmpFormatCommons.calculatePadding(width);
        for(int y = height - 1; y >= 0; y--) {
            for(int x = 0; x < width; x++) {
                final Color color = picture.getPixelColor(
                        new Coordinates2d(x + bounds.getBase().getX(), y + bounds.getBase().getY()));
                if(color == null) {
                    writeZeroes(BmpFormatCommons.BYTES_PER_PIXEL, outputStream);
                } else {
                    writeOne(ColorCalculator.colorComponentToByte(color.getBlue()), outputStream);
                    writeOne(ColorCalculator.colorComponentToByte(color.getGreen()), outputStream);
                    writeOne(ColorCalculator.colorComponentToByte(color.getRed()), outputStream);
                    writeZeroes(BmpFormatCommons.BYTES_PER_PIXEL - 3, outputStream);
                }
            }
            writeZeroes(padding, outputStream);
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

    private void writeZeroes(final int count, final OutputStream outputStream) throws IOException {
        for(int i = 0; i < count; i++) {
            writeOne(0, outputStream);
        }
    }

}

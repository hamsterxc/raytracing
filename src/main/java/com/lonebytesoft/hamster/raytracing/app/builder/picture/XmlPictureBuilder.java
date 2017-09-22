package com.lonebytesoft.hamster.raytracing.app.builder.picture;

import com.lonebytesoft.hamster.raytracing.app.builder.parser.XmlParser;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.factory.XmlParserFactory;
import com.lonebytesoft.hamster.raytracing.app.builder.parser.scene.definition.SceneDefinition;
import com.lonebytesoft.hamster.raytracing.app.builder.picture.builder.PictureBuilder;
import com.lonebytesoft.hamster.raytracing.app.builder.picture.factory.PictureBuilderFactory;
import com.lonebytesoft.hamster.raytracing.coordinates.Coordinates2d;
import com.lonebytesoft.hamster.raytracing.format.writer.BmpWriter;
import com.lonebytesoft.hamster.raytracing.picture.Picture;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class XmlPictureBuilder {

    public static void main(String[] args) throws Exception {
        final String inputFileName = System.getProperty("raytracing.xmlpicturebuilder.input");
        final String pictureName = System.getProperty("raytracing.xmlpicturebuilder.picture", "picture.bmp");

        if(inputFileName == null) {
            throw new RuntimeException("Input file not specified");
        }

        final Document document;
        try(final InputStream inputStream = new FileInputStream(inputFileName)) {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
        }

        final XmlParser xmlParser = new XmlParserFactory().obtainXmlParser();
        final SceneDefinition sceneDefinition = xmlParser.parse(document);

        final int pictureDimensions = sceneDefinition.getPictureDimensions();
        switch(pictureDimensions) {
            case 2:
                final PictureBuilder<?, Coordinates2d> pictureBuilder =
                        new PictureBuilderFactory().obtainPictureBuilder(pictureDimensions);
                final Picture<Coordinates2d> picture = pictureBuilder.buildPicture(sceneDefinition);

                try(final BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(pictureName))) {
                    new BmpWriter().write(picture, outputStream);
                }

                break;

            default:
                throw new RuntimeException("Unsupported picture dimensions: " + pictureDimensions);
        }
    }

}

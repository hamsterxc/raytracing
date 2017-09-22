package com.lonebytesoft.hamster.raytracing.app.builder.parser;

public class XmlParserException extends RuntimeException {

    public XmlParserException(final String message) {
        super(message);
    }

    public XmlParserException(final String subject, final String value) {
        this(subject, value, (Throwable) null);
    }

    public XmlParserException(final String subject, final String value, final Throwable cause) {
        super("Could not parse " + subject + " = '" + value + "'", cause);
    }

    public XmlParserException(final String subject, final String value, final String message) {
        this(subject, value, message, null);
    }

    public XmlParserException(final String subject, final String value, final String message, final Throwable cause) {
        super("Could not parse " + subject + " '" + value + "': " + message, cause);
    }

}

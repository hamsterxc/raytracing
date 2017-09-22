package com.lonebytesoft.hamster.raytracing.app.builder.parser;

import org.w3c.dom.Node;

public interface TagParser<T> {

    T parse(Node node);

}

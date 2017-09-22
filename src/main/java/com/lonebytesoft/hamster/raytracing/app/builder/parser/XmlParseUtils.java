package com.lonebytesoft.hamster.raytracing.app.builder.parser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public final class XmlParseUtils {

    public static String[] splitValue(final String value) {
        return value.split("\\s+");
    }

    public static Optional<String> getAttribute(final Node node, final String attributeName) {
        final Node attribute = node.getAttributes().getNamedItem(attributeName);
        if(attribute != null) {
            return Optional.of(attribute.getNodeValue());
        } else {
            return Optional.empty();
        }
    }

    public static Map<String, Node> convertToMap(final NodeList nodeList) {
        final Map<String, Node> result = new HashMap<>();

        final int length = nodeList.getLength();
        for(int i = 0; i < length; i++) {
            final Node node = nodeList.item(i);
            result.put(node.getNodeName().toLowerCase(), node);
        }

        return result;
    }

    public static Stream<Node> convertToStream(final NodeList nodeList) {
        final int length = nodeList.getLength();

        final Node[] nodes = new Node[length];
        for(int i = 0; i < length; i++) {
            nodes[i] = nodeList.item(i);
        }

        return Stream.of(nodes)
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE);
    }

    public static <T> T processNodeMandatory(
            final Map<String, Node> nodes, final String name, final Function<Node, T> processor) {
        if(nodes.containsKey(name)) {
            return processor.apply(nodes.get(name));
        } else {
            throw new XmlParserException(name, "<node '" + name + "'>", "Mandatory node not present");
        }
    }

    public static <T> Optional<T> processNodeOptional(
            final Map<String, Node> nodes, final String name, final Function<Node, T> processor) {
        if(nodes.containsKey(name)) {
            return Optional.of(processor.apply(nodes.get(name)));
        } else {
            return Optional.empty();
        }
    }

    public static void consumeNode(final Map<String, Node> nodes, final String name,
                                   final boolean mandatory, final Consumer<Node> consumer) {
        final Function<Node, Node> processor = node -> {
            consumer.accept(node);
            return node;
        };

        if(mandatory) {
            processNodeMandatory(nodes, name, processor);
        } else {
            processNodeOptional(nodes, name, processor);
        }
    }

}

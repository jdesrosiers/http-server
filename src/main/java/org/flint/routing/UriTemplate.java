package org.flint.routing;

import javaslang.control.Option;
import javaslang.collection.HashMap;
import javaslang.collection.Map;

public class UriTemplate {
    private String uriTemplate;

    public UriTemplate(String uriTemplate) {
        this.uriTemplate = uriTemplate;
    }

    public Option<Map<String, String>> getMatchFor(String uri) {
        if (uriTemplate.equals("*") || uri.equals(uriTemplate)) {
            Map<String, String> matches = HashMap.of();
            return Option.of(matches);
        } else {
            return Option.none();
        }
    }
}

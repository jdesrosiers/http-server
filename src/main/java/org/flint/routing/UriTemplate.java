package org.flint.routing;

import java.util.Arrays;
import java.util.regex.Pattern;

import javaslang.control.Option;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;

public class UriTemplate {
    private String uriTemplate;

    public UriTemplate(String uriTemplate) {
        this.uriTemplate = templateToPattern(uriTemplate);
    }

    private String templateToPattern(String uriTemplate) {
        return "\\Q" + uriTemplate.replaceAll("\\*", "\\\\E.*?\\\\Q") + "\\E";
    }

    public Option<Map<String, String>> getMatchFor(String uri) {
        if (Pattern.matches(uriTemplate, uri)) {
            Map<String, String> matches = HashMap.empty();
            return Option.of(matches);
        } else {
            return Option.none();
        }
    }
}

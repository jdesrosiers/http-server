package org.flint;

import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.control.Option;
import javaslang.Tuple;

public class MediaType {
    private static Map<String, List<String>> mediaType = HashMap.ofEntries(
        Tuple.of("text/plain; charset=utf-8", List.of("txt")),
        Tuple.of("text/html; charset=utf-8", List.of("html", "htm")),
        Tuple.of("image/jpeg", List.of("jpeg", "jpg")),
        Tuple.of("image/png", List.of("png")),
        Tuple.of("image/gif", List.of("gif"))
    );

    public static Option<String> fromExtension(String ext) {
        return mediaType
            .find((value) -> value._2.contains(ext))
            .map((type) -> type._1);
    }
}

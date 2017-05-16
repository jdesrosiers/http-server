package org.flint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.control.Option;
import javaslang.Tuple;

import org.util.FileSystem;

public class MediaType {
    private static Map<String, List<String>> mediaType = HashMap.ofEntries(
        Tuple.of("text/plain", List.of("txt")),
        Tuple.of("text/html", List.of("html", "htm")),
        Tuple.of("image/jpeg", List.of("jpeg", "jpg")),
        Tuple.of("image/png", List.of("png")),
        Tuple.of("image/gif", List.of("gif"))
    );

    public static Option<String> get(String ext) {
        return mediaType
            .find((value) -> value._2.contains(ext))
            .map((type) -> type._1);
    }

    public static Option<String> fromExtension(Path path) {
        String extension = FileSystem.getExtension(path.toString());
        return MediaType.get(extension);
    }

    public static Option<String> fromContentProbe(Path path) throws IOException {
        return Option.of(Files.probeContentType(path));
    }

    public static Option<String> fromPath(Path path) throws IOException {
        return MediaType.fromContentProbe(path)
            .orElse(() -> MediaType.fromExtension(path));
    }
}

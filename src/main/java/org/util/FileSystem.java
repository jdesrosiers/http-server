package org.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystem {
    public static void copyStreams(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int size = in.read(buffer);
        while (size != -1) {
            out.write(buffer, 0, size);
            size = in.read(buffer);
        }
    }

    public static String fileToString(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }

    public static String inputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        copyStreams(is, os);
        return os.toString();
    }
}

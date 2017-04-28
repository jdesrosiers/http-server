package org.util;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;

import javaslang.control.Try;

public class FileSystem {
    public static String getExtension(String path) {
        int extIndex = path.lastIndexOf(".");
        int sepIndex = path.lastIndexOf("/");
        if (extIndex > 0 && sepIndex < extIndex) {
            return path.substring(extIndex + 1);
        } else {
            return "";
        }
    }

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

    public static String eTagFor(Path path) {
        return Try.of(() -> {
            byte[] contents = Files.readAllBytes(path);
            byte[] hash = MessageDigest.getInstance("SHA1").digest(contents);
            return DatatypeConverter.printHexBinary(hash);
        }).get();
    }
}

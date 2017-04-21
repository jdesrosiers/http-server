package org.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
}

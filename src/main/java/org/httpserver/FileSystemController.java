package org.httpserver;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileSystemController {
    private Path rootPath;

    public FileSystemController(Path rootPath) {
        this.rootPath = rootPath;
    }

    public Response get(Request request) {
        Path targetPath = rootPath.resolve("." + request.getRequestTarget()).normalize();

        try {
            Response response = Response.create();
            String extension = FileSystem.getExtension(request.getRequestTarget());
            String contentType = MediaType.fromExtension(extension).getOrElse("application/octet-stream");
            response.setHeader("Content-Type", contentType);
            response.setBody(Files.newInputStream(targetPath));

            return response;
        } catch (IOException ioe) {
            return Response.create(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Response index(Request request) {
        Path targetPath = rootPath.resolve("." + request.getRequestTarget()).normalize();

        try {
            StringBuilder builder = new StringBuilder();
            builder.append("<html>");
            builder.append("  <head>");
            builder.append("    <title>Index - " + request.getRequestTarget() + "</title>");
            builder.append("  </head>");
            builder.append("  <body>");
            builder.append("    <h1>Index - " + request.getRequestTarget() + "</h1>");
            builder.append("    <ul>");

            Files.walk(targetPath)
                .filter(Files::isRegularFile)
                .forEach((filePath) -> {
                    Path href = rootPath.relativize(filePath);
                    Path displayHref = targetPath.relativize(filePath);
                    String item = String.format("<li><a href=\"/%s\">%s</a></li>", href, displayHref);
                    builder.append(item);
                });

            builder.append("    </ul>");
            builder.append("  </body>");
            builder.append("</html>");

            Response response = Response.create();
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            response.setBody(builder.toString());

            return response;
        } catch (IOException ioe) {
            return Response.create(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Response delete(Request request) {
        Path targetPath = rootPath.resolve("." + request.getRequestTarget()).normalize();

        try {
            Files.delete(targetPath);
            return Response.create();
        } catch (IOException ioe) {
            return Response.create(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Response write(Request request) {
        Path targetPath = rootPath.resolve("." + request.getRequestTarget()).normalize();

        try {
            ByteArrayInputStream body = new ByteArrayInputStream(request.getBody().getBytes());
            if (Files.exists(targetPath)) {
                Files.copy(body, targetPath, StandardCopyOption.REPLACE_EXISTING);
                return Response.create();
            } else {
                Files.copy(body, targetPath);
                return Response.create(StatusCode.CREATED);
            }
        } catch (IOException ioe) {
            return Response.create(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }
}

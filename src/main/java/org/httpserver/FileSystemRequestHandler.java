package org.httpserver;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

import javaslang.control.Try;

public class FileSystemRequestHandler extends RequestHandler {
    private Path publicPath;

    public FileSystemRequestHandler(Path publicPath) {
        this.publicPath = publicPath;
    }

    @Override
    protected Response handleGET(Request request) {
        Path targetPath = publicPath.resolve("." + request.getRequestTarget()).normalize();

        if (Files.exists(targetPath)) {
            try {
                if (Files.isRegularFile(targetPath)) {
                    return retrieve(targetPath, request);
                } else if (Files.isDirectory(targetPath)) {
                    return index(targetPath, request);
                } else {
                    return defaultResponse(StatusCode.METHOD_NOT_ALLOWED);
                }
            } catch (IOException ioe) {
                return defaultResponse(StatusCode.INTERNAL_SERVER_ERROR);
            }
        } else {
            return defaultResponse(StatusCode.NOT_FOUND);
        }
    }

    private Response defaultResponse(int statusCode) {
        Response response = Response.create(statusCode);
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setBody(defaultBody(response));

        return response;
    }

    private String defaultBody(Response response) {
        int status = response.getStatusCode();
        String message = String.format("%s %s", status, StatusCode.getMessage(status).get());
        String template = "<html><head><title>%s</title></head><body><h1>%s</h1></body></html>";
        return String.format(template, message, message);
    }

    private Response retrieve(Path targetPath, Request request) throws IOException {
        Response response = Response.create();
        response.setBody(Files.newInputStream(targetPath));

        return response;
    }

    private Response index(Path targetPath, Request request) throws IOException {
        Response response = Response.create();
        response.setHeader("Content-Type", "text/html; charset=utf-8");

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
            .forEach((filePath) -> builder.append(indexItem(filePath, targetPath)));

        builder.append("    </ul>");
        builder.append("  </body>");
        builder.append("</html>");
        response.setBody(builder.toString());

        return response;
    }

    private String indexItem(Path filePath, Path targetPath) {
        return String.format("<li><a href=\"%s\">%s</a></li>", publicPath.relativize(filePath), targetPath.relativize(filePath));
    }
}

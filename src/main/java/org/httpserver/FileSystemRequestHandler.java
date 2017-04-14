package org.httpserver;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

import java.util.stream.Stream;

public class FileSystemRequestHandler extends RequestHandler {
    private String publicPath;

    public FileSystemRequestHandler(String publicPath) {
        this.publicPath = publicPath;
    }

    @Override
    protected Response handleGET(Request request) {
        Path targetPath = Paths.get(publicPath, request.getRequestTarget());

        if (Files.exists(targetPath)) {
            try {
                if (Files.isRegularFile(targetPath)) {
                    return retrieve(targetPath, request);
                } else if (Files.isDirectory(targetPath)) {
                    return index(targetPath, request);
                } else {
                    return methodNotAllowed(request);
                }
            } catch (IOException ioe) {
                return internalServerError(request);
            }
        } else {
            return notFound(request);
        }
    }

    private Response notFound(Request request) {
        Response response = Response.create(StatusCode.NOT_FOUND);
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setBody("<html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1></body></html>");

        return response;
    }

    private Response methodNotAllowed(Request request) {
        Response response = Response.create(StatusCode.METHOD_NOT_ALLOWED);
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setBody("<html><head><title>415 Method Not Allowed</title></head><body><h1>415 Method Not Allowed</h1></body></html>");

        return response;
    }

    private Response internalServerError(Request request) {
        Response response = Response.create(StatusCode.INTERNAL_SERVER_ERROR);
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setBody("<html><head><title>500 Internal Server Error</title></head><body><h1>500 Internal Server Error</h1></body></html>");

        return response;
    }

    private Response retrieve(Path targetPath, Request request) throws IOException {
        Response response = Response.create();
        String body = new String(Files.readAllBytes(targetPath));
        response.setBody(body);

        return response;
    }

    private Response index(Path targetPath, Request request) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("  <head>");
        builder.append("    <title>Index - " + request.getRequestTarget() + "</title>");
        builder.append("  </head>");
        builder.append("  <body>");
        builder.append("    <h1>Index - " + request.getRequestTarget() + "</h1>");
        builder.append("    <ul>");

        Stream<Path> paths = Files.walk(targetPath);
        paths.forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                builder.append("      <li><a href=\"" + filePath.toString().substring(Paths.get(publicPath).toString().length()) + "\">" + targetPath.relativize(filePath) + "</a></li>");
            }
        });

        builder.append("    </ul>");
        builder.append("  </body>");
        builder.append("</html>");

        Response response = Response.create();
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setBody(builder.toString());

        return response;
    }
}

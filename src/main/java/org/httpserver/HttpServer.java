package org.httpserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

class HttpServer {
    public static void main(String[] args) throws IOException {
        Application app = new Application();

        app.get("/", (request) -> {
            Response response;
            Path publicPath = Paths.get("public");
            Path targetPath = publicPath.resolve("." + request.getRequestTarget()).normalize();

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
                        String item = String.format("<li><a href=\"/%s\">%s</a></li>", publicPath.relativize(filePath), targetPath.relativize(filePath));
                        builder.append(item);
                    });

                builder.append("    </ul>");
                builder.append("  </body>");
                builder.append("</html>");

                response = Response.create();
                response.setHeader("Content-Type", "text/html; charset=utf-8");

                response.setBody(builder.toString());
            } catch (IOException ioe) {
                response = Response.create(StatusCode.INTERNAL_SERVER_ERROR);
            }

            return response;
        });

        app.get("/file1", (request) -> {
            Response response;
            Path publicPath = Paths.get("public");
            Path targetPath = publicPath.resolve("." + request.getRequestTarget()).normalize();

            try {
                response = Response.create();
                response.setBody(Files.newInputStream(targetPath));
            } catch (IOException ioe) {
                response = Response.create(StatusCode.INTERNAL_SERVER_ERROR);
            }

            return response;
        });

        app.get("/image.jpeg", (request) -> {
            Response response;
            Path publicPath = Paths.get("public");
            Path targetPath = publicPath.resolve("." + request.getRequestTarget()).normalize();

            try {
                response = Response.create();
                response.setHeader("Content-Type", "image/jpeg");
                response.setBody(Files.newInputStream(targetPath));
            } catch (IOException ioe) {
                response = Response.create(StatusCode.INTERNAL_SERVER_ERROR);
            }

            return response;
        });

        app.get("/image.png", (request) -> {
            Response response;
            Path publicPath = Paths.get("public");
            Path targetPath = publicPath.resolve("." + request.getRequestTarget()).normalize();

            try {
                response = Response.create();
                response.setHeader("Content-Type", "image/png");
                response.setBody(Files.newInputStream(targetPath));
            } catch (IOException ioe) {
                response = Response.create(StatusCode.INTERNAL_SERVER_ERROR);
            }

            return response;
        });

        app.get("/image.gif", (request) -> {
            Response response;
            Path publicPath = Paths.get("public");
            Path targetPath = publicPath.resolve("." + request.getRequestTarget()).normalize();

            try {
                response = Response.create();
                response.setHeader("Content-Type", "image/gif");
                response.setBody(Files.newInputStream(targetPath));
            } catch (IOException ioe) {
                response = Response.create(StatusCode.INTERNAL_SERVER_ERROR);
            }

            return response;
        });

        app.get("/text-file.txt", (request) -> {
            Response response;
            Path publicPath = Paths.get("public");
            Path targetPath = publicPath.resolve("." + request.getRequestTarget()).normalize();

            try {
                response = Response.create();
                response.setHeader("Content-Type", "text/plain");
                response.setBody(Files.newInputStream(targetPath));
            } catch (IOException ioe) {
                response = Response.create(StatusCode.INTERNAL_SERVER_ERROR);
            }

            return response;
        });

        app.get("/form", (request) -> {
            return Response.create();
        });

        app.delete("/form", (request) -> {
            return Response.create();
        });

        app.post("/form", (request) -> {
            return Response.create();
        });

        app.put("/form", (request) -> {
            return Response.create();
        });

        app.get("/redirect", (request) -> {
            Response response = Response.create(StatusCode.FOUND);
            response.setHeader("Location", "/");
            return response;
        });

        app.options("/method_options", (request) -> {
            Response response = Response.create();
            response.setHeader("Allow", "GET,HEAD,POST,OPTIONS,PUT");
            return response;
        });

        app.get("/method_options", (request) -> {
            return Response.create();
        });

        app.put("/method_options", (request) -> {
            return Response.create();
        });

        app.post("/method_options", (request) -> {
            return Response.create();
        });

        app.options("/method_options2", (request) -> {
            Response response = Response.create();
            response.setHeader("Allow", "GET,OPTIONS");
            return response;
        });

        app.get("/method_options2", (request) -> {
            return Response.create();
        });

        app.run(5000);
    }
}

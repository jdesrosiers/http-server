package org.cobspec;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.cobspec.controller.FileSystemController;
import org.httpserver.Application;
import org.httpserver.Response;
import org.httpserver.Request;
import org.httpserver.StatusCode;

class Main {
    public static void main(String[] args) throws IOException {
        Application app = new Application();

        FileSystemController fileSystemController = new FileSystemController(Paths.get("public"));

        app.get("/", fileSystemController::index);
        app.get("/file1", fileSystemController::get);
        app.get("/file2", fileSystemController::get);
        app.get("/image.jpeg", fileSystemController::get);
        app.get("/image.png", fileSystemController::get);
        app.get("/image.gif", fileSystemController::get);
        app.get("/partial_content.txt", fileSystemController::get);
        app.get("/text-file.txt", fileSystemController::get);

        app.get("/patch-content.txt", fileSystemController::get);
        app.patch("/patch-content.txt", (request) -> {
            Response response = fileSystemController.write(request);
            response.setStatusCode(StatusCode.NO_CONTENT);

            return response;
        });

        app.post("/form", (request) -> fileSystemController.write(request, "/form/feline"));
        app.get("/form/feline", fileSystemController::get);
        app.put("/form/feline", fileSystemController::write);
        app.delete("/form/feline", fileSystemController::delete);

        app.post("/noop-form", (request) -> Response.create());

        app.get("/redirect", (request) -> {
            Response response = Response.create(StatusCode.FOUND);
            response.setHeader("Location", "/");
            return response;
        });

        app.get("/method_options", fileSystemController::get);
        app.put("/method_options", fileSystemController::write);

        app.options("/method_options", (request) -> {
            Response response = Response.create();
            response.setHeader("Allow", "GET,HEAD,POST,OPTIONS,PUT");
            return response;
        });

        app.post("/method_options", (request) -> {
            return Response.create();
        });

        app.options("/method_options2", (request) -> {
            Response response = Response.create();
            response.setHeader("Allow", "GET,OPTIONS");
            return response;
        });

        app.get("/method_options2", fileSystemController::get);

        app.get("/logs", (request) -> {
            String auth = request.getHeader("Authorization").getOrElse("");
            if (auth.equals("Basic YWRtaW46aHVudGVyMg==")) {
                return fileSystemController.get(request);
            } else {
                Response response = Response.create(StatusCode.UNAUTHORIZED);
                response.setHeader("WWW-Authenticate", "Basic realm-\"httpserver-logs\"");

                return response;
            }
        });

        app.get("/tea", (request) -> Response.create());
        app.get("/coffee", (request) -> {
            Response response = Response.create(StatusCode.IM_A_TEAPOT);
            response.setBody("I'm a teapot");

            return response;
        });

        app.run(5000);
    }
}
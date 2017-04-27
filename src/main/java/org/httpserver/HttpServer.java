package org.httpserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import javaslang.collection.Map;

import org.core.Application;
import org.core.Response;
import org.core.Request;
import org.core.StatusCode;

import org.httpserver.controller.CookieController;
import org.httpserver.controller.FileSystemController;
import org.httpserver.controller.RedirectController;

class HttpServer {
    public static void main(String[] args) throws IOException {
        Application app = new Application();

        Map<String, String> arguments = Arguments.PARSER.parse(String.join(" ", args));
        int port = Integer.valueOf(arguments.get("p").getOrElse("5000"));
        String directory = arguments.get("d").getOrElse("public");

        FileSystemController fileSystemController = new FileSystemController(Paths.get(directory));

        app.get("/", fileSystemController::index);
        app.get("/file1", fileSystemController::get);
        app.get("/file2", fileSystemController::get);
        app.get("/image.jpeg", fileSystemController::get);
        app.get("/image.png", fileSystemController::get);
        app.get("/image.gif", fileSystemController::get);
        app.get("/partial_content.txt", fileSystemController::get);
        app.get("/text-file.txt", fileSystemController::get);

        app.get("/patch-content.txt", fileSystemController::get);
        app.patch("/patch-content.txt", fileSystemController::patch);

        app.post("/form", (request) -> fileSystemController.write(request, "/form/feline"));
        app.get("/form/feline", fileSystemController::get);
        app.put("/form/feline", fileSystemController::write);
        app.delete("/form/feline", fileSystemController::delete);

        app.post("/noop-form", (request) -> Response.create());

        RedirectController redirectController = new RedirectController();
        app.get("/redirect", (request) -> redirectController.redirect(request, "/"));

        app.get("/method_options", (request) -> Response.create());
        app.post("/method_options", (request) -> Response.create());
        app.put("/method_options", fileSystemController::write);
        app.options("/method_options", (request) -> {
            Response response = Response.create();
            response.setHeader("Allow", "GET,HEAD,POST,OPTIONS,PUT");
            return response;
        });

        app.get("/method_options2", (request) -> Response.create());
        app.options("/method_options2", (request) -> {
            Response response = Response.create();
            response.setHeader("Allow", "GET,OPTIONS");
            return response;
        });

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

        CookieController cookieController = new CookieController();
        app.get("/cookie", cookieController::writeCookie);
        app.get("/eat_cookie", cookieController::useCookie);

        app.run(port);
    }
}

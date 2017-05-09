package org.cobspec;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import javaslang.collection.List;
import javaslang.collection.Map;

import org.flint.Application;
import org.flint.response.Response;

import org.cobspec.controller.CoffeePotController;
import org.cobspec.controller.CookieController;
import org.cobspec.controller.FileSystemController;
import org.cobspec.controller.ParameterDecodeController;
import org.cobspec.controller.RedirectController;

class CobSpec {
    public static void main(String[] args) throws IOException {
        Map<String, String> arguments = Arguments.PARSER.parse(String.join(" ", args));
        int port = Integer.valueOf(arguments.get("p").getOrElse("5000"));
        String directory = arguments.get("d").getOrElse("public");

        LoggerMiddleware loggerMiddleware = new LoggerMiddleware(getLogger());
        Application app = new Application()
            .before(loggerMiddleware::logRequest);

        FileSystemController fileSystemController = new FileSystemController(Paths.get(directory));

        app.get("/", fileSystemController::index);
        app.get("/file1", fileSystemController::get);
        app.get("/file2", fileSystemController::get);
        app.get("/image.jpeg", fileSystemController::get);
        app.get("/image.png", fileSystemController::get);
        app.get("/image.gif", fileSystemController::get);
        app.get("/text-file.txt", fileSystemController::get);

        RangeMiddleware rangeMiddleware = new RangeMiddleware();
        app.get("/partial_content.txt", request -> {
            Response response = fileSystemController.get(request);
            return rangeMiddleware.handleRange(request, response);
        });

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
        app.put("/method_options", (request) -> Response.create());
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

        FileSystemController logsController = new FileSystemController(Paths.get("."));
        List<String> authorizedUsers = List.of("Basic YWRtaW46aHVudGVyMg==");
        AuthorizationMiddleware authenticationMiddleware = new AuthorizationMiddleware(authorizedUsers);
        app.get("/logs", request -> {
            request = authenticationMiddleware.auth(request);
            return logsController.get(request);
        });

        CoffeePotController coffeePotController = new CoffeePotController();
        app.get("/tea", coffeePotController::tea);
        app.get("/coffee", coffeePotController::coffee);

        CookieController cookieController = new CookieController();
        app.get("/cookie", cookieController::writeCookie);
        app.get("/eat_cookie", cookieController::useCookie);

        ParameterDecodeController parameterDecodeController = new ParameterDecodeController();
        app.get("/parameters", parameterDecodeController::run);

        app.run(port);
    }

    private static Logger getLogger() throws IOException {
        FileHandler fileHandler = new FileHandler("logs");
        fileHandler.setFormatter(new SimpleFormatter());

        Logger logger = Logger.getAnonymousLogger();
        logger.addHandler(fileHandler);

        return logger;
    }
}

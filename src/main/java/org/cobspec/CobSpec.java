package org.cobspec;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.control.Option;
import javaslang.control.Try;

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
        Option<Integer> port = getPort(arguments);
        Option<Path> directory = getDirectory(arguments);

        if (directory.isEmpty()) {
            System.out.println("Invalid value for -d option.  Failed to start server.");
        } else if (port.isEmpty()) {
            System.out.println("Invalid value for -p option.  Failed to start server.");
        } else {
            Application app = buildCobSpec(directory.get().toAbsolutePath().normalize());
            app.run(port.get());
        }
    }

    public static Option<Integer> getPort(Map<String, String> arguments) {
        return Try.of(() -> Integer.valueOf(arguments.get("p").getOrElse("5000"))).toOption();
    }

    public static Option<Path> getDirectory(Map<String, String> arguments) {
        Path directory = Paths.get(arguments.get("d").getOrElse("."));
        return Files.exists(directory) && Files.isDirectory(directory) ? Option.of(directory) : Option.none();
    }

    public static Application buildCobSpec(Path directory) throws IOException {
        LoggerMiddleware loggerMiddleware = new LoggerMiddleware(getLogger());
        Application app = new Application()
            .before(loggerMiddleware::logRequest);

        FileSystemController fileSystemController = new FileSystemController(directory);

        RangeMiddleware rangeMiddleware = new RangeMiddleware();
        app.get("/partial_content.txt", fileSystemController::get)
            .after(rangeMiddleware::handleRange);

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
        app.get("/logs", logsController::get)
            .before(authenticationMiddleware::auth);

        CoffeePotController coffeePotController = new CoffeePotController();
        app.get("/tea", coffeePotController::tea);
        app.get("/coffee", coffeePotController::coffee);

        CookieController cookieController = new CookieController();
        app.get("/cookie", cookieController::writeCookie);
        app.get("/eat_cookie", cookieController::useCookie);

        ParameterDecodeController parameterDecodeController = new ParameterDecodeController();
        app.get("/parameters", parameterDecodeController::run);

        app.get("*", fileSystemController::get);

        return app;
    }

    private static Logger getLogger() throws IOException {
        FileHandler fileHandler = new FileHandler("logs");
        fileHandler.setFormatter(new SimpleFormatter());

        Logger logger = Logger.getAnonymousLogger();
        logger.addHandler(fileHandler);

        return logger;
    }
}

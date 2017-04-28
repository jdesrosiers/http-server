package org.core;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import javaslang.collection.Queue;
import javaslang.CheckedFunction1;

import org.jparsec.error.ParserException;

import org.core.parse.Http;
import org.util.FileSystem;

public class Application {
    Queue<Route> routes = Queue.empty();

    public Route match(String method, String uriTemplate, CheckedFunction1<Request, Response> controller) {
        Route route = new Route(method, new UriTemplate(uriTemplate), controller);

        routes = routes.enqueue(route);

        return route;
    }

    public Route get(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match("GET", uriTemplate, controller);
    }

    public Route post(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match("POST", uriTemplate, controller);
    }

    public Route put(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match("PUT", uriTemplate, controller);
    }

    public Route delete(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match("DELETE", uriTemplate, controller);
    }

    public Route options(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match("OPTIONS", uriTemplate, controller);
    }

    public Route patch(String uriTemplate, CheckedFunction1<Request, Response> controller) {
        return match("PATCH", uriTemplate, controller);
    }

    public Server getServer() {
        return new Server(routes.toList());
    }

    public void run(int port) throws IOException {
        try(ServerSocket listener = new ServerSocket(port)) {
            while (true) {
                try(Socket socket = listener.accept()) {
                    InputStreamReader reader = new InputStreamReader(socket.getInputStream());
                    OutputStream os = socket.getOutputStream();
                    PrintWriter out = new PrintWriter(os, true);
                    Response response;

                    try {
                        Request request = Http.request(reader);
                        System.out.println(String.format("%s: %s %s HTTP/1.1", LocalDateTime.now(), request.getMethod(), request.getRequestTarget().toString()));
                        response = getServer().handle(request);
                        writeHttpMessage(response, os);
                    } catch (IOException ioe) {
                        response = Response.create(StatusCode.INTERNAL_SERVER_ERROR);
                        writeHttpMessage(response, os);
                    } catch (ParserException pe) {
                        response = Response.create(StatusCode.BAD_REQUEST);
                        writeHttpMessage(response, os);
                    }
                }
            }
        }
    }

    private void writeHttpMessage(Response response, OutputStream os) throws IOException {
        PrintWriter out = new PrintWriter(os, true);

        int statusCode = response.getStatusCode();
        String statusMessage = StatusCode.getMessage(statusCode).getOrElse("");
        out.println(String.format("HTTP/1.1 %s %s\r", statusCode, statusMessage));

        response.getHeaders()
            .forEach((header, value) -> out.println(String.format("%s: %s\r", header, value)));
        out.println("\r");

        InputStream body = response.getBody();
        if (body.available() > 0) {
            FileSystem.copyStreams(body, os);
            out.println("\r");
        }
    }
}

package org.httpserver;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;

import javaslang.Function1;
import javaslang.collection.Queue;

public class Application {
    Queue<Route> routes = Queue.empty();

    public Route match(String method, String uriTemplate, Function1<Request, Response> controller) {
        Route route = new Route(method, new UriTemplate(uriTemplate), controller);

        routes = routes.enqueue(route);

        return route;
    }

    public Route get(String uriTemplate, Function1<Request, Response> controller) {
        return match("GET", uriTemplate, controller);
    }

    public Route post(String uriTemplate, Function1<Request, Response> controller) {
        return match("POST", uriTemplate, controller);
    }

    public Route put(String uriTemplate, Function1<Request, Response> controller) {
        return match("PUT", uriTemplate, controller);
    }

    public Route delete(String uriTemplate, Function1<Request, Response> controller) {
        return match("DELETE", uriTemplate, controller);
    }

    public Route options(String uriTemplate, Function1<Request, Response> controller) {
        return match("OPTIONS", uriTemplate, controller);
    }

    public Route patch(String uriTemplate, Function1<Request, Response> controller) {
        return match("PATCH", uriTemplate, controller);
    }

    public Server getServer() {
        return new Server(routes.toList());
    }

    public void run(int port) throws IOException {
        ServerSocket listener = new ServerSocket(port);

        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                    InputStreamReader reader = new InputStreamReader(socket.getInputStream());
                    OutputStream os = socket.getOutputStream();
                    PrintWriter out = new PrintWriter(os, true);
                    Response response;

                    try {
                        Request request = new HttpParser(reader).request();
                        response = getServer().handle(request);
                        response.writeHttpMessage(os);
                    } catch (IOException pe) {
                        response = Response.create(StatusCode.INTERNAL_SERVER_ERROR);
                        response.writeHttpMessage(os);
                    } catch (ParseException pe) {
                        response = Response.create(StatusCode.BAD_REQUEST);
                        response.writeHttpMessage(os);
                    } catch (TokenMgrError tme) {
                        response = Response.create(StatusCode.BAD_REQUEST);
                        response.writeHttpMessage(os);
                    }
                } finally {
                    socket.close();
                }
            }
        } finally {
            listener.close();
        }
    }
}

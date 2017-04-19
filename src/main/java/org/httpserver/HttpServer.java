package org.httpserver;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;

class HttpServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer();

        ServerSocket listener = new ServerSocket(5000);
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
                        RequestHandler handler = new FileSystemRequestHandler(Paths.get("public"));
                        response = handler.handle(request);
                        response.writeHttpMessage(os);
                    } catch (IOException ioe) {
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

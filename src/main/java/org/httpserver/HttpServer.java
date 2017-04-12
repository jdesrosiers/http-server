package org.httpserver;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class HttpServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer();

        ServerSocket listener = new ServerSocket(5000);
        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                    InputStreamReader reader = new InputStreamReader(socket.getInputStream());
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    try {
                        Request request = new HttpParser(reader).request();
                        System.out.println(request.toString());
                        out.println("HTTP/1.1 200 OK");
                    } catch (ParseException pe) {
                        out.println("HTTP/1.1 500 Internal Server Error");
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

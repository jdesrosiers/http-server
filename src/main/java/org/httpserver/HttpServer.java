package com.httpserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javaslang.collection.Stream;
import javaslang.control.Try;

class HttpServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer();

        ServerSocket listener = new ServerSocket(5000);
        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    String request = server.captureMessage(in);
                    System.out.println(request);
                    out.println("HTTP/1.1 200 OK");
                } finally {
                    socket.close();
                }
            }
        } finally {
            listener.close();
        }
    }

    public String captureMessage(BufferedReader in) {
        return Stream.continually(() -> Try.of(in::readLine).get())
            .takeWhile((line) -> line.length() > 0)
            .map((line) -> line + "\r\n")
            .fold("", String::concat);
    }
}

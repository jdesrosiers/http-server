package org.flint;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

import javaslang.Function1;
import javaslang.Tuple2;

import org.jparsec.error.ParserException;

import org.flint.parse.Http;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;
import org.util.FileSystem;

public class Server {
    private Function1<Request, Response> requestHandler;

    public Server(Function1<Request, Response> requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void run(int port) throws IOException {
        try(ServerSocket listener = new ServerSocket(port)) {
            System.out.println("Server running at http://localhost:" + port);
            while (true) {
                try(Socket socket = listener.accept()) {
                    InputStreamReader reader = new InputStreamReader(socket.getInputStream());
                    OutputStream os = socket.getOutputStream();

                    handleHttpMessage(reader, os);
                }
            }
        }
    }

    public void handleHttpMessage(Reader reader, OutputStream os) throws IOException {
        try {
            Request request = readRequest(reader);
            Response response = requestHandler.apply(request);
            writeResponse(response, os);
        } catch (ParserException pe) {
            Response response = Response.create(StatusCode.BAD_REQUEST);
            writeResponse(response, os);
        } catch (Throwable t) {
            Response response = Response.create(StatusCode.INTERNAL_SERVER_ERROR);
            writeResponse(response, os);
        }
    }

    private Request readRequest(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);

        Request request = Http.REQUEST_LINE.parse(in.readLine() + "\r\n");

        String line = in.readLine();
        while (line.length() > 0) {
            Tuple2<String, String> header = Http.HEADER_FIELD.parse(line);
            request.setHeader(header._1, header._2);
            line = in.readLine();
        }

        int contentLength = Integer.parseInt(request.getHeader("Content-Length").getOrElse("0"));
        if (contentLength > 0) {
            char[] body = new char[contentLength];
            in.read(body, 0, contentLength);
            request.setBody(new String(body));
        }

        return request;
    }

    private void writeResponse(Response response, OutputStream os) throws IOException {
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

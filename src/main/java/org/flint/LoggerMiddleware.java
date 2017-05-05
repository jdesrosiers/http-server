package org.flint;

import java.time.LocalDateTime;

import org.flint.request.Request;
import org.flint.request.RequestTarget;

public class LoggerMiddleware {
    public static Request logRequest(Request request) {
        LocalDateTime timestamp = LocalDateTime.now();
        String method = request.getMethod();
        RequestTarget requestTarget = request.getRequestTarget();

        String message = String.format("%s: %s %s HTTP/1.1", timestamp, method, requestTarget.toString());

        System.out.println(message);

        return request;
    }
}

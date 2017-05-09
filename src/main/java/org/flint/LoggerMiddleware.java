package org.flint;

import java.util.logging.Logger;

import org.flint.request.Request;
import org.flint.request.RequestTarget;

public class LoggerMiddleware {
    private Logger logger;

    public LoggerMiddleware(Logger logger) {
        this.logger = logger;
    }

    public Request logRequest(Request request) {
        String method = request.getMethod();
        RequestTarget requestTarget = request.getRequestTarget();

        String message = String.format("%s %s HTTP/1.1", method, requestTarget.toString());
        logger.info(message);

        return request;
    }
}

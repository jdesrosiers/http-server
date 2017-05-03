package org.flint.exception;

import org.flint.StatusCode;

public class MethodNotAllowedHttpException extends HttpException {
    public MethodNotAllowedHttpException(String allow) {
        super(StatusCode.METHOD_NOT_ALLOWED);

        setHeader("Allow", allow);
    }

    public MethodNotAllowedHttpException(String allow, String message) {
        super(StatusCode.METHOD_NOT_ALLOWED, message);

        setHeader("Allow", allow);
    }

    public MethodNotAllowedHttpException(String allow, String message, Throwable cause) {
        super(StatusCode.METHOD_NOT_ALLOWED, message, cause);

        setHeader("Allow", allow);
    }

    public MethodNotAllowedHttpException(String allow, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(StatusCode.METHOD_NOT_ALLOWED, message, cause, enableSuppression, writableStackTrace);

        setHeader("Allow", allow);
    }
}

package org.flint.exception;

import org.flint.StatusCode;

public class UnauthorizedHttpException extends HttpException {
    public UnauthorizedHttpException(String challenge) {
        super(StatusCode.UNAUTHORIZED);

        setHeader("WWW-Authenticate", challenge);
    }

    public UnauthorizedHttpException(String challenge, String message) {
        super(StatusCode.UNAUTHORIZED, message);

        setHeader("WWW-Authenticate", challenge);
    }

    public UnauthorizedHttpException(String challenge, String message, Throwable cause) {
        super(StatusCode.UNAUTHORIZED, message, cause);

        setHeader("WWW-Authenticate", challenge);
    }

    public UnauthorizedHttpException(String challenge, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(StatusCode.UNAUTHORIZED, message, cause, enableSuppression, writableStackTrace);

        setHeader("WWW-Authenticate", challenge);
    }
}

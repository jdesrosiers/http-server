package org.flint.exception;

import org.flint.response.StatusCode;

public class BadRequestHttpException extends HttpException {
    public BadRequestHttpException() {
        super(StatusCode.BAD_REQUEST);
    }

    public BadRequestHttpException(String message) {
        super(StatusCode.BAD_REQUEST, message);
    }

    public BadRequestHttpException(String message, Throwable cause) {
        super(StatusCode.BAD_REQUEST, message, cause);
    }

    public BadRequestHttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(StatusCode.BAD_REQUEST, message, cause, enableSuppression, writableStackTrace);
    }

    public BadRequestHttpException(Throwable cause) {
        super(StatusCode.BAD_REQUEST, cause);
    }
}

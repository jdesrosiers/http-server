package org.flint.exception;

import org.flint.response.StatusCode;

public class InternalServerErrorHttpException extends HttpException {
    public InternalServerErrorHttpException() {
        super(StatusCode.INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorHttpException(String message) {
        super(StatusCode.INTERNAL_SERVER_ERROR, message);
    }

    public InternalServerErrorHttpException(String message, Throwable cause) {
        super(StatusCode.INTERNAL_SERVER_ERROR, message, cause);
    }

    public InternalServerErrorHttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(StatusCode.INTERNAL_SERVER_ERROR, message, cause, enableSuppression, writableStackTrace);
    }

    public InternalServerErrorHttpException(Throwable cause) {
        super(StatusCode.INTERNAL_SERVER_ERROR, cause);
    }
}

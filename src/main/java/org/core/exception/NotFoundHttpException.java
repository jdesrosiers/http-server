package org.core.exception;

import org.core.StatusCode;

public class NotFoundHttpException extends HttpException {
    public NotFoundHttpException() {
        super(StatusCode.NOT_FOUND);
    }

    public NotFoundHttpException(String message) {
        super(StatusCode.NOT_FOUND, message);
    }

    public NotFoundHttpException(String message, Throwable cause) {
        super(StatusCode.NOT_FOUND, message, cause);
    }

    public NotFoundHttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(StatusCode.NOT_FOUND, message, cause, enableSuppression, writableStackTrace);
    }
}

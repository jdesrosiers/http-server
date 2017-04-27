package org.core.exception;

import org.core.StatusCode;

public class UnsupportedMediaTypeHttpException extends HttpException {
    public UnsupportedMediaTypeHttpException() {
        super(StatusCode.UNSUPPORTED_MEDIA_TYPE);
    }

    public UnsupportedMediaTypeHttpException(String message) {
        super(StatusCode.UNSUPPORTED_MEDIA_TYPE, message);
    }

    public UnsupportedMediaTypeHttpException(String message, Throwable cause) {
        super(StatusCode.UNSUPPORTED_MEDIA_TYPE, message, cause);
    }

    public UnsupportedMediaTypeHttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(StatusCode.UNSUPPORTED_MEDIA_TYPE, message, cause, enableSuppression, writableStackTrace);
    }
}


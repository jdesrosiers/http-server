package org.core.exception;

import org.core.StatusCode;

public class PreconditionFailedHttpException extends HttpException {
    public PreconditionFailedHttpException() {
        super(StatusCode.PRECONDITION_FAILED);
    }

    public PreconditionFailedHttpException(String message) {
        super(StatusCode.PRECONDITION_FAILED, message);
    }

    public PreconditionFailedHttpException(String message, Throwable cause) {
        super(StatusCode.PRECONDITION_FAILED, message, cause);
    }

    public PreconditionFailedHttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(StatusCode.PRECONDITION_FAILED, message, cause, enableSuppression, writableStackTrace);
    }
}

package org.flint.middleware;

import javaslang.CheckedFunction1;
import javaslang.collection.Queue;

import org.flint.request.Request;

public class BeforeMiddleware {
    private Queue<CheckedFunction1<Request, Request>> handlers;

    public BeforeMiddleware() {
        this.handlers = Queue.empty();
    }

    private BeforeMiddleware(Queue<CheckedFunction1<Request, Request>> handlers) {
        this.handlers = handlers;
    }

    public BeforeMiddleware enqueue(CheckedFunction1<Request, Request> handler) {
        return new BeforeMiddleware(handlers.enqueue(handler));
    }

    public Request applyAll(Request request) throws Throwable {
        return handlers
            .fold(CheckedFunction1.identity(), (f, g) -> f.andThen(g))
            .apply(request);
    }
}

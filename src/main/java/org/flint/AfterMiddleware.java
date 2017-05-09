package org.flint;

import javaslang.CheckedFunction1;
import javaslang.CheckedFunction2;
import javaslang.collection.Queue;
import javaslang.control.Try;

import org.flint.request.Request;
import org.flint.response.Response;

public class AfterMiddleware {
    private Queue<CheckedFunction2<Request, Response, Response>> handlers;

    public AfterMiddleware() {
        this.handlers = Queue.empty();
    }

    private AfterMiddleware(Queue<CheckedFunction2<Request, Response, Response>> handlers) {
        this.handlers = handlers;
    }

    public AfterMiddleware enqueue(CheckedFunction2<Request, Response, Response> handler) {
        return new AfterMiddleware(handlers.enqueue(handler));
    }

    public Response applyMiddleware(Request request, Response response) throws Throwable {
        return handlers
            .map(handler -> Try.of(() -> handler.apply(request)).get())
            .fold(CheckedFunction1.identity(), (f, g) -> f.andThen(g))
            .apply(response);
    }
}

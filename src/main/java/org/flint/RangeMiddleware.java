package org.flint;

import java.io.IOException;

import javaslang.CheckedFunction2;
import javaslang.control.Option;
import javaslang.control.Try;

import org.jparsec.error.ParserException;

import org.flint.range.Range;
import org.flint.request.Request;
import org.flint.response.Response;
import org.flint.response.StatusCode;

public class RangeMiddleware implements CheckedFunction2<Request, Response, Response> {
    public Response apply(Request request, Response response) {
        return Option.of(request)
            .filter((r) -> r.getMethod().equals("GET") && response.getStatusCode() == StatusCode.OK)
            .flatMap((r) -> r.getHeader("Range"))
            .toTry()
            .flatMap((range) -> Try.of(() -> Range.fromHeader(range)))
            .flatMap((range) -> Try.of(() -> range.makePartial(response)))
            .getOrElse(response);
    }
}

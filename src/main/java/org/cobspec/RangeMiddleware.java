package org.cobspec;

import java.io.IOException;

import javaslang.control.Option;
import javaslang.control.Try;

import org.jparsec.error.ParserException;

import org.flint.range.Range;
import org.flint.request.Request;
import org.flint.request.Method;
import org.flint.response.Response;
import org.flint.response.StatusCode;

public class RangeMiddleware {
    public Response handleRange(Request request, Response response) {
        return Option.of(request)
            .filter((r) -> r.getMethod().equals(Method.GET) && response.getStatusCode() == StatusCode.OK)
            .flatMap((r) -> r.getHeader("Range"))
            .toTry()
            .flatMap((range) -> Try.of(() -> Range.fromHeader(range)))
            .flatMap((range) -> Try.of(() -> range.makePartial(response)))
            .getOrElse(response);
    }
}

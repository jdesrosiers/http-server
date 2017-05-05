package org.flint.range;

import java.io.InputStream;
import java.io.IOException;

import javaslang.control.Option;

import org.flint.Response;
import org.flint.StatusCode;

public class StandardByteRange extends SingleByteRange {
    private int from;
    private Option<Integer> to;

    public StandardByteRange(int from, int to) {
        this.from = from;
        this.to = Option.of(to);
    }

    public StandardByteRange(int from) {
        this.from = from;
        this.to = Option.none();
    }

    @Override
    protected String getPartialContent(Response response) throws IOException {
        int length = getTo(response) - from + 1;
        byte[] bytes = new byte[length];
        InputStream in = response.getBody();
        in.skip(from);
        in.read(bytes);
        return new String(bytes);
    }

    @Override
    protected String getContentRangeHeader(Response response) {
        String contentLength = response.getHeader("Content-Length").getOrElse("*");
        return String.format("bytes %s-%s/%s", from, getTo(response), contentLength);
    }

    private int getTo(Response response) {
        return to
            .filter((pos) -> pos < response.getContentLength())
            .getOrElse(() -> response.getContentLength() - 1);
    }
}

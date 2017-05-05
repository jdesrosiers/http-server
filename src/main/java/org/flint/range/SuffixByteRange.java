package org.flint.range;

import java.io.InputStream;
import java.io.IOException;

import javaslang.control.Option;

import org.flint.Response;
import org.flint.StatusCode;

public class SuffixByteRange extends SingleByteRange {
    private int length;

    public SuffixByteRange(int length) {
        this.length = length;
    }

    @Override
    protected String getPartialContent(Response response) throws IOException {
        InputStream in = response.getBody();
        byte[] bytes = new byte[Math.min(length, response.getContentLength())];
        in.skip(getFrom(response));
        in.read(bytes);
        return new String(bytes);
    }

    @Override
    protected String getContentRangeHeader(Response response) {
        int contentLength = Integer.parseInt(response.getHeader("Content-Length").getOrElse("*"));
        return String.format("bytes %s-%s/%s", getFrom(response), contentLength - 1, contentLength);
    }

    private int getFrom(Response response) {
        return Option.of(response.getContentLength() - length)
            .filter(l -> l >= 0)
            .getOrElse(0);
    }
}

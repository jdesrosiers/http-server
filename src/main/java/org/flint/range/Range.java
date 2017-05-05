package org.flint.range;

import java.io.IOException;

import javaslang.collection.List;

import org.flint.parse.RangeRequests;
import org.flint.response.Response;

public abstract class Range {
    public abstract Response makePartial(Response response) throws IOException;
    protected abstract String getPartialContent(Response response) throws IOException;
    protected abstract String getContentRangeHeader(Response response);

    public static ByteRange createByteRange(List<SingleByteRange> ranges) {
        return ranges.head();
    }

    public static Range fromHeader(String header) {
        return RangeRequests.RANGE.parse(header);
    }
}

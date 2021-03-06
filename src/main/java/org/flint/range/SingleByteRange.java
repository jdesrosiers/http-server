package org.flint.range;

import java.io.IOException;

import org.flint.response.Response;
import org.flint.response.StatusCode;

public abstract class SingleByteRange extends ByteRange {
    @Override
    public Response makePartial(Response response) throws IOException {
        String partialContent = getPartialContent(response);
        response.setStatusCode(StatusCode.PARTIAL_CONTENT);
        response.setHeader("Content-Range", getContentRangeHeader(response));
        response.setBody(partialContent);

        return response;
    }
}

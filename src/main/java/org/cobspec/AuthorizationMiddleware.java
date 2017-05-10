package org.cobspec;

import javaslang.collection.List;
import org.flint.request.Request;
import org.flint.exception.UnauthorizedHttpException;

public class AuthorizationMiddleware {
    private List<String> authorizedUsers;

    public AuthorizationMiddleware(List<String> authorizedUsers) {
        this.authorizedUsers = authorizedUsers;
    }

    public Request auth(Request request) {
        String auth = request.getHeader("Authorization").getOrElse("");

        if (!authorizedUsers.contains(auth)) {
            throw new UnauthorizedHttpException("Basic realm=\"cobspec-logs\"");
        }

        return request;
    }
}

package org.cobspec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static org.junit.Assert.fail;
import org.junit.Test;

import javaslang.collection.List;

import org.flint.exception.UnauthorizedHttpException;
import org.flint.request.OriginForm;
import org.flint.request.Request;
import org.flint.request.Method;

public class AuthorizationMiddlewareTest {
    private List<String> authorizedUsers = List.of("Basic foo==");

    @Test
    public void itShouldThrowAnUnauthorizedHttpExceptionIfThereIsNoAuthorizationHeader() {
        AuthorizationMiddleware middleware = new AuthorizationMiddleware(authorizedUsers);
        Request request = new Request(Method.GET, new OriginForm("/"));

        try {
            Request result = middleware.auth(request);
            fail();
        } catch (UnauthorizedHttpException uhe) {
        }
    }

    @Test
    public void itShouldThrowAnUnauthorizedHttpExceptionIfTheAuthorizationHeaderIsNotAMatch() {
        AuthorizationMiddleware middleware = new AuthorizationMiddleware(authorizedUsers);
        Request request = new Request(Method.GET, new OriginForm("/"));
        request.setHeader("Authorization", "Basic xxx==");

        try {
            Request result = middleware.auth(request);
            fail();
        } catch (UnauthorizedHttpException uhe) {
        }
    }

    @Test
    public void itShouldReturnTheRequestIfTheAuthorizationHeaderIsAMatch() {
        AuthorizationMiddleware middleware = new AuthorizationMiddleware(authorizedUsers);
        Request request = new Request(Method.GET, new OriginForm("/"));
        request.setHeader("Authorization", "Basic foo==");

        Request result = middleware.auth(request);
        assertThat(result, equalTo(request));
    }

}

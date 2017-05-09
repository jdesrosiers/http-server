package org.cobspec.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

import org.flint.formurlencoded.FormUrlencoded;
import org.flint.request.OriginForm;
import org.flint.request.Request;
import org.flint.response.Response;

public class ParameterDecodeControllerTest {

    @Test
    public void itShouldHandleASimpleQueryParam() {
        ParameterDecodeController controller = new ParameterDecodeController();

        Request request = new Request("GET", new OriginForm("/parmeters", "foo=bar"));
        Response response = controller.run(request);

        assertThat(response.getBodyAsString(), containsString("foo = bar"));
    }

    @Test
    public void itShouldHandleMultipleQueryParams() {
        ParameterDecodeController controller = new ParameterDecodeController();

        Request request = new Request("GET", new OriginForm("/parmeters", "foo=bar&abc=123"));
        Response response = controller.run(request);

        String body = response.getBodyAsString();
        assertThat(body, containsString("foo = bar"));
        assertThat(body, containsString("abc = 123"));
    }

    @Test
    public void itShouldDecodeUrlencodedValues() {
        ParameterDecodeController controller = new ParameterDecodeController();

        Request request = new Request("GET", new OriginForm("/parmeters", "variable_1=Operators%20%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20%26%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F&variable_2=stuff"));
        Response response = controller.run(request);

        String body = response.getBodyAsString();
        assertThat(body, containsString("variable_1 = Operators <, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?"));
        assertThat(body, containsString("variable_2 = stuff"));
    }

}

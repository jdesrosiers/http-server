package org.httpserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import java.io.StringReader;

import javaslang.control.Try;

@RunWith(DataProviderRunner.class)
public class RequestTest {

    @DataProvider
    public static Object[][] dataProviderHttpMethods() {
    	return new Object[][] {
    		{ "GET" },
    		{ "HEAD" },
    		{ "POST" },
    		{ "PUT" },
    		{ "DELETE" },
    		{ "OPTIONS" }
    	};
    }

    @Test
    @UseDataProvider("dataProviderHttpMethods")
    public void itShouldGetTheMethodFromTheMessage(String method) {
        String message = "";
        message += method + " /hello.html HTTP/1.1\r\n";
        message += "User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3\r\n";
        message += "Host: www.example.com\r\n";
        message += "Accept-Language: en, mi\r\n";

        StringReader in = new StringReader(message);

        Request request = Try.of(() -> new HttpParser(in).request()).get();
        assertThat(request.getMethod(), equalTo(method));
    }

    @DataProvider
    public static Object[][] dataProviderUris() {
    	return new Object[][] {
    		{ "/" },
            { "/hello.html" },
            { "/foo/hello.html" },
            { "/foo/bar/hello.html" },
            { "/hello.html?foo=bar" }
    	};
    }

    @Test
    @UseDataProvider("dataProviderUris")
    public void itShouldGetTheRequestTargetFromTheMessage(String uri) {
        String message = "";
        message += "GET " + uri + " HTTP/1.1\r\n";
        message += "User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3\r\n";
        message += "Host: www.example.com\r\n";
        message += "Accept-Language: en, mi\r\n";

        StringReader in = new StringReader(message);

        Request request = Try.of(() -> new HttpParser(in).request()).get();
        assertThat(request.getRequestTarget(), equalTo(uri));
    }

    @Test
    public void itShouldHaveAStringRepresentationThatMatchesTheOriginalRequest() {
        String message = "";
        message += "GET /hello.html HTTP/1.1\r\n";
        message += "User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3\r\n";
        message += "Host: www.example.com\r\n";
        message += "Accept-Language: en, mi\r\n";

        StringReader in = new StringReader(message);

        Request request = Try.of(() -> new HttpParser(in).request()).get();
        assertThat(request.toString(), equalTo("GET /hello.html HTTP/1.1\r\n"));
    }

}
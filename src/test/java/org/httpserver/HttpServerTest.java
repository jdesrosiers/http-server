package com.httpserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class HttpServerTest {

    @Test
    public void captureMessageShouldRetreiveAMessageFromAStream() {
        HttpServer server = new HttpServer();

        String expected = "";
        expected += "GET /hello.txt HTTP/1.1\r\n";
        expected += "User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3\r\n";
        expected += "Host: www.example.com\r\n";
        expected += "Accept-Language: en, mi\r\n";

        String message = expected;
        message += "\r\n";
        message += "DON'T CAPTURE THIS\r\n";

        BufferedReader in = toBufferedReader(message);

        assertThat(server.captureMessage(in), equalTo(expected));
    }

    private BufferedReader toBufferedReader(String message) {
        InputStream in = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));
        return new BufferedReader(new InputStreamReader(in));
    }
}

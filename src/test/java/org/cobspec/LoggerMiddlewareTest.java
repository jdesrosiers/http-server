package org.cobspec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.flint.request.OriginForm;
import org.flint.request.Method;
import org.flint.request.Request;

public class LoggerMiddlewareTest {

    private Logger logger;
    private ByteArrayOutputStream output;
    private StreamHandler streamHandler;

    public LoggerMiddlewareTest() {
        // Suppress default console output
        Logger rootLogger = Logger.getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
    }

    @Before
    public void setUp() {
        this.output = new ByteArrayOutputStream();
        this.logger = Logger.getAnonymousLogger();
        this.streamHandler = new StreamHandler(output, new SimpleFormatter());
        this.logger.addHandler(streamHandler);
    }

    @Test
    public void itShouldLogARequest() {
        LoggerMiddleware middleware = new LoggerMiddleware(logger);

        Request request = new Request(Method.GET, new OriginForm("/foo"));
        middleware.logRequest(request);
        streamHandler.flush();

        assertThat(output.toString(), containsString("GET /foo HTTP/1.1"));
    }

}

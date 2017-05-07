package org.flint.parse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class UriTest {

    @Test
    public void itShouldDecodeAnEmptyString() {
        assertThat(Uri.DECODE.parse(""), equalTo(""));
    }

    @Test
    public void itShouldDecodeAStringWithoutAnyPercentEncodedValues() {
        assertThat(Uri.DECODE.parse("foo"), equalTo("foo"));
    }

    @Test
    public void itShouldDecodeAStringWithPercentEncodedValues() {
        assertThat(Uri.DECODE.parse("foo%20bar"), equalTo("foo bar"));
    }

    @Test
    public void itShouldDecodeAStringWithALotOfPercentEncodedValues() {
        String subject = "Operators%20%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20%26%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F";
        String expected = "Operators <, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?";
        assertThat(Uri.DECODE.parse(subject), equalTo(expected));
    }

    @Test
    public void itShouldEncodeAnEmptyString() {
        assertThat(Uri.ENCODE.parse(""), equalTo(""));
    }

    @Test
    public void itShouldEncodeAStringWithoutAnyPercentEncodedValues() {
        assertThat(Uri.ENCODE.parse("foo"), equalTo("foo"));
    }

    @Test
    public void itShouldEncodeAStringWithPercentEncodedValues() {
        assertThat(Uri.ENCODE.parse("foo bar"), equalTo("foo%20bar"));
    }

    @Test
    public void itShouldEncodeAStringWithALotOfPercentEncodedValues() {
        String subject = "Operators <, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?";
        String expected = "Operators%20%3C%2C%20%3E%2C%20%3D%2C%20%21%3D%3B%20%2B%2C%20-%2C%20%2A%2C%20%26%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F";
        assertThat(Uri.ENCODE.parse(subject), equalTo(expected));
    }

}

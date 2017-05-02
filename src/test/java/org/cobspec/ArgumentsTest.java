package org.cobspec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import javaslang.collection.HashMap;

public class ArgumentsTest {

    @Test
    public void itShouldParseParameters() {
        HashMap<String, String> args = Arguments.PARSER.parse("-p 5000 -d public");

        assertThat(args.get("p").getOrElse("5000"), equalTo("5000"));
        assertThat(args.get("d").getOrElse("public"), equalTo("public"));
    }

    @Test
    public void itShouldParseNoArgs() {
        HashMap<String, String> args = Arguments.PARSER.parse("");

        assertThat(args.get("p").getOrElse("5000"), equalTo("5000"));
        assertThat(args.get("d").getOrElse("public"), equalTo("public"));
    }

    @Test
    public void itShouldParseThePortArg() {
        HashMap<String, String> args = Arguments.PARSER.parse("-p 1234");

        assertThat(args.get("p").getOrElse("5000"), equalTo("1234"));
        assertThat(args.get("d").getOrElse("public"), equalTo("public"));
    }

    @Test
    public void itShouldParseTheDirectoryArg() {
        HashMap<String, String> args = Arguments.PARSER.parse("-d foo/bar");

        assertThat(args.get("p").getOrElse("5000"), equalTo("5000"));
        assertThat(args.get("d").getOrElse("public"), equalTo("foo/bar"));
    }

    @Test
    public void itShouldParseThePortAndDirectoryArgs() {
        HashMap<String, String> args = Arguments.PARSER.parse("-p 1234 -d foo/bar");

        assertThat(args.get("p").getOrElse("5000"), equalTo("1234"));
        assertThat(args.get("d").getOrElse("public"), equalTo("foo/bar"));
    }

    @Test
    public void itShouldParseThePortAndDirectoryArgsInAnyOrder() {
        HashMap<String, String> args = Arguments.PARSER.parse("-d foo/bar -p 1234");

        assertThat(args.get("p").getOrElse("5000"), equalTo("1234"));
        assertThat(args.get("d").getOrElse("public"), equalTo("foo/bar"));
    }

}

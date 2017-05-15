package org.cobspec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import java.nio.file.Paths;

import javaslang.collection.HashMap;
import javaslang.control.Option;
import javaslang.Tuple;

public class CobSpecTest {

    @Test
    public void itShouldUse5000AsTheDefaultPort() {
        HashMap<String, String> arguments = HashMap.empty();
        assertThat(CobSpec.getPort(arguments), equalTo(Option.of(5000)));
    }

    @Test
    public void itShouldUseThePortInThePOption() {
        HashMap<String, String> arguments = HashMap.ofEntries(
            Tuple.of("p", "4000")
        );
        assertThat(CobSpec.getPort(arguments), equalTo(Option.of(4000)));
    }

    @Test
    public void itShouldFailGivenAnInvalidPort() {
        HashMap<String, String> arguments = HashMap.ofEntries(
            Tuple.of("p", "foo")
        );
        assertThat(CobSpec.getPort(arguments), equalTo(Option.none()));
    }

    @Test
    public void itShouldUseTheCurrentDirectoryAsTheDefaultDirectory() {
        HashMap<String, String> arguments = HashMap.empty();
        assertThat(CobSpec.getDirectory(arguments), equalTo(Option.of(Paths.get("."))));
    }

    @Test
    public void itShouldUseTheDirectoryInTheDOption() {
        HashMap<String, String> arguments = HashMap.ofEntries(
            Tuple.of("d", "public")
        );
        assertThat(CobSpec.getDirectory(arguments), equalTo(Option.of(Paths.get("public"))));
    }

    @Test
    public void itShouldFailGivenADirectoryThatDoesntExist() {
        HashMap<String, String> arguments = HashMap.ofEntries(
            Tuple.of("d", "foo")
        );
        assertThat(CobSpec.getDirectory(arguments), equalTo(Option.none()));
    }

    @Test
    public void itShouldFailGivenADirectoryThatIsNotADirectory() {
        HashMap<String, String> arguments = HashMap.ofEntries(
            Tuple.of("d", "file1")
        );
        assertThat(CobSpec.getDirectory(arguments), equalTo(Option.none()));
    }

}

package org.flint.parse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import static org.junit.Assert.fail;
import org.junit.Test;

import org.jparsec.error.ParserException;

import org.flint.range.ByteRange;
import org.flint.range.Range;
import org.flint.range.StandardByteRange;
import org.flint.range.SuffixByteRange;

public class RangeRequestsTest {

    @Test
    public void itShouldAcceptByteRangesInTheRangeHeader() {
        Range range = RangeRequests.RANGE.parse("bytes=0-4");
        assertThat(range, instanceOf(StandardByteRange.class));
    }

    @Test
    public void itShouldAcceptSuffixByteRangesInTheRangeHeader() {
        Range range = RangeRequests.RANGE.parse("bytes=-4");
        assertThat(range, instanceOf(SuffixByteRange.class));
    }

    @Test
    public void itShouldNotAcceptOtherRangesInTheRangeHeader() {
        try {
            RangeRequests.RANGE.parse("foo=0-4");
            fail();
        } catch (ParserException pe) {
        }
    }

    @Test
    public void itShouldNotAcceptInvalidByteRangesInTheRangeHeader() {
        try {
            RangeRequests.RANGE.parse("byte=a-d");
            fail();
        } catch (ParserException pe) {
        }
    }

}

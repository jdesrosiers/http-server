package org.flint.parse;

import javaslang.collection.List;
import javaslang.Function1;
import javaslang.Tuple;
import javaslang.Tuple2;

import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;

import static org.flint.parse.Http.OWS;
import static org.flint.parse.Abnf.DIGIT;
import org.flint.range.ByteRange;
import org.flint.range.Range;
import org.flint.range.SingleByteRange;
import org.flint.range.StandardByteRange;
import org.flint.range.SuffixByteRange;

public class RangeRequests {
    public static final Parser<String> BYTES_UNIT = Scanners.string("bytes").source();
    public static final Parser<Integer> FIRST_BYTE_POS = DIGIT.many1().label("first-byte-pos")
        .source()
        .map(Integer::parseInt);
    public static final Parser<Integer> LAST_BYTE_POS = DIGIT.many1().label("last-byte-pos")
        .source()
        .map(Integer::parseInt);
    public static final Parser<StandardByteRange> BYTE_RANGE_SPEC = Parsers.sequence(
            FIRST_BYTE_POS,
            Scanners.isChar('-'),
            LAST_BYTE_POS.optional(),
            (from, _2, to) -> to == null ? new StandardByteRange(from) : new StandardByteRange(from, to)
        ).label("byte-range-spec");
    public static final Parser<Integer> SUFFIX_LENGTH = DIGIT.many1().label("suffix-length")
        .source()
        .map(Integer::parseInt);
    public static final Parser<SuffixByteRange> SUFFIX_BYTE_RANGE_SPEC = Parsers.sequence(
            Scanners.isChar('-'),
            SUFFIX_LENGTH,
            (_1, length) -> new SuffixByteRange(length)
        ).label("suffix-byte-range-spec");
    public static final Parser<List<SingleByteRange>> BYTE_RANGE_SET = Parsers.sequence(
            Parsers.sequence(Scanners.isChar(','), OWS).many(),
            Parsers.or(BYTE_RANGE_SPEC, SUFFIX_BYTE_RANGE_SPEC),
            Parsers.sequence(
                OWS,
                Scanners.isChar(','),
                Parsers.sequence(
                    OWS,
                    Parsers.or(BYTE_RANGE_SPEC, SUFFIX_BYTE_RANGE_SPEC),
                    (_1, range) -> range
                ).optional()
            ).many().map(List::ofAll),
            (_1, head, tail) -> tail.prepend(head)
        ).label("byte-range-set");
    public static final Parser<ByteRange> BYTE_RANGES_SPECIFIER = Parsers.sequence(
            BYTES_UNIT,
            Scanners.isChar('='),
            BYTE_RANGE_SET,
            (_1, _2, byteRangeSet) -> Range.createByteRange(byteRangeSet)
        ).label("byte-ranges-specifier");
    public static final Parser<Range> OTHER_RANGES_SPECIFIER = Parsers.never(); // Not supported
    public static final Parser<Range> RANGE = Parsers.or(
            BYTE_RANGES_SPECIFIER,
            OTHER_RANGES_SPECIFIER
        ).label("range");
}

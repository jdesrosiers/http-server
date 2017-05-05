package org.flint.range;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.collection.List;

@RunWith(DataProviderRunner.class)
public class RangeTest {

    @DataProvider
    public static Object[][] dataProviderRanges() {
        StandardByteRange range1 = new StandardByteRange(0, 4);
        StandardByteRange range2 = new StandardByteRange(5, 12);

        return new Object[][] {
            { List.of(range1), range1 },
            { List.of(range1, range2), range1 }
        };
    }

    @Test
    @UseDataProvider("dataProviderRanges")
    public void createByteRangeShouldReturnTheFirstRangeInTheList(List<SingleByteRange> ranges, SingleByteRange expectedRange) {
        assertThat(Range.createByteRange(ranges), equalTo(expectedRange));
    }

}

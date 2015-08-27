package io.leishvl.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Point;
import org.junit.Test;

import static io.leishvl.core.util.GeoJsons.POINT_FUZZY_EQUALS;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Tests GeoJson features.
 * @author Erik Torres <ertorser@upv.es>
 */
public class GeoJsonTest {

    @Test
    public void test() {
        System.out.println("GeoJsonTest.test()");
        try {
            final Point point = new Point(-0.3762881000000107d, 39.4699075d);
            assertThat("original point is not null", point, notNullValue());

            final ObjectMapper mapper = new ObjectMapper();
            assertThat("mapper is not null", mapper, notNullValue());

            final String payload = mapper.writeValueAsString(point);
            assertThat("JSON is not empty", trim(payload), allOf(notNullValue(), not(equalTo(""))));

            final Point point2 = mapper.readValue(payload, Point.class);
            assertThat("new point is not null", point2, notNullValue());
            assertThat("new point does not coincide with original", point2, not(equalTo(point)));
            assertThat("new point coincides with original using tolerant comparison", POINT_FUZZY_EQUALS.apply(point, point2), equalTo(true));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("GeoJsonTest.test() failed: " + e.getMessage());
        } finally {
            System.out.println("GeoJsonTest.test() has finished");
        }
    }

}
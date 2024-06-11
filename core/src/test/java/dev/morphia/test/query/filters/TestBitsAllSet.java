package dev.morphia.test.query.filters;

import dev.morphia.test.ServerVersion;

import org.testng.annotations.Test;

import static dev.morphia.query.filters.Filters.bitsAllSet;

public class TestBitsAllSet extends FilterTest {
    @Test
    public void testExample1() {
        testQuery(ServerVersion.ANY, false, true, (query) -> query.filter(
                bitsAllSet("a", new int[] { 1, 5 })));
    }

    @Test
    public void testExample2() {
        testQuery(ServerVersion.ANY, false, true, (query) -> query.filter(
                bitsAllSet("a", 50)));
    }

    @Test
    public void testExample3() {
        testQuery(ServerVersion.ANY, false, true, (query) -> query.filter(
                bitsAllSet("a", new byte[] { 48 })));
    }

}
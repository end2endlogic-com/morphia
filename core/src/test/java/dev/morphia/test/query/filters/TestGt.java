package dev.morphia.test.query.filters;

import org.testng.annotations.Test;

import static dev.morphia.query.filters.Filters.gt;

public class TestGt extends FilterTest {

    /**
     * test data: dev/morphia/test/query/filters/gt/example1
     */
    @Test(testName = "Match Document Fields")
    public void testExample1() {
        testQuery(new QueryTestOptions().removeIds(true),
                (query) -> query.filter(
                        gt("quantity", 20)));
    }
}
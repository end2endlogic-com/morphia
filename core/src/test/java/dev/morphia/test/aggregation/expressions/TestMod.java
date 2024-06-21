package dev.morphia.test.aggregation.expressions;

import dev.morphia.test.ServerVersion;
import dev.morphia.test.TemplatedTestBase;
import dev.morphia.test.util.ActionTestOptions;

import org.testng.annotations.Test;

import static dev.morphia.aggregation.expressions.MathExpressions.mod;
import static dev.morphia.aggregation.stages.Projection.project;

public class TestMod extends TemplatedTestBase {
    /**
     * test data: dev/morphia/test/aggregation/expressions/mod/example1
     * 
     */
    @Test(testName = "main")
    public void testExample1() {
        testPipeline(new ActionTestOptions().serverVersion(ServerVersion.ANY).removeIds(false).orderMatters(true),
                (aggregation) -> aggregation.pipeline(project().include("remainder", mod("$hours", "$tasks"))));
    }

    /**
     * test data: dev/morphia/test/aggregation/expressions/mod/example2
     * 
     */
    @Test(testName = "Negative Dividend")
    public void testExample2() {
        testPipeline(new ActionTestOptions().serverVersion(ServerVersion.ANY).removeIds(false).orderMatters(true),
                (aggregation) -> aggregation.pipeline(project().include("remainder", mod("$dividend", "$divisor"))));
    }

}

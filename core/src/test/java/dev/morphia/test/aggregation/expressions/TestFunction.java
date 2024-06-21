package dev.morphia.test.aggregation.expressions;

import dev.morphia.test.ServerVersion;
import dev.morphia.test.aggregation.AggregationTest;

import org.testng.annotations.Test;

import static dev.morphia.aggregation.expressions.AccumulatorExpressions.function;
import static dev.morphia.aggregation.stages.AddFields.addFields;

public class TestFunction extends AggregationTest {
    /**
     * test data: dev/morphia/test/aggregation/expressions/function/example1
     * 
     */
    @Test(testName = "Example 1: Usage Example")
    public void testExample1() {
        skipActionCheck = true;
        testPipeline(ServerVersion.ANY, false, true,
                (aggregation) -> aggregation.pipeline(addFields().field("isFound", function("""
                        function(name) {
                        return hex_md5(name) == "15b0a220baa16331e8d80e15367677ad"
                        }""", "$name")).field("message", function("""
                        function(name, scores) {
                        let total = Array.sum(scores);
                        return `Hello ${name}.  Your total score is ${total}.`
                        }""", "$name", "$scores"))));
    }

    /**
     * test data: dev/morphia/test/aggregation/expressions/function/example2
     * 
     */
    @Test(testName = "Example 2: Alternative to ``$where``")
    public void testExample2() {
        // example doesn't really apply to morphia
    }

}

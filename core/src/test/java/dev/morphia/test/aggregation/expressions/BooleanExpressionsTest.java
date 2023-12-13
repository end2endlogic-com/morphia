package dev.morphia.test.aggregation.expressions;

import org.testng.annotations.Test;

import static dev.morphia.aggregation.expressions.BooleanExpressions.not;
import static dev.morphia.aggregation.expressions.BooleanExpressions.or;
import static dev.morphia.aggregation.expressions.Expressions.value;

public class BooleanExpressionsTest extends ExpressionsTestBase {

    @Test
    public void testNot() {
        assertAndCheckDocShape("{ $not: [ true ] }", not(value(true)), false);
    }

    @Test
    public void testOr() {
        assertAndCheckDocShape("{ $or: [ true, false ] }", or()
                .add(value(true))
                .add(value(false)),
                true);
    }
}
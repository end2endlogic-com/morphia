package dev.morphia.aggregation.experimental.expressions.impls;

import dev.morphia.Datastore;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static dev.morphia.aggregation.experimental.codecs.ExpressionHelper.document;
import static dev.morphia.aggregation.experimental.codecs.ExpressionHelper.value;

public class MetaExpression extends Expression {

    public MetaExpression() {
        super("$meta");
    }

    @Override
    public void encode(Datastore datastore, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, () -> value(datastore, writer, getOperation(), "textScore", encoderContext));
    }
}
package ru.otus.dataprocessor.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.otus.model.Measurement;

import java.io.IOException;

public class MeasurementDeserializer extends StdDeserializer<Measurement> {

    public MeasurementDeserializer() {
        this(null);
    }

    protected MeasurementDeserializer(Class<Measurement> clazz) {
        super(clazz);
    }

    @Override
    public Measurement deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        final JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        final String name = jsonNode.get("name").asText();
        final double value = jsonNode.get("value").doubleValue();

        return new Measurement(name, value);
    }
}
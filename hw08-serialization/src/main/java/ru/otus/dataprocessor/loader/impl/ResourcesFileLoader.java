package ru.otus.dataprocessor.loader.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.otus.dataprocessor.deserializer.MeasurementDeserializer;
import ru.otus.dataprocessor.exception.FileProcessException;
import ru.otus.dataprocessor.loader.Loader;
import ru.otus.model.Measurement;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private final ObjectMapper mapper;

    public ResourcesFileLoader(String fileName) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Measurement.class, new MeasurementDeserializer());

        this.fileName = fileName;
        this.mapper = new ObjectMapper().registerModule(module);
    }

    @Override
    public List<Measurement> load() {

        if (isNull(fileName)) {
            return new ArrayList<>();
        }
        final ClassLoader classLoader = this.getClass().getClassLoader();

        try (var inputStream = classLoader.getResourceAsStream(fileName)) {
            return mapper.readValue(inputStream, new TypeReference<>() {});
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }
}
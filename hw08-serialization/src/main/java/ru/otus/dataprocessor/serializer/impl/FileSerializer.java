package ru.otus.dataprocessor.serializer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.dataprocessor.exception.FileProcessException;
import ru.otus.dataprocessor.serializer.Serializer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;
    private final ObjectMapper mapper;
    public FileSerializer(String fileName) {
        this.fileName = fileName;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void serialize(Map<String, Double> data) {

        try {
            mapper.writeValue(new File(fileName), data);
        } catch (IOException e) {
            throw new FileProcessException(e.getMessage());
        }
    }
}
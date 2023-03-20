package ru.otus.dataprocessor.serializer.impl;

import com.google.gson.Gson;
import ru.otus.dataprocessor.exception.FileProcessException;
import ru.otus.dataprocessor.serializer.Serializer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class GsonFileSerializer implements Serializer {

    private final String fileName;
    private final Gson gson;
    public GsonFileSerializer(String fileName) {
        this.fileName = fileName;
        this.gson = new Gson();
    }
    @Override
    public void serialize(Map<String, Double> data) {

        try (Writer writer = new FileWriter(fileName)){
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new FileProcessException(e.getMessage());
        }
    }
}
package ru.otus.dataprocessor.loader.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import ru.otus.dataprocessor.exception.FileProcessException;
import ru.otus.dataprocessor.loader.Loader;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class GsonResourcesFileLoader implements Loader {

    private final String fileName;
    private final Gson gson;

    public GsonResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.gson = new Gson();
    }

    @Override
    public List<Measurement> load() {

        if (isNull(fileName)) {
            return new ArrayList<>();
        }
        final ClassLoader classLoader = this.getClass().getClassLoader();

        final InputStream resourceAsStream = classLoader.getResourceAsStream(fileName);
        if (isNull(resourceAsStream)) {
            throw new FileProcessException("Error: resource can not be found");
        }
        try (var reader = new InputStreamReader(resourceAsStream)){
           return gson.fromJson(reader, new TypeToken<List<Measurement>>(){}.getType());
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
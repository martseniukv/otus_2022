package ru.otus.jdbc.mapper.clazz;

import ru.otus.crm.annotion.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> type;
    private final Field idField;
    private final List<Field> fieldWithoutId;
    private final Constructor<T> defaultConstructor;

    public EntityClassMetaDataImpl(Class<T> type) {
        requireNonNull(type);
        this.type = type;
        fieldWithoutId = getFieldWithoutId(type);
        idField = getFieldId(type);
        defaultConstructor = getDefaultConstructor(type);
    }

    @Override
    public String getName() {
        return type.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return defaultConstructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        var fields = new ArrayList<>(fieldWithoutId);
        fields.add(idField);
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldWithoutId;
    }

    private List<Field> getFieldWithoutId(Class<T> type) {
        return stream(type.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }

    private Constructor<T> getDefaultConstructor(Class<T> type) {
        try {
            return type.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Entity must contain default constructor");
        }
    }

    private Field getFieldId(Class<T> type) {
        return stream(type.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Entity must contain field marked '@Id'"));
    }
}
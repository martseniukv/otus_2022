package ru.otus.jdbc.mapper.sql;

import ru.otus.jdbc.mapper.clazz.EntityClassMetaData;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private static final String QUESTION_MARK = "?";
    private static final String SELECT_ALL_SQL = "SELECT %s FROM %s";
    private static final String SELECT_BY_ID = "SELECT %s FROM %s WHERE %s = ?";
    private static final String INSERTION_SQL = "INSERT INTO %s (%s) VALUES (%s)";
    private static final String UPDATE_SQL = "UPDATE %s SET (%s) = (%s) WHERE %s = ?";

    private final String allFields;
    private final String fieldsWithoutId;
    private final String placeHolderWithoutId;

    private final EntityClassMetaData<T> entityClassMetaDataClient;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaDataClient) {
        requireNonNull(entityClassMetaDataClient);
        this.entityClassMetaDataClient = entityClassMetaDataClient;

        allFields = getAllFields(entityClassMetaDataClient);
        fieldsWithoutId = getFieldsWithoutId(entityClassMetaDataClient);
        placeHolderWithoutId = getPlaceHolderWithoutId(entityClassMetaDataClient);
    }

    @Override
    public String getSelectAllSql() {
        return format(SELECT_ALL_SQL, allFields, entityClassMetaDataClient.getName());
    }

    @Override
    public String getSelectByIdSql() {
        final String tableName = entityClassMetaDataClient.getName();
        final String idFiledName = entityClassMetaDataClient.getIdField().getName();
        return format(SELECT_BY_ID, allFields, tableName, idFiledName);
    }

    @Override
    public String getInsertSql() {
        final String tableName = entityClassMetaDataClient.getName();
        return format(INSERTION_SQL, tableName, fieldsWithoutId, placeHolderWithoutId);
    }

    @Override
    public String getUpdateSql() {

        final String tableName = entityClassMetaDataClient.getName();
        final String idFiledName = entityClassMetaDataClient.getIdField().getName();
        return format(UPDATE_SQL, tableName, fieldsWithoutId, placeHolderWithoutId, idFiledName);
    }

    private String getAllFields(EntityClassMetaData<T> entityClassMetaDataClient) {
        return entityClassMetaDataClient.getAllFields().stream()
                .filter(Objects::nonNull)
                .map(Field::getName)
                .collect(Collectors.joining(","));
    }

    private String getFieldsWithoutId(EntityClassMetaData<T> entityClassMetaDataClient) {
        return entityClassMetaDataClient.getFieldsWithoutId().stream()
                .filter(Objects::nonNull)
                .map(Field::getName)
                .collect(Collectors.joining(","));
    }

    private String getPlaceHolderWithoutId(EntityClassMetaData<T> entityClassMetaDataClient) {
        return entityClassMetaDataClient.getFieldsWithoutId().stream()
                .filter(Objects::nonNull)
                .map(m -> QUESTION_MARK)
                .collect(Collectors.joining(","));
    }
}
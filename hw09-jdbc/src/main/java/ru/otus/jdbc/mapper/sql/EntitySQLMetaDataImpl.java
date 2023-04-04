package ru.otus.jdbc.mapper.sql;

import ru.otus.jdbc.mapper.clazz.EntityClassMetaData;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private static final String DELIMITER = ",";

    private final String insertSql;
    private final String updateSql;
    private final String findAllSql;
    private final String findByIdSql;

    private final EntityClassMetaData<T> entityClassMetaDataClient;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaDataClient) {
        requireNonNull(entityClassMetaDataClient);
        this.entityClassMetaDataClient = entityClassMetaDataClient;

        String allFields = getAllFields(entityClassMetaDataClient);
        String fieldsWithoutId = getFieldsWithoutId(entityClassMetaDataClient);
        String placeHolderWithoutId = getPlaceHolderWithoutId(entityClassMetaDataClient);
        insertSql = createInsertSql(fieldsWithoutId, placeHolderWithoutId);
        updateSql = createUpdateSql(fieldsWithoutId, placeHolderWithoutId);
        findAllSql = createFindAllSql(allFields);
        findByIdSql = createFindByIdSql(allFields);
    }

    @Override
    public String getSelectAllSql() {
        return findAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return findByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }

    private String createFindByIdSql(String allFields) {
        final String tableName = entityClassMetaDataClient.getName();
        final String idFiledName = entityClassMetaDataClient.getIdField().getName();
        return format("SELECT %s FROM %s WHERE %s = ?", allFields, tableName, idFiledName);
    }

    private String createFindAllSql(String allFields) {
        return format("SELECT %s FROM %s", allFields, entityClassMetaDataClient.getName());
    }

    private String createInsertSql(String fieldsWithoutId, String placeHolderWithoutId) {
        final String name = entityClassMetaDataClient.getName();
        return format("INSERT INTO %s (%s) VALUES (%s)", name, fieldsWithoutId, placeHolderWithoutId);
    }

    private String createUpdateSql(String fieldsWithoutId, String placeHolderWithoutId) {
        final String tableName = entityClassMetaDataClient.getName();
        final String idName = entityClassMetaDataClient.getIdField().getName();
        return format("UPDATE %s SET (%s) = (%s) WHERE %s = ?", tableName, fieldsWithoutId, placeHolderWithoutId, idName);
    }

    private String getAllFields(EntityClassMetaData<T> entityClassMetaDataClient) {
        return entityClassMetaDataClient.getAllFields().stream()
                .filter(Objects::nonNull)
                .map(Field::getName)
                .collect(Collectors.joining(DELIMITER));
    }

    private String getFieldsWithoutId(EntityClassMetaData<T> entityClassMetaDataClient) {
        return entityClassMetaDataClient.getFieldsWithoutId().stream()
                .filter(Objects::nonNull)
                .map(Field::getName)
                .collect(Collectors.joining(DELIMITER));
    }

    private String getPlaceHolderWithoutId(EntityClassMetaData<T> entityClassMetaDataClient) {
        String questionMark = "?";
        return entityClassMetaDataClient.getFieldsWithoutId().stream()
                .filter(Objects::nonNull)
                .map(m -> questionMark)
                .collect(Collectors.joining(DELIMITER));
    }
}
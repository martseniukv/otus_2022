package ru.otus.jdbc.mapper.sql;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.mapper.clazz.EntityClassMetaData;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), resultSet -> {
            try {
                if (resultSet.next()) {
                    return mapEntity(resultSet);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), new ArrayList<>(), resultSet -> {
            try {
                List<T> entities = new ArrayList<>();
                while (resultSet.next()) {
                    entities.add(mapEntity(resultSet));
                }
                return entities;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElse(new ArrayList<>());
    }

    @Override
    public long insert(Connection connection, T client) {

        final List<Object> params = getParams(entityClassMetaData.getFieldsWithoutId(), client);
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
    }

    @Override
    public void update(Connection connection, T client) {

        final List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        fields.add(entityClassMetaData.getIdField());

        final List<Object> params = getParams(fields, client);
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
    }

    private T mapEntity(ResultSet resultSet) throws SQLException {
        try {
            final T instance = entityClassMetaData.getConstructor().newInstance();

            for (Field field : entityClassMetaData.getAllFields()) {

                field.setAccessible(true);
                field.set(instance, resultSet.getObject(field.getName(), field.getType()));
            }
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getParams(List<Field> fields, T entity) {

        try {
            var params = new ArrayList<>();
            for (var field : fields) {
                field.setAccessible(true);
                params.add(field.get(entity));
            }
            return params;
        } catch (ReflectiveOperationException e) {
            throw new ClassMetaDataReadException(e);
        }
    }
}
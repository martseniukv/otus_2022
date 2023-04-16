package ru.otus.homework.base;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.otus.homework.crm.dbmigrations.MigrationsExecutorFlyway;

import static ru.otus.homework.demo.DbServiceDemo.HIBERNATE_CFG_FILE;


public abstract class AbstractHibernateTest {
    protected SessionFactory sessionFactory;

    protected String dbUrl;
    protected String dbUserName;
    protected String dbPassword;

    private static TestContainersConfig.CustomPostgreSQLContainer CONTAINER;

    @BeforeAll
    public static void init() {
        CONTAINER = TestContainersConfig.CustomPostgreSQLContainer.getInstance();
        CONTAINER.start();
    }

    @AfterAll
    public static void shutdown() {
        CONTAINER.stop();
    }

    @BeforeEach
    protected void setUp() {

        dbUrl = System.getProperty("app.datasource.demo-db.jdbcUrl");
        dbUserName = System.getProperty("app.datasource.demo-db.username");
        dbPassword = System.getProperty("app.datasource.demo-db.password");

        var migrationsExecutor = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutor.executeMigrations();
    }

    protected Configuration getConfiguration() {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        configuration.setProperty("hibernate.connection.url", dbUrl);
        configuration.setProperty("hibernate.connection.username", dbUserName);
        configuration.setProperty("hibernate.connection.password", dbPassword);
        return configuration;
    }
}
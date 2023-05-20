package ru.otus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.model.entity.*;
import ru.otus.server.ClientsServerImpl;
import ru.otus.service.DBServiceClient;
import ru.otus.service.DbServiceClientImpl;
import ru.otus.service.login.DbLoginService;
import ru.otus.service.template.TemplateProcessor;
import ru.otus.service.template.TemplateProcessorImpl;
import ru.otus.service.user.UserService;
import ru.otus.service.user.UserServiceImpl;

import java.util.List;

public class WebServerWithBasicSecurityDemo {

    private static final int WEB_SERVER_PORT = 8085;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {

        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        dbMigration(configuration);
        var transactionManager = getTransactionManagerHibernate(configuration);

        DBServiceClient serviceClient = new DbServiceClientImpl(transactionManager, new DataTemplateHibernate<>(Client.class));
        UserService userService = new UserServiceImpl(new DataTemplateHibernate<>(User.class), transactionManager);

        createUsers(userService);
        createClient(serviceClient);

        var objectMapper = new ObjectMapper();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        LoginService loginService = new DbLoginService(userService);
        ClientsServerImpl clientServer = new ClientsServerImpl(WEB_SERVER_PORT, loginService, serviceClient, objectMapper, templateProcessor);

        clientServer.start();
        clientServer.join();
    }

    private static void createClient(DBServiceClient serviceClient) {
        Client client = new Client("Client1");
        client.setAddress(new Address("Street1"));
        Phone phone = new Phone();
        phone.setNumber("456456456");
        client.setPhones(List.of(phone));

        serviceClient.saveClient(client);
    }

    private static void createUsers(UserService userService) {
        User adminUser = new User("vlad@gmail.com", "12345", List.of(new Role("admin")));
        User usualUser = new User("user@gmail.com", "54321", List.of(new Role("user")));
        userService.save(adminUser);
        userService.save(usualUser);
    }

    private static TransactionManagerHibernate getTransactionManagerHibernate(Configuration configuration) {
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class, User.class, Role.class);
        return new TransactionManagerHibernate(sessionFactory);
    }

    private static void dbMigration(Configuration configuration) {
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
    }
}
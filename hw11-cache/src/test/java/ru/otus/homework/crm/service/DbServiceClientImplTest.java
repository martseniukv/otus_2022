package ru.otus.homework.crm.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.homework.base.AbstractHibernateTest;
import ru.otus.homework.cache.MyCache;
import ru.otus.homework.core.repository.DataTemplateHibernate;
import ru.otus.homework.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.homework.crm.model.Address;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.model.Phone;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.WeakHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.homework.core.repository.HibernateUtils.buildSessionFactory;

@Slf4j
class DbServiceClientImplTest extends AbstractHibernateTest {

    private static final int MAX_LISTENER_SIZE = 5;
    private static final int MAX_CACHE_SIZE = 5;
    private DBServiceClient dbServiceClient;
    private MyCache<Long, Client> clientCache;
    private TransactionManagerHibernate transactionManager;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        sessionFactory = buildSessionFactory(getConfiguration(), Client.class, Address.class, Phone.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        clientCache = new MyCache<>(MAX_CACHE_SIZE, MAX_LISTENER_SIZE);
        DataTemplateHibernate<Client> clientTemplate = new DataTemplateHibernate<>(Client.class);
        transactionManager = new TransactionManagerHibernate(sessionFactory);
        dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, clientCache);
    }

    @Test
    void shouldCorrectSaveClient() {

        Client client = new Client();
        client.setName("Client_1");

        var savedClient = dbServiceClient.saveClient(client);

        assertThat(clientCache.get(savedClient.getId())).isEqualTo(savedClient);
    }

    @Test
    void getClient_with_speed_measurement_with_and_without_cache() {

        Client client = new Client();
        client.setName("Client_1");

        Client savedClient = transactionManager.doInTransaction(session -> {
            session.persist(client);
            return client;
        });

        long startWithouCache = System.currentTimeMillis();
        Optional<Client> loadClient = dbServiceClient.getClient(savedClient.getId());
        long endWithoutCache = System.currentTimeMillis();

        assertThat(loadClient).isPresent();

        long timeWithoutCache = endWithoutCache - startWithouCache;

        long startWithCache = System.currentTimeMillis();
        Optional<Client> loadClientFromCache = dbServiceClient.getClient(savedClient.getId());
        long endWithCache = System.currentTimeMillis();

        assertThat(loadClientFromCache).isPresent();

        long timeWithCache = endWithCache - startWithCache;

        assertTrue(timeWithCache < timeWithoutCache);
    }

    @Test
    @SneakyThrows
    void update_client_stored_in_cache() {

        Client client = new Client();
        client.setName("Client_1");

        Client savedClient = transactionManager.doInTransaction(session -> {
            session.persist(client);
            return client;
        });
        Client loadClient = transactionManager.doInTransaction(session -> session.find(Client.class, savedClient.getId()));
        assertThat(loadClient).isNotNull();

        String newName = "Client_2";
        client.setName(newName);
        Client updatedClient = dbServiceClient.saveClient(client);
        assertThat(updatedClient.getName()).isEqualTo(newName);
        assertThat(updatedClient.getId()).isEqualTo(savedClient.getId());

        Field declaredField = clientCache.getClass().getDeclaredField("cache");
        declaredField.setAccessible(true);
        WeakHashMap<Long, Client> clientWeakHashMap = (WeakHashMap<Long, Client>) declaredField.get(clientCache);
        assertThat(clientWeakHashMap).hasSize(1);
    }
}
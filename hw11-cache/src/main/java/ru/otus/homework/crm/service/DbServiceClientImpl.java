package ru.otus.homework.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.cache.HwCache;
import ru.otus.homework.core.repository.DataTemplate;
import ru.otus.homework.core.sessionmanager.TransactionManager;
import ru.otus.homework.crm.model.Client;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<Long, Client> clientCache;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate,
                               HwCache<Long, Client> clientCache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.clientCache = clientCache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.copy();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            clientCache.put(clientCloned.getId(), clientCloned);
            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        final Client chacheClient = clientCache.get(id);
        if (nonNull(chacheClient)) {
            return Optional.of(chacheClient);
        }
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            clientOptional.ifPresent(client ->  clientCache.put(client.getId(), client));
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }
}

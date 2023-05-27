package ru.otus.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.model.entity.Address;
import ru.otus.model.entity.Client;
import ru.otus.model.entity.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ClientResultSetExtractorClass implements ResultSetExtractor<List<Client>> {

    private static final String ID = "id";
    private static final String NAME = "name";

    private static final String ADDRESS_ID = "address_id";
    private static final String ADDRESS_STREET = "address_street";

    private static final String PHONE_ID = "phone_id";
    private static final String PHONE_NUMBER = "phone_number";

    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {

        var clientList = new ArrayList<Client>();
        Long prevClientId = null;
        Client prevClient = null;
        while (rs.next()) {
            var clientId = rs.getLong(ID);
            if (isNull(prevClientId) || !prevClientId.equals(clientId)) {

                var clientName = rs.getString(NAME);
                Client client = new Client(clientId, clientName, new HashSet<>());

                var addressId = rs.getObject(ADDRESS_ID, Long.class);
                if (nonNull(addressId)) {
                    var addressStreet = rs.getString(ADDRESS_STREET);
                    client.setAddress(new Address(addressId, addressStreet, clientId));
                }
                clientList.add(client);
                prevClientId = clientId;
                prevClient = client;
            }
            var phoneId = rs.getObject(PHONE_ID, Long.class);
            if (phoneId != null) {
                prevClient.getPhones().add(new Phone(phoneId, rs.getString(PHONE_NUMBER)));
            }
        }
        return clientList;
    }
}

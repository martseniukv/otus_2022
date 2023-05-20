package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.model.entity.Address;
import ru.otus.model.entity.Client;
import ru.otus.model.entity.Phone;
import ru.otus.service.DBServiceClient;
import ru.otus.service.template.TemplateProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
public class ClientsServlet extends HttpServlet {

    private static final String CLIENT_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";
    public static final String NAME_PARAM_ATTR = "name";
    public static final String STREET_PARAM_ATTR = "street";
    public static final String PHONE_PARAM_ATTR = "phones";

    private final transient DBServiceClient dbServiceClient;
    private final transient TemplateProcessor templateProcessor;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var clients = dbServiceClient.findAll();

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_CLIENTS, clients);

        resp.setContentType("text/html");
        resp.getWriter().println(templateProcessor.getPage(CLIENT_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String name = req.getParameter(NAME_PARAM_ATTR);
        String street = req.getParameter(STREET_PARAM_ATTR);
        String phoneNumbers = req.getParameter(PHONE_PARAM_ATTR);

        var client = new Client();
        client.setName(name);

        if (nonNull(street) && !street.isEmpty()) {
            client.setAddress(new Address(street));
        }

        if (nonNull(phoneNumbers) && !phoneNumbers.isEmpty()) {
            List<Phone> phones = Arrays.stream(phoneNumbers.split(","))
                    .map(phone -> new Phone(null, phone))
                    .toList();
            client.setPhones(phones);
        }

        Client saveClient = dbServiceClient.saveClient(client);
        log.info("Saved client: {}", saveClient);

        resp.sendRedirect("/clients");
    }
}
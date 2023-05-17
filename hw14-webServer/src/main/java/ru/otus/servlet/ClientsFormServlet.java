package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.service.template.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
public class ClientsFormServlet extends HttpServlet {

    private static final String CONTENT_TYPE = "text/html";
    private static final String CLIENTS_PAGE_TEMPLATE = "clients_form.html";
    private final transient TemplateProcessor templateProcessor;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType(CONTENT_TYPE);
        resp.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, new HashMap<>()));
    }
}
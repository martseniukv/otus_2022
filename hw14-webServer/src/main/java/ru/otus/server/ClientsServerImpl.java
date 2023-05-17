package ru.otus.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import ru.otus.service.DBServiceClient;
import ru.otus.service.template.TemplateProcessor;
import ru.otus.servlet.ClientApiServlet;
import ru.otus.servlet.ClientsFormServlet;
import ru.otus.servlet.ClientsServlet;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.util.FileSystemHelper.localFileNameOrResourceNameToFullPath;

public class ClientsServerImpl implements ClientsServer {

    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private static final String ROLE_NAME_USER = "user";
    private static final String ROLE_NAME_ADMIN = "admin";
    private static final String CONSTRAINT_NAME = "auth";
    private final ObjectMapper objectMapper;
    protected final TemplateProcessor templateProcessor;
    private final Server server;
    private final DBServiceClient dbServiceClient;
    private final LoginService loginService;

    public ClientsServerImpl(int port, LoginService loginService, DBServiceClient clientServer, ObjectMapper objectMapper, TemplateProcessor templateProcessor) {
        this.objectMapper = objectMapper;
        this.dbServiceClient = clientServer;
        this.templateProcessor = templateProcessor;
        this.loginService = loginService;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }


    protected Handler applySecurity(ServletContextHandler servletContextHandler) {

        List<ConstraintMapping> constraintMappings = getConstraintMapping(new String[]{ROLE_NAME_USER, ROLE_NAME_ADMIN}, HttpMethod.GET, "/clients", "/api/clients/*");
        constraintMappings.addAll(getConstraintMapping(new String[]{ROLE_NAME_ADMIN}, HttpMethod.GET, "/clients/add"));
        constraintMappings.addAll(getConstraintMapping(new String[]{ROLE_NAME_ADMIN}, HttpMethod.POST, "/clients/"));

        var security = new ConstraintSecurityHandler();

        security.setLoginService(loginService);
        security.setAuthenticator(new BasicAuthenticator());
        security.setConstraintMappings(constraintMappings);
        security.setHandler(new HandlerList(servletContextHandler));

        return security;

    }

    private static List<ConstraintMapping> getConstraintMapping(String[] roles, HttpMethod httpMethod, String... pathSpec) {
        return Arrays.stream(pathSpec)
                .map(f -> {

                    var constraint = new Constraint();
                    constraint.setName(CONSTRAINT_NAME);
                    constraint.setAuthenticate(true);
                    constraint.setRoles(roles);

                    var mapping = new ConstraintMapping();
                    mapping.setPathSpec(f);
                    mapping.setMethod(httpMethod.asString());
                    mapping.setConstraint(constraint);
                    return mapping;
                }).collect(Collectors.toList());
    }

    private void initContext() {

        ResourceHandler resourceHandler = getResourceHandler();
        ServletContextHandler servletContextHandler = getServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler));

        server.setHandler(handlers);
    }

    private ServletContextHandler getServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(dbServiceClient, templateProcessor)), "/clients");
        servletContextHandler.addServlet(new ServletHolder(new ClientsFormServlet(templateProcessor)), "/clients/add");
        servletContextHandler.addServlet(new ServletHolder(new ClientApiServlet(objectMapper, dbServiceClient)), "/api/clients/*");
        return servletContextHandler;
    }

    private ResourceHandler getResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }
}
package ru.otus.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.service.DBServiceClient;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ClientApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final transient ObjectMapper objectMapper;
    private final transient DBServiceClient dbServiceClient;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var client = dbServiceClient.getClient(extractIdFromRequest(req)).orElse(null);

        resp.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = resp.getOutputStream();
        out.print(objectMapper.writeValueAsString(client));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1)
                ? path[ID_PATH_PARAM_POSITION]
                : String.valueOf(- 1);
        return Long.parseLong(id);
    }
}
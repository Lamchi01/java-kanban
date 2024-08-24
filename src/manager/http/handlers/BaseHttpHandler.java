package manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected TaskManager taskManager;
    protected Gson gson;

    protected enum Endpoint {GET, GET_BY_ID, GET_SUBS_BY_EPIC_ID, POST, POST_BY_ID, DELETE_BY_ID, UNKNOWN}

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    protected Endpoint getEndpoint(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        String requestMethod = exchange.getRequestMethod();

        switch (requestMethod) {
            case "GET": {
                if (path.length == 3 && isNum(path[2])) {
                    return Endpoint.GET_BY_ID;
                } else if (path.length == 4 && path[1].equals("epics") && path[3].equals("subtasks")) {
                    return Endpoint.GET_SUBS_BY_EPIC_ID;
                }
                return Endpoint.GET;
            }
            case "POST": {
                if (path.length == 3 && isNum(path[2])) {
                    return Endpoint.POST_BY_ID;
                }
                return Endpoint.POST;
            }
            case "DELETE": {
                if (isNum(path[2])) {
                    return Endpoint.DELETE_BY_ID;
                }
            }
            default: {
                return Endpoint.UNKNOWN;
            }
        }
    }

    protected void writeResponse(HttpExchange exchange,
                                 int responseCode,
                                 String responseString) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, 0);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, 0);
        h.close();
    }

    protected String getTaskFromRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            writeResponse(exchange, 500, null);
            throw e;
        }
    }

    private boolean isNum(String number) {
        try {
            int id = Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
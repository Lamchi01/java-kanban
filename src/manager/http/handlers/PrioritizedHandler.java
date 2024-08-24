package manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String[] split = exchange.getRequestURI().getPath().split("/");

        if (requestMethod.equals("GET") && split[1].equals("prioritized")) {
            try {
                sendText(exchange, gson.toJson(taskManager.getPrioritized()));
            } catch (Exception e) {
                writeResponse(exchange, 500, "");
            }
        } else {
            writeResponse(exchange, 400, "");
        }
    }
}

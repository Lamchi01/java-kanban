package manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ManagerSaveException;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;

public class EpicHandlers extends BaseHttpHandler implements HttpHandler {
    public EpicHandlers(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        String[] split = exchange.getRequestURI().getPath().split("/");

        try {
            switch (endpoint) {
                case GET:
                    sendText(exchange, gson.toJson(taskManager.getAllEpic()));
                    break;
                case GET_BY_ID:
                    sendText(exchange, gson.toJson(taskManager.findEpicById(Integer.parseInt(split[2]))));
                    break;
                case GET_SUBS_BY_EPIC_ID:
                    sendText(exchange, gson.toJson(taskManager.findAllSubtaskByEpicId(Integer.parseInt(split[2]))));
                case POST:
                    epic = gson.fromJson(getTaskFromRequestBody(exchange), Epic.class);
                    taskManager.createEpic(epic);
                    writeResponse(exchange, 201, "Задача добавлена");
                    break;
                case DELETE_BY_ID:
                    epic = taskManager.findEpicById(Integer.parseInt(split[2]));
                    taskManager.removeEpicById(epic.getId());
                    writeResponse(exchange, 201, "Задача удалена");
                    break;
                case UNKNOWN:
                    sendNotFound(exchange);
                    break;
            }
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        } catch (ManagerSaveException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            writeResponse(exchange, 500, "");
        }
    }
}

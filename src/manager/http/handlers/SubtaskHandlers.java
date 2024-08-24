package manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ManagerSaveException;
import manager.TaskManager;
import tasks.Subtask;

import java.io.IOException;

public class SubtaskHandlers extends BaseHttpHandler implements HttpHandler {
    public SubtaskHandlers(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        String[] split = exchange.getRequestURI().getPath().split("/");

        try {
            switch (endpoint) {
                case GET:
                    sendText(exchange, gson.toJson(taskManager.getAllSubtask()));
                    break;
                case GET_BY_ID:
                    sendText(exchange, gson.toJson(taskManager.findSubtaskById(Integer.parseInt(split[2]))));
                    break;
                case POST:
                    subtask = gson.fromJson(getTaskFromRequestBody(exchange), Subtask.class);
                    taskManager.createSubtask(subtask);
                    writeResponse(exchange, 201, "Задача добавлена");
                    break;
                case POST_BY_ID:
                    subtask = gson.fromJson(getTaskFromRequestBody(exchange), Subtask.class);
                    taskManager.updateSubtask(subtask);
                    writeResponse(exchange, 201, "Задача обновлена");
                    break;
                case DELETE_BY_ID:
                    subtask = taskManager.findSubtaskById(Integer.parseInt(split[2]));
                    taskManager.removeSubtaskById(subtask.getId());
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
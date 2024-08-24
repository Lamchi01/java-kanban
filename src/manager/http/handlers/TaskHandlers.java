package manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ManagerSaveException;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;

public class TaskHandlers extends BaseHttpHandler implements HttpHandler {

    private Task task;

    public TaskHandlers(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        String[] split = exchange.getRequestURI().getPath().split("/");

        try {
            switch (endpoint) {
                case GET:
                    sendText(exchange, gson.toJson(taskManager.getAllTask()));
                    break;
                case GET_BY_ID:
                    sendText(exchange, gson.toJson(taskManager.findTaskById(Integer.parseInt(split[2]))));
                    break;
                case POST:
                    task = gson.fromJson(getTaskFromRequestBody(exchange), Task.class);
                    taskManager.createTask(task);
                    writeResponse(exchange, 201, "Задача добавлена");
                    break;
                case POST_BY_ID:
                    task = gson.fromJson(getTaskFromRequestBody(exchange), Task.class);
                    taskManager.updateTask(task);
                    writeResponse(exchange, 201, "Задача обновлена");
                    break;
                case DELETE_BY_ID:
                    task = taskManager.findTaskById(Integer.parseInt(split[2]));
                    taskManager.removeTaskById(task.getId());
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
            writeResponse(exchange, 500, null);
        }
    }
}
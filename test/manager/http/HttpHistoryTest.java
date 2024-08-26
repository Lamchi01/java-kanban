package manager.http;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.http.token.TaskTypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpHistoryTest {
    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static final String BASE_URL = "http://localhost:8080/history";

    @BeforeEach
    void setUp() {
        manager.deleteAllTask();
        manager.deleteAllSubtasks();
        manager.deleteAllEpic();
        taskServer.start();
    }

    @AfterEach
    void shutDown() {
        taskServer.stop();
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        Task task1 = new Task(1, "task", Status.NEW, "description",
                LocalDateTime.of(2024, 8, 24, 20, 40), Duration.ofMinutes(15));
        Epic epic2 = new Epic(1, "epic", Status.NEW, "description",
                LocalDateTime.of(2024, 8, 24, 10, 0),
                LocalDateTime.of(2024, 8, 24, 10, 15),
                Duration.ofMinutes(15));
        Subtask subtask3 = new Subtask(4, "subtask", Status.NEW, "description",
                LocalDateTime.of(2024, 8, 26, 20, 40), Duration.ofMinutes(15), 2);

        int idTask = manager.createTask(task1);
        int idEpic = manager.createEpic(epic2);
        int idSubtask = manager.createSubtask(subtask3);

        //для добавления в историю воспользуемся TaskManager
        manager.findEpicById(idEpic);
        manager.findTaskById(idTask);
        manager.findSubtaskById(idSubtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> historyByHttp = gson.fromJson(response.body(), new TaskTypeToken().getType());

        assertEquals(200, response.statusCode());
        assertEquals(3, historyByHttp.size());
        assertEquals(idEpic, historyByHttp.get(0).getId());
        assertEquals(idTask, historyByHttp.get(1).getId());
        assertEquals(idSubtask, historyByHttp.get(2).getId());
    }
}
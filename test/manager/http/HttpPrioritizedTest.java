package manager.http;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.http.token.TaskTypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
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

class HttpPrioritizedTest {
    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static final String BASE_URL = "http://localhost:8080/prioritized";

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
    void getPrioritized() throws IOException, InterruptedException {
        Task task1 = new Task(1, "task1", Status.NEW, "description1",
                LocalDateTime.of(2024, 8, 25, 20, 40), Duration.ofMinutes(15));
        Task task2 = new Task(2, "task2", Status.NEW, "description2",
                LocalDateTime.of(2024, 8, 24, 20, 40), Duration.ofMinutes(15));
        Task task3 = new Task(3, "task3", Status.NEW, "description3",
                LocalDateTime.of(2024, 8, 26, 20, 40), Duration.ofMinutes(15));

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> prioritizedByHttp = gson.fromJson(response.body(), new TaskTypeToken().getType());

        // порядок должен быть task2, task1, task3
        assertEquals(task2, prioritizedByHttp.get(0));
        assertEquals(task1, prioritizedByHttp.get(1));
        assertEquals(task3, prioritizedByHttp.get(2));
    }

}
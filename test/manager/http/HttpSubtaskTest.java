package manager.http;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.http.token.SubtaskTypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpSubtaskTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static final String BASE_URL = "http://localhost:8080/subtasks";

    @BeforeEach
    void setUp() {
        manager.deleteAllTask();
        manager.deleteAllSubtasks();
        manager.deleteAllEpic();
        taskServer.start();
        Epic epic = new Epic("Test 2", "Testing epic 2");
        manager.createEpic(epic);
    }

    @AfterEach
    void shutDown() {
        taskServer.stop();
    }

    @Test
    void getSubtask() throws IOException, InterruptedException {
        Subtask subtask1 = new Subtask(2, "subtask1", Status.NEW, "description1",
                LocalDateTime.of(2024, 8, 24, 20, 40), Duration.ofMinutes(15), 1);
        Subtask subtask2 = new Subtask(3, "subtask2", Status.NEW, "description2",
                LocalDateTime.of(2024, 8, 25, 20, 40), Duration.ofMinutes(15), 1);
        Subtask subtask3 = new Subtask(4, "subtask3", Status.NEW, "description3",
                LocalDateTime.of(2024, 8, 26, 20, 40), Duration.ofMinutes(15), 1);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> fromManager = manager.getAllSubtask();
        List<Subtask> fromHttp = gson.fromJson(response.body(), new SubtaskTypeToken().getType());

        assertEquals(fromManager.size(), fromHttp.size());
        assertEquals(fromManager.get(0), fromHttp.get(0));
        assertEquals(fromManager.get(1), fromHttp.get(1));
        assertEquals(fromManager.get(2), fromHttp.get(2));
    }

    @Test
    void getSubtaskById() throws IOException, InterruptedException {
        Subtask subtask3 = new Subtask(4, "subtask3", Status.NEW, "description3",
                LocalDateTime.of(2024, 8, 26, 20, 40), Duration.ofMinutes(15), 1);
        manager.createSubtask(subtask3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + subtask3.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskByHttp = gson.fromJson(response.body(), Subtask.class);

        assertEquals(200, response.statusCode());
        assertEquals(subtask3, subtaskByHttp);
    }

    @Test
    void testAddSubtask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("Test 2", "Testing subtask 2", 1);
        subtask.setDuration(Duration.ofMinutes(5));
        subtask.setStartTime(LocalDateTime.now());

        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> tasksFromManager = manager.getAllSubtask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Testing subtask 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    void testUpdateSubtask() throws IOException, InterruptedException {
        Subtask subtask1 = new Subtask(2, "subtask1", Status.NEW, "description1",
                LocalDateTime.of(2024, 8, 24, 20, 40), Duration.ofMinutes(15), 1);
        Subtask newSubtask1 = new Subtask(2, "subtask2", Status.NEW, "description2",
                LocalDateTime.of(2024, 8, 25, 10, 40), Duration.ofMinutes(15), 1);
        manager.createSubtask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + subtask1.getId());
        String subtaskJson = gson.toJson(newSubtask1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(newSubtask1, manager.findSubtaskById(2), "Задача не совпадает.");
    }

    @Test
    void testStatusCode406() throws IOException, InterruptedException {
        Subtask subtask1 = new Subtask(2, "subtask1", Status.NEW, "description1",
                LocalDateTime.of(2024, 8, 24, 15, 30), Duration.ofMinutes(15), 1);
        Subtask newSubtasks2 = new Subtask(3, "subtask2", Status.IN_PROGRESS, "description2",
                LocalDateTime.of(2024, 8, 24, 15, 30), Duration.ofMinutes(15), 1);
        manager.createSubtask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + subtask1.getId());
        String taskJson = gson.toJson(newSubtasks2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }

    @Test
    void deleteSubtask() throws IOException, InterruptedException {
        Subtask subtask1 = new Subtask("subtask1", "description1", 1);
        manager.createSubtask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + subtask1.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(0, manager.getAllSubtask().size());
    }

    @Test
    void deleteTaskStatus404() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}
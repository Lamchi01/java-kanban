package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends TaskManagerTest {

    File file;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void beforeEach() throws IOException {
        taskManager = new InMemoryTaskManager();
        file = File.createTempFile("test", ".csv");

        task = new Task("test", "testName", Status.IN_PROGRESS);
        epic = new Epic("test", "testName", Status.IN_PROGRESS);
        subtask = new Subtask("test", "testName", Status.IN_PROGRESS, 2);
    }

    @Test
    void loadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        task.setStartTime(LocalDateTime.of(2024, 8, 10, 15, 0));
        task.setDuration(Duration.ofMinutes(15));
        fileBackedTaskManager.createTask(task);

        epic.setStartTime(LocalDateTime.of(2024, 8, 10, 16, 0));
        epic.setDuration(Duration.ofMinutes(15));
        fileBackedTaskManager.createEpic(epic);

        subtask.setStartTime(LocalDateTime.of(2024, 8, 10, 17, 0));
        subtask.setDuration(Duration.ofMinutes(15));
        fileBackedTaskManager.createSubtask(subtask); // Инициализируем менеджер и добавляем задачи в тестовый файл

        assertEquals(1, fileBackedTaskManager.tasks.size()); // проверяем, добавились ли задачи в списки
        assertEquals(1, fileBackedTaskManager.epics.size());
        assertEquals(1, fileBackedTaskManager.subtasks.size());

        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);
        /* создаём уже выгруженный менеджер из метода loadFromFile не создавая задач
        Сразу же проверяем одинаковые ли задачи в 1 и во 2 менеджере */

        assertEquals(fileBackedTaskManager.getAllTask(), fileManager.getAllTask());
        assertEquals(fileBackedTaskManager.getAllEpic(), fileManager.getAllEpic());
        assertEquals(fileBackedTaskManager.getAllSubtask(), fileManager.getAllSubtask());
    }
}
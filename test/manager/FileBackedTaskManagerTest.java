package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {

    File file;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void beforeEach() throws IOException {
        file = File.createTempFile("test", ".csv");

        task = new Task("test", "testName", Status.IN_PROGRESS);
        epic = new Epic("test", "testName", Status.IN_PROGRESS);
        subtask = new Subtask("test", "testName", Status.IN_PROGRESS, 2);
    }

    @Test
    void loadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.createEpic(epic);
        fileBackedTaskManager.createSubtask(subtask); // Инициализируем менеджер и добавляем задачи в тестовый файл

        assertEquals(1, fileBackedTaskManager.tasks.size()); // проверяем, добавились ли задачи в списки
        assertEquals(1, fileBackedTaskManager.epics.size());
        assertEquals(1, fileBackedTaskManager.subtasks.size());

        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);
        /* создаём уже выгруженный менеджер из метода loadFromFile не создавая задач
        Сразу же проверяем одинаковые ли задачи в 1 и во 2 менеджере */

        assertEquals(fileBackedTaskManager.tasks.get(0), fileManager.tasks.get(0));
        assertEquals(fileBackedTaskManager.epics.get(1), fileManager.epics.get(1));
        assertEquals(fileBackedTaskManager.subtasks.get(2), fileManager.subtasks.get(2));
    }
}
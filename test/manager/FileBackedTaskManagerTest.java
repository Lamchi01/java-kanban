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
    void shouldReturnNoEmptyLoadFromStringFile() {
        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);

        fileManager.createTask(task);
        fileManager.createEpic(epic);
        fileManager.createSubtask(subtask);

        assertEquals(1, fileManager.tasks.size());
        assertEquals(1, fileManager.epics.size());
        assertEquals(1, fileManager.tasks.size());
    }

    @Test
    void shouldReturnEmptyLoadFromStringFile() {
        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(0, fileManager.tasks.size());
        assertEquals(0, fileManager.epics.size());
        assertEquals(0, fileManager.tasks.size());
    }
}
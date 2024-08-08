package manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InMemoryHistoryManagerTest {

    private final HistoryManager historyManager = new InMemoryHistoryManager();
    static List<Task> historyList;
    static Task task;
    static Epic epic;
    static Subtask subtask;

    @BeforeAll
    static void beforeAll() {
        task = new Task("Test addNewTask", "Test addNewTask description");
        epic = new Epic("Test addNewTask", "Test addNewTask description");
        subtask = new Subtask("Test addNewTask", "Test addNewTask description", 2);
        historyList = new ArrayList<>();

        task.setId(1);
        epic.setId(2);
        subtask.setId(3);
    }

    @Test
    void add() {
        historyManager.add(task);
        historyList = historyManager.getHistory();

        assertNotNull(historyList, "История пустая.");
        assertEquals(1, historyList.size(), "Ожидаемое значение не совпадает");
    }

    @Test
    void checkForUniquenessInList() {
        TaskManager taskManager = new InMemoryTaskManager();
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.add(task);

        historyList = historyManager.getHistory();

        assertNotEquals(task, historyList.getFirst());
    }
}
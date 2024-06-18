package manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {

    private final TaskManager taskManager = new InMemoryTaskManager();
    static ArrayList<Task> historyList;
    static Task task;

    @BeforeAll
    static void beforeAll() {
        task = new Task("Test addNewTask", "Test addNewTask description");
        historyList = new ArrayList<>();
    }

    @Test
    void add() {
        taskManager.createTask(task);
        taskManager.findTaskById(task.getId());

        historyList = taskManager.getHistory();

        assertNotNull(historyList, "История пустая.");
        assertEquals(1, historyList.size(), "Ожидаемое значение не совпадает");
    }

    @Test
    void add20TasksAndReturnListWith10Tasks() {
        taskManager.createTask(task);
        for (int i = 0; i < 20; i++) {
            taskManager.findTaskById(task.getId());
        }
        historyList = taskManager.getHistory();
        assertEquals(10, historyList.size(), "История не соответствует ожидаемому значению");
    }
}
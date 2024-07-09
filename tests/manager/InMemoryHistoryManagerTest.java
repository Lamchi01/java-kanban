package manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {

    private final HistoryManager historyManager = new InMemoryHistoryManager();
    static ArrayList<Task> historyList;
    static Task task;

    @BeforeAll
    static void beforeAll() {
        task = new Task("Test addNewTask", "Test addNewTask description");
        historyList = new ArrayList<>();
    }

    @Test
    void add() {
        historyManager.add(task);
        historyList = historyManager.getHistory();

        assertNotNull(historyList, "История пустая.");
        assertEquals(1, historyList.size(), "Ожидаемое значение не совпадает");
    }

    @Test
    void add20TasksAndReturnListWith10Tasks() {
        for (int i = 0; i < 20; i++) {
            historyManager.add(task);
        }
        historyList = historyManager.getHistory();
        assertEquals(10, historyList.size(), "История не соответствует ожидаемому значению");
    }
}
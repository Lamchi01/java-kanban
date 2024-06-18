package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    private final TaskManager taskManager = new InMemoryTaskManager();
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void getDefault() {
        assertNotNull(taskManager, "TaskManager не найден");
    }

    @Test
    void getDefaultHistory() {
        assertNotNull(historyManager, "HistoryManager не найден");
    }
}
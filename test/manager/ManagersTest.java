package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefault() {
        assertNotNull(Managers.getDefault(), "TaskManager не найден");
    }

    @Test
    void getDefaultHistory() {
        assertNotNull(Managers.getDefaultHistory(), "HistoryManager не найден");
    }
}
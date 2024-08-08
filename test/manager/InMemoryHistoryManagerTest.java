package manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

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

        assertNotNull(historyManager.getHistory(), "История пустая.");
        assertEquals(historyManager.getHistory().getFirst(), task, "Ожидаемое значение не совпадает");
    }

    @Test
    void checkForUniquenessInList() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.add(task);

        assertNotEquals(task, historyManager.getHistory().getFirst());
    }

    @Test
    void removeFirstHistoryTask() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        historyManager.remove(task.getId());

        assertEquals(historyManager.getHistory(), List.of(epic, subtask));
    }

    @Test
    void removeMidHistoryTask() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        historyManager.remove(epic.getId());

        assertEquals(historyManager.getHistory(), List.of(task, subtask));
    }

    @Test
    void removeLastHistoryTask() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        historyManager.remove(subtask.getId());

        assertEquals(historyManager.getHistory(), List.of(task, epic));
    }
}
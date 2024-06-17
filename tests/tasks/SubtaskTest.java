package tasks;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void taskEqualsById() {
        Subtask subtask = new Subtask("Помыть пол", "Уборка", 3);
        Subtask subtask1 = new Subtask("Пропылесосить ковры", "Уборка", 3);

        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask1);
        subtask1.setId(1);
        assertEquals(subtask, subtask1, "Это не одна задача");
    }
}
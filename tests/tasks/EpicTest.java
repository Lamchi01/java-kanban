package tasks;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void epicEqualsById() {
        Epic epic = new Epic("Помыть пол", "Уборка");
        Epic epic1 = new Epic("Пропылесосить ковры", "Уборка");

        taskManager.createEpic(epic);
        taskManager.createEpic(epic1);
        epic1.setId(1);
        assertEquals(epic, epic1, "Это не одна задача");
    }

}
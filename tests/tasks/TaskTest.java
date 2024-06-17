package tasks;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void taskEqualsById() {
        Task task = new Task("Помыть пол", "Уборка");
        Task task1 = new Task("Пропылесосить ковры", "Уборка");

        taskManager.createTask(task);
        taskManager.createTask(task1);
        task1.setId(1);
        assertEquals(task, task1, "Это не одна задача");
    }
}
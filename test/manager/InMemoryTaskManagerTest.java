package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;
    private static Task task;
    private static Epic epic;
    private static Subtask subtask;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
        task = new Task("Test addNewTask description", "Test addNewTask");
        epic = new Epic("Test addNewEpic description", "Test addNewEpic");
        subtask = new Subtask("Test addNewSubtask description", "Test addNewSubtask", 1);
    }

    @Test
    void addNewTask() {
        final int taskId = taskManager.createTask(task);
        final Task savedTask = taskManager.findTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        final int epicId = taskManager.createEpic(epic);
        final Epic savedEpic = taskManager.findEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают");

        final List<Epic> epics = taskManager.getAllEpic();

        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.getFirst(), "Задачи не совпадают");
    }

    @Test
    void addNewSubtask() {
        taskManager.createEpic(epic);

        final int subtaskId = taskManager.createSubtask(subtask);
        final Subtask savedSubtask = taskManager.findSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают");

        final List<Subtask> subtasks = taskManager.getAllSubtask();

        assertNotNull(subtasks, "Задачи не возвращаются");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.getFirst(), "Задачи не совпадают");
    }

    @Test
    void shouldReturnTaskAndFindById() {
        final Task task = new Task("addNewTaskDescription", "addNewTask");
        taskManager.createTask(task);
        assertNotNull(taskManager.findTaskById(task.getId()));
    }

    @Test
    void shouldReturnEpicAndFindById() {
        final Epic epic = new Epic("addNewEpicDescription", "addNewEpic");
        taskManager.createEpic(epic);
        assertNotNull(taskManager.findEpicById(epic.getId()));
    }

    @Test
    void shouldReturnSubtaskAndFindById() {
        taskManager.createEpic(epic);
        final Subtask subtask = new Subtask("addNewSubtaskDescription", "addNewSubtask", epic.getId());
        taskManager.createSubtask(subtask);
        assertNotNull(taskManager.findSubtaskById(subtask.getId()));
    }

    @Test
    void deleteTasksAndShouldReturnEmptyList() {
        taskManager.deleteAllTask();
        List<Task> tasks = taskManager.getAllTask();
        assertTrue(tasks.isEmpty(), "Список не пуст");
    }

    @Test
    void deleteEpicsAndShouldReturnEmptyList() {
        taskManager.deleteAllEpic();
        List<Epic> epics = taskManager.getAllEpic();
        assertTrue(epics.isEmpty(), "Список не пуст");
    }

    @Test
    void deleteSubtasksAndShouldReturnEmptyList() {
        taskManager.deleteAllSubtasks();
        List<Subtask> subtasks = taskManager.getAllSubtask();
        assertTrue(subtasks.isEmpty(), "Список не пуст");
    }

    @Test
    void shouldReturnEmptyListSubtasksAfterDeleteEpic() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        taskManager.removeEpicById(1);

        assertEquals(0, epic.getSubtaskId().size());
    }
}
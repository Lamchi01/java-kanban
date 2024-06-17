package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static HistoryManager historyManager;
    private static TaskManager taskManager;
    private static Task task;
    private static Epic epic;
    private static Subtask subtask;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
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
        addNewEpic();
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
    void addHistory() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void shouldReturnInitializedTaskManagerOfManagersClass() {
        assertNotNull(taskManager);
    }

    @Test
    void shouldReturnAllTypesAndFindById() {
        Task task = new Task("addNewTaskDescription", "addNewTask");
        Epic epic = new Epic("addNewEpicDescription", "addNewEpic");
        Subtask subtask = new Subtask("addNewSubtaskDescription", "addNewSubtask", 2);
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        assertNotNull(taskManager.findTaskById(1));
        assertNotNull(taskManager.findEpicById(2));
        assertNotNull(taskManager.findSubtaskById(3));
    }

    @Test
    void deleteTasksAndShouldReturnEmptyList() {
        taskManager.deleteAllTask();
        ArrayList<Task> tasks = taskManager.getAllTask();
        assertTrue(tasks.isEmpty(), "Список не пуст");
        taskManager.deleteAllEpic();
        ArrayList<Epic> epics = taskManager.getAllEpic();
        assertTrue(epics.isEmpty(), "Список не пуст");
        taskManager.deleteAllSubtasks();
        ArrayList<Subtask> subtasks = taskManager.getAllSubtask();
        assertTrue(subtasks.isEmpty(), "Список не пуст");
    }
}
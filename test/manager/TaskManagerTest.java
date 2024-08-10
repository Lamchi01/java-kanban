package manager;

import exception.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    @BeforeEach
    void beforeEach() {
        task = new Task("addNewTaskDescription", "addNewTask");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));

        epic = new Epic("addNewEpicDescription", "addNewEpic");

        subtask = new Subtask("addNewSubtaskDescription", "addNewSubtask", 2);
        subtask.setStartTime(LocalDateTime.now().plusHours(1));
        subtask.setDuration(Duration.ofMinutes(15));
    }

    @Test
    void addNewTask() {
        int taskId = taskManager.createTask(task);
        Task savedTask = taskManager.findTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        int epicId = taskManager.createEpic(epic);
        Epic savedEpic = taskManager.findEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают");

        final List<Epic> epics = taskManager.getAllEpic();

        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.getFirst(), "Задачи не совпадают");
    }

    @Test
    void addNewSubtask() {
        taskManager.createTask(task);
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
        taskManager.createTask(task);
        assertNotNull(taskManager.findTaskById(task.getId()));
    }

    @Test
    void shouldReturnEpicAndFindById() {
        taskManager.createEpic(epic);
        assertNotNull(taskManager.findEpicById(epic.getId()));
    }

    @Test
    void shouldReturnSubtaskAndFindById() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
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
    void removeTaskById() {
        taskManager.createTask(task);
        assertNotNull(taskManager.findTaskById(task.getId()));

        taskManager.removeTaskById(task.getId());
        assertEquals(0, taskManager.getAllTask().size());
    }

    @Test
    void shouldReturnEmptyListSubtasksAfterDeleteEpic() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        taskManager.removeEpicById(1);

        assertEquals(0, epic.getSubtaskId().size());
    }

    @Test
    void removeSubtaskById() {
        taskManager.createTask(task);
        assertEquals(taskManager.getAllTask(), List.of(task));

        taskManager.removeTaskById(task.getId());
        assertNotEquals(taskManager.getAllTask(), task);
    }

    @Test
    void getHistory() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);

        taskManager.findTaskById(task.getId());
        taskManager.findEpicById(epic.getId());

        assertEquals(taskManager.getHistory(), List.of(task, epic));
    }

    @Test
    void getPrioritized() {
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));
        taskManager.createTask(task);

        taskManager.createEpic(epic);

        subtask.setStartTime(LocalDateTime.now().minusMinutes(60));
        subtask.setDuration(Duration.ofMinutes(15));
        taskManager.createSubtask(subtask);

        assertEquals(taskManager.getPrioritized(), List.of(subtask, task));
    }

    @Test
    void checkOnCorrectIntersection() {
        epic.setStartTime(LocalDateTime.of(2024, 8, 10, 10,0));
        epic.setDuration(Duration.ofMinutes(15));
        taskManager.createEpic(epic);

        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(60));
        taskManager.createTask(task);

        subtask.setStartTime(LocalDateTime.now().plusMinutes(20));
        subtask.setDuration(Duration.ofMinutes(60));

        assertThrows(ManagerSaveException.class, () -> taskManager.createSubtask(subtask));
    }
}
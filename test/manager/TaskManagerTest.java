package manager;

import exception.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    static Task task;
    static Epic epic;
    static Subtask subtask;

    @BeforeEach
    void beforeEach() throws IOException {
        task = new Task("addNewTaskDescription", "addNewTask");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));
        epic = new Epic("addNewEpicDescription", "addNewEpic");
        subtask = new Subtask("addNewSubtaskDescription", "addNewSubtask", epic.getId());
        subtask.setStartTime(LocalDateTime.now().plusHours(1));
        subtask.setDuration(Duration.ofMinutes(15));
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
        final Epic epic = new Epic("addNewEpicDescription", "addNewEpic");
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
    void removeTaskById() {
        final Task task = new Task("addNewTaskDescription", "addNewTask");
        taskManager.createTask(task);
        assertNotNull(taskManager.findTaskById(task.getId()));

        taskManager.removeTaskById(task.getId());
        assertEquals(0, taskManager.getAllTask().size());
    }

    @Test
    void shouldReturnEmptyListSubtasksAfterDeleteEpic() {
        final Epic epic = new Epic("addNewEpicDescription", "addNewEpic");
        final Subtask subtask = new Subtask("addNewSubtaskDescription", "addNewSubtask", epic.getId());
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        taskManager.removeEpicById(1);

        assertEquals(0, epic.getSubtaskId().size());
    }

    @Test
    void removeSubtaskById() {
        final Task task = new Task("addNewTaskDescription", "addNewTask");
        taskManager.createTask(task);
        assertEquals(taskManager.getAllTask(), List.of(task));

        taskManager.removeTaskById(task.getId());
        assertNotEquals(taskManager.getAllTask(), task);
    }

    @Test
    void getHistory() {
        final Task task = new Task("addNewTaskDescription", "addNewTask");
        final Epic epic = new Epic("addNewEpicDescription", "addNewEpic");
        taskManager.createTask(task);
        taskManager.createEpic(epic);

        taskManager.findTaskById(task.getId());
        taskManager.findEpicById(epic.getId());

        assertEquals(taskManager.getHistory(), List.of(task, epic));
    }

    @Test
    void getPrioritized() {
        final Task task = new Task("addNewTaskDescription", "addNewTask");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));
        taskManager.createTask(task);

        final Epic epic = new Epic("addNewEpicDescription", "addNewEpic");
        taskManager.createEpic(epic);

        final Subtask subtask = new Subtask("addNewSubtaskDescription", "addNewSubtask", epic.getId());
        subtask.setStartTime(LocalDateTime.now().minusMinutes(60));
        subtask.setDuration(Duration.ofMinutes(15));
        taskManager.createSubtask(subtask);

        assertEquals(taskManager.getPrioritized(), List.of(subtask, task));
    }

    @Test
    void checkOnCorrectIntersection() {
        final Epic epic = new Epic("addNewEpicDescription", "addNewEpic");
        epic.setStartTime(LocalDateTime.of(2024, 8, 10, 10,0));
        epic.setDuration(Duration.ofMinutes(15));
        taskManager.createEpic(epic);

        final Task task = new Task("addNewTaskDescription", "addNewTask");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(60));
        taskManager.createTask(task);

        final Subtask subtask = new Subtask("addNewSubtaskDescription", "addNewSubtask", epic.getId());
        subtask.setStartTime(LocalDateTime.now().plusMinutes(20));
        subtask.setDuration(Duration.ofMinutes(60));

        assertThrows(ManagerSaveException.class, () -> taskManager.createSubtask(subtask));
    }
}
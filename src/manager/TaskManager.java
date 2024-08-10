package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getAllTask();

    List<Epic> getAllEpic();

    List<Subtask> getAllSubtask();

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubtask(Subtask subtask);

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubtasks();

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(Integer id);

    Task findTaskById(int id);

    Epic findEpicById(int id);

    Subtask findSubtaskById(int id);

    List<Subtask> findAllSubtaskByEpicId(int id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateEpicStatus(Epic epic);

    void updateSubtask(Subtask newSubtask);

    List<Task> getHistory();

    List<Task> getPrioritized();
}
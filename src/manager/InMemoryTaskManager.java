package manager;

import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private int id;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;

    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        historyManager = new InMemoryHistoryManager();
        id = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    private int generateId() {
        return ++id;
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public int createTask(Task task) {
        int newTaskId = generateId();
        task.setId(newTaskId);
        tasks.put(newTaskId, task);
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        int newEpicId = generateId();
        epic.setId(newEpicId);
        epics.put(newEpicId, epic);
        return epic.getId();
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int newSubtaskId = generateId();
        subtask.setId(newSubtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subtasks.put(newSubtaskId, subtask);
            epic.addSubtaskId(newSubtaskId);
            updateEpicStatus(epic);
        }
        return subtask.getId();
    }

    @Override
    public void deleteAllTask() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().clear();
            updateEpicStatus(epic);
        }
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtask : epic.getSubtaskId()) {
                subtasks.remove(subtask);
            }
            epic.getSubtaskId().clear();
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtaskId().remove(id);
            updateEpicStatus(epic);
            subtasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public Task findTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic findEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask findSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Subtask> findAllSubtaskByEpicId(int id) {
        ArrayList<Subtask> subtasksNew = new ArrayList<>();
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskId()) {
                subtasksNew.add(subtasks.get(subtaskId));
            }
        }
        return subtasksNew;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.replace(epic.getId(), epic);
            updateEpicStatus(epic);
        }
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }

        ArrayList<Subtask> epicSubtasks = new ArrayList<>();

        int countNew = 0;
        int countDone = 0;

        for (int i = 0; i < epic.getSubtaskId().size(); i++) {
            epicSubtasks.add(subtasks.get(epic.getSubtaskId().get(i)));
        }

        for (Subtask subtask : epicSubtasks) {
            if (subtask.getStatus() == Status.DONE) {
                countDone++;
            } else if (subtask.getStatus() == Status.NEW) {
                countNew++;
            } else {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }

            if (countNew == epicSubtasks.size()) {
                epic.setStatus(Status.NEW);
            } else if (countDone == epicSubtasks.size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        if ((newSubtask == null) || (!subtasks.containsKey(newSubtask.getId()))) {
            return;
        }
        Epic epic = epics.get(newSubtask.getEpicId());
        if (epic == null) {
            return;
        }
        subtasks.replace(newSubtask.getId(), newSubtask);
        updateEpicStatus(epic);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
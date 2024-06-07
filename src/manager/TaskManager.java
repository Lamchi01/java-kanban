package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import status.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        id = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    private int generateId() {
        return ++id;
    }

    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    public void createTask(Task task) {
        int newTaskId = generateId();
        task.setId(newTaskId);
        tasks.put(newTaskId, task);
    }

    public void createEpic(Epic epic) {
        int newEpicId = generateId();
        epic.setId(newEpicId);
        epics.put(newEpicId, epic);
    }

    public void createSubtask(Subtask subtask) {
        int newSubtaskId = generateId();
        subtask.setId(newSubtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subtasks.put(newSubtaskId, subtask);
            epic.addSubtaskId(newSubtaskId);
            updateEpicStatus(epic);
        }
    }

    public void deleteAllTask() {
        tasks.clear();
    }

    public void deleteAllEpic() {
        subtasks.clear();
        epics.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().clear();
            updateEpicStatus(epic);
        }
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskId()) {
                epic.getSubtaskId().remove(subtaskId);
            }
        }
        epics.remove(id);
    }

    public void removeSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtaskId().remove(id);
            updateEpicStatus(epic);
            subtasks.remove(id);
        }
    }

    public Task findTaskById(int id) {
        return tasks.get(id);
    }

    public Epic findEpicById(int id) {
        return epics.get(id);
    }

    public Subtask findSubtaskById(int id) {
        return subtasks.get(id);
    }

    public ArrayList<Subtask> findAllSubtaskByEpicId(int id) {
        ArrayList<Subtask> subtasksNew = new ArrayList<>();
            Epic epic = epics.get(id);
            if (epic != null) {
                for (Integer subtaskId : epic.getSubtaskId()) {
                    subtasksNew.add(subtasks.get(subtaskId));
                }
            }
        return subtasksNew;
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    private void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.replace(epic.getId(), epic);
            updateEpicStatus(epic);
        }
    }

    private void updateEpicStatus(Epic epic) {
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
            }
            if (subtask.getStatus() == Status.NEW) {
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
}
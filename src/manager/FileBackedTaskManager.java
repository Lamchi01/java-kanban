package manager;

import exception.ManagerSaveException;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);
        try (BufferedReader bf = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = bf.readLine();
            while (bf.ready()) {
                line = bf.readLine();

                Task task = fromString(line);

                if (task.getType().equals(TypeTask.EPIC)) {
                    fileManager.epics.put(task.getId(), (Epic) task);
                } else if (task.getType().equals(TypeTask.SUBTASK)) {
                    fileManager.subtasks.put(task.getId(), (Subtask) task);
                } else {
                    fileManager.tasks.put(task.getId(), task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось получить данные из файла");
        }
        return fileManager;
    }

    private void save() {
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл для сохранения");
        }

        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            fw.write("id,type,name,status,description,epic\n");

            for (Task task : getAllTask()) {
                fw.write(toString(task) + "\n");
            }

            for (Epic epic : getAllEpic()) {
                fw.write(toString(epic) + "\n");
            }

            for (Subtask subtask : getAllSubtask()) {
                fw.write(toString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось произвести сохранение");
        }
    }

    private static Task fromString(String value) {
        String[] arrays = value.split(",");
        String name = arrays[2];
        String description = arrays[4];
        Status status = Status.valueOf(arrays[3]);
        if (arrays[1].equals("EPIC")) {
            Epic epic = new Epic(description, name);
            epic.setId(Integer.parseInt(arrays[0]));
            epic.setStatus(status);
            return epic;
        } else if (arrays[1].equals("SUBTASK")) {
            int epicId = Integer.parseInt(arrays[5]);
            Subtask subtask = new Subtask(description, name, epicId);
            subtask.setId(Integer.parseInt(arrays[0]));
            subtask.setStatus(status);
            return subtask;
        } else {
            Task task = new Task(description, name);
            task.setId(Integer.parseInt(arrays[0]));
            task.setStatus(status);
            return task;
        }
    }


    private String toString(Task task) {
        return task.getId() + "," +
                getType(task) + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                getEpicIdForSubtask(task);
    }

    private static TypeTask getType(Task task) {
        if (task instanceof Epic) {
            return TypeTask.EPIC;
        } else if (task instanceof Subtask) {
            return TypeTask.SUBTASK;
        }
        return TypeTask.TASK;
    }

    private String getEpicIdForSubtask(Task task) {
        if (task instanceof Subtask) {
            return Integer.toString(((Subtask) task).getEpicId());
        }
        return "";
    }

    @Override
    public int createTask(Task task) {
        super.createTask(task);
        save();
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(Integer id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }
}

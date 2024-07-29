package manager;

import exception.ManagerSaveException;
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
    private static final String HEADER = "id,type,name,status,description,epic\n";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);
        try (BufferedReader bf = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = bf.readLine(); // используем чтение 1 строки, что бы её пропустить
            while (bf.ready()) {
                line = bf.readLine();

                Task task = FromAndToStringHelper.fromString(line);

                if (task.getType().equals(TypeTask.EPIC)) {
                    fileManager.epics.put(task.getId(), (Epic) task);
                } else if (task.getType().equals(TypeTask.SUBTASK)) {
                    fileManager.subtasks.put(task.getId(), (Subtask) task);
                    fileManager.epics.get(((Subtask) task).getEpicId()).addSubtaskId(task.getId());
                } else {
                    fileManager.tasks.put(task.getId(), task);
                }
                if (fileManager.id < task.getId()) {
                    fileManager.id = task.getId();
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
            fw.write(HEADER); // Записываем хедер в первую строку

            for (Task task : getAllTask()) {
                fw.write(FromAndToStringHelper.toString(task) + "\n");
            }

            for (Epic epic : getAllEpic()) {
                fw.write(FromAndToStringHelper.toString(epic) + "\n");
            }

            for (Subtask subtask : getAllSubtask()) {
                fw.write(FromAndToStringHelper.toString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось произвести сохранение");
        }
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

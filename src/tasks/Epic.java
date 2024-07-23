package tasks;

import manager.TypeTask;
import status.Status;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subtaskId;

    public Epic(String description, String name) {
        super(description, name);
        subtaskId = new ArrayList<>();
    }

    public Epic(String description, String name, Status status) {
        super(description, name, status);
        subtaskId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void addSubtaskId(int id) {
        subtaskId.add(id);
    }

    @Override
    public TypeTask getType() {
        return TypeTask.EPIC;
    }

    @Override
    public String toString() {
        return "Epic {" +
                "Описание ='" + getDescription() + '\'' +
                ", Айди задачи =" + getId() +
                ", Название ='" + getName() + '\'' +
                ", Статус выполнения: " + getStatus() +
                '}';
    }
}
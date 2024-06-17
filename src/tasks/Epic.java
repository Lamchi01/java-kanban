package tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subtaskId;

    public Epic(String description, String name) {
        super(description, name);
        subtaskId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void addSubtaskId(int id) {
        subtaskId.add(id);
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
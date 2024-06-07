package tasks;

import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskId, epic.subtaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskId);
    }
}
package tasks;

import manager.TypeTask;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private final int epicId;

    public Subtask(String description, String name, int epicId) {
        super(description, name);
        this.epicId = epicId;
    }

    public Subtask(String description, String name, Status status, int epicId) {
        super(description, name, status);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, Status status, String description, LocalDateTime startTime,
                   Duration duration, int epicId) {
        super(id, name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask {" +
                "Описание ='" + getDescription() + '\'' +
                ", Айди задачи =" + getId() +
                ", Название ='" + getName() + '\'' +
                ", Статус выполнения: " + getStatus() +
                '}';
    }
}
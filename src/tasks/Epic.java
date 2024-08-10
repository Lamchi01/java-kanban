package tasks;

import manager.TypeTask;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subtaskId = new ArrayList<>();
    private LocalDateTime endTime;
    private LocalDateTime startTime;
    private Duration duration;

    public Epic(String description, String name) {
        super(description, name);
    }

    public Epic(int id, String name, Status status, String description, LocalDateTime startTime,
                LocalDateTime endTime, Duration duration) {
        super(id, name, status, description, startTime, duration);
        this.endTime = endTime;
    }

    public Epic(String description, String name, Status status) {
        super(description, name, status);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
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
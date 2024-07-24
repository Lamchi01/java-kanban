package tasks;

import manager.TypeTask;
import status.Status;

import java.util.Objects;

public class Task {
    private String description;
    private int id;
    private String name;
    private Status status;

    public Task(String description, String name) {
        this.description = description;
        this.name = name;
        status = Status.NEW;
    }

    public Task(String description, String name, Status status) {
        this.description = description;
        this.name = name;
        this.status = status;
    }

    public Task(int id, String name, Status status, String description){
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TypeTask getType() {
        return TypeTask.TASK;
    }

    @Override
    public String toString() {
        return "Task {" +
                "Описание ='" + description + '\'' +
                ", Айди задачи =" + id +
                ", Название ='" + name + '\'' +
                ", Статус выполнения: " + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, name);
    }
}




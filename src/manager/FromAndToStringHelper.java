package manager;

import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class FromAndToStringHelper {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    protected static Task fromString(String value) {
        String[] arrays = value.split(",");
        int id = Integer.parseInt(arrays[0]);
        String typeTask = arrays[1];
        String name = arrays[2];
        Status status = Status.valueOf(arrays[3]);
        String description = arrays[4];
        LocalDateTime startTime = LocalDateTime.parse(arrays[5], formatter);
        Duration duration = Duration.ofMinutes(Long.parseLong(arrays[7]));

        if (typeTask.equals("EPIC")) {
            LocalDateTime epicEndTime = LocalDateTime.parse(arrays[6], formatter);
            return new Epic(id, name, status, description, startTime, epicEndTime, duration);
        } else if (typeTask.equals("SUBTASK")) {
            int epicId = Integer.parseInt(arrays[8]);
            return new Subtask(id, name, status, description, startTime, duration, epicId);
        } else {
            return new Task(id, name, status, description, startTime, duration);
        }
    }

    private static String getEpicIdForSubtask(Task task) {
        if (task.getType().equals(TypeTask.SUBTASK)) {
            return Integer.toString(((Subtask) task).getEpicId());
        }
        return "";
    }

    protected static String toString(Task task) {

        String startTime = task.getStartTime() != null ? task.getStartTime().format(formatter) : "";
        String endTime = task.getEndTime() != null ? task.getEndTime().format(formatter) : "";
        String duration = task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "";

        return task.getId() + "," +
                task.getType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                startTime + "," +
                endTime + "," +
                duration + "," +
                getEpicIdForSubtask(task);
    }
}
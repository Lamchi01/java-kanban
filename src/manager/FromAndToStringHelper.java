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
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy,HH:mm");
    static DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    static DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

    protected static Task fromString(String value) {
        String[] arrays = value.split(",");
        int id = Integer.parseInt(arrays[0]);
        String typeTask = arrays[1];
        String name = arrays[2];
        Status status = Status.valueOf(arrays[3]);
        String description = arrays[4];
        LocalDateTime startTime = LocalDateTime.of(LocalDate.parse(arrays[5], formatterDate),
                LocalTime.parse(arrays[6], formatterTime));
        LocalDateTime endTime = LocalDateTime.of(LocalDate.parse(arrays[7], formatterDate),
                LocalTime.parse(arrays[8], formatterTime));
        Duration duration = Duration.ofMinutes(Long.parseLong(arrays[9]));

        if (typeTask.equals("EPIC")) {
            return new Epic(id, name, status, description, startTime, endTime, duration);
        } else if (typeTask.equals("SUBTASK")) {
            int epicId = Integer.parseInt(arrays[10]);
            return new Subtask(id, name, status, description, startTime, endTime, duration, epicId);
        } else {
            return new Task(id, name, status, description, startTime, endTime, duration);
        }
    }

    private static String getEpicIdForSubtask(Task task) {
        if (task.getType().equals(TypeTask.SUBTASK)) {
            return Integer.toString(((Subtask) task).getEpicId());
        }
        return "";
    }

    protected static String toString(Task task) {

        return task.getId() + "," +
                task.getType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                task.getStartTime().format(formatter) + "," +
                task.getEndTime().format(formatter) + "," +
                task.getDuration().toMinutes() + "," +
                getEpicIdForSubtask(task);
    }
}
package manager;

import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

class FromAndToStringHelper {

    protected static Task fromString(String value) {
        String[] arrays = value.split(",");
        int id = Integer.parseInt(arrays[0]);
        String typeTask = (arrays[1]);
        String name = arrays[2];
        Status status = Status.valueOf(arrays[3]);
        String description = arrays[4];

        if (typeTask.equals("EPIC")) {
            return new Epic(id, name, status, description);
        } else if (typeTask.equals("SUBTASK")) {
            int epicId = Integer.parseInt(arrays[5]);
            return new Subtask(id, name, status, description, epicId);
        } else {
            return new Task(id, name, status, description);
        }
    }

    private static String getEpicIdForSubtask(Task task) {
        if (task instanceof Subtask) {
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
                getEpicIdForSubtask(task);
    }
}
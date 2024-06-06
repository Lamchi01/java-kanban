import status.Status;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.createTask(new Task("С 18.00 до 22.00", "Учёба в Practicum"));
        taskManager.createTask(new Task("Посмотреть фильм с женой", "Отдохнуть"));
        taskManager.createTask(new Task("Погулять с собакой", "Выйти на улицу"));

        taskManager.createEpic(new Epic("Уборка", "Убраться в квартире"));
        taskManager.createEpic(new Epic("Чистка", "Убраться в машине"));

        taskManager.createSubtask(new Subtask("Помыть пол", "Уборка", 4));
        taskManager.createSubtask(new Subtask("Пропылесосить ковры", "Уборка", 4));

        taskManager.createSubtask(new Subtask("Пропылесосить салон", "Чистка", 5));
        taskManager.createSubtask(new Subtask("Съездить на мойку", "Чистка", 5));
        System.out.println("Вывод на экран задач");
        for (Task task : taskManager.printAllTask()) { // добавил вывод через for, что бы было не всё в 1 куче
            System.out.println(task);
        }

        System.out.println("\nВывод на экран эпиков");
        for (Epic epic : taskManager.printAllEpic()) {
            System.out.println(epic);
        }

        System.out.println("\nВывод на экран подзадачи");
        for (Subtask subtask : taskManager.printAllSubtask()) {
            System.out.println(subtask);
        }

        System.out.println("\nОбновление задачи");
        Task task1 = new Task("Вспомнил, что собаки нет", "Купить продукты");
        task1.setId(2);
        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        for (Task task : taskManager.printAllTask()) {
            System.out.println(task);
        }

        System.out.println("\nОбновление подзадачи");
        Subtask subtask = new Subtask("Чистка салона", "Пропылесосить салон", 5);
        subtask.setStatus(Status.IN_PROGRESS);
        subtask.setId(8);
        taskManager.updateSubtask(subtask);
        for (Subtask subtask1 : taskManager.printAllSubtask()) {
            System.out.println(subtask1);
        }

        System.out.println("\nПроверка обновился ли эпик");
        for (Epic epic : taskManager.printAllEpic()) {
            System.out.println(epic);
        }

        System.out.println("\nПоиск по айди задачи");
        Task task = taskManager.findTaskById(2);
        System.out.println(task);

        System.out.println("\nПоиск по айди эпика");
        Epic epic = taskManager.findEpicById(5);
        System.out.println(epic);

        System.out.println("\nПоиск по айди подзадачи");
        Subtask subtask1 = taskManager.findSubtaskById(6);
        System.out.println(subtask1);

        System.out.println("\nПоиск подзадач для эпика по айди");
        ArrayList<Subtask> subtaskByEpicId = taskManager.findAllSubtaskByEpicId(4);
        for (Subtask subtask2 : subtaskByEpicId) {
            System.out.println(subtask2);
        }

        System.out.println("\nУдаление задачи по айди");
        taskManager.removeTaskById(2);
        for (Task task2 : taskManager.printAllTask()) {
            System.out.println(task2);
        }

        System.out.println("\nУдаление подзадачи по айди");
        taskManager.removeSubtaskById(7);
        for (Subtask subtask2 : taskManager.printAllSubtask()) {
            System.out.println(subtask2);
        }

        System.out.println("\nУдаление эпика по айди");
        taskManager.removeEpicById(5);
        for (Epic epic1 : taskManager.printAllEpic()) {
            System.out.println(epic1);
        }

        System.out.println("\nУдаление всех задач");
        taskManager.deleteAllTask();
        System.out.println(taskManager.printAllTask());

        System.out.println("\nУдаление всех подзадач");
        taskManager.deleteAllSubtasks();
        System.out.println(taskManager.printAllSubtask());

        System.out.println("\nУдаление всех эпиков с их задачами");
        taskManager.deleteAllEpic();
        System.out.println(taskManager.printAllEpic());
        System.out.println(taskManager.printAllSubtask());

    }
}
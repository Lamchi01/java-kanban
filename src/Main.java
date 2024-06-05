import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.createTask(new Task("С 18.00 до 22.00", "Учёба в Practicum", Status.NEW));
        taskManager.createTask(new Task("Посмотреть фильм с женой", "Отдохнуть", Status.NEW));
        taskManager.createTask(new Task("Погулять с собакой", "Выйти на улицу", Status.NEW));

        taskManager.createEpic(new Epic("Уборка", "Убраться в квартире", Status.NEW));
        taskManager.createEpic(new Epic("Чистка", "Убраться в машине", Status.NEW));

        taskManager.createSubtask(new Subtask("Помыть пол", "Уборка", Status.NEW, 4));
        taskManager.createSubtask(new Subtask("Пропылесосить ковры", "Уборка", Status.NEW, 4));

        taskManager.createSubtask(new Subtask("Пропылесосить салон", "Чистка", Status.NEW, 5));
        taskManager.createSubtask(new Subtask("Съездить на мойку", "Чистка", Status.NEW, 5));
        System.out.println("Вывод на экран задач");
        taskManager.printAllTask();

        System.out.println("\nВывод на экран эпиков");
        taskManager.printAllEpic();

        System.out.println("\nВывод на экран подзадачи");
        taskManager.printAllSubtask();

        System.out.println("\nОбновление задачи");
        Task task1 = new Task("Вспомнил, что собаки нет", "Купить продукты", Status.IN_PROGRESS);
        task1.setId(2);
        taskManager.updateTask(task1);
        taskManager.printAllTask();

        System.out.println("\nОбновление подзадачи");

        Subtask subtask = new Subtask("Чистка салона", "Пропылесосить салон", Status.IN_PROGRESS, 5);
        subtask.setId(8);
        taskManager.updateSubtask(subtask);
        taskManager.printAllSubtask();

        System.out.println("\nПроверка обновился ли эпик");

        taskManager.printAllEpic();

        System.out.println("\nПоиск по айди задачи");
        Task task = taskManager.foundTaskById(2);
        System.out.println(task);

        System.out.println("\nПоиск по айди эпика");
        Epic epic = taskManager.foundEpicById(5);
        System.out.println(epic);

        System.out.println("\nПоиск по айди подзадачи");
        Subtask subtask1 = taskManager.foundSubtaskById(6);
        System.out.println(subtask1);

        System.out.println("\nПоиск подзадач для эпика по айди");
        ArrayList<Subtask> subtaskByEpicId = taskManager.foundAllSubtaskByEpicId(4);
        for (Subtask subtask2 : subtaskByEpicId) {
            System.out.println(subtask2);
        }

        System.out.println("\nУдаление задачи по айди");
        taskManager.removeTaskById(2);
        taskManager.printAllTask();

        System.out.println("\nУдаление подзадачи по айди");
        taskManager.removeSubtaskById(7);
        taskManager.printAllSubtask();

        System.out.println("\nУдаление эпика по айди");
        taskManager.removeEpicById(5);
        taskManager.printAllEpic();

        System.out.println("\nУдаление всех задач");
        taskManager.deleteAllTask();
        taskManager.printAllTask();

        System.out.println("\nУдаление всех подзадач");
        taskManager.deleteAllSubtasks();
        taskManager.printAllSubtask();

        System.out.println("\nУдаление всех эпиков с их задачами");
        taskManager.deleteAllEpic();
        taskManager.printAllEpic();
        taskManager.printAllSubtask();

    }
}
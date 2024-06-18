package manager;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final static int MAX_LIMIT_OF_HISTORY = 10;
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() >= MAX_LIMIT_OF_HISTORY) {
                history.removeFirst();
            }
            history.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}

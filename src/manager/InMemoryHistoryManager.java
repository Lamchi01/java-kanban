package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList history = new CustomLinkedList();

    @Override
    public void add(Task task) {
        int taskId = task.getId();
        if (history.customMap.containsKey(taskId)) {
            history.removeNode(history.customMap.get(taskId));
        }
        history.linkLast(task);
    }

    @Override
    public void remove(int id) {
        history.removeNode(history.customMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return history.getTaskList();
    }

    private static class Node {
        private Node prev;
        private final Task task;
        private Node next;

        private Node(Task task) {
            this.task = task;
        }
    }

    private static class CustomLinkedList {
        private final Map<Integer, Node> customMap = new HashMap<>();
        private Node head;
        private Node tail;


        private void linkLast(Task task) {
            Node newNode = new Node(task);
            // пытался создать ссылку на ноду строкой Node prev = newNode.prev не получается
            // во время проверки prev остаётся null
            if (tail == null) {
                head = newNode;
            } else {
                newNode.prev = tail;
                tail.next = newNode;
            }
            tail = newNode;
            customMap.put(task.getId(), newNode);
        }

        private void removeNode(Node node) {
            if (node != null) {
                customMap.remove(node.task.getId());
                Node prev = node.prev;
                Node next = node.next;

                if (prev != null) {
                    prev.next = next;
                }
                if (next != null) {
                    next.prev = prev;
                }

                node.prev = null;
                node.next = null;

                if (node == head) {
                    head = next;
                }

                if (node == tail) {
                    tail = prev;
                }
            }
        }

        private List<Task> getTaskList() {
            List<Task> list = new ArrayList<>();
            Node element = head;
            while (element != null) {
                list.add(element.task);
                element = element.next;
            }
            return list;
        }
    }
}
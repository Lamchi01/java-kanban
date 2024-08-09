package manager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    InMemoryTaskManagerTest() {
        taskManager = new InMemoryTaskManager();
    }
}
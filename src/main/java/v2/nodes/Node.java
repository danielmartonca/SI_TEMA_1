package v2.nodes;

import v2.Messenger;
import v2.algorithms.EncryptionAlgorithm;
import v2.tasks.Tasks;

public abstract class Node implements Tasks {
    final protected Messenger messenger;
    protected int currentTask = 1;
    protected EncryptionAlgorithm algorithm;

    protected void doTask() throws InterruptedException {
        if (currentTask == 1) task1();
        if (currentTask == 2) task2();
        if (currentTask == 3) task3();
        if (currentTask == 4) task4();
        if (currentTask == 5) task5();
        if (currentTask == 6) task6();
        if (currentTask == 7) task7();
        if (currentTask > 7) quit();
        currentTask++;
    }

    public Node(Messenger messengerAB) {
        this.messenger = messengerAB;
    }

    public void sendMessage(String message) {
        try {
            messenger.sendMessageToAB(message);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        try {
            return messenger.getMessageFromAB();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
